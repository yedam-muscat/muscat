package egovframework.com.muscat.board.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.bbs.service.BoardMaster;
import egovframework.com.cop.bbs.service.BoardMasterVO;
import egovframework.com.cop.bbs.service.EgovBBSMasterService;

@Controller
public class BoardContoller {

	@Resource(name = "EgovBBSMasterService")
	private EgovBBSMasterService egovBBSMasterService;

	@Resource(name = "EgovCmmUseService")
	private EgovCmmUseService cmmUseService;

	@Resource(name = "propertiesService")
	protected EgovPropertyService propertyService;

	@Resource(name = "egovBBSMstrIdGnrService")
	private EgovIdGnrService idgenServiceBbs;

	@Resource(name = "egovBlogIdGnrService")
	private EgovIdGnrService idgenServiceBlog;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Autowired
	private DefaultBeanValidator beanValidator;

	/*
	 * 게시판 목록 페이지
	 */
	@GetMapping("/board/masterList.do")
	public String masterList() {
		return "board/masterList.html";
	}

	/*
	 * 게시판 목록 조회
	 */
	@GetMapping("/board/masterList")
	@ResponseBody
	public Map<String, Object> boardMaster(BoardMasterVO boardMasterVO) {
		boardMasterVO.setPageUnit(propertyService.getInt("pageUnit"));
		boardMasterVO.setPageSize(propertyService.getInt("pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(boardMasterVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(boardMasterVO.getPageUnit());
		paginationInfo.setPageSize(boardMasterVO.getPageSize());

		boardMasterVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		boardMasterVO.setLastIndex(paginationInfo.getLastRecordIndex());
		boardMasterVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		Map<String, Object> map = egovBBSMasterService.selectBBSMasterInfs(boardMasterVO);
		int totCnt = Integer.parseInt((String) map.get("resultCnt"));

		paginationInfo.setTotalRecordCount(totCnt);

		return map;
	}

	/*
	 * 게시판 등록 페이지
	 */
	@GetMapping("/board/masterRegist.do")
	public String masterRegist() {
		return "board/masterRegist.html";
	}

	/*
	 * 게시판 등록
	 */
	@PostMapping("/board/masterInsert")
	@ResponseBody
	public Map<String, Object> insertBoardMaster(@RequestBody BoardMaster boardMaster) {
		Map<String, Object> resultMap = new java.util.HashMap<>();

		try {
			LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

			if (!isAuthenticated) {
				resultMap.put("resultCode", "FAIL");
				resultMap.put("resultMessage", "로그인이 필요합니다.");
				return resultMap;
			}

			// 널 체크 직접 수행
			String uniqId = (user != null && user.getUniqId() != null) ? user.getUniqId() : "";
			boardMaster.setFrstRegisterId(uniqId);

			// Blog 여부 보정
			boardMaster.setBlogAt("Y".equals(boardMaster.getBlogAt()) ? "Y" : "N");

			egovBBSMasterService.insertBBSMasterInf(boardMaster);

			resultMap.put("resultCode", "SUCCESS");
			resultMap.put("resultMessage", "등록이 완료되었습니다.");
		} catch (Exception e) {
			resultMap.put("resultCode", "FAIL");
			resultMap.put("resultMessage", "오류 발생: " + e.getMessage());
		}

		return resultMap;
	}

	/*
	 * 게시판 상세 페이지
	 */
	@GetMapping("/board/masterDetail.do")
	public String masterDetailPage() {
		return "board/masterDetail.html";
	}

	/*
	 * 게시판 상세
	 */
	@GetMapping("/board/masterDetail")
	@ResponseBody
	public BoardMasterVO getBoardMasterDetail(@RequestParam("bbsId") String bbsId) throws Exception {
		BoardMasterVO searchVO = new BoardMasterVO();
		searchVO.setBbsId(bbsId);
		return egovBBSMasterService.selectBBSMasterInf(searchVO);
	}
	
	/*
	 * 게시판 삭제
	 */
	@PostMapping("/board/masterDelete")
	@ResponseBody
	public Map<String, Object> deleteBoardMaster(@RequestBody BoardMasterVO vo) {
	    Map<String, Object> result = new HashMap<>();
	    try {
	        egovBBSMasterService.deleteBBSMasterInf(vo);
	        result.put("resultCode", "SUCCESS");
	        result.put("resultMessage", "삭제되었습니다.");
	    } catch (Exception e) {
	        result.put("resultCode", "FAIL");
	        result.put("resultMessage", "삭제 실패: " + e.getMessage());
	    }
	    return result;
	}
}
