package egovframework.com.muscat.alarm.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.muscat.alarm.mapper.AlarmMapper;
import egovframework.com.muscat.alarm.service.AlarmService;
import egovframework.com.muscat.alarm.service.AlarmVO;

@Service
public class AlarmServiceImpl implements AlarmService {

	@Autowired
	AlarmMapper alarMapper;
	
	//게시글 알람 등록
	@Override
	public int insertPost(AlarmVO alarmPost) {
		
		return alarMapper.insertPost(alarmPost);
	}
	
	//채팅 알람 등록
	@Override
	public int insertChat(AlarmVO alarmChat) {
		
		return alarMapper.insertChat(alarmChat);
	}

	//알람 조회
	@Override
	public List<AlarmVO> findAlarm(AlarmVO findAlarm) {
		
		return alarMapper.findAlarm(findAlarm);
	}
	
	//알람 전체 삭제
	@Override
	public int deletewhole(AlarmVO deletewhole) {
		
		return alarMapper.deletewhole(deletewhole);
	}
	
	//알람 단건 삭제
	@Override
	public int deleteAlram(AlarmVO deleteAlarm) {
		
		return alarMapper.deleteAlram(deleteAlarm);
	}

}
