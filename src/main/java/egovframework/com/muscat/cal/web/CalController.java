package egovframework.com.muscat.cal.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.muscat.cal.mapper.ScudVO;
import egovframework.com.muscat.cal.service.CalService;

@Controller
public class CalController {

	@Autowired
	private CalService calService;

	@PostMapping("/cal/insertSchedule.json")
	@ResponseBody
	public String insertSchedule(@RequestBody ScudVO scudVO) throws Exception {
		calService.insertSchedule(scudVO);
		return "success";
	}

//    @GetMapping("/cal/listSchedule")
//    public ModelAndView listSchedule(@RequestParam(required = false) String start,
//                                     @RequestParam(required = false) String end) throws Exception {
//    	ModelAndView mav = new ModelAndView("jsonView");
//    	mav.addObject("list", calService.selectScheduleList());
//    	
//        return mav;
//    }

	@GetMapping("/cal/listSchedule")
	@ResponseBody
	public List<Map> listSchedule(@RequestParam(required = false) String start,
			@RequestParam(required = false) String end) throws Exception {

		return calService.selectScheduleList();
	}

	@RequestMapping("cal/calDetail.do")
	public String calDetail(@RequestParam String date, Model model) {
		model.addAttribute("date", date);
		return "cal/calDetail";
	}

	@RequestMapping("cal/calMonth.do")
	public String calMonth() {
		return "cal/calMonth";
	}
}
