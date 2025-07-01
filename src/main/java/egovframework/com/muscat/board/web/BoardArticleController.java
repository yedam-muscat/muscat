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
import egovframework.com.cmm.web.EgovFileMngController;
import egovframework.com.cop.bbs.service.Board;
import egovframework.com.cop.bbs.service.BoardMasterVO;
import egovframework.com.cop.bbs.service.BoardVO;
import egovframework.com.cop.bbs.service.EgovArticleService;
import egovframework.com.cop.bbs.service.EgovBBSMasterService;

@Controller
public class BoardArticleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoardArticleController.class);

	@Resource(name = "EgovArticleService")
	private EgovArticleService egovArticleService;

	@Resource(name = "EgovBBSMasterService")
	private EgovBBSMasterService egovBBSMasterService;

	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileMngService;

	@Resource(name = "EgovFileMngUtil")
	private EgovFileMngUtil fileUtil;

	@Resource(name = "propertiesService")
	protected EgovPropertyService propertyService;

	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Autowired
	private DefaultBeanValidator beanValidator;
	
	// 파일 관련 서비스
	private static EgovEnvCryptoService cryptoService;

	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;

	@Resource(name = "egovEnvCryptoService")
	public void setEgovEnvCryptoService(EgovEnvCryptoService cryptoService) {
		BoardArticleController.cryptoService = cryptoService;
	}

	/** XSS 방지 필터링 */
	protected String unscript(String data) {
		if (data == null || data.trim().isEmpty())
			return "";
		return data.replaceAll("(?i)<script", "&lt;script").replaceAll("(?i)</script", "&lt;/script")
				.replaceAll("(?i)<object", "&lt;object").replaceAll("(?i)</object", "&lt;/object")
				.replaceAll("(?i)<applet", "&lt;applet").replaceAll("(?i)</applet", "&lt;/applet")
				.replaceAll("(?i)<embed", "&lt;embed").replaceAll("(?i)</embed", "&lt;/embed")
				.replaceAll("(?i)<form", "&lt;form").replaceAll("(?i)</form", "&lt;/form");
	}

	/** 📄 게시글 목록 페이지 이동 */
	@GetMapping("/board/articleList.do")
	public String articleListPage(@RequestParam("bbsId") String bbsId, Model model) throws Exception {
		BoardMasterVO boardInfo = new BoardMasterVO();
		boardInfo.setBbsId(bbsId);

		model.addAttribute("bbsId", bbsId);
		model.addAttribute("boardInfo", egovBBSMasterService.selectBBSMasterInf(boardInfo));
		return "board/articleList.html";
	}

	/** 📑 게시글 목록 데이터 (JSON) */
	@GetMapping("/board/articleList")
	@ResponseBody
	public Map<String, Object> articleList(@ModelAttribute("searchVO") BoardVO boardVO,
			@RequestParam("bbsId") String bbsId) throws Exception {
		Map<String, Object> result = new HashMap<>();

		if (!EgovUserDetailsHelper.isAuthenticated()) {
			result.put("resultCode", "FAIL");
			result.put("resultMessage", "로그인이 필요합니다.");
			return result;
		}

		boardVO.setBbsId(bbsId);
		boardVO.setPageUnit(propertyService.getInt("pageUnit"));
		boardVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setCurrentPageNo(boardVO.getPageIndex());
		pageInfo.setRecordCountPerPage(boardVO.getPageUnit());
		pageInfo.setPageSize(boardVO.getPageSize());

		boardVO.setFirstIndex(pageInfo.getFirstRecordIndex());
		boardVO.setLastIndex(pageInfo.getLastRecordIndex());
		boardVO.setRecordCountPerPage(pageInfo.getRecordCountPerPage());

		Map<String, Object> dataMap = egovArticleService.selectArticleList(boardVO);
		int totalCount = Integer.parseInt((String) dataMap.get("resultCnt"));

		pageInfo.setTotalRecordCount(totalCount);

		result.put("resultCode", "SUCCESS");
		result.put("resultList", dataMap.get("resultList"));
		result.put("resultCnt", dataMap.get("resultCnt"));
		result.put("pageIndex", boardVO.getPageIndex());
		result.put("pageUnit", boardVO.getPageUnit());
		result.put("totalPageCount", pageInfo.getTotalPageCount());

		return result;
	}

	/** 📄 게시글 상세 페이지 */
	@GetMapping("/board/articleDetail.do")
	public String articleDetailPage(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") int nttId,
			Model model) {
		model.addAttribute("bbsId", bbsId);
		model.addAttribute("nttId", nttId);
		return "board/articleDetail.html";
	}

	/** 📑 게시글 상세 정보 (JSON) */
	@GetMapping("/board/articleDetail")
	@ResponseBody
	public Map<String, Object> articleDetail(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") int nttId, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		try {
			BoardVO vo = new BoardVO();
			vo.setBbsId(bbsId);
			vo.setNttId(nttId);

			BoardVO article = egovArticleService.selectArticleDetail(vo);
			
			FileVO fileVO = new FileVO();
			fileVO.setAtchFileId(article.getAtchFileId());
			List<FileVO> fileList = fileService.selectFileInfs(fileVO);

			// FileId를 유추하지 못하도록 세션ID와 함께 암호화하여 표시한다. (2022.12.06 추가) - 파일아이디가 유추 불가능하도록 조치
			for (FileVO file : fileList) {
				String sessionId = request.getSession().getId();
				String toEncrypt = sessionId + "|" + file.atchFileId;
				file.setAtchFileId(Base64.getEncoder().encodeToString(cryptoService.encrypt(toEncrypt).getBytes()));
			}

			result.put("fileList", fileList);
			result.put("fileListCnt", fileList.size());
			
			result.put("resultCode", "SUCCESS");
			result.put("article", article);
		
		} catch (Exception e) {
			result.put("resultCode", "FAIL");
			result.put("resultMessage", "오류 발생: " + e.getMessage());
		}
		return result;
	}

	/** 📝 게시글 등록 화면 */
	@GetMapping("/board/articleRegist.do")
	public String articleRegistPage(@RequestParam("bbsId") String bbsId, Model model) {
		if (!EgovUserDetailsHelper.isAuthenticated()) {
			return "redirect:/uat/uia/egovLoginUsr.do";
		}

		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		BoardMasterVO boardMaster = new BoardMasterVO();
		boardMaster.setBbsId(bbsId);
		boardMaster.setUniqId(user.getUniqId());

		try {
			boardMaster = egovBBSMasterService.selectBBSMasterInf(boardMaster);
		} catch (Exception e) {
			LOGGER.error("게시판 정보 조회 실패", e);
		}

		if (boardMaster.getTmplatCours() == null || boardMaster.getTmplatCours().isEmpty()) {
			boardMaster.setTmplatCours("/css/egovframework/com/cop/tpl/egovBaseTemplate.css");
		}

		model.addAttribute("boardMasterVO", boardMaster);
		return "board/articleRegist.html";
	}

	/** ✅ 게시글 등록 처리 (AJAX) */
	@PostMapping("/board/articleInsert.do")
	@ResponseBody
	public Map<String, Object> insertArticleAjax(final MultipartHttpServletRequest multiRequest) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			if (!EgovUserDetailsHelper.isAuthenticated()) {
				resultMap.put("resultCode", "FAIL");
				resultMap.put("resultMessage", "로그인이 필요합니다.");
				return resultMap;
			}

			LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

			BoardVO board = new BoardVO();
			board.setBbsId(multiRequest.getParameter("bbsId"));
			board.setNttSj(multiRequest.getParameter("nttSj"));
			board.setNttCn(unscript(multiRequest.getParameter("nttCn")));
			board.setNoticeAt(multiRequest.getParameter("noticeAt") != null ? "Y" : "N");
			board.setSecretAt(multiRequest.getParameter("secretAt") != null ? "Y" : "N");
			board.setAnonymousAt(multiRequest.getParameter("anonymousAt") != null ? "Y" : "N");
			board.setSjBoldAt(multiRequest.getParameter("sjBoldAt") != null ? "Y" : "N");
			board.setNtceBgnde(multiRequest.getParameter("ntceBgnde"));
			board.setNtceEndde(multiRequest.getParameter("ntceEndde"));

			// 등록자 처리
			if ("Y".equals(board.getAnonymousAt())) {
				board.setNtcrId("anonymous");
				board.setNtcrNm("익명");
				board.setFrstRegisterId("anonymous");
			} else {
				board.setNtcrId(user.getUniqId());
				board.setNtcrNm(user.getName());
				board.setFrstRegisterId(user.getUniqId());
			}

			// 첨부파일
			List<MultipartFile> files = multiRequest.getFiles("file");

			egovArticleService.insertArticleAndFiles(board, files);

			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMessage", "게시글이 등록되었습니다.");

		} catch (Exception e) {
			resultMap.put("resultCode", "FAIL");
			resultMap.put("resultMessage", "등록 중 오류 발생");
			e.printStackTrace();
		}

		return resultMap;
	}

	/** ✏️ 게시글 수정 화면 */
	@GetMapping("/board/articleUpdt.do")
	public String updateArticleView(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") int nttId,
			ModelMap model) throws Exception {

		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		BoardVO boardVO = new BoardVO();
		boardVO.setBbsId(bbsId);
		boardVO.setNttId(nttId);

		BoardVO article = egovArticleService.selectArticleDetail(boardVO);

		BoardMasterVO boardMasterVO = new BoardMasterVO();
		boardMasterVO.setBbsId(bbsId);
		boardMasterVO.setUniqId(user != null ? user.getUniqId() : "");

		BoardMasterVO boardMaster = egovBBSMasterService.selectBBSMasterInf(boardMasterVO);

		if ("anonymous".equals(article.getNtcrId())) {
			model.addAttribute("result", article);
			model.addAttribute("boardMasterVO", boardMaster);
			return "board/articleDetail.html";
		}

		model.addAttribute("articleVO", article);
		model.addAttribute("boardMasterVO", boardMaster);
		return "board/articleUpdt.html";
	}

	/** ✅ 게시글 수정 처리 (AJAX) */
	@PostMapping("/board/articleUpdate.do")
	@ResponseBody
	public Map<String, Object> updateArticle(MultipartHttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			if (user == null || user.getUniqId() == null) {
				resultMap.put("resultCode", "FAIL");
				resultMap.put("resultMessage", "로그인이 필요합니다.");
				return resultMap;
			}

			BoardVO board = new BoardVO();
			board.setBbsId(request.getParameter("bbsId"));
			board.setNttId(Integer.parseInt(request.getParameter("nttId")));
			board.setNttSj(request.getParameter("nttSj"));
			board.setNttCn(unscript(request.getParameter("nttCn")));
			board.setNoticeAt(request.getParameter("noticeAt") != null ? "Y" : "N");
			board.setSecretAt(request.getParameter("secretAt") != null ? "Y" : "N");
			board.setAnonymousAt(request.getParameter("anonymousAt") != null ? "Y" : "N");
			board.setSjBoldAt(request.getParameter("sjBoldAt") != null ? "Y" : "N");
			board.setNtceBgnde(request.getParameter("ntceBgnde"));
			board.setNtceEndde(request.getParameter("ntceEndde"));
			board.setLastUpdusrId(user.getUniqId());

			List<MultipartFile> files = request.getFiles("file");
			BoardVO origin = egovArticleService.selectArticleDetail(board);
			board.setAtchFileId(origin.getAtchFileId());

			egovArticleService.updateArticleAndFiles(board, files, board.getAtchFileId());

			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMessage", "게시글이 수정되었습니다.");
		} catch (Exception e) {
			resultMap.put("resultCode", "FAIL");
			resultMap.put("resultMessage", "수정 중 오류 발생");
			LOGGER.error("게시글 수정 오류", e);
		}
		return resultMap;
	}

	/** ❌ 게시글 삭제 처리 (AJAX) */
	@PostMapping("/board/articleDelete")
	@ResponseBody
	public Map<String, Object> deleteArticle(@RequestBody BoardVO boardVO, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

			if (!isAuthenticated || user == null || user.getUniqId() == null) {
				resultMap.put("resultCode", "FAIL");
				resultMap.put("resultMessage", "로그인이 필요합니다.");
				return resultMap;
			}

			LOGGER.debug("📌 게시글 삭제 요청 - bbsId={}, nttId={}", boardVO.getBbsId(), boardVO.getNttId());

			// 원글 조회
			BoardVO origin = egovArticleService.selectArticleDetail(boardVO);

			// 익명 글은 삭제 불가
			if ("anonymous".equals(origin.getNtcrId())) {
				resultMap.put("resultCode", "FAIL");
				resultMap.put("resultMessage", "익명으로 등록된 글은 삭제할 수 없습니다.");
				return resultMap;
			}

			// XSS 권한 체크
			EgovXssChecker.checkerUserXss(request, origin.getFrstRegisterId());

			// 삭제 수행
			Board deleteBoard = new Board();
			deleteBoard.setBbsId(boardVO.getBbsId());
			deleteBoard.setNttId(boardVO.getNttId());
			deleteBoard.setLastUpdusrId(user.getUniqId());

			egovArticleService.deleteArticle(deleteBoard);

			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMessage", "게시글이 성공적으로 삭제되었습니다.");

		} catch (Exception e) {
			LOGGER.error("게시글 삭제 오류", e);
			resultMap.put("resultCode", "FAIL");
			resultMap.put("resultMessage", "삭제 중 오류가 발생했습니다.");
		}

		return resultMap;
	}

	/**
	 * ✏️ 게시글 답글 작성 화면
	 */
	@GetMapping("/board/articleReply.do")
	public String articleReplyForm(@RequestParam("nttId") String nttId,
	                               @RequestParam("bbsId") String bbsId,
	                               Model model) throws Exception {

	    // 로그인 사용자 정보 확인
	    if (!EgovUserDetailsHelper.isAuthenticated()) {
	        return "redirect:/uat/uia/egovLoginUsr.do";
	    }

	    LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

	    // 게시판 정보
	    BoardMasterVO master = new BoardMasterVO();
	    master.setBbsId(bbsId);
	    master.setUniqId(user.getUniqId());
	    master = egovBBSMasterService.selectBBSMasterInf(master);

	    // 원본 게시글 정보
	    BoardVO origin = new BoardVO();
	    origin.setBbsId(bbsId);
	    origin.setNttId(Long.parseLong(nttId));
	    origin = egovArticleService.selectArticleDetail(origin);

	    // 모델에 데이터 전달
	    model.addAttribute("boardMasterVO", master);
	    model.addAttribute("origin", origin);
	    model.addAttribute("reply", new BoardVO());

	    return "board/articleReply";
	}

	/**
	 * 💾 게시글 답글 저장 (AJAX, JSON 방식)
	 */
	@PostMapping("/articleReply")
	@ResponseBody
	public Map<String, Object> saveArticleReply(@RequestBody BoardVO reply) throws Exception {
	    Map<String, Object> result = new HashMap<>();

	    if (!EgovUserDetailsHelper.isAuthenticated()) {
	        result.put("success", false);
	        result.put("message", "로그인이 필요합니다.");
	        return result;
	    }

	    LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

	    // 원글 정보 조회
	    BoardVO origin = new BoardVO();
	    origin.setBbsId(reply.getBbsId());
	    origin.setNttId(Long.parseLong(reply.getParnts()));
	    origin = egovArticleService.selectArticleDetail(origin);

	    if (origin == null) {
	        result.put("success", false);
	        result.put("message", "원본 게시글을 찾을 수 없습니다.");
	        return result;
	    }

	    // 답글 설정
	    reply.setReplyAt("Y"); // 답글 여부
	    reply.setSortOrdr(origin.getSortOrdr());
	    reply.setReplyLc(String.valueOf(Integer.parseInt(origin.getReplyLc()) + 1));
	    reply.setFrstRegisterId(user.getUniqId());
	    reply.setLastUpdusrId(user.getUniqId());

	    // 익명 처리
	    if ("Y".equals(reply.getAnonymousAt())) {
	        reply.setNtcrId("anonymous");
	        reply.setNtcrNm("익명");
	        reply.setFrstRegisterId("anonymous");
	    } else {
	        reply.setNtcrId(user.getId());
	        reply.setNtcrNm(user.getName());
	    }

	    // XSS 방지
	    reply.setNttCn(unscript(reply.getNttCn()));

	    try {
	        // 파일 없음 → 빈 리스트 전달
	        egovArticleService.insertArticleAndFiles(reply, List.of());
	        result.put("success", true);
	    } catch (Exception e) {
	        result.put("success", false);
	        result.put("message", "답글 등록 중 오류 발생: " + e.getMessage());
	    }

	    return result;
	}

}
