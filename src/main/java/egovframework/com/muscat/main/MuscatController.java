package egovframework.com.muscat.main;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cop.bbs.service.BoardVO;
import egovframework.com.cop.bbs.service.EgovArticleService;
import egovframework.com.muscat.main.mapper.MainMapper;

@Controller
public class MuscatController {
	
	@Resource(name = "EgovArticleService")
	private EgovArticleService egovArticleService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertyService;

	@Autowired
	private MainMapper mainMapper;
	
	// 메인 페이지
	@GetMapping("/main.do")
	public String main(HttpServletRequest request, Model model) {
		LoginVO loginUser = (LoginVO) request.getSession().getAttribute("loginVO");
	    model.addAttribute("user", loginUser);
		return "main.html";
	}

	// 메인 결재내역 게시판
	@GetMapping("/main/recentAppr.do")
	@ResponseBody
	public String recentAppr(HttpServletRequest request) {
		return "main.html";
	}

	// 메인 공지사항 게시판
	@GetMapping("/main/recentBoard.do")
	@ResponseBody
	public Map<String, Object> recentBoard(HttpServletRequest request) {
		
		Map<String, Object> result = new HashMap<>();
		
		// 공지사항 게시판 ID 조회
		String bbsId = mainMapper.selectBssId();
		
		BoardVO boardVO = new BoardVO();

		boardVO.setBbsId(bbsId);
		boardVO.setPageIndex(1);
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
		result.put("bbsId", bbsId);

		return result;
	
	}
	
	
	
}
