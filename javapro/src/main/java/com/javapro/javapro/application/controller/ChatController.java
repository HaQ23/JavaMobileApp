package com.javapro.javapro.application.controller;

import com.javapro.javapro.application.dto.ChatRequest;
import com.javapro.javapro.application.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

//...

@RestController
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        ChatRequest request = new ChatRequest(model, prompt);

        try {
            ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);

            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                logger.error("No response from OpenAI API.");
                return "No response";
            }

            return response.getChoices().get(0).getMessage().getContent();
        } catch (HttpClientErrorException e) {
            logger.error("Error during API call: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            return "Error: " + e.getStatusCode() + " " + e.getResponseBodyAsString();
        } catch (Exception e) {
            logger.error("Unexpected error during API call", e);
            return "An unexpected error occurred.";
        }
    }
}
