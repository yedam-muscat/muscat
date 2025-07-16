package egovframework.com.muscat.board.web;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cryptography.EgovEnvCryptoService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cmm.util.EgovXssChecker;
import egovframework.com.cop.bbs.service.Board;
import egovframework.com.cop.bbs.service.BoardMasterVO;
import egovframework.com.cop.bbs.service.BoardVO;
import egovframework.com.cop.bbs.service.EgovArticleService;
import egovframework.com.cop.bbs.service.EgovBBSMasterService;
import egovframework.com.cop.bbs.service.EgovBBSSatisfactionService;
import egovframework.com.cop.cmt.service.CommentVO;
import egovframework.com.cop.cmt.service.EgovArticleCommentService;
import egovframework.com.utl.fcc.service.EgovStringUtil;

@Controller
public class BoardArticleController {

	/* -------------------------------------------------
	   ▣ 1. 상수 · 공용 메서드
	------------------------------------------------- */
	private static final Logger LOGGER = LoggerFactory.getLogger(BoardArticleController.class);

	private static final int DEFAULT_PAGE_UNIT = 10;   // propertiesService 부재 시 기본
	private static final int DEFAULT_PAGE_SIZE = 10;

	private LoginVO currentUser() {
		return (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
	}
	private String uniqId() {
		LoginVO u = currentUser();
		return (u == null ? "" : u.getUniqId());
	}
	private void applyDefaultTemplate(BoardMasterVO master) {
		if (master != null &&
			(master.getTmplatCours() == null || master.getTmplatCours().isBlank())) {
			master.setTmplatCours("/css/egovframework/com/cop/tpl/egovBaseTemplate.css");
		}
	}

	/** XSS 방지 필터링 */
	protected String unscript(String data) {
		if (data == null || data.trim().isEmpty()) return "";
		return data.replaceAll("(?i)<script", "&lt;script")
				   .replaceAll("(?i)</script", "&lt;/script")
				   .replaceAll("(?i)<object", "&lt;object")
				   .replaceAll("(?i)</object", "&lt;/object")
				   .replaceAll("(?i)<applet", "&lt;applet")
				   .replaceAll("(?i)</applet", "&lt;/applet")
				   .replaceAll("(?i)<embed", "&lt;embed")
				   .replaceAll("(?i)</embed", "&lt;/embed")
				   .replaceAll("(?i)<form", "&lt;form")
				   .replaceAll("(?i)</form", "&lt;/form");
	}

	/* -------------------------------------------------
	   ▣ 2. 빈 주입
	------------------------------------------------- */
	@Resource(name = "EgovArticleService")        private EgovArticleService         egovArticleService;
	@Resource(name = "EgovBBSMasterService")      private EgovBBSMasterService       egovBBSMasterService;
	@Resource(name = "EgovFileMngService")        private EgovFileMngService         fileMngService;
	@Resource(name = "EgovFileMngUtil")           private EgovFileMngUtil            fileUtil;
	@Resource(name = "propertiesService")         protected EgovPropertyService      propertyService;
	@Resource(name = "egovMessageSource")         EgovMessageSource                 egovMessageSource;
	@Resource(name = "EgovArticleCommentService") private EgovArticleCommentService  commentService;
	@Resource(name = "EgovBBSSatisfactionService")private EgovBBSSatisfactionService bbsSatisfactionService; // ★ 만족도 서비스
	@Autowired                                     private DefaultBeanValidator       beanValidator;

	private static EgovEnvCryptoService cryptoService;
	@Resource(name = "egovEnvCryptoService")
	public void setEgovEnvCryptoService(EgovEnvCryptoService cs) { BoardArticleController.cryptoService = cs; }

	/* -------------------------------------------------
	   ▣ 3. 화면 이동 (목록·상세 etc.)
	------------------------------------------------- */

	/** 📄 게시글 목록 페이지 */
	@GetMapping("/board/articleList.do")
	public String articleList(@RequestParam("bbsId") String bbsId, Model model) {
		model.addAttribute("bbsId", bbsId);
		return "board/articleList.html";
	}

	/** 📄 게시글 상세 페이지 */
	@GetMapping("/board/articleDetail.do")
	public String articleDetailPage(@RequestParam("bbsId") String bbsId,
	                                @RequestParam("nttId") int nttId, Model model){
		model.addAttribute("bbsId", bbsId);
		model.addAttribute("nttId", nttId);
		return "board/articleDetail.html";
	}

	/** 📝 게시글 등록 화면 */
	@GetMapping("/board/articleRegist.do")
	public String articleRegistPage(@RequestParam("bbsId") String bbsId, Model model) throws Exception {
		if (!EgovUserDetailsHelper.isAuthenticated()) return "redirect:/uat/uia/egovLoginUsr.do";

		BoardMasterVO master = new BoardMasterVO();
		master.setBbsId(bbsId);
		master.setUniqId(uniqId());
		master = egovBBSMasterService.selectBBSMasterInf(master);
		applyDefaultTemplate(master);

		model.addAttribute("boardMasterVO", master);
		return "board/articleRegist.html";
	}

	/** ✏️ 게시글 수정 화면 */
	@GetMapping("/board/articleUpdt.do")
	public String updateArticleView(@RequestParam("bbsId") String bbsId,
	                                @RequestParam("nttId") int nttId, ModelMap model) throws Exception {
		BoardVO key = new BoardVO(); key.setBbsId(bbsId); key.setNttId(nttId);
		BoardVO article = egovArticleService.selectArticleDetail(key);

		BoardMasterVO master = new BoardMasterVO();
		master.setBbsId(bbsId); master.setUniqId(uniqId());
		master = egovBBSMasterService.selectBBSMasterInf(master); applyDefaultTemplate(master);

		if ("anonymous".equals(article.getNtcrId())) {           // 익명글 수정 불가
			model.addAttribute("result", article);
			model.addAttribute("boardMasterVO", master);
			return "board/articleDetail.html";
		}
		model.addAttribute("articleVO", article);
		model.addAttribute("boardMasterVO", master);
		return "board/articleUpdt.html";
	}

	/** ✏️ 답글 작성 화면 */
	@GetMapping("/board/articleReply.do")
	public String articleReplyForm(@RequestParam("nttId") String nttId,
	                               @RequestParam("bbsId") String bbsId, Model model) throws Exception {
		if (!EgovUserDetailsHelper.isAuthenticated()) return "redirect:/uat/uia/egovLoginUsr.do";

		BoardMasterVO master = new BoardMasterVO();
		master.setBbsId(bbsId); master.setUniqId(uniqId());
		master = egovBBSMasterService.selectBBSMasterInf(master); applyDefaultTemplate(master);

		BoardVO origin = new BoardVO(); origin.setBbsId(bbsId); origin.setNttId(Long.parseLong(nttId));
		origin = egovArticleService.selectArticleDetail(origin);

		model.addAttribute("boardMasterVO", master);
		model.addAttribute("origin", origin);
		model.addAttribute("reply", new BoardVO());
		return "board/articleReply.html";
	}

	/* -------------------------------------------------
	   ▣ 4. 목록 JSON
	------------------------------------------------- */
	@GetMapping("/board/articleList")
	@ResponseBody
	public Map<String,Object> listJson(@ModelAttribute("searchVO") BoardVO vo,
	                                   @RequestParam("bbsId") String bbsId) throws Exception {

		Map<String,Object> res = new HashMap<>();
		if(!EgovUserDetailsHelper.isAuthenticated()){
			res.put("resultCode","FAIL"); res.put("resultMessage","로그인이 필요합니다."); return res;
		}

		/* 페이징 */
		vo.setBbsId(bbsId);
		vo.setPageUnit(propertyService.getInt("pageUnit", DEFAULT_PAGE_UNIT));
		vo.setPageSize(propertyService.getInt("pageSize" , DEFAULT_PAGE_SIZE));

		PaginationInfo pi = new PaginationInfo();
		pi.setCurrentPageNo(vo.getPageIndex());
		pi.setRecordCountPerPage(vo.getPageUnit());
		pi.setPageSize(vo.getPageSize());

		vo.setFirstIndex(pi.getFirstRecordIndex());
		vo.setLastIndex (pi.getLastRecordIndex());
		vo.setRecordCountPerPage(pi.getRecordCountPerPage());

		/* 목록 조회 */
		Map<String,Object> map = egovArticleService.selectArticleList(vo);
		@SuppressWarnings("unchecked")
		List<BoardVO> list = (List<BoardVO>) map.getOrDefault("resultList", List.of());

		/* 댓글 수 계산 */
		for(BoardVO row : list){
			CommentVO cvo = new CommentVO();
			cvo.setBbsId(row.getBbsId()); cvo.setNttId(row.getNttId());
			int cnt = 0;
			try{
				Map<String,Object> cm = commentService.selectArticleCommentList(cvo);
				cnt = Integer.parseInt((String)cm.getOrDefault("resultCnt","0"));
			}catch(Exception e){
				LOGGER.warn("댓글 수 조회 실패 bbsId={}, nttId={}", row.getBbsId(), row.getNttId());
			}
			row.setCommentCnt(cnt);
		}

		int total = Integer.parseInt((String)map.get("resultCnt"));
		pi.setTotalRecordCount(total);

		/* 공지글 */
		List<BoardVO> noticeList = egovArticleService.selectNoticeArticleList(vo);

		res.put("resultCode","SUCCESS");
		res.put("resultList",list);
		res.put("noticeList",noticeList);
		res.put("resultCnt",total);
		res.put("pageIndex",vo.getPageIndex());
		res.put("pageUnit",vo.getPageUnit());
		res.put("totalPageCount",pi.getTotalPageCount());
		return res;
	}

	/* -------------------------------------------------
	   ▣ 5. 상세 JSON
	------------------------------------------------- */
	@GetMapping("/board/articleDetail")
	@ResponseBody
	public Map<String,Object> articleDetail(@RequestParam("bbsId") String bbsId,
	                                        @RequestParam("nttId") int nttId,
	                                        HttpServletRequest request) {

		Map<String,Object> result = new HashMap<>();
		try{
			BoardVO key = new BoardVO(); key.setBbsId(bbsId); key.setNttId(nttId);
			BoardVO article = egovArticleService.selectArticleDetail(key);

			/* 비밀글 접근 제어 */
			if("Y".equals(article.getSecretAt())){
				boolean mine  = uniqId().equals(article.getFrstRegisterId());
				boolean admin = EgovUserDetailsHelper.getAuthorities().contains("ROLE_ADMIN");
				if(!mine && !admin){
					result.put("resultCode","DENIED");
					result.put("resultMessage","열람 권한이 없습니다."); return result;
				}
			}

			/* 첨부파일 암호화 ID */
			List<FileVO> fileList = List.of();
			if(article.getAtchFileId()!=null){
				FileVO f = new FileVO(); f.setAtchFileId(article.getAtchFileId());
				fileList = fileMngService.selectFileInfs(f);
				for(FileVO fi : fileList){
					String sid = request.getSession().getId();
					String enc = cryptoService.encrypt(sid+"|"+fi.getAtchFileId());
					fi.setAtchFileId(Base64.getEncoder().encodeToString(enc.getBytes()));
				}
			}

			/* 댓글/만족도 사용 여부 */
			boolean useComment      = commentService!=null &&
			                          commentService.canUseComment(bbsId);
			boolean useSatisfaction = bbsSatisfactionService!=null &&
			                          bbsSatisfactionService.canUseSatisfaction(bbsId);

			result.put("resultCode","SUCCESS");
			result.put("article",article);
			result.put("fileList",fileList);
			result.put("fileListCnt",fileList.size());
			result.put("useComment",useComment);
			result.put("useSatisfaction",useSatisfaction);
		}catch(Exception e){
			LOGGER.error("상세 조회 오류",e);
			result.put("resultCode","FAIL");
			result.put("resultMessage","오류: "+e.getMessage());
		}
		return result;
	}

	/* -------------------------------------------------
	   ▣ 6. 게시글 등록
	------------------------------------------------- */
	@PostMapping("/board/articleInsert.do")
	@ResponseBody
	public Map<String,Object> insertArticleAjax(final MultipartHttpServletRequest req){
		Map<String,Object> out=new HashMap<>();
		try{
			if(!EgovUserDetailsHelper.isAuthenticated())
				return fail(out,"로그인이 필요합니다.");

			LoginVO user=currentUser();
			BoardVO board=new BoardVO();
			board.setBbsId(req.getParameter("bbsId"));
			board.setNttSj(req.getParameter("nttSj"));
			board.setNttCn(unscript(req.getParameter("nttCn")));
			board.setNoticeAt(req.getParameter("noticeAt")!=null?"Y":"N");
			board.setSecretAt(req.getParameter("secretAt")!=null?"Y":"N");
			board.setAnonymousAt(req.getParameter("anonymousAt")!=null?"Y":"N");
			board.setSjBoldAt(req.getParameter("sjBoldAt")!=null?"Y":"N");
			board.setNtceBgnde(req.getParameter("ntceBgnde"));
			board.setNtceEndde(req.getParameter("ntceEndde"));

			/* 규칙 검사 */
			if("Y".equals(board.getAnonymousAt()) && "Y".equals(board.getNoticeAt()))
				return fail(out,"익명글은 공지로 등록할 수 없습니다.");
			if("Y".equals(board.getSecretAt()) &&
			  ("Y".equals(board.getAnonymousAt()) || "Y".equals(board.getNoticeAt())))
				return fail(out,"비밀글은 익명/공지와 함께 사용할 수 없습니다.");

			/* 등록자 */
			if("Y".equals(board.getAnonymousAt())){
				board.setNtcrId("anonymous"); board.setNtcrNm("익명"); board.setFrstRegisterId("anonymous");
			}else{
				board.setNtcrId(user.getUniqId()); board.setNtcrNm(user.getName()); board.setFrstRegisterId(user.getUniqId());
			}

			List<MultipartFile> files=req.getFiles("file");
			beanValidator.validate(board,new BindException(board,"board"));
			egovArticleService.insertArticleAndFiles(board, files);

			out.put("resultCode","SUCCESS"); out.put("resultMessage","게시글이 등록되었습니다.");
		}catch(Exception e){
			LOGGER.error("등록 오류",e); fail(out,"등록 중 오류 발생");
		}
		return out;
	}

	/* -------------------------------------------------
	   ▣ 7. 게시글 수정
	------------------------------------------------- */
	@PostMapping("/board/articleUpdate.do")
	@ResponseBody
	public Map<String,Object> updateArticle(MultipartHttpServletRequest req){
		Map<String,Object> out=new HashMap<>();
		try{
			if(!EgovUserDetailsHelper.isAuthenticated()) return fail(out,"로그인이 필요합니다.");
			LoginVO user=currentUser();

			BoardVO board=new BoardVO();
			board.setBbsId(req.getParameter("bbsId"));
			board.setNttId(Integer.parseInt(req.getParameter("nttId")));
			board.setNttSj(req.getParameter("nttSj"));
			board.setNttCn(unscript(req.getParameter("nttCn")));
			board.setNoticeAt(req.getParameter("noticeAt")!=null?"Y":"N");
			board.setSecretAt(req.getParameter("secretAt")!=null?"Y":"N");
			board.setAnonymousAt(req.getParameter("anonymousAt")!=null?"Y":"N");
			board.setSjBoldAt(req.getParameter("sjBoldAt")!=null?"Y":"N");
			board.setNtceBgnde(req.getParameter("ntceBgnde"));
			board.setNtceEndde(req.getParameter("ntceEndde"));
			board.setLastUpdusrId(user.getUniqId());

			/* 규칙 동일 */
			if("Y".equals(board.getAnonymousAt()) && "Y".equals(board.getNoticeAt()))
				return fail(out,"익명글은 공지로 등록할 수 없습니다.");
			if("Y".equals(board.getSecretAt()) &&
			  ("Y".equals(board.getAnonymousAt()) || "Y".equals(board.getNoticeAt())))
				return fail(out,"비밀글은 익명/공지와 함께 사용할 수 없습니다.");

			List<MultipartFile> files=req.getFiles("file");
			BoardVO origin=egovArticleService.selectArticleDetail(board);
			board.setAtchFileId(origin.getAtchFileId());

			beanValidator.validate(board,new BindException(board,"board"));
			egovArticleService.updateArticleAndFiles(board, files, board.getAtchFileId());

			out.put("resultCode","SUCCESS"); out.put("resultMessage","게시글이 수정되었습니다.");
		}catch(Exception e){
			LOGGER.error("수정 오류",e); fail(out,"수정 중 오류 발생");
		}
		return out;
	}

	/* -------------------------------------------------
	   ▣ 8. 게시글 삭제
	------------------------------------------------- */
	@PostMapping("/board/articleDelete")
	@ResponseBody
	public Map<String,Object> deleteArticle(@RequestBody BoardVO in, HttpServletRequest req){
		Map<String,Object> out=new HashMap<>();
		try{
			if(!EgovUserDetailsHelper.isAuthenticated()) return fail(out,"로그인이 필요합니다.");

			BoardVO origin=egovArticleService.selectArticleDetail(in);
			if("anonymous".equals(origin.getNtcrId()))
				return fail(out,"익명으로 등록된 글은 삭제할 수 없습니다.");

			EgovXssChecker.checkerUserXss(req, origin.getFrstRegisterId());

			Board del=new Board();
			del.setBbsId(in.getBbsId());
			del.setNttId(in.getNttId());
			del.setLastUpdusrId(uniqId());

			egovArticleService.deleteArticle(del);
			out.put("resultCode","SUCCESS"); out.put("resultMessage","삭제되었습니다.");
		}catch(Exception e){
			LOGGER.error("삭제 오류",e); fail(out,"삭제 중 오류 발생");
		}
		return out;
	}

	/* -------------------------------------------------
	   ▣ 9. 답글 저장
	------------------------------------------------- */
	@PostMapping("/board/articleReply.do")
	@ResponseBody
	public Map<String,Object> saveArticleReply(BoardVO reply,
	              @RequestParam(value="file_1",required=false) List<MultipartFile> files)throws Exception{

		Map<String,Object> out=new HashMap<>();
		if(!EgovUserDetailsHelper.isAuthenticated()) return fail(out,"로그인이 필요합니다.");

		/* 원글 조회 */
		BoardVO origin=new BoardVO();
		origin.setBbsId(reply.getBbsId());
		origin.setNttId(Long.parseLong(reply.getParnts()));
		origin=egovArticleService.selectArticleDetail(origin);

		if(origin==null) return fail(out,"원본 게시글을 찾을 수 없습니다.");

		/* 비밀글 답변 가능 여부 */
		boolean admin=EgovUserDetailsHelper.getAuthorities().contains("ROLE_ADMIN");
		if("Y".equals(origin.getSecretAt()) && !admin && !uniqId().equals(origin.getFrstRegisterId()))
			return fail(out,"비밀글에는 답글을 달 수 없습니다.");

		/* 계층 설정 */
		reply.setReplyAt("Y");
		reply.setSortOrdr(origin.getSortOrdr());
		int lc=EgovStringUtil.isEmpty(origin.getReplyLc())?0:Integer.parseInt(origin.getReplyLc());
		reply.setReplyLc(Integer.toString(lc+1));

		/* 작성자 */
		LoginVO user=currentUser();
		if("Y".equals(reply.getAnonymousAt())){
			reply.setNtcrId("anonymous"); reply.setNtcrNm("익명"); reply.setFrstRegisterId("anonymous");
		}else{
			reply.setNtcrId(user.getId()); reply.setNtcrNm(user.getName()); reply.setFrstRegisterId(user.getUniqId());
		}
		reply.setLastUpdusrId(uniqId());
		reply.setNttCn(unscript(reply.getNttCn()));

		try{
			egovArticleService.insertArticleAndFiles(reply, files==null?List.of():files);
			out.put("success",true);
		}catch(Exception e){
			LOGGER.error("답글 저장 오류",e); fail(out,"답글 등록 중 오류: "+e.getMessage());
		}
		return out;
	}

	/* -------------------------------------------------
	   ▣ 10. 유틸
	------------------------------------------------- */
	private Map<String,Object> fail(Map<String,Object> m,String msg){
		m.put("resultCode","FAIL");
		m.put("resultMessage",msg);
		return m;
	}
}
