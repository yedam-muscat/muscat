package egovframework.com.muscat.bot.web;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/chatbot")
public class ChatBotController {

	@PostMapping("/chatbot/message.do")
	public String handleMessage(@RequestBody Map<String, String> payload) {
	    System.out.println("handleMessage 호출됨, 메시지: " + payload.get("message"));
	    String message = payload.get("message");

	    // ✅ 1. 도움말 명령어 처리
	    if (message.contains("도움말") || message.contains("명령어")) {
	        return "🧠 하리봇 도움말\n\n"
	             + "하리봇이 할 수 있는 일:\n"
	             + "------------------------------\n"
	             + "⏰ [시간 확인] 지금 몇 시야?\n"
	             + "🌍 [세계 시간] 세계 시간 알려줘\n"
	             + "📅 [캘린더] #캘린더, 일정 보여줘\n"
	             + "🧍 [직원 검색] 김하리 찾아줘\n"
	             + "📝 [게시판] #게시판, 공지 보여줘\n"
	             + "🌦️ [날씨] 날씨 어때?, 서울 날씨 알려줘\n"
	             + "🕘 [출퇴근] 출근했어?, 근태 보여줘\n"
	             + "❓ [도움말] 도움말, 명령어 알려줘\n"
	             + "------------------------------\n"
	             + "궁금한 걸 자유롭게 물어보세요!";
	    }

	    // ✅ 2. 시간 관련 메시지
	    if (message.contains("시간") || message.contains("몇 시") || message.contains("지금")) {
	        if (message.contains("세계") || message.contains("다른 나라")) {
	            return getWorldTime();
	        } else {
	            return getKoreanTime();
	        }
	    }

	    // ✅ 3. 기본 응답
	    return "무엇을 도와드릴까요?";
	}


    private String getKoreanTime() {
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        return "🕒 지금 시간은 " + now.format(DateTimeFormatter.ofPattern("HH:mm")) + "입니다.";
    }

    private String getWorldTime() {
        ZonedDateTime seoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime newYork = ZonedDateTime.now(ZoneId.of("America/New_York"));
        ZonedDateTime london = ZonedDateTime.now(ZoneId.of("Europe/London"));
        ZonedDateTime tokyo = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        ZonedDateTime sydney = ZonedDateTime.now(ZoneId.of("Australia/Sydney"));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        return String.format(
            "🌍 세계 시간 안내입니다:\n" +
            "🇰🇷 서울: %s\n" +
            "🇺🇸 뉴욕: %s\n" +
            "🇬🇧 런던: %s\n" +
            "🇯🇵 도쿄: %s\n" +
            "🇦🇺 시드니: %s",
            seoul.format(fmt),
            newYork.format(fmt),
            london.format(fmt),
            tokyo.format(fmt),
            sydney.format(fmt)
        );
    }
}
