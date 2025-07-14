package egovframework.com.muscat.chat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.LoginVO;
import egovframework.com.muscat.chat.mapper.RoomMapper;
import egovframework.com.muscat.chat.service.ChattingService;
import egovframework.com.muscat.chat.service.MessageVO;
import egovframework.com.muscat.chat.service.RoomVO;
import egovframework.com.muscat.chat.service.UserVO;
import egovframework.com.uss.umt.service.MberManageVO;
@Service
public class ChattingServiceImpl implements ChattingService{

	@Autowired RoomMapper roomMapper;
	
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
	public String insertRoom(LoginVO userman , List<String> userList) {
		
		RoomVO roomVO = new RoomVO();
		
		//방등록
		String roomName = userman.getName() + ( userList.size() > 0 ?  "외 " + userList.size() + "명" : "" ) ;
		roomVO.setRoomName(roomName); // roomname을 만들어준다.
		int result = roomMapper.roomi(roomVO);  // db안에 있는 변화되는 행 갯수를 반환 해준다.
		
		//초대 한 사람 등록
		insertUsers(roomVO.getRoomId(), userList);

		return roomVO.getRoomId();
	}
	

	//초대
	@Override
	public String insertUsers(String roomid, List<String> userList) {
		   
		UserVO userVO = new UserVO();
		userVO.setRoomId(roomid);
			  for(String user : userList) { 
				  userVO.setUserId(user);
				  roomMapper.insertUser(userVO); 
		  
			  }
		  
		  return "success"; 
	}
	
	//메시지 등록 
	@Override
	public String insertMessage(MessageVO messaged) {
		int insertCount = roomMapper.insertMessage(messaged);
		return insertCount > 0 ? "success" : "fail";
	}
	
	//메시지 조회
	@Override
	public List<MessageVO> findMessage(MessageVO findmessage) {	
		return roomMapper.findMessage(findmessage);
		
	}
	
	// 채팅방 조회
	@Override
	public List<RoomVO> findroom(UserVO findroom) {
		
		return roomMapper.findroom(findroom);
	}
	
	 //채팅방 인원 조회 
	 @Override
	 public List<String> findParticipantsByRoomId(String roomId) {
	        return roomMapper.findParticipantsByRoomId(roomId);
	        
	    }

	 //인원 조회
	@Override
	public List<MberManageVO> findMember() {	
		return roomMapper.findMember();
	}
	
	//방삭제
	@Override
	public int deleteuser(UserVO userman) {
		
		return roomMapper.deleteuser(userman);
	}

}
