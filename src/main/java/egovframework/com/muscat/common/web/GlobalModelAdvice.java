package egovframework.com.muscat.common.web;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;

@ControllerAdvice
public class GlobalModelAdvice {

	@ModelAttribute
	public void addGlobalAttributes(Model model, HttpServletRequest request) {

        Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
    	if (isAuthenticated.booleanValue()) {
			LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
			List<String> auth = EgovUserDetailsHelper.getAuthorities();

			model.addAttribute("loginUser", user);
			model.addAttribute("loginUserAuth", auth);
    	}
		
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            return;
        }
		String egovLatestServerTime = "";
		String egovExpireSessionTime = "";

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("egovLatestServerTime".equals(cookie.getName())) {
					egovLatestServerTime = cookie.getValue();
				} else if ("egovExpireSessionTime".equals(cookie.getName())) {
					egovExpireSessionTime = cookie.getValue();
				}
			}
		}

		model.addAttribute("egovLatestServerTime", egovLatestServerTime);
		model.addAttribute("egovExpireSessionTime", egovExpireSessionTime);
	}
}
