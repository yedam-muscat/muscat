package egovframework.com.muscat.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.muscat.common.ResultVO;
import egovframework.com.muscat.group.service.GroupService;
import egovframework.com.sec.ram.service.EgovAuthorManageService;
import egovframework.com.sym.ccm.cca.service.CmmnCode;
import egovframework.com.sym.ccm.cca.service.CmmnCodeVO;
import egovframework.com.sym.ccm.cca.service.EgovCcmCmmnCodeManageService;
import egovframework.com.sym.ccm.ccc.service.CmmnClCodeVO;
import egovframework.com.sym.ccm.ccc.service.EgovCcmCmmnClCodeManageService;
import egovframework.com.sym.ccm.cde.service.CmmnDetailCodeVO;
import egovframework.com.sym.ccm.cde.service.EgovCcmCmmnDetailCodeManageService;
import egovframework.com.uss.umt.service.EgovMberManageService;

@Controller
public class CodeController {

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

	@GetMapping("/admin/code/cmcMng.do")
	public String cmcMng(@ModelAttribute("searchVO") CmmnCodeVO searchVO, ModelMap model) throws Exception {

		// 미인증 사용자에 대한 보안처리
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		if (!isAuthenticated) {
			return "/main.do";
		}

		return "admin/cmcMng.html";
	}

	/** 공통코드 */

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
	public ResultVO cmcReg(@RequestBody CmmnCodeVO cmmnCodeVO, BindingResult bindingResult, ModelMap model)
			throws Exception {

		ResultVO result = new ResultVO();

		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		if (cmmnCodeVO.getCodeId() != null) {

			CmmnCode vo = cmmnCodeManageService.selectCmmnCodeDetail(cmmnCodeVO);
			if (vo != null) {
				result.setResultCode("");
				result.setResultMsg("이미 등록된 공통코드입니다");
				result.setResultSuccess(false);

				return result;
			}
		}

		cmmnCodeVO.setFrstRegisterId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
		cmmnCodeManageService.insertCmmnCode(cmmnCodeVO);

		result.setResultCode("");
		result.setResultMsg("공통코드가 등록되었습니다");
		result.setResultSuccess(true);

		return result;
	}

	/**
	 * 공통코드를 수정한다.
	 * 
	 * @param cmmnCodeVO
	 * @param status
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/admin/code/cmcModify.do")
	@ResponseBody
	public ResultVO cmcModify(@RequestBody CmmnCodeVO cmmnCodeVO, BindingResult bindingResult, ModelMap model)
			throws Exception {
		
		ResultVO result = new ResultVO();

		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		cmmnCodeVO.setLastUpdusrId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
		cmmnCodeManageService.updateCmmnCode(cmmnCodeVO);
		
		result.setResultCode("");
		result.setResultMsg("세부코드가 수정되었습니다");
		result.setResultSuccess(true);

		return result;
	}

	/** 공통상세코드 */

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

		CmmnCodeVO searchCodeVO = new CmmnCodeVO();
		searchCodeVO.setRecordCountPerPage(999999);
		searchCodeVO.setFirstIndex(0);
		searchCodeVO.setSearchCondition("clCode");
		searchCodeVO.setSearchKeyword("EFC");

		List<CmmnCodeVO> codeList = cmmnCodeManageService.selectCmmnCodeList(searchCodeVO);
		model.addAttribute("codeList", codeList);

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
	@ResponseBody
	public ResultVO insertCmmnDetailCode(@RequestBody CmmnDetailCodeVO cmmnDetailCodeVO, BindingResult bindingResult)
			throws Exception {

		ResultVO result = new ResultVO();

		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		if (cmmnDetailCodeVO.getCodeId() != null) {

			CmmnDetailCode vo = cmmnDetailCodeManageService.selectCmmnDetailCodeDetail(cmmnDetailCodeVO);
			if (vo != null) {
				result.setResultCode("");
				result.setResultMsg("이미 등록된 세부코드ID 입니다");
				result.setResultSuccess(false);
				return result;
			}
		}

		cmmnDetailCodeVO.setFrstRegisterId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
		cmmnDetailCodeManageService.insertCmmnDetailCode(cmmnDetailCodeVO);

		result.setResultCode("");
		result.setResultMsg("세부코드가 등록되었습니다");
		result.setResultSuccess(true);

		return result;
	}

	/**
	 * 공통상세코드를 수정한다.
	 *
	 * @param cmmnDetailCodeVO
	 * @param model
	 * @return "egovframework/com/sym/ccm/cde/EgovCcmCmmnDetailCodeUpdt",
	 *         "/sym/ccm/cde/SelectCcmCmmnDetailCodeList.do"
	 * @throws Exception
	 */
	@PostMapping("/admin/code/cmdcModify.do")
	@ResponseBody
	public ResultVO updateCmmnDetailCode(@RequestBody CmmnDetailCodeVO cmmnDetailCodeVO, BindingResult bindingResult)
			throws Exception {

		ResultVO result = new ResultVO();

		LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		cmmnDetailCodeVO.setLastUpdusrId((user == null || user.getUniqId() == null) ? "" : user.getUniqId());
		cmmnDetailCodeManageService.updateCmmnDetailCode(cmmnDetailCodeVO);

		result.setResultCode("");
		result.setResultMsg("세부코드가 수정되었습니다");
		result.setResultSuccess(true);

		return result;
	}

}
