package egovframework.com.muscat.appov.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.muscat.appov.service.ApprovService;

@Controller
public class ApprovContoller {
	
	@Autowired ApprovService approvService;
	
	@RequestMapping(value="/approv.do")
//	@ResponseBody
	public String test() {
		return "egovframework/" + approvService.test();
	}

}
