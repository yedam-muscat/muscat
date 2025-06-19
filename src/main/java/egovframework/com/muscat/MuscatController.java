package egovframework.com.muscat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MuscatController {

	@RequestMapping("/main.do")
	public String test() {
		return "main";
	}
}
