package egovframework.com.muscat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.LoginVO;

@Controller
public class MuscatController {

	// 메인 페이지
	@RequestMapping("/main.do")
	public String main(HttpServletRequest request, Model model) {
		LoginVO loginUser = (LoginVO) request.getSession().getAttribute("loginVO");
	    model.addAttribute("user", loginUser);
		return "main";
	}

	// 예제 페이지

	@RequestMapping("/ex_app_email.do")
	public String ex1() {
		return "example/application-email";
	}
}
