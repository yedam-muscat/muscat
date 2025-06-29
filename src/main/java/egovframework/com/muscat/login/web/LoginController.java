package egovframework.com.muscat.login.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.EgovComponentChecker;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.config.EgovLoginConfig;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.muscat.common.ResultVO;
import egovframework.com.uat.uia.service.EgovLoginService;
import egovframework.com.uss.umt.service.EgovMberManageService;
import egovframework.com.uss.umt.service.MberManageVO;
import egovframework.com.utl.sim.service.EgovClntInfo;

/* 
 * package egovframework.com.uat.uia.web
 * EgovLoginController 참고
 * 
 */
@Controller
public class LoginController {

	/** EgovLoginService */
	@Resource(name = "loginService")
	private EgovLoginService loginService;

	/** EgovCmmUseService */
	@Resource(name = "EgovCmmUseService")
	private EgovCmmUseService cmmUseService;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "egovLoginConfig")
	EgovLoginConfig egovLoginConfig;

	/** mberManageService */
	@Resource(name = "mberManageService")
	private EgovMberManageService mberManageService;

	/** DefaultBeanValidator beanValidator */
	@Autowired
	private DefaultBeanValidator beanValidator;

	/** log */
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	/**
	 * 로그인 화면으로 들어간다
	 * 
	 * @param vo - 로그인후 이동할 URL이 담긴 LoginVO
	 * @return 로그인 페이지
	 * @exception Exception
	 */
	@GetMapping("/uat/uia/login.do")
	public String loginUsrView(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {
		if (EgovComponentChecker.hasComponent("mberManageService")) {
			model.addAttribute("useMemberManage", "true");
		}

		// 권한체크시 에러 페이지 이동
		String auth_error = request.getParameter("auth_error") == null ? ""
				: (String) request.getParameter("auth_error");
		if (auth_error != null && auth_error.equals("1")) {
			return "egovframework/com/cmm/error/accessDenied";
		}

		LOGGER.debug(loginVO.getId());
		LOGGER.debug(loginVO.getPassword());

		String authType = EgovProperties.getProperty("Globals.Auth").trim();
		model.addAttribute("authType", authType);

		String message = request.getParameter("loginMessage");
		if (message != null)
			model.addAttribute("loginMessage", message);

		return "login/login.html";
	}

	/**
	 * 일반(세션) 로그인을 처리한다
	 * 
	 * @param vo      - 아이디, 비밀번호가 담긴 LoginVO
	 * @param request - 세션처리를 위한 HttpServletRequest
	 * @return result - 로그인결과(세션정보)
	 * @exception Exception
	 */
	@RequestMapping(value = "/login/actionLogin.do")
	@ResponseBody
	public LoginVO actionLogin(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, ModelMap model)
			throws Exception {

		// 로그인 처리
		LoginVO resultVO = loginService.actionLogin(loginVO);
		String userIp = EgovClntInfo.getClntIP(request);
		resultVO.setIp(userIp);

		// 일반 로그인 처리
		if (resultVO.getId() != null && !resultVO.getId().equals("")) {

			// 로그인 정보를 세션에 저장
			request.getSession().setAttribute("loginVO", resultVO);
			// 로그인 인증세션 추가
			request.getSession().setAttribute("accessUser", resultVO.getUserSe().concat(resultVO.getId()));
		}
		return resultVO;
	}

	/**
	 * 로그아웃한다.
	 * 
	 * @return String
	 * @exception Exception
	 */
	@RequestMapping(value = "/login/actionLogout.do")
	public String actionLogout(HttpServletRequest request) throws Exception {

		request.getSession().setAttribute("loginVO", null);
		request.getSession().setAttribute("accessUser", null);

		return "redirect:/login/login.do";
	}

	/**
	 * 일반회원등록 페이지.
	 * 
	 * @return String
	 * @exception Exception
	 */
	@GetMapping("/login/userReg.do")
	public String userReg(HttpServletRequest request, ModelMap model) throws Exception {

//        // 미인증 사용자에 대한 보안처리
//        Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
//        if (!isAuthenticated) {
//            return "index";
//        }

		ComDefaultCodeVO comDefaultCodeVO = new ComDefaultCodeVO();

		// 패스워드힌트목록을 코드정보로부터 조회
		comDefaultCodeVO.setCodeId("COM022");
		List<CmmnDetailCode> passwordHint_result = cmmUseService.selectCmmCodeDetail(comDefaultCodeVO);
		// 성별구분코드를 코드정보로부터 조회
		comDefaultCodeVO.setCodeId("COM014");
		List<CmmnDetailCode> sexdstnCode_result = cmmUseService.selectCmmCodeDetail(comDefaultCodeVO);
		// 사용자상태코드를 코드정보로부터 조회
		comDefaultCodeVO.setCodeId("COM013");
		List<CmmnDetailCode> mberSttus_result = cmmUseService.selectCmmCodeDetail(comDefaultCodeVO);
		// 그룹정보를 조회 - GROUP_ID정보
		comDefaultCodeVO.setTableNm("COMTNORGNZTINFO");
		List<CmmnDetailCode> groupId_result = cmmUseService.selectGroupIdDetail(comDefaultCodeVO);

		model.addAttribute("passwordHint_result", passwordHint_result); // 패스워트힌트목록
		model.addAttribute("sexdstnCode_result", sexdstnCode_result); // 성별구분코드목록
		model.addAttribute("mberSttus_result", mberSttus_result); // 사용자상태코드목록
		model.addAttribute("groupId_result", groupId_result); // 그룹정보 목록

		return "login/userReg.html";
	}

	@PostMapping("/login/userReg.do")
	@ResponseBody
	public ResultVO insertMber(MberManageVO mberManageVO) throws Exception {

		ResultVO resultVO = new ResultVO();

		if ("".equals(mberManageVO.getGroupId())) {// KISA 보안약점 조치 (2018-10-29, 윤창원)
			mberManageVO.setGroupId(null);
		}
		mberManageService.insertMber(mberManageVO);

		resultVO.setResultSuccess(true);
		resultVO.setResultCode("REQUSRREG_SUCCESS");
		resultVO.setResultMsg("회원가입 요청이 완료되었습니다");

		return resultVO;
	}

	/**
	 * 아이디/비밀번호 찾기 화면으로 들어간다
	 * 
	 * @param
	 * @return 아이디/비밀번호 찾기 페이지
	 * @exception Exception
	 */
	@RequestMapping(value = "/login/egovIdPasswordSearch.do")
	public String idPasswordSearchView(ModelMap model) throws Exception {

		// 1. 비밀번호 힌트 공통코드 조회
		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		vo.setCodeId("COM022");
		List<CmmnDetailCode> code = cmmUseService.selectCmmCodeDetail(vo);
		model.addAttribute("pwhtCdList", code);

		return "egovframework/com/uat/uia/EgovIdPasswordSearch";
	}

	/**
	 * 아이디를 찾는다.
	 * 
	 * @param vo - 이름, 이메일주소, 사용자구분이 담긴 LoginVO
	 * @return result - 아이디
	 * @exception Exception
	 */
	@RequestMapping(value = "/login/searchId.do")
	public String searchId(@ModelAttribute("loginVO") LoginVO loginVO, ModelMap model) throws Exception {

		if (loginVO == null || loginVO.getName() == null || loginVO.getName().equals("") && loginVO.getEmail() == null
				|| loginVO.getEmail().equals("") && loginVO.getUserSe() == null || loginVO.getUserSe().equals("")) {
			return "egovframework/com/cmm/egovError";
		}

		// 1. 아이디 찾기
		loginVO.setName(loginVO.getName().replaceAll(" ", ""));
		LoginVO resultVO = loginService.searchId(loginVO);

		if (resultVO != null && resultVO.getId() != null && !resultVO.getId().equals("")) {

			model.addAttribute("resultInfo", "아이디는 " + resultVO.getId() + " 입니다.");
			return "egovframework/com/uat/uia/EgovIdPasswordResult";
		} else {
			model.addAttribute("resultInfo", egovMessageSource.getMessage("fail.common.idsearch"));
			return "egovframework/com/uat/uia/EgovIdPasswordResult";
		}
	}

	/**
	 * 비밀번호를 찾는다.
	 * 
	 * @param vo - 아이디, 이름, 이메일주소, 비밀번호 힌트, 비밀번호 정답, 사용자구분이 담긴 LoginVO
	 * @return result - 임시비밀번호전송결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/login/searchPassword.do")
	public String searchPassword(@ModelAttribute("loginVO") LoginVO loginVO, ModelMap model) throws Exception {

		// KISA 보안약점 조치 (2018-10-29, 윤창원)
		if (loginVO == null || loginVO.getId() == null || loginVO.getId().equals("") && loginVO.getName() == null
				|| "".equals(loginVO.getName()) && loginVO.getEmail() == null
				|| loginVO.getEmail().equals("") && loginVO.getPasswordHint() == null
				|| "".equals(loginVO.getPasswordHint()) && loginVO.getPasswordCnsr() == null
				|| "".equals(loginVO.getPasswordCnsr()) && loginVO.getUserSe() == null
				|| "".equals(loginVO.getUserSe())) {
			return "egovframework/com/cmm/egovError";
		}

		// 1. 비밀번호 찾기
		boolean result = loginService.searchPassword(loginVO);

		// 2. 결과 리턴
		if (result) {
			model.addAttribute("resultInfo", "임시 비밀번호를 발송하였습니다.");
			return "egovframework/com/uat/uia/EgovIdPasswordResult";
		} else {
			model.addAttribute("resultInfo", egovMessageSource.getMessage("fail.common.pwsearch"));
			return "egovframework/com/uat/uia/EgovIdPasswordResult";
		}
	}

	/**
	 * 세션타임아웃 시간을 연장한다. Cookie에 egovLatestServerTime, egovExpireSessionTime 기록하도록
	 * 한다.
	 * 
	 * @return result - String
	 * @exception Exception
	 */
	@RequestMapping(value = "/login/refreshSessionTimeout.do")
	public ModelAndView refreshSessionTimeout(@RequestParam Map<String, Object> commandMap) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");

		modelAndView.addObject("result", "ok");

		return modelAndView;
	}
}
