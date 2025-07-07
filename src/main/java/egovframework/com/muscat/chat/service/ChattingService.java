package egovframework.com.muscat.chat.service;

import java.util.List;

import egovframework.com.cmm.LoginVO;
import egovframework.com.uss.umt.service.MberManageVO;

public interface ChattingService {
	String chat();

	String getRoomId();

	String getUsername();
	
	String insertRoom(LoginVO userman, List<String> userList); // 방 등록
	
	String insertUsers(String roomid,List<String> userList); // 사용자 등록 
	
	String insertMessage(MessageVO messaged); // 메시지 등록
	
	List<MessageVO> findMessage(MessageVO findmessage); // 메시지 조회
	
	List<RoomVO> findroom(UserVO findroom); // 채팅방 리스트 조회 
	
	List<String> findParticipantsByRoomId(String roomId); // 채팅방 인원 조회
	
	List<MberManageVO> findMember();// 인원 조회
}
