package egovframework.com.muscat.cal.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cmm.LoginVO;
import egovframework.com.muscat.cal.mapper.CalMapper;
import egovframework.com.muscat.cal.mapper.CalendarShareVO;
import egovframework.com.muscat.cal.mapper.CalendarVO;
import egovframework.com.muscat.cal.mapper.ReservationVO;
import egovframework.com.muscat.cal.mapper.ScudVO;
import egovframework.com.muscat.cal.service.CalService;

@Controller
public class CalController {

	@Autowired
	private CalService calService;

	@Autowired
	private CalMapper calMapper;

	/** log */
	private static final Logger LOGGER = LoggerFactory.getLogger(CalController.class);

	// 등록
	@PostMapping("/cal/insertSchedule")
	@ResponseBody
	public String insertSchedule(@RequestBody ScudVO vo) throws Exception {
		if (vo.getSchdulId() == null || vo.getSchdulId().isEmpty()) {
			vo.setSchdulId(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
		}

		if (vo.getLeaderId() == null || vo.getLeaderId().isEmpty()) {
			vo.setLeaderId("admin"); // 또는 로그인 사용자 ID 등
		}

		// 기본 일정 저장
		calService.insertSchedule(vo);

		// 반복 일정 로직 (예: 1년 동안 12개 반복 생성) - 원하는 기간만큼 조절 가능
		String reptitSeCode = vo.getReptitSeCode();
		if (reptitSeCode != null && !"N".equalsIgnoreCase(reptitSeCode)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			LocalDateTime startDate = LocalDateTime.parse(vo.getSchdulBgnde(), formatter);
			LocalDateTime endDate = LocalDateTime.parse(vo.getSchdulEndde(), formatter);

			// 반복 횟수 (예: 12회 반복)
			int repeatCount = 12;

			for (int i = 1; i < repeatCount; i++) {
				LocalDateTime newStart = null;
				LocalDateTime newEnd = null;

				switch (reptitSeCode.toUpperCase()) {
				case "D": // 매일
					newStart = startDate.plusDays(i);
					newEnd = endDate.plusDays(i);
					break;
				case "W": // 매주
					newStart = startDate.plusWeeks(i);
					newEnd = endDate.plusWeeks(i);
					break;
				case "M": // 매월
					newStart = startDate.plusMonths(i);
					newEnd = endDate.plusMonths(i);
					break;
				case "Y": // 매년
					newStart = startDate.plusYears(i);
					newEnd = endDate.plusYears(i);
					break;
				default:
					// 반복 없음
					break;
				}

				if (newStart != null && newEnd != null) {
					ScudVO repeatVo = new ScudVO();
					repeatVo.setSchdulId(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
					repeatVo.setSchdulNm(vo.getSchdulNm());
					repeatVo.setSchdulPlace(vo.getSchdulPlace());
					repeatVo.setSchdulCn(vo.getSchdulCn());
					repeatVo.setSchdulBgnde(newStart.format(formatter));
					repeatVo.setSchdulEndde(newEnd.format(formatter));
					repeatVo.setLeaderId(vo.getLeaderId());
					repeatVo.setSchdulChargerId(vo.getSchdulChargerId());
					repeatVo.setFrstRegisterId(vo.getFrstRegisterId());
					repeatVo.setLastUpdusrId(vo.getLastUpdusrId());
					repeatVo.setCalId(vo.getCalId());
					repeatVo.setReptitSeCode(vo.getReptitSeCode());
					repeatVo.setReservedRoom(vo.getReservedRoom());

					calService.insertSchedule(repeatVo);
				}
			}
			if (vo.getReservedRoom() != null && !vo.getReservedRoom().isEmpty()) {
				ReservationVO r = new ReservationVO();
				r.setResveId(UUID.randomUUID().toString().substring(0, 20));
				r.setMtgrumId(vo.getReservedRoom());
				r.setRsvctmId(vo.getLeaderId());
				r.setResveDe(vo.getSchdulBgnde().substring(0, 8));
				r.setResveBeginTm(vo.getSchdulBgnde().substring(8, 14));
				r.setResveEndTm(vo.getSchdulEndde().substring(8, 14));
				r.setFrstRegisterId(vo.getFrstRegisterId());
				r.setLastUpdusrId(vo.getLastUpdusrId());
				calService.insertRoomReserve(r);
			}
		}

		return "success";
	}

	// 조회
	@GetMapping("/cal/listSchedule")
	@ResponseBody
	public Map<String, Object> listSchedule(HttpSession session) throws Exception {
		LoginVO loginVO = (LoginVO) session.getAttribute("loginVO");
		String loginId = loginVO.getId();
		List<Map> scheduleList = calService.selectScheduleList(loginId, loginId); // 동일 ID 사용

		Map<String, Object> result = new HashMap<>();
		result.put("result", scheduleList);
		return result;
	}

	@GetMapping("/cal/getSchedule")
	@ResponseBody
	public ResponseEntity<ScudVO> getSchedule(@RequestParam String schdulId) {
		ScudVO schedule = calService.selectScheduleById(schdulId);
		if (schedule != null) {
			return ResponseEntity.ok(schedule);
		} else {
			return ResponseEntity.status(404).body(null);
		}
	}
	// 단건조회
//	@GetMapping("/cal/calDetail.do")
//	public String getScheduleDetail(@RequestParam("scheduleId") String scheduleId, Model model) {
//	    ScudVO schedule = calService.selectScheduleById(scheduleId);
//	    model.addAttribute("schedule", schedule);
//	    return "cal/calDetail";  
//	}

	@RequestMapping("cal/calDetail.do")
	public String calDetail(@RequestParam(defaultValue = "", required = false) String schdulId, Model model) {
		ScudVO schedule = null;
		System.out.println("스케줄 아이디");
		System.out.println(schdulId);
		schedule = calService.selectScheduleById(schdulId);
		model.addAttribute("schedule", schedule);

		return "cal/calDetail.html";
	}

	@RequestMapping("cal/calMonth.do")
	public String calMonth() {
		return "cal/calMonth.html";
	}

	@GetMapping("/cal/listCalendar")
	@ResponseBody
	public List<Map> listCalendar(HttpSession session) {
		LoginVO loginVO = (LoginVO) session.getAttribute("loginVO");
		String loginId = loginVO.getId();
		return calService.selectCalendarList(loginId);
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

	@GetMapping("/cal/availableRooms")
	@ResponseBody
	public List<Map<String, Object>> availableRooms(@RequestParam String reserveDateTime) {
		return calService.selectAvailableRooms(reserveDateTime);
	}

	@GetMapping("/cal/allRooms")
	@ResponseBody
	public List<Map> getAllRooms() {
		return calMapper.selectAllRooms();
	}

	@PostMapping("/cal/updateSchedule")
	@ResponseBody
	public ResponseEntity<String> updateSchedule(@RequestBody ScudVO vo) {
		int result = calService.updateSchedule(vo);
		if (result > 0)
			return ResponseEntity.ok("수정 성공");
		return ResponseEntity.status(404).body("찾을 수 없는 일정입니다.");
	}

	@DeleteMapping("/cal/deleteSchedule")
	@ResponseBody
	public ResponseEntity<String> deleteSchedule(@RequestParam String schdulId) {
		int result = calService.deleteSchedule(schdulId);
		if (result > 0)
			return ResponseEntity.ok("삭제 성공");
		return ResponseEntity.status(404).body("찾을 수 없는 일정입니다.");
	}

	@PostMapping("/cal/insertSharedCalendar")
	@ResponseBody
	public String insertSharedCalendar(@RequestBody List<CalendarShareVO> shareList) {
		for (CalendarShareVO vo : shareList) {
			if (vo.getShareId() == null || vo.getShareId().isEmpty()) {
				vo.setShareId(UUID.randomUUID().toString().substring(0, 20));
			}
			if (vo.getShDe() == null) {
				vo.setShDe(java.sql.Date.valueOf(java.time.LocalDate.now()));
			}
			calService.insertCalendarShare(vo);
		}
		return "공유 완료";
	}

	@GetMapping("/cal/sharedCalendars")
	@ResponseBody
	public List<Map> getSharedCalendars(HttpSession session) {
		LoginVO loginVO = (LoginVO) session.getAttribute("loginVO");
		String loginId = loginVO.getId();
		return calService.getSharedCalendars(loginId);
	}
	
	@PostMapping("/cal/updateCalendar")
	@ResponseBody
	public ResponseEntity<String> updateCalendar(@RequestBody CalendarVO vo) {
	    try {
	        calService.updateCalendar(vo);
	        return ResponseEntity.ok("수정 성공");
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("수정 실패: " + e.getMessage());
	    }
	}
	
	@DeleteMapping("/cal/deleteCalendar")
	@ResponseBody
	public ResponseEntity<String> deleteCalendar(@RequestParam String calId, @RequestParam String calType) {
	    int result = 0;

	    if ("personal".equalsIgnoreCase(calType)) {
	        result = calService.deleteCalendar(calId);
	    } else if ("shared".equalsIgnoreCase(calType)) {
	        result = calService.deleteSharedCalendar(calId); // 이 메서드도 CalService에 필요함
	    }

	    if (result > 0) {
	        return ResponseEntity.ok("삭제 성공");
	    } else {
	        return ResponseEntity.status(404).body("삭제 실패 또는 캘린더 없음");
	    }
	}
	
	@DeleteMapping("/cal/deleteSharedCalendar")
	@ResponseBody
	public ResponseEntity<String> deleteSharedCalendar(@RequestParam String calId, @RequestParam String calType) {
	    int result = calService.deleteSharedCalendar(calId);
	    if (result > 0) {
	        return ResponseEntity.ok("삭제 성공");
	    } else {
	        return ResponseEntity.status(404).body("삭제 실패 또는 캘린더 없음");
	    }
	}

}
