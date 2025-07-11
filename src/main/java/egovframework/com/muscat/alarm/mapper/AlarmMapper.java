package egovframework.com.muscat.alarm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import egovframework.com.muscat.alarm.service.AlarmVO;

@Mapper
public interface AlarmMapper {
	
	int insertPost(AlarmVO alarmPost); // 게시글 알람 등록
	
	int insertChat(AlarmVO alarmChat); // 채팅 알람 등록
	
	List<AlarmVO> findAlarm(AlarmVO findAlarm); // 알람조회
	
	int deletewhole(AlarmVO deletewhole); // 알람 전체 삭제
	
	int deleteAlram(AlarmVO deleteAlarm); // 알람 단건 삭제
}
