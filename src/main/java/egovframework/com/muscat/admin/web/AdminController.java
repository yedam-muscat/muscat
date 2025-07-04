package egovframework.com.muscat.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
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
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.muscat.common.ResultVO;
import egovframework.com.muscat.group.service.GroupService;
import egovframework.com.muscat.group.service.GroupVO;
import egovframework.com.uss.umt.service.EgovMberManageService;
import egovframework.com.uss.umt.service.MberManageVO;
import egovframework.com.uss.umt.service.UserDefaultVO;

@Controller
public class AdminController {

	/** mberManageService */
	@Resource(name = "mberManageService")
	private EgovMberManageService mberManageService;

	/** cmmUseService */
	@Resource(name = "EgovCmmUseService")
	private EgovCmmUseService cmmUseService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** DefaultBeanValidator beanValidator */
	@Autowired
	private DefaultBeanValidator beanValidator;
	
	/** */
	@Autowired
	private GroupService groupService;

	/**
	 * 일반회원목록을 조회한다. (pageing)
	 *
	 * @param userSearchVO 검색조건정보
	 * @param model        화면모델
	 * @return uss/umt/EgovMberManage
	 * @throws Exception
	 */

	// user

	// user 목록 페이지
	@GetMapping("/admin/user/userMng.do")
	public String userMng(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO, ModelMap model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			return "/main.do";
		}

		return "admin/userMng.html";
	}

	// user 목록 조회
	@GetMapping("/admin/user/userList.do")
	@ResponseBody
	public Map<String, Object> userList(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO) throws Exception {

		Map<String, Object> resultMap = new HashMap<>();

		/** EgovPropertyService */
		userSearchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		userSearchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(userSearchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(userSearchVO.getPageUnit());
		paginationInfo.setPageSize(userSearchVO.getPageSize());

		userSearchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		userSearchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		userSearchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<MberManageVO> resultList = mberManageService.selectMberList(userSearchVO);
		resultMap.put("resultList", resultList);

		int totCnt = mberManageService.selectMberListTotCnt(userSearchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		resultMap.put("paginationInfo", paginationInfo);

		// 일반회원 상태코드를 코드정보로부터 조회
//        ComDefaultCodeVO comDefaultCodeVO = new ComDefaultCodeVO();
//        comDefaultCodeVO.setCodeId("COM013");
//        List<CmmnDetailCode> mberSttus_result = cmmUseService.selectCmmCodeDetail(comDefaultCodeVO);
//        resultMap.put("entrprsMberSttus_result", mberSttus_result);// 기업회원상태코드목록

		return resultMap;
	}

	// user 상세 조회

	@GetMapping("/admin/user/userDetail.do")
	public String userDetail(@RequestParam("selectedId") String mberId,
			@ModelAttribute("searchVO") UserDefaultVO userSearchVO, 
			HttpServletRequest request, 
			Model model)
			throws Exception {

		ComDefaultCodeVO vo = new ComDefaultCodeVO();

		// 패스워드힌트목록을 코드정보로부터 조회
		vo.setCodeId("COM022");
		List<CmmnDetailCode> passwordHint_result = cmmUseService.selectCmmCodeDetail(vo);

		// 성별구분코드를 코드정보로부터 조회
		vo.setCodeId("COM014");
		List<CmmnDetailCode> sexdstnCode_result = cmmUseService.selectCmmCodeDetail(vo);

		// 사용자상태코드를 코드정보로부터 조회
		vo.setCodeId("COM013");
		List<CmmnDetailCode> mberSttus_result = cmmUseService.selectCmmCodeDetail(vo);

		// 그룹정보를 조회 - GROUP_ID정보
		vo.setTableNm("COMTNORGNZTINFO");
		List<CmmnDetailCode> groupId_result = cmmUseService.selectGroupIdDetail(vo);

		// 직급코드를 코드정보로부터 조회
		vo.setCodeId("COM103");
		List<CmmnDetailCode> rank_result = cmmUseService.selectCmmCodeDetail(vo);
		
		// 부서정보 조회
		List<GroupVO> dept_result = groupService.getGroupChartData();

		Map<Boolean, List<GroupVO>> part = dept_result.stream()
				.collect(Collectors.partitioningBy(d -> "#".equals(d.getParent())));

		List<GroupVO> parent = part.get(true);
		List<GroupVO> child = part.get(false);
		

		model.addAttribute("passwordHint_result", passwordHint_result); // 패스워트힌트목록
		model.addAttribute("sexdstnCode_result", sexdstnCode_result); // 성별구분코드목록
		model.addAttribute("mberSttus_result", mberSttus_result); // 사용자상태코드목록
		model.addAttribute("groupId_result", groupId_result); // 그룹정보 목록
		model.addAttribute("rank_result", rank_result);
		model.addAttribute("parent_dept_result", parent);
		model.addAttribute("child_dept_result", child);

		MberManageVO mberManageVO = mberManageService.selectMber(mberId);
		model.addAttribute("mberManageVO", mberManageVO);
		model.addAttribute("userSearchVO", userSearchVO);

		return "admin/userDetail.html";
	}

	@PostMapping("/admin/user/modifyUser.do")
	@ResponseBody
	public ResultVO modifyUser(@RequestBody MberManageVO mberManageVO)
			throws Exception {
		
		ResultVO result = new ResultVO();

		
		
		
		
		return result;
	}
}
