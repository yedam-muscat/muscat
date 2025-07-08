package egovframework.com.muscat.chat.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cmm.LoginVO;
import egovframework.com.muscat.chat.service.ChattingService;
import egovframework.com.muscat.chat.service.MessageVO;
import egovframework.com.muscat.chat.service.UserVO;
import egovframework.com.uss.umt.service.MberManageVO;

@Controller
public class ChattingController {

    @Autowired
    private ChattingService chattingService;
    
    // 메신저 메인 화면
    @RequestMapping("/chat/chat.do")
    public String chatMain(HttpSession session, ModelMap model) {
        String roomId = chattingService.getRoomId();
        String username = chattingService.getUsername();

        model.addAttribute("loginVO", session.getAttribute("loginVO"));  // 로그인 사용자
        model.addAttribute("roomId", roomId);                            // 채팅방 ID
        model.addAttribute("username", username);                        // 사용자 이름

        return "chat/chat.html";  // /WEB-INF/jsp/chat/chatMain.jsp 렌더링
    }

    // 채팅방 팝업
    @RequestMapping("/chat/room.do")
    public String chatRoom(String roomId, String username, HttpSession session, ModelMap model) {
    	LoginVO loginVO = (LoginVO) session.getAttribute("loginVO"); // 세션에서 꺼냄
        model.addAttribute("loginVO", loginVO);
        model.addAttribute("roomId", roomId);
        model.addAttribute("username", username);
        model.addAttribute("loginUserId", loginVO != null ? loginVO.getId() : "");

        return "chat/chatPopup";  // /WEB-INF/jsp/chat/chatPopup.jsp
    }
    
    //채팅방 등록
    @ResponseBody
    @RequestMapping("/chat/insertroom.do")
    public Map<String, String> insertRoom(@RequestBody List<String> users, HttpSession session) {
        Map<String, String> resultMap = new HashMap<>();
        LoginVO loginVO = (LoginVO) session.getAttribute("loginVO");
        String roomId = chattingService.insertRoom(loginVO, users);
        
        resultMap.put("result", roomId != null ? "SUCCESS" : "FAIL");
        resultMap.put("roomId", roomId);  // 클라이언트에 방 ID 제공
        return resultMap;
    }
    
    //사용자 등록(초대)
    @ResponseBody
    @RequestMapping("chat/insertUser.do")
    public Map<String, String> insertuser (@RequestBody List<String> userList){
    	Map<String, String > map = new HashMap<String, String>();
    	
    	String roomId ="";
    	map.put("result",chattingService.insertUsers( roomId,userList));
    		
    	return map;
  
    };
    
    
    //채팅방 조회
    @ResponseBody
    @RequestMapping("/chat/findroom.do")
    public Map<String, Object> findroom (UserVO findroom){
    
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	map.put("result",chattingService.findroom(findroom));
    	
    	System.out.println("받은 userId: " + findroom.getUserId());
    	System.out.println("채팅방 수: " + chattingService.findroom(findroom).size());
    	return map;
    }
    
    //채팅방 인원 조회
    @ResponseBody
    @RequestMapping("/chat/participants.do")
    public List<String> getParticipants(@RequestParam String roomId) {
        return chattingService.findParticipantsByRoomId(roomId);
    }
   
    
    //메시지 등록
    @ResponseBody
    @RequestMapping("/chat/insertMessage.do")
    public Map<String, String> insertMessage (@RequestBody MessageVO messaged){

    	// messageId가 null이면 새로 생성
        if (messaged.getMessageId() == null || messaged.getMessageId().isEmpty()) {
            messaged.setMessageId(UUID.randomUUID().toString()); // 원래는 시퀀스를 사용하면 됩니다. uuid ㅠㅠ
        }

        System.out.println("메시지 등록 요청 들어옴: " + messaged);

        Map<String, String > map = new HashMap<>();
        map.put("result", chattingService.insertMessage(messaged));
        return map;
    }
    
    // 메시지 조회
    @ResponseBody
    @RequestMapping("/chat/findMessage.do")
    public Map<String, Object> findMessage (MessageVO message){
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("result",chattingService.findMessage(message));
    	
    	return map;
    }
    
    //인원조회
    @ResponseBody
    @RequestMapping("/chat/findMember.do")
    public Map<String, Object> findMember (){
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("result",chattingService.findMember());
    	
    	return map;
    }
    
    
}
