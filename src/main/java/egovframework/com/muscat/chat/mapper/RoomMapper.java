package egovframework.com.muscat.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import egovframework.com.muscat.chat.service.MessageVO;
import egovframework.com.muscat.chat.service.RoomVO;
import egovframework.com.muscat.chat.service.UserVO;
import egovframework.com.uss.umt.service.MberManageVO;
@Mapper
public interface RoomMapper {
	
	int roomi(RoomVO room); // 방 등록
	
	int deleteuser(UserVO deleteuser); // 방삭제
	
	int insertUser(UserVO user);//사용자 등록
	
	int insertMessage(MessageVO messages); // 메시지 등록
	
	List<MessageVO> findMessage(MessageVO findmessage); // 메시지 조회
	
	List<RoomVO> findroom(UserVO findroom); // 채팅방 리스트 조회

	List<String> findParticipantsByRoomId(String roomId); // 채팅방 인원 조회
	
	List<MberManageVO> findMember(); //  인원 조회
	
	List<String> findroomAlarm(String userId); // 조직도 채팅
}
