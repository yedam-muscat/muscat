package egovframework.com.muscat.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor; // ← 이름 확인

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");   // 1) 먼저 애플리케이션 프리픽스
        registry.enableSimpleBroker("/topic", "/queue");      // 2) 브로커 경로
        registry.setUserDestinationPrefix("/user");           // 3) 1:1 전송 프리픽스
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor()) // ← 클래스명 교체
                .withSockJS();
    }
}
