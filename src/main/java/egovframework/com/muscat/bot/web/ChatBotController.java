package egovframework.com.muscat.bot.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.muscat.bot.service.Gemini;
import egovframework.com.muscat.cal.service.CalService;




@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chatbot")
public class ChatBotController {
	
	@Autowired
	private Gemini geminiApiClient;  // 위 클래스 빈 등록 필요
	
	@Autowired
	private CalService scheduleService;

	@PostMapping("/message.do")
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
	 // 3. 날씨 관련 메시지
	    if (message.contains("날씨") || message.contains("서울 날씨")) {
	        // 서울 = 대략 nx=60, ny=127
	        return getWeather("60", "127");
	    }
	  
//	    //  일정 등록 처리
//	    if (message.contains("일정") && message.contains("등록")) {
//	        // 예: "7월 10일 오전 10시에 회의 일정 등록"
//	        String title = extractTitle(message); // 일정 제목
//	        LocalDate date = extractDate(message); // 날짜
//	        LocalTime time = extractTime(message); // 시간
//
//	        // 실제 로그인 사용자 ID로 대체 (예: session에서 가져오기)
//	        String userId = "testUser";
//
//	        // 일정 저장
//	        scheduleService.insertCalendar(title, date, time, userId);
//
//	        return "✅ 일정이 등록되었습니다: " + date + " " + time + " - " + title;
//	    }
	    
	
	 // Gemini API 호출
	    try {
	        String jsonResponse = Gemini.askGemini(message);
	        // jsonResponse에서 실제 답변 텍스트 추출 필요
	        // 예) Jackson 라이브러리로 파싱
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode root = mapper.readTree(jsonResponse);
	        String answer = root.at("/choices/0/message/content").asText();  // API 응답 형식에 따라 변경
	        return answer;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "⚠️ 챗봇 응답에 오류가 발생했습니다.";
	    }
	    
	}

//	// handleMessage 끝난 뒤, 클래스 내에 별도로 위치
//	private String extractTitle(String message) {
//	    return "회의"; // 임시 제목
//	}
//
//	private LocalDate extractDate(String message) {
//	    return LocalDate.now(); // 오늘 날짜
//	}
//
//	private LocalTime extractTime(String message) {
//	    return LocalTime.of(10, 0); // 오전 10시
//	}



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
    
    //날씨
    private String getWeather(String nx, String ny) {
        String serviceKey = "zLzDOKLTH0MnOy%2FJvMhvAEg1nkrknsHN1qxM%2BolnBQqGf0Ode1qcka7A9PfgCO9UK8u4F%2By1PD1yntKEheP83Q%3D%3D"; // 인코딩된 키 사용
        String baseDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = "0800"; // 예: 오전 8시 기준

        String apiUrl = String.format(
            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst" +
            "?serviceKey=%s&numOfRows=10&pageNo=1&dataType=JSON&base_date=%s&base_time=%s&nx=%s&ny=%s",
            serviceKey, baseDate, baseTime, nx, ny
        );

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8")
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // 응답 파싱 (기온, 강수량 등 추출)
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());
            JsonNode items = root.at("/response/body/items/item");

            StringBuilder result = new StringBuilder("🌦️ 현재 날씨 정보\n");
            for (JsonNode item : items) {
                String category = item.get("category").asText();
                String obsrValue = item.get("obsrValue").asText();

                switch (category) {
                    case "T1H": result.append("🌡️ 기온: ").append(obsrValue).append("℃\n"); break;
                    case "RN1": result.append("☔ 1시간 강수량: ").append(obsrValue).append("mm\n"); break;
                    case "REH": result.append("💧 습도: ").append(obsrValue).append("%\n"); break;
                    case "WSD": result.append("💨 풍속: ").append(obsrValue).append("m/s\n"); break;
                }
            }

            return result.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ 날씨 정보를 불러오는 중 오류가 발생했어요.";
        }
    }

    
}

