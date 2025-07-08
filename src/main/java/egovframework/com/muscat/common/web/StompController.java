package egovframework.com.muscat.common.web;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import egovframework.com.muscat.common.StompMessage;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final SimpMessagingTemplate messaging;

    // ① 채팅 전송
    @MessageMapping("/chat.send")        // /pub/chat.send 로 들어옴
    public void sendMessage(@Payload StompMessage msg, Principal principal) {
        // 보안 세션이 있다면 principal.getName() 으로 강제 설정도 가능
        messaging.convertAndSend("/topic/room/" + msg.getRoomId(), msg);
    }

    // ② 방 입장 알림 (선택)
    @MessageMapping("/chat.enter")
    public void enter(StompMessage msg) {
        msg.setType(StompMessage.Type.ENTER);
        messaging.convertAndSend("/topic/room/" + msg.getRoomId(), msg);
    }
}