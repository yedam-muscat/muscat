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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.muscat.common.ResultVO;
import egovframework.com.muscat.group.service.GroupService;
import egovframework.com.muscat.group.service.GroupVO;
import egovframework.com.sec.ram.service.AuthorManageVO;
import egovframework.com.sec.ram.service.EgovAuthorManageService;
import egovframework.com.sym.ccm.cca.service.CmmnCode;
import egovframework.com.sym.ccm.cca.service.CmmnCodeVO;
import egovframework.com.sym.ccm.cca.service.EgovCcmCmmnCodeManageService;
import egovframework.com.sym.ccm.ccc.service.CmmnClCodeVO;
import egovframework.com.sym.ccm.ccc.service.EgovCcmCmmnClCodeManageService;
import egovframework.com.sym.ccm.cde.service.CmmnDetailCodeVO;
import egovframework.com.sym.ccm.cde.service.EgovCcmCmmnDetailCodeManageService;
import egovframework.com.uss.umt.service.EgovMberManageService;
import egovframework.com.uss.umt.service.MberManageVO;
import egovframework.com.uss.umt.service.UserDefaultVO;

//@Controller
public class AdminController {

	/** mberManageService */
	@Resource(name = "mberManageService")
	private EgovMberManageService mberManageService;

	/** cmmUseService */
	@Resource(name = "EgovCmmUseService")
	private EgovCmmUseService cmmUseService;

	@Resource(name = "egovAuthorManageService")
	private EgovAuthorManageService egovAuthorManageService;

	@Resource(name = "CmmnCodeManageService")
	private EgovCcmCmmnCodeManageService cmmnCodeManageService;

	@Resource(name = "CmmnClCodeManageService")
	private EgovCcmCmmnClCodeManageService cmmnClCodeManageService;

	@Resource(name = "CmmnDetailCodeManageService")
	private EgovCcmCmmnDetailCodeManageService cmmnDetailCodeManageService;

	/** Egov Common */
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** DefaultBeanValidator beanValidator */
	@Autowired
	private DefaultBeanValidator beanValidator;

	/** group service */
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
		ComDefaultCodeVO comDefaultCodeVO = new ComDefaultCodeVO();
		comDefaultCodeVO.setCodeId("COM013");
		List<CmmnDetailCode> mberSttus_result = cmmUseService.selectCmmCodeDetail(comDefaultCodeVO);
		resultMap.put("entrprsMberSttus_result", mberSttus_result);// 기업회원상태코드목록

