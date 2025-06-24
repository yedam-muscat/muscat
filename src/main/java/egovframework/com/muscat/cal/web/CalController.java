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
import egovframework.com.muscat.cal.mapper.ScudVO;
import egovframework.com.muscat.cal.service.CalService;

@Controller
public class CalController {

	@Autowired
	private CalService calService;
	
	@Autowired
    private CalMapper calMapper;
	

	//등록
	@PostMapping("/cal/insertSchedule.json")
	@ResponseBody
	public ResponseEntity<?> insertSchedule(@RequestBody ScudVO vo) throws Exception {
		vo.setLeaderId("admin");
	    calMapper.insertSchedule(vo); // DB 저장
	    return ResponseEntity.ok().build();
	}

//    @GetMapping("/cal/listSchedule")
//    public ModelAndView listSchedule(@RequestParam(required = false) String start,
//                                     @RequestParam(required = false) String end) throws Exception {
//    	ModelAndView mav = new ModelAndView("jsonView");
//    	mav.addObject("list", calService.selectScheduleList());
//    	
//        return mav;
//    }

	//조회
	@GetMapping("/cal/listSchedule")
	@ResponseBody
	public List<Map> listSchedule(@RequestParam(required = false) String start,
			@RequestParam(required = false) String end) throws Exception {

		return calService.selectScheduleList();
	}

	@RequestMapping("cal/calDetail.do")
	public String calDetail(@RequestParam String date, Model model) {
		model.addAttribute("date", date);
		return "cal/calDetail.html";
	}

	@RequestMapping("cal/calMonth.do")
	public String calMonth() {
		return "cal/calMonth.html";
	}
	
	@PostMapping("cal/save")
	public String saveSchedule(HttpServletRequest request, RedirectAttributes redirectAttributes) {
	    try {
	        ScudVO scudVO = new ScudVO();
	        scudVO.setSchdulId(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
	        scudVO.setSchdulNm(request.getParameter("title"));
	        scudVO.setSchdulCn(request.getParameter("description"));
	        scudVO.setSchdulPlace(request.getParameter("location"));
	        scudVO.setSchdulDeptId("DEPT001"); // 실제 로그인 사용자 기준 설정
	        scudVO.setSchdulChargerId("admin"); // 로그인 사용자 ID

	        scudVO.setSchdulKindCode(request.getParameter("calendarId"));

	        scudVO.setSchdulSe(request.getParameter("private") != null ? "PRIVATE" : "PUBLIC");

	        scudVO.setSchdulIpcrCode(request.getParameter("companyEvent") != null ? "CORP" : "PERSONAL");

	        scudVO.setReptitSeCode(request.getParameter("repeat") != null ? "REPEAT" : "ONCE");

	        DateTimeFormatter inputFmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	        DateTimeFormatter dbFmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	        String start = request.getParameter("start");
	        String end = request.getParameter("end");

	        scudVO.setSchdulBgnde(LocalDateTime.parse(start, inputFmt).format(dbFmt));
	        scudVO.setSchdulEndde(LocalDateTime.parse(end, inputFmt).format(dbFmt));

	        scudVO.setFrstRegisterId("admin");

	        calMapper.insertSchedule(scudVO);

	        redirectAttributes.addFlashAttribute("success", true);
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("success", false);
	    }

	    return "redirect:/cal/calMonth.html";
	}


}
