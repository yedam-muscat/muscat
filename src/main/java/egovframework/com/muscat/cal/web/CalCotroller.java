package egovframework.com.muscat.cal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.muscat.cal.service.CalService;


@Controller
public class CalCotroller {
	@Autowired CalService calService;
	
	@RequestMapping("/cal.do")
	public String calMain() {
		return "cal/cal";
	}
	
}
