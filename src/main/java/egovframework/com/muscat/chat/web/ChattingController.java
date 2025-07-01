package egovframework.com.muscat.chat.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.muscat.chat.service.ChattingService;

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
}
