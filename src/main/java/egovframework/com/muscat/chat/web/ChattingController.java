package egovframework.com.muscat.chat.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cmm.LoginVO;
import egovframework.com.muscat.chat.service.ChattingService;
import egovframework.com.muscat.chat.service.MessageVO;
import egovframework.com.muscat.chat.service.UserVO;

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
        model.addAttribute("loginVO", session.getAttribute("loginVO"));
        model.addAttribute("roomId", roomId);
        model.addAttribute("username", username);

        return "chat/chatPopup";  // /WEB-INF/jsp/chat/chatPopup.jsp
    }
    
    //채팅방 등록
    @ResponseBody
    @RequestMapping("chat/insertroom.do")
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
    
    //메시지 등록
    @ResponseBody
    @RequestMapping("chat/insertMessage.do")
    public Map<String, String> insertMessage (@RequestBody MessageVO messaged){
    	
    	
    	Map<String, String > map = new HashMap<String, String>();
    	
    	
    	map.put("result",chattingService.insertMessage(messaged) );
    	
    	
    	
    	return map;
    }
    //채팅방 조회
    @ResponseBody
    @RequestMapping("chat/findroom.do")
    public Map<String, Object> findroom (UserVO findroom){
    
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	map.put("result",chattingService.findroom(findroom));
    	return map;
    }
   
}
