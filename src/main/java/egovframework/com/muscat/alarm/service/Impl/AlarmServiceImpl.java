package egovframework.com.muscat.alarm.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.muscat.alarm.mapper.AlarmMapper;
import egovframework.com.muscat.alarm.service.AlarmService;

@Service
public class AlarmServiceImpl implements AlarmService{

	@Autowired AlarmMapper alarMapper;
	
	@Override
	public List<String> findroomAlarm(String userId) {
		
		return null;
	}
	
	
}
