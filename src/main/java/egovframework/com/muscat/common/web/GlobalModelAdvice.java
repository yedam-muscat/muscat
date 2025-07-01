package egovframework.com.muscat.common.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

	@ModelAttribute
	public void addGlobalAttributes(Model model, HttpServletRequest request) {
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
