package egovframework.com.muscat.login.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.EgovComponentChecker;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.config.EgovLoginConfig;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.uat.uia.service.EgovLoginService;
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

	/** log */
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	/**
	 * 로그인 화면으로 들어간다
	 * 
	 * @param vo - 로그인후 이동할 URL이 담긴 LoginVO
	 * @return 로그인 페이지
	 * @exception Exception
	 */
	@RequestMapping(value = "/login/login.do")
	public String loginUsrView(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws Exception {
		if (EgovComponentChecker.hasComponent("mberManageService")) {
			model.addAttribute("useMemberManage", "true");
		}

		String authType = EgovProperties.getProperty("Globals.Auth").trim();
		model.addAttribute("authType", authType);

		String message = request.getParameter("loginMessage");
		if (message != null)
			model.addAttribute("loginMessage", message);

		return "login/login";
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
	public String actionLogout(HttpServletRequest request, ModelMap model) throws Exception {

		request.getSession().setAttribute("loginVO", null);
		request.getSession().setAttribute("accessUser", null);

		return "redirect:/login/login.do";
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

//
//	/**
//	 * 인증서안내 화면으로 들어간다
//	 * 
//	 * @return 인증서안내 페이지
//	 * @exception Exception
//	 */
//	@RequestMapping(value = "/uat/uia/egovGpkiIssu.do")
//	public String gpkiIssuView(ModelMap model) throws Exception {
//		return "egovframework/com/uat/uia/EgovGpkiIssu";
//	}
//
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

//	/**
//	 * 개발 시스템 구축 시 발급된 GPKI 서버용인증서에 대한 암호화데이터를 구한다. 최초 한번만 실행하여, 암호화데이터를
//	 * EgovGpkiVariables.js의 ServerCert에 넣는다.
//	 * 
//	 * @return String
//	 * @exception Exception
//	 */
//	@RequestMapping(value = "/uat/uia/getEncodingData.do")
//	public void getEncodingData() throws Exception {
//
//		/*
//		 * X509Certificate x509Cert = null; byte[] cert = null; String base64cert =
//		 * null; try { x509Cert = Disk.readCert(
//		 * "/product/jeus/egovProps/gpkisecureweb/certs/SVR1311000011_env.cer"); cert =
//		 * x509Cert.getCert(); Base64 base64 = new Base64(); base64cert =
//		 * base64.encode(cert); log.info("+++ Base64로 변환된 인증서는 : " + base64cert);
//		 * 
//		 * } catch (GpkiApiException e) { e.printStackTrace(); }
//		 */
//	}
//
//	/**
//	 * 인증서 DN추출 팝업을 호출한다.
//	 * 
//	 * @return 인증서 등록 페이지
//	 * @exception Exception
//	 */
//	@RequestMapping(value = "/uat/uia/EgovGpkiRegist.do")
//	public String gpkiRegistView(HttpServletRequest request, HttpServletResponse response, ModelMap model)
//			throws Exception {
//
//		/** GPKI 인증 부분 */
//		// OS에 따라 (local NT(로컬) / server Unix(서버)) 구분
//		String os = System.getProperty("os.arch");
//		LOGGER.debug("OS : {}", os);
//
//		// String virusReturn = null;
//
//		/*
//		 * // 브라우저 이름을 받기위한 처리 String webKind = EgovClntInfo.getClntWebKind(request);
//		 * String[] ss = webKind.split(" "); String browser = ss[1];
//		 * model.addAttribute("browser",browser); // -- 여기까지 if
//		 * (os.equalsIgnoreCase("x86")) { //Local Host TEST 진행중 } else { if
//		 * (browser.equalsIgnoreCase("Explorer")) { GPKIHttpServletResponse gpkiresponse
//		 * = null; GPKIHttpServletRequest gpkirequest = null;
//		 * 
//		 * try { gpkiresponse = new GPKIHttpServletResponse(response); gpkirequest = new
//		 * GPKIHttpServletRequest(request);
//		 * 
//		 * gpkiresponse.setRequest(gpkirequest); model.addAttribute("challenge",
//		 * gpkiresponse.getChallenge());
//		 * 
//		 * return "egovframework/com/uat/uia/EgovGpkiRegist";
//		 * 
//		 * } catch (Exception e) { return "egovframework/com/cmm/egovError"; } } }
//		 */
//		return "egovframework/com/uat/uia/EgovGpkiRegist";
//	}
//
//	/**
//	 * 인증서 DN값을 추출한다
//	 * 
//	 * @return result - dn값
//	 * @exception Exception
//	 */
//	@RequestMapping(value = "/uat/uia/actionGpkiRegist.do")
//	public String actionGpkiRegist(HttpServletRequest request, HttpServletResponse response, ModelMap model)
//			throws Exception {
//
//		/** GPKI 인증 부분 */
//		// OS에 따라 (local NT(로컬) / server Unix(서버)) 구분
//		String os = System.getProperty("os.arch");
//		LOGGER.debug("OS : {}", os);
//
//		// String virusReturn = null;
//		@SuppressWarnings("unused")
//		String dn = "";
//
//		// 브라우저 이름을 받기위한 처리
//		String browser = EgovClntInfo.getClntWebKind(request);
//		model.addAttribute("browser", browser);
//		/*
//		 * // -- 여기까지 if (os.equalsIgnoreCase("x86")) { // Local Host TEST 진행중 } else {
//		 * if (browser.equalsIgnoreCase("Explorer")) { GPKIHttpServletResponse
//		 * gpkiresponse = null; GPKIHttpServletRequest gpkirequest = null; try {
//		 * gpkiresponse = new GPKIHttpServletResponse(response); gpkirequest = new
//		 * GPKIHttpServletRequest(request); gpkiresponse.setRequest(gpkirequest);
//		 * X509Certificate cert = null;
//		 * 
//		 * // byte[] signData = null; // byte[] privatekey_random = null; // String
//		 * signType = ""; // String queryString = "";
//		 * 
//		 * cert = gpkirequest.getSignerCert(); dn = cert.getSubjectDN();
//		 * 
//		 * model.addAttribute("dn", dn); model.addAttribute("challenge",
//		 * gpkiresponse.getChallenge());
//		 * 
//		 * return "egovframework/com/uat/uia/EgovGpkiRegist"; } catch (Exception e) {
//		 * return "egovframework/com/cmm/egovError"; } } }
//		 */
//		return "egovframework/com/uat/uia/EgovGpkiRegist";
//	}

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

//	/**
//	 * 비밀번호 유효기간 팝업을 출력한다. Cookie에 egovLatestServerTime, egovExpireSessionTime 기록하도록
//	 * 한다.
//	 * 
//	 * @return result - String
//	 * @exception Exception
//	 */
//	@RequestMapping(value = "/uat/uia/noticeExpirePwd.do")
//	public String noticeExpirePwd(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
//
//		// 설정된 비밀번호 유효기간을 가져온다. ex) 180이면 비밀번호 변경후 만료일이 앞으로 180일
//		String propertyExpirePwdDay = EgovProperties.getProperty("Globals.ExpirePwdDay");
//		int expirePwdDay = 0;
//		try {
//			expirePwdDay = Integer.parseInt(propertyExpirePwdDay);
//		} catch (NumberFormatException e) {
//			LOGGER.debug("convert expirePwdDay Err : " + e.getMessage());
//		}
//
//		model.addAttribute("expirePwdDay", expirePwdDay);
//
//		// 비밀번호 설정일로부터 몇일이 지났는지 확인한다. ex) 3이면 비빌번호 설정후 3일 경과
//		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
//		model.addAttribute("loginVO", loginVO);
//		int passedDayChangePWD = 0;
//		if (loginVO != null) {
//			LOGGER.debug("===>>> loginVO.getId() = " + loginVO.getId());
//			LOGGER.debug("===>>> loginVO.getUniqId() = " + loginVO.getUniqId());
//			LOGGER.debug("===>>> loginVO.getUserSe() = " + loginVO.getUserSe());
//			// 비밀번호 변경후 경과한 일수
//			passedDayChangePWD = loginService.selectPassedDayChangePWD(loginVO);
//			LOGGER.debug("===>>> passedDayChangePWD = " + passedDayChangePWD);
//			model.addAttribute("passedDay", passedDayChangePWD);
//		}
//
//		// 만료일자로부터 경과한 일수 => ex)1이면 만료일에서 1일 경과
//		model.addAttribute("elapsedTimeExpiration", passedDayChangePWD - expirePwdDay);
//
//		return "egovframework/com/uat/uia/EgovExpirePwd";
//	}

}
