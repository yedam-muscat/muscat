package egovframework.com.muscat.chat.service.impl;

import org.springframework.stereotype.Service;

import egovframework.com.muscat.chat.service.ChattingService;
@Service
public class ChattingServiceImpl implements ChattingService{

	@Override
	public String chat() {
		
		return "chat";
	}

	@Override
	public String getRoomId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
