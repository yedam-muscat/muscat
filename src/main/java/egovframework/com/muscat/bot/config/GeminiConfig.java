package egovframework.com.muscat.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import egovframework.com.muscat.bot.service.Gemini;

@Configuration
public class GeminiConfig {

    @Bean
    public Gemini geminiApiClient() {
        return new Gemini();
    }
}