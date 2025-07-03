package egovframework.com.muscat.chat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.muscat.chat.mapper.RoomMapper;
import egovframework.com.muscat.chat.service.ChattingService;
import egovframework.com.muscat.chat.service.MessageVO;
import egovframework.com.muscat.chat.service.RoomVO;
import egovframework.com.muscat.chat.service.UserVO;
@Service
public class ChattingServiceImpl implements ChattingService{

	@Autowired RoomMapper heyroom;
	
	@Override
	public String chat() {
		
		return "chat";
	}

	@Override
	public String getRoomId() {
		
		return null;
	}

	@Override
	public String getUsername() {
		
		return null;
	}

	@Override
	public String insertRoom(RoomVO droom) {		
		int tomy = heyroom.roomi(droom); // db안에 있는 변화되는 행 갯수를 반환 해준다.
		if (tomy > 0 ) {
			return "success";
		}else {
			return "false";
		}
	}

	@Override
	public String insertUsers(RoomVO room) {
		String rommId ="room";
		room.setRoomId(rommId);
		int rooms = heyroom.roomi(room);
		
		if(rooms > 0) {
			for(UserVO user : room.getUsered()) {
				user.setRoomId(rommId);
				heyroom.insertUser(user);
			}
			

			return "success";
		}else {
			
			return "false";
		}
	}

	@Override
	public String insertMessage(MessageVO messaged) {
		int insertmessage = heyroom.insertMessage(messaged);
		return null;
	}
	

}
