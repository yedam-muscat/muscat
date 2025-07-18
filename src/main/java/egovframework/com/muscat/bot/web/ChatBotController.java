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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;





@RestController
@RequestMapping("/chatbot")
public class ChatBotController {
	
	@SuppressWarnings("deprecation")
	@PostMapping("/message.do")
	public ResponseEntity<String> handleMessage(@RequestBody Map<String, String> payload) {
	    String message = payload.get("message");
	    String reply;

	    if (message.contains("도움말") || message.contains("명령어")) {
	        reply = " 하리봇 도움말"
	             + "하리봇이 할 수 있는 일:"
	             + "------------------------------"
	             + " [시간 확인] 지금 몇 시야?"
	             + " [세계 시간] 세계 시간 알려줘"
	             + " [캘린더] #캘린더, 일정 보여줘"
	             + " [직원 검색] 김하리 찾아줘"
	             + " [게시판] #게시판, 공지 보여줘"
	             + " [날씨] 날씨 어때?, 서울 날씨 알려줘"
	             + " [출퇴근] 출근했어?, 근태 보여줘"
	             + " [도움말] 도움말, 명령어 알려줘"
	             + "------------------------------"
	             + "궁금한 걸 자유롭게 물어보세요!";
	    } else if (message.contains("시간") || message.contains("몇 시") || message.contains("지금")) {
	        if (message.contains("세계") || message.contains("다른 나라")) {
	            reply = getWorldTime();
	        } else {
	            reply = getKoreanTime();
	        }
	    } else if (message.contains("날씨") || message.contains("서울 날씨")) {
	        reply = getWeather("60", "127"); // 서울 
	        reply = getWeather("98", "76"); // 부산
	        reply = getWeather("89", "90"); // 대구
	        reply = getWeather("55", "124"); // 인천
	        reply = getWeather("58", "74"); // 광주
	        reply = getWeather("67", "100"); // 대전
	        reply = getWeather("102", "84"); // 울산
	        reply = getWeather("52", "38"); // 제주
	        
	    } else {
	        reply = "죄송해요, 이해하지 못했어요. 도움말을 보시려면 '도움말'이라고 입력해주세요.";
	    }

	  
	    try {
	        
	        return ResponseEntity.ok()
	                .header("Content-Type", "application/json;charset=UTF-8")
	                .body(reply);
	    } catch (Exception e) {
	        return ResponseEntity.status(500)
	                .body("\" 오류가 발생했습니다.\"");
	    }
	}
	private String getKoreanTime() {
		LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        return " 지금 시간은 " + now.format(DateTimeFormatter.ofPattern("HH:mm")) + "입니다.";
    }

    private String getWorldTime() {
        ZonedDateTime seoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        ZonedDateTime newYork = ZonedDateTime.now(ZoneId.of("America/New_York"));
        ZonedDateTime london = ZonedDateTime.now(ZoneId.of("Europe/London"));
        ZonedDateTime tokyo = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        ZonedDateTime sydney = ZonedDateTime.now(ZoneId.of("Australia/Sydney"));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        return String.format(
        	    " 세계 시간 안내입니다:" +
        	    " 서울: %s" +
        	    " 뉴욕: %s" +
        	    " 런던: %s" +
        	    " 도쿄: %s" +
        	    " 시드니: %s",
        	    seoul.format(fmt),
        	    newYork.format(fmt),
        	    london.format(fmt),
        	    tokyo.format(fmt),
        	    sydney.format(fmt)
        	);
    }
    
    //날씨
    private String getWeather(String nx, String ny) {
        String serviceKey = "zLzDOKLTH0MnOy%2FJvMhvAEg1nkrknsHN1qxM%2BolnBQqGf0Ode1qcka7A9PfgCO9UK8u4F%2By1PD1yntKEheP83Q%3D%3D"; 
        String baseDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = "0800"; 

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

            StringBuilder result = new StringBuilder(" 현재 날씨 정보");
            for (JsonNode item : items) {
                String category = item.get("category").asText();
                String obsrValue = item.get("obsrValue").asText();

                switch (category) {
                    case "T1H": result.append(" 기온: ").append(obsrValue).append("℃"); break;
                    case "RN1": result.append(" 1시간 강수량: ").append(obsrValue).append("mm"); break;
                    case "REH": result.append(" 습도: ").append(obsrValue).append("%"); break;
                    case "WSD": result.append(" 풍속: ").append(obsrValue).append("m/s"); break;
                }
            }

            return result.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "⚠ 날씨 정보를 불러오는 중 오류가 발생했어요.";
        }
    }

 
}

