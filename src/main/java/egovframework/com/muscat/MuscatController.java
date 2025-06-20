package egovframework.com.muscat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MuscatController {

	// 메인 페이지
	@RequestMapping("/main.do")
	public String main() {
		return "main";
	}
	
	// 로그인 페이지
	@RequestMapping("/login.do")
	public String login() {
		return "login/login";
	}
	
	// 예제 페이지
	
	@RequestMapping("/ex_app_email.do")
	public String ex1() {
		return "example/application-email";
	}
}
