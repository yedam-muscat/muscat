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
public class BoardMasterContoller {

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
	@GetMapping("/board/masterList") // 클라이언트에서 /board/masterList로 GET 요청이 들어오면 이 메서드를 실행
	@ResponseBody // 메서드 반환값(Map)을 JSON으로 변환해서 HTTP 응답 본문에 직접 출력
	public Map<String, Object> boardMaster(BoardMasterVO boardMasterVO) {
		boardMasterVO.setPageUnit(propertyService.getInt("pageUnit")); // 한 페이지에 보여줄 목록 수를 설정값에서 가져와 세팅
		boardMasterVO.setPageSize(propertyService.getInt("pageSize")); // 하단에 표시할 페이지 번호 개수를 설정값에서 가져와 세팅

		PaginationInfo paginationInfo = new PaginationInfo(); // 전자정부프레임워크의 페이징 계산 도우미 객체 생성

		paginationInfo.setCurrentPageNo(boardMasterVO.getPageIndex()); // 현재 페이지 번호 설정 (기본은 1)
		paginationInfo.setRecordCountPerPage(boardMasterVO.getPageUnit()); // 페이지당 데이터 개수 설정
		paginationInfo.setPageSize(boardMasterVO.getPageSize()); // 페이지 번호 몇 개 보여줄지 설정

		boardMasterVO.setFirstIndex(paginationInfo.getFirstRecordIndex()); // 시작 인덱스 계산 후 VO에 설정 (예: 0, 10, 20)
		boardMasterVO.setLastIndex(paginationInfo.getLastRecordIndex()); // 끝 인덱스 계산 후 VO에 설정 (일반적으로 사용 안됨)
		boardMasterVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage()); // DAO에서 LIMIT 처리용으로 사용

		Map<String, Object> map = egovBBSMasterService.selectBBSMasterInfs(boardMasterVO); // 실제 DB에서 목록과 전체 개수 조회
		int totCnt = Integer.parseInt((String) map.get("resultCnt")); // 조회된 전체 게시판 수를 String → int로 변환

		paginationInfo.setTotalRecordCount(totCnt); // 총 레코드 수를 paginationInfo에 세팅 (뷰에서 페이지 계산용)

		return map; // resultList, resultCnt 등이 담긴 Map을 JSON 응답으로 반환
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

	/*
	 * 게시판 수정 페이지
	 */
	@GetMapping("/board/masterUpdt.do")
	public String masterEditPage() {
		return "board/masterUpdt.html";
	}

	/*
	 * 게시판 수정 처리
	 */
	@PostMapping("/board/masterUpdt")
	@ResponseBody
	public Map<String, Object> updateBoardMaster(@RequestBody BoardMaster boardMaster) {
		Map<String, Object> result = new HashMap<>();
		try {
			LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

			if (!isAuthenticated) {
				result.put("resultCode", "FAIL");
				result.put("resultMessage", "로그인이 필요합니다.");
				return result;
			}

			// 유효성 검사 (서버 측 간단 검증 예시)
			if (boardMaster.getBbsId() == null || boardMaster.getBbsId().isEmpty()) {
				result.put("resultCode", "FAIL");
				result.put("resultMessage", "게시판 ID가 없습니다.");
				return result;
			}

			// 최종 수정자 ID 설정
			boardMaster.setLastUpdusrId(user != null ? user.getUniqId() : "");

			// 옵션값 비어있으면 기본값 처리
			if (boardMaster.getOption() == null || boardMaster.getOption().isEmpty()) {
				boardMaster.setOption("na");
			}

			egovBBSMasterService.updateBBSMasterInf(boardMaster);

			result.put("resultCode", "SUCCESS");
			result.put("resultMessage", "수정이 완료되었습니다.");
		} catch (Exception e) {
			result.put("resultCode", "FAIL");
			result.put("resultMessage", "오류 발생: " + e.getMessage());
		}
		return result;
	}
}
