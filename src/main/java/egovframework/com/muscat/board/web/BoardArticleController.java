package egovframework.com.muscat.board.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.bbs.service.BoardMasterVO;
import egovframework.com.cop.bbs.service.BoardVO;
import egovframework.com.cop.bbs.service.EgovArticleService;
import egovframework.com.cop.bbs.service.EgovBBSMasterService;
import egovframework.com.cop.bbs.service.EgovBBSSatisfactionService;
import egovframework.com.cop.cmt.service.EgovArticleCommentService;
import egovframework.com.cop.tpl.service.EgovTemplateManageService;

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

	@Resource(name = "EgovArticleCommentService")
	protected EgovArticleCommentService egovArticleCommentService;

	@Resource(name = "EgovBBSSatisfactionService")
	private EgovBBSSatisfactionService bbsSatisfactionService;

	@Resource(name = "EgovTemplateManageService")
	private EgovTemplateManageService egovTemplateManageService;

	@Autowired
	private DefaultBeanValidator beanValidator;

	/**
	 * XSS 방지 처리.
	 * 
	 * @param data
	 * @return
	 */
	protected String unscript(String data) {
		if (data == null || data.trim().equals("")) {
			return "";
		}

		String ret = data;

		ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
		ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");

		ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
		ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");

		ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
		ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");

		ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
		ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");

		ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
		ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");

		return ret;
	}

	/*
	 * 게시글 목록 페이지
	 */
	@GetMapping("/board/articleList.do")
	public String articleListPage(@RequestParam("bbsId") String bbsId, Model model) throws Exception {
		BoardMasterVO masterVO = new BoardMasterVO();
		masterVO.setBbsId(bbsId);

		BoardMasterVO boardInfo = egovBBSMasterService.selectBBSMasterInf(masterVO);

		model.addAttribute("bbsId", bbsId); // URL에 같이 전달된 게시판 ID
		model.addAttribute("boardInfo", boardInfo); // 게시판 정보 (이름, 설명 등)

		return "board/articleList.html";
	}

	/*
	 * 게시물 목록 (JSON, DataTable용)
	 */
	@GetMapping("/board/articleList")
	@ResponseBody
	public Map<String, Object> articleList(@ModelAttribute("searchVO") BoardVO boardVO,
			@RequestParam("bbsId") String bbsId) throws Exception {
		Map<String, Object> result = new HashMap<>();

		// 1. 로그인 사용자 정보 확인
		if (!EgovUserDetailsHelper.isAuthenticated()) {
			result.put("resultCode", "FAIL");
			result.put("resultMessage", "로그인이 필요합니다.");
			return result;
		}

		// 2. 게시판 ID 설정
		boardVO.setBbsId(bbsId);

		// 3. 페이징 처리
		boardVO.setPageUnit(propertyService.getInt("pageUnit"));
		boardVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(boardVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardVO.getPageUnit());
		paginationInfo.setPageSize(boardVO.getPageSize());

		boardVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		// 4. 게시글 목록 조회
		Map<String, Object> map = egovArticleService.selectArticleList(boardVO);
		int totalCount = Integer.parseInt((String) map.get("resultCnt"));
		paginationInfo.setTotalRecordCount(totalCount);

		// 5. 결과 응답 구성
		result.put("resultCode", "SUCCESS");
		result.put("resultList", map.get("resultList"));
		result.put("resultCnt", map.get("resultCnt"));
		result.put("pageIndex", boardVO.getPageIndex());
		result.put("pageUnit", boardVO.getPageUnit());
		result.put("totalPageCount", paginationInfo.getTotalPageCount());

		return result;
	}

	/*
	 * 게시물 상세 페이지 → articleDetail.html 화면 렌더링
	 */
	@GetMapping("/board/articleDetail.do")
	public String articleDetailPage(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") int nttId,
			Model model) {
		// URL 파라미터를 화면에 전달
		model.addAttribute("bbsId", bbsId);
		model.addAttribute("nttId", nttId);
		return "board/articleDetail.html";
	}

	/*
	 * 게시물 상세 정보 조회 (AJAX 요청) → JSON으로 반환
	 */
	@GetMapping("/board/articleDetail")
	@ResponseBody
	public Map<String, Object> articleDetail(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") int nttId) {
		Map<String, Object> result = new HashMap<>();
		try {
			BoardVO vo = new BoardVO();
			vo.setBbsId(bbsId);
			vo.setNttId(nttId);

			BoardVO article = egovArticleService.selectArticleDetail(vo);

			result.put("resultCode", "SUCCESS");
			result.put("article", article);
		} catch (Exception e) {
			result.put("resultCode", "FAIL");
			result.put("resultMessage", "오류 발생: " + e.getMessage());
		}

		return result;
	}

	/**
	 * 게시글 등록 페이지 이동 (articleRegist.html 렌더링)
	 */
	@GetMapping("/board/articleRegist.do")
	public String articleRegistPage(@RequestParam("bbsId") String bbsId, Model model) {
		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		BoardMasterVO boardMasterVO = new BoardMasterVO();
		if (isAuthenticated) {
			boardMasterVO.setBbsId(bbsId);
			boardMasterVO.setUniqId(user != null ? user.getUniqId() : "");
			try {
				boardMasterVO = egovBBSMasterService.selectBBSMasterInf(boardMasterVO);
			} catch (Exception e) {
				// 예외 로깅
			}
		}

		// 템플릿 경로 보정
		if (boardMasterVO.getTmplatCours() == null || boardMasterVO.getTmplatCours().isEmpty()) {
			boardMasterVO.setTmplatCours("/css/egovframework/com/cop/tpl/egovBaseTemplate.css");
		}

		model.addAttribute("boardMasterVO", boardMasterVO);
		return "board/articleRegist.html"; // articleRegist.html
	}

	/**
	 * 게시글 등록 처리 (AJAX용, 파일 제외)
	 */
	@PostMapping("/board/articleInsert")
	@ResponseBody
	public Map<String, Object> insertArticle(@RequestParam("bbsId") String bbsId, @RequestParam("nttSj") String nttSj,
			@RequestParam("nttCn") String nttCn, @RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			if (!EgovUserDetailsHelper.isAuthenticated()) {
				resultMap.put("resultCode", "FAIL");
				resultMap.put("message", "로그인이 필요합니다.");
				return resultMap;
			}

			LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

			BoardVO board = new BoardVO();
			board.setBbsId(bbsId);
			board.setNttSj(nttSj);
			board.setNttCn(unscript(nttCn));
			board.setFrstRegisterId(user.getUniqId());
			board.setNtcrId(user.getUniqId());
			board.setNtcrNm(user.getName());

			// 파일 리스트로 래핑
			List<MultipartFile> files = new ArrayList<>();
			if (file != null && !file.isEmpty()) {
				files.add(file);
			}

			egovArticleService.insertArticleAndFiles(board, files);

			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("message", "게시글이 등록되었습니다.");
		} catch (Exception e) {
			resultMap.put("resultCode", "FAIL");
			resultMap.put("message", "오류 발생: " + e.getMessage());
		}

		return resultMap;
	}

	/**
	 * 게시글 수정 화면 이동 (articleEdit.html 렌더링)
	 */
	@GetMapping("/board/articleEdit.do")
	public String articleEditPage(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") int nttId, Model model) {
		// bbsId와 nttId를 화면에 넘겨서 상세조회 및 초기 세팅에 사용
		model.addAttribute("bbsId", bbsId);
		model.addAttribute("nttId", nttId);
		return "board/articleEdit.html";
	}

	/**
	 * 게시글 수정 처리 (AJAX용)
	 */
	@PostMapping("/board/articleUpdate")
	@ResponseBody
	public Map<String, Object> updateArticle(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") int nttId,
			@RequestParam("nttSj") String nttSj, @RequestParam("nttCn") String nttCn,
			@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {

		Map<String, Object> resultMap = new HashMap<>();

		try {
			// 로그인 체크
			if (!EgovUserDetailsHelper.isAuthenticated()) {
				resultMap.put("resultCode", "FAIL");
				resultMap.put("message", "로그인이 필요합니다.");
				return resultMap;
			}

			LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

			// 게시글 정보 설정
			BoardVO board = new BoardVO();
			board.setBbsId(bbsId);
			board.setNttId(nttId);
			board.setNttSj(nttSj);
			board.setNttCn(unscript(nttCn));
			board.setLastUpdusrId(user.getUniqId());

			// 파일 처리
			List<MultipartFile> files = new ArrayList<>();
			if (file != null && !file.isEmpty()) {
				files.add(file);
			}

			// 게시글 수정 서비스 호출
			egovArticleService.updateArticleAndFiles(board, files);

			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("message", "게시글이 수정되었습니다.");
		} catch (Exception e) {
			resultMap.put("resultCode", "FAIL");
			resultMap.put("message", "오류 발생: " + e.getMessage());
		}

		return resultMap;
	}

	/**
	 * 게시글 삭제 처리 (AJAX 요청)
	 */
	@PostMapping("/board/articleDelete")
	@ResponseBody
	public Map<String, Object> deleteArticle(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") int nttId) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			// 로그인 확인
			if (!EgovUserDetailsHelper.isAuthenticated()) {
				resultMap.put("resultCode", "FAIL");
				resultMap.put("message", "로그인이 필요합니다.");
				return resultMap;
			}

			// 삭제할 게시글 VO 구성
			BoardVO board = new BoardVO();
			board.setBbsId(bbsId);
			board.setNttId(nttId);

			// 게시글 삭제 서비스 호출
			egovArticleService.deleteArticle(board);

			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("message", "게시글이 삭제되었습니다.");
		} catch (Exception e) {
			resultMap.put("resultCode", "FAIL");
			resultMap.put("message", "오류 발생: " + e.getMessage());
		}

		return resultMap;
	}
}
