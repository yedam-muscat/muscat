package egovframework.com.muscat.alarm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.muscat.alarm.service.AlarmService;
import egovframework.com.muscat.alarm.service.AlarmVO;

@Controller
public final class AlarmController {
	@Autowired
	private AlarmService alarmService;
	
	//게시글 알람 등록
	@ResponseBody
	@RequestMapping("/alarm/alarmPost.do")
	public String alarmPost(@RequestBody AlarmVO alarmpost) {
		alarmService.insertPost(alarmpost);	
		
		return null;
		
	}
	//채팅 알람 등록
		@ResponseBody
		@RequestMapping("/alarm/alarmChat.do")
		public String alarmChat(@RequestBody AlarmVO alarmchat) {
			alarmService.insertChat(alarmchat);	
			
			return null;
			
		}
		//알람 조회 
				@ResponseBody
				@RequestMapping("/alarm/alarmfind.do")
				public String alarmfind(@RequestBody AlarmVO alarmfind) {
					alarmService.findAlarm(alarmfind);	
					
					return null;
					
				}
}
