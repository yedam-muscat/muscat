package egovframework.com.muscat.chat.mapper;

import java.util.List;

import egovframework.com.muscat.chat.service.MessageVO;
import egovframework.com.muscat.chat.service.RoomVO;
import egovframework.com.muscat.chat.service.UserVO;

public interface RoomMapper {
	
	int roomi(RoomVO room); // 방 등록
	
	int insertUser(UserVO user);//사용자 등록
	
	int insertMessage(MessageVO messages); // 메시지 등록
	
	List<RoomVO> findroom(UserVO findroom); // 채팅방 리스트 조회

}
