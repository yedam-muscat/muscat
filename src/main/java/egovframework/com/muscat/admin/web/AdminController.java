package egovframework.com.muscat.admin.web;

import java.util.List;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
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
	public String userMng(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO, ModelMap model)
			throws Exception {

		// 미인증 사용자에 대한 보안처리
//		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
//		if (!isAuthenticated) {
//			return "/main.do";
//		}

//		List<MberManageVO> resultList = mberManageService.selectMberList(userSearchVO);
//		model.addAttribute("resultList", resultList);

		// 일반회원 상태코드를 코드정보로부터 조회
		ComDefaultCodeVO comDefaultCodeVO = new ComDefaultCodeVO();
		comDefaultCodeVO.setCodeId("COM013");
		List<CmmnDetailCode> mberSttus_result = cmmUseService.selectCmmCodeDetail(comDefaultCodeVO);
		model.addAttribute("entrprsMberSttus_result", mberSttus_result);// 기업회원상태코드목록

		return "admin/userMng.html";
	}
	
	// user 목록 조회
	@GetMapping("/admin/user/userList.do")
	@ResponseBody
	public List<MberManageVO> userList(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO)
			throws Exception {
		return mberManageService.selectMberList(userSearchVO);
	}
}
