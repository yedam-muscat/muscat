package egovframework.com.muscat.chat.service.impl;

import org.springframework.stereotype.Service;

import egovframework.com.muscat.chat.service.ChattingService;
@Service
public class ChattingServiceImpl implements ChattingService{

	@Override
	public String chat() {
		
		return "chat";
	}
	

}