		return resultMap;
	}

	// user 상세 조회

	@GetMapping("/admin/user/userDetail.do")
	public String userDetail(@RequestParam("selectedId") String mberId,
			@ModelAttribute("searchVO") UserDefaultVO userSearchVO, HttpServletRequest request, Model model)
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

		// 권한정보 조회
		List<AuthorManageVO> auth_result = egovAuthorManageService.selectAuthorAllList(new AuthorManageVO());

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
		model.addAttribute("auth_result", auth_result);

		MberManageVO mberManageVO = mberManageService.selectMber(mberId);
		model.addAttribute("mberManageVO", mberManageVO);
		model.addAttribute("userSearchVO", userSearchVO);

		return "admin/userDetail.html";
	}

	@PostMapping("/admin/user/modifyUser.do")
	@ResponseBody
	public ResultVO modifyUser(@RequestBody MberManageVO mberManageVO) throws Exception {

		ResultVO result = new ResultVO();

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			result.setResultCode("");
			result.setResultSuccess(false);
			result.setResultMsg("수정 권한 없음");
			return result;
		}

		if ("".equals(mberManageVO.getGroupId())) {// KISA 보안약점 조치 (2018-10-29, 윤창원)
			mberManageVO.setGroupId(null);
		}
		mberManageService.updateMber(mberManageVO);

		result.setResultCode("");
		result.setResultSuccess(true);
		result.setResultMsg("수정 완료");

		return result;
	}

	@GetMapping("/admin/code/cmcMng.do")
	public String cmcMng(@ModelAttribute("searchVO") CmmnCodeVO searchVO, ModelMap model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			return "/main.do";
		}

		return "admin/cmcMng.html";
	}

	/**
	 * 공통분류코드 목록을 조회한다.
	 * 
	 * @param searchVO
	 * @param model
	 * @return "egovframework/com/sym/ccm/cca/EgovCcmCmmnCodeList"
	 * @throws Exception
	 */
	@GetMapping("/admin/code/cmcList.do")
	@ResponseBody
	public Map<String, Object> cmcList(@ModelAttribute("searchVO") CmmnCodeVO searchVO, ModelMap model)
			throws Exception {

		Map<String, Object> resultMap = new HashMap<>();

		/** EgovPropertyService.sample */
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<CmmnCodeVO> CmmnCodeList = cmmnCodeManageService.selectCmmnCodeList(searchVO);
		resultMap.put("resultList", CmmnCodeList);

		int totCnt = cmmnCodeManageService.selectCmmnCodeListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		resultMap.put("paginationInfo", paginationInfo);

		return resultMap;
	}

	/**
	 * 공통코드 상세항목을 조회한다.
	 * 
	 * @param loginVO
	 * @param cmmnCodeVO
	 * @param model
	 * @return "egovframework/com/sym/ccm/cca/EgovCcmCmmnCodeDetail"
	 * @throws Exception
	 */
	@GetMapping("/admin/code/cmcDetail.do")
	public String cmcDetail(@ModelAttribute("loginVO") LoginVO loginVO, CmmnCodeVO cmmnCodeVO, ModelMap model)
			throws Exception {

		CmmnCodeVO vo = cmmnCodeManageService.selectCmmnCodeDetail(cmmnCodeVO);

		model.addAttribute("result", vo);

		return "admin/cmcDetail.html";
	}

	/**
	 * 공통코드 등록을 위한 등록페이지로 이동한다.
	 * 
	 * @param cmmnCodeVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/admin/code/cmcReg.do")
	public String cmcReg(@ModelAttribute("cmmnCodeVO") CmmnCodeVO cmmnCodeVO, ModelMap model) throws Exception {

		CmmnClCodeVO searchVO = new CmmnClCodeVO();
		searchVO.setFirstIndex(0);
		List<CmmnClCodeVO> clCodeList = cmmnClCodeManageService.selectCmmnClCodeList(searchVO);

		model.addAttribute("clCodeList", clCodeList);

		return "admin/cmcReg.html";
	}

	/**
	 * 공통코드를 등록한다.
	 * 
	 * @param CmmnCodeVO
	 * @param CmmnCodeVO
	 * @param status
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/admin/code/cmcReg.do")
	@ResponseBody
	public ResultVO cmcReg(@ModelAttribute("searchVO") CmmnCodeVO cmmnCode,
			@ModelAttribute("cmmnCodeVO") CmmnCodeVO cmmnCodeVO, BindingResult bindingResult, ModelMap model)
			throws Exception {

		ResultVO result = new ResultVO();

		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		if (cmmnCode.getCodeId() != null) {
			CmmnCode vo = cmmnCodeManageService.selectCmmnCodeDetail(cmmnCode);
			if (vo != null) {
				result.setResultCode("");
				result.setResultMsg("공통코드 등록 실패");
				result.setResultSuccess(false);
			}
		}

		cmmnCodeVO.setFrstRegisterId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
		cmmnCodeManageService.insertCmmnCode(cmmnCodeVO);

		result.setResultCode("");
		result.setResultMsg("공통코드 등록 성공");
		result.setResultSuccess(true);

		return result;
	}

	@GetMapping("/admin/code/cmdcMng.do")
	public String cmdcMng(@ModelAttribute("loginVO") LoginVO loginVO,
			@ModelAttribute("searchVO") CmmnDetailCodeVO searchVO, ModelMap model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			return "/main.do";
		}

		return "admin/cmdcMng.html";
	}

	/**
	 * 공통상세코드 목록을 조회한다.
	 * 
	 * @param loginVO
	 * @param searchVO
	 * @param model
	 * @return "egovframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeList"
	 * @throws Exception
	 */
	@GetMapping("/admin/code/cmdcList.do")
	@ResponseBody
	public Map<String, Object> cmdcList(@ModelAttribute("loginVO") LoginVO loginVO,
			@ModelAttribute("searchVO") CmmnDetailCodeVO searchVO, ModelMap model) throws Exception {

		Map<String, Object> resultMap = new HashMap<>();

		/** EgovPropertyService.sample */
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** pageing */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<CmmnDetailCodeVO> CmmnCodeList = cmmnDetailCodeManageService.selectCmmnDetailCodeList(searchVO);
		resultMap.put("resultList", CmmnCodeList);

		int totCnt = cmmnDetailCodeManageService.selectCmmnDetailCodeListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		resultMap.put("paginationInfo", paginationInfo);

		return resultMap;
	}

	/**
	 * 공통상세코드 상세항목을 조회한다.
	 * 
	 * @param loginVO
	 * @param cmmnDetailCodeVO
	 * @param model
	 * @return "egovframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeDetail"
	 * @throws Exception
	 */
	@GetMapping("/admin/code/cmdcDetail.do")
	public String cmdcDetail(@ModelAttribute("loginVO") LoginVO loginVO, CmmnDetailCodeVO cmmnDetailCodeVO,
			ModelMap model) throws Exception {
		CmmnDetailCode vo = cmmnDetailCodeManageService.selectCmmnDetailCodeDetail(cmmnDetailCodeVO);
		model.addAttribute("result", vo);

		return "admin/cmdcDetail.html";
	}

	/**
	 * 공통상세코드 등록을 위한 등록페이지로 이동한다.
	 *
	 * @param cmmnDetailCodeVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/admin/code/cmdcReg.do")
	public String cmdcReg(@ModelAttribute("loginVO") LoginVO loginVO,
			@ModelAttribute("cmmnCodeVO") CmmnCodeVO cmmnCodeVO,
			@ModelAttribute("cmmnDetailCodeVO") CmmnDetailCodeVO cmmnDetailCodeVO, ModelMap model) throws Exception {

		CmmnClCodeVO searchClCodeVO = new CmmnClCodeVO();
		searchClCodeVO.setFirstIndex(0);
		List<CmmnClCodeVO> clCodeList = cmmnClCodeManageService.selectCmmnClCodeList(searchClCodeVO);
		model.addAttribute("clCodeList", clCodeList);

		CmmnCodeVO clCode = new CmmnCodeVO();
		clCode.setClCode(cmmnCodeVO.getClCode());

		if (!cmmnCodeVO.getClCode().equals("")) {

			CmmnCodeVO searchCodeVO = new CmmnCodeVO();
			searchCodeVO.setRecordCountPerPage(999999);
			searchCodeVO.setFirstIndex(0);
			searchCodeVO.setSearchCondition("clCode");
			searchCodeVO.setSearchKeyword(cmmnCodeVO.getClCode());

			List<CmmnCodeVO> codeList = cmmnCodeManageService.selectCmmnCodeList(searchCodeVO);
			model.addAttribute("codeList", codeList);
		}

		return "admin/cmdcReg.html";
	}

	/**
	 * 공통상세코드를 등록한다.
	 *
	 * @param CmmnDetailCodeVO
	 * @param CmmnDetailCodeVO
	 * @param status
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/admin/code/cmdcReg.do")
	public String insertCmmnDetailCode(@ModelAttribute("cmmnDetailCodeVO") CmmnDetailCodeVO cmmnDetailCodeVO,
			BindingResult bindingResult, ModelMap model) throws Exception {

		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		CmmnClCodeVO searchClCodeVO = new CmmnClCodeVO();
		searchClCodeVO.setFirstIndex(0);

		beanValidator.validate(cmmnDetailCodeVO, bindingResult);

		if (bindingResult.hasErrors()) {

			List<CmmnClCodeVO> clCodeList = cmmnClCodeManageService.selectCmmnClCodeList(searchClCodeVO);
			model.addAttribute("clCodeList", clCodeList);

			return "egovframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeRegist";
		}

		if (cmmnDetailCodeVO.getCodeId() != null) {

			CmmnDetailCode vo = cmmnDetailCodeManageService.selectCmmnDetailCodeDetail(cmmnDetailCodeVO);
			if (vo != null) {
				model.addAttribute("message", egovMessageSource.getMessage("comSymCcmCde.validate.codeCheck"));

				List<CmmnClCodeVO> clCodeList = cmmnClCodeManageService.selectCmmnClCodeList(searchClCodeVO);
				model.addAttribute("clCodeList", clCodeList);

				return "egovframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeRegist";
			}
		}

		cmmnDetailCodeVO.setFrstRegisterId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
		cmmnDetailCodeManageService.insertCmmnDetailCode(cmmnDetailCodeVO);

		model.addAttribute("searchCondition", cmmnDetailCodeVO.getSearchCondition());
		model.addAttribute("searchKeyword", cmmnDetailCodeVO.getSearchKeyword());
		model.addAttribute("pageIndex", cmmnDetailCodeVO.getPageIndex());

		return "redirect:/sym/ccm/cde/SelectCcmCmmnDetailCodeList.do";
	}
}
