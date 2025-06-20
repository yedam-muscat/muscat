package egovframework.com.muscat.cal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.muscat.cal.service.CalService;


@Controller
public class CalCotroller {
	@Autowired CalService calService;
	
	@RequestMapping("/calmonth.do")
	public String calMonthMain() {
		return "cal/calMonth";
	}
	
	@RequestMapping("/calDetail.do")
	public String caldetail(@RequestParam String date, Model model) {
	    model.addAttribute("date", date);
	    return "cal/calDetail";
	}
	
}
