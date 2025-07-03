package egovframework.com.muscat.chat.service;

import java.util.List;

public interface ChattingService {
	String chat();

	String getRoomId();

	String getUsername();
	
	String insertRoom(RoomVO droom); // 방 등록
	
	String insertUsers(RoomVO room); // 사용자 등록 
	
	String insertMessage(MessageVO messaged); // 메시지 등록
}
