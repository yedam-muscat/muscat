package egovframework.com.muscat.bot.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Gemini {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro:generateContent";
    private static final String API_KEY = "";  //  반드시 입력하세요

    public static String askGemini(String prompt) throws Exception {
        URL url = new URL(API_URL + "?key=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String jsonInputString = "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                "      \"parts\": [\n" +
                "        { \"text\": \"" + prompt.replace("\"", "\\\"") + "\" }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 🔧 Content-Length 및 요청 바디 write
        byte[] inputBytes = jsonInputString.getBytes(StandardCharsets.UTF_8);
        conn.setRequestProperty("Content-Length", String.valueOf(inputBytes.length));

        try (OutputStream os = conn.getOutputStream()) {
            os.write(inputBytes);
        }

        //  응답 수신
        int responseCode = conn.getResponseCode();
        InputStream is = responseCode == 200 ? conn.getInputStream() : conn.getErrorStream();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }

            if (responseCode != 200 || !response.toString().startsWith("{")) {
                System.err.println("API 호출 실패: " + response);
                throw new RuntimeException("API 호출 실패: " + response);
            }

            return response.toString();
        }
    }
}
