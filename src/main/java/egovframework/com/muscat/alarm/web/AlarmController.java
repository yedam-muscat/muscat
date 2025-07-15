package egovframework.com.muscat.alarm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cmm.LoginVO;
import egovframework.com.muscat.alarm.service.AlarmService;
import egovframework.com.muscat.alarm.service.AlarmVO;

@Controller
public final class AlarmController {
	@Autowired
	private AlarmService alarmService;

	// 게시글 알람 등록
	@ResponseBody
	@RequestMapping("/alarm/alarmPost.do")
	public String alarmPost(@RequestBody AlarmVO alarmpost) {
		alarmService.insertPost(alarmpost);

		return null;

	}

	// 채팅 알람 등록
	@ResponseBody
	@RequestMapping("/alarm/alarmChat.do")
	public String alarmChat(@RequestBody AlarmVO alarmchat) {
		alarmService.insertChat(alarmchat);

		return null;

	}

	// 알람 조회
	@ResponseBody
	@RequestMapping("/alarm/alarmfind.do")
	public List<AlarmVO> alarmfind(HttpServletRequest request, AlarmVO alarmfind) {
		LoginVO loginUser = (LoginVO) request.getSession().getAttribute("loginVO");
		System.out.println(loginUser);
		alarmfind.setMberId(loginUser.getId());
		return alarmService.findAlarm(alarmfind);

	}

	// 전체 삭제
	@PostMapping("/alarm/deleteall.do")
	@ResponseBody
	public Map<String, Object> deleteAllAlarms(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		try {
			LoginVO loginUser = (LoginVO) request.getSession().getAttribute("loginVO");

			AlarmVO vo = new AlarmVO();
			vo.setMberId(loginUser.getId());

			int deletedCount = alarmService.deletewhole(vo);
			result.put("success", true);
			result.put("deletedCount", deletedCount);
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "알람 전체 삭제 중 오류가 발생했습니다.");
		}
		return result;
	}

	
	  //단건삭제	  
	@PostMapping("/alarm/delete.do")
	@ResponseBody
	public Map<String, Object> deleteAlarm(@RequestBody AlarmVO vo) {
	    Map<String, Object> map = new HashMap<>();
	    try {
	        alarmService.deleteAlram(vo); 
	        map.put("success", true);
	    } catch (Exception e) {
	        map.put("success", false);
	    }
	    return map;
	}
	
	//  알람 읽음 처리
	@PostMapping("/alarm/read.do")
	@ResponseBody
	public Map<String, Object> markAlarmAsRead(@RequestBody AlarmVO vo) {
	    Map<String, Object> result = new HashMap<>();
	    try {
	        alarmService.markAsRead(vo);  // ntcnNo 기준으로 처리
	        result.put("success", true);
	    } catch (Exception e) {
	        result.put("success", false);
	    }
	    return result;
	}

	//  안 읽은 알람 개수 조회 (뱃지용)
	@PostMapping("/alarm/unreadcount.do")
	@ResponseBody
	public int getUnreadCount(HttpServletRequest request) {
	    LoginVO loginUser = (LoginVO) request.getSession().getAttribute("loginVO");
	    AlarmVO vo = new AlarmVO();
	    vo.setMberId(loginUser.getId());
	    return alarmService.countUnreadAlarms(vo);
	}
	 
}
