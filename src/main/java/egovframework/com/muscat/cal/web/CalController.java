package egovframework.com.muscat.cal.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.com.muscat.cal.mapper.CalMapper;
import egovframework.com.muscat.cal.mapper.CalendarVO;
import egovframework.com.muscat.cal.mapper.ScudVO;
import egovframework.com.muscat.cal.service.CalService;

@Controller
public class CalController {

	@Autowired
	private CalService calService;

	@Autowired
	private CalMapper calMapper;

	// 등록
	@PostMapping("/cal/insertSchedule")
	@ResponseBody
	public String insertSchedule(@RequestBody ScudVO vo) throws Exception {
	    calService.insertSchedule(vo); // 오류 시 자동으로 예외 발생
	    return "success";
	}

	// 조회
	@GetMapping("/cal/listSchedule")
	@ResponseBody
	public List<Map> listSchedule(@RequestParam(required = false) String start,
			@RequestParam(required = false) String end) throws Exception {

		return calService.selectScheduleList();
	}

	@RequestMapping("cal/calDetail.do")
	public String calDetail(@RequestParam(required = false) String start, @RequestParam(required = false) String end,
			Model model) {

		model.addAttribute("start", start);
		model.addAttribute("end", end);
		return "cal/calDetail.html";
	}

	@RequestMapping("cal/calMonth.do")
	public String calMonth() {
		return "cal/calMonth.html";
	}


	@GetMapping("/cal/listCalendar")
	@ResponseBody
	public List<Map> listCalendar() {
	    return calService.selectCalendarList();
	}
	
	@PostMapping("/cal/insertCalendar")
	@ResponseBody
	public ResponseEntity<String> insertCalendar(@RequestBody CalendarVO calendarVO) {
	    try {
	        if (calendarVO.getCalId() == null || calendarVO.getCalId().isEmpty()) {
	            calendarVO.setCalId(UUID.randomUUID().toString().substring(0, 20));
	        }
	        if (calendarVO.getOwnerId() == null || calendarVO.getOwnerId().isEmpty()) {
	            calendarVO.setOwnerId("admin");
	        }
	   
	        calService.insertCalendar(calendarVO);
	        return ResponseEntity.ok("등록 성공");
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("등록 실패: " + e.getMessage());
	    }
	}
}
