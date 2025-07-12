package egovframework.com.muscat.alarm.service;

import java.util.List;

public interface AlarmService {
	
	int insertPost(AlarmVO alarmPost); // 게시글 알람 등록
	
	int insertChat(AlarmVO alarmChat); // 채팅 알람 등록
	
	List<AlarmVO> findAlarm(AlarmVO findAlarm); // 알람조회
	
	int deletewhole(AlarmVO deletewhole); // 알람 전체 삭제
	
	int deleteAlram(AlarmVO deleteAlarm); // 알람 단건 삭제
	
	
	
}
