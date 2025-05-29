package ru.isshepelev.flavorscape.infrastructure.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.isshepelev.flavorscape.infrastructure.service.ReviewService;
import ru.isshepelev.flavorscape.infrastructure.service.dto.ReviewRequestDto;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerateReview {
    private final ReviewService reviewService;

    @Value("${api_key.ai}")
    private String apiKeyAI;

    public String generateAIReview(Long placeId, String username) throws IOException {
        List<ReviewRequestDto> allReviews = reviewService.getAllReviewsForPlace(placeId);
        List<ReviewRequestDto> friendsReviews = reviewService.getFriendsReviewsForPlace(placeId, username);

        if (allReviews.isEmpty()) {
            return "Пока нет отзывов об этом месте.";
        }

        String prompt = buildCombinedPrompt(allReviews, friendsReviews);

        String aiResponse = sendRequestToChatGPT(prompt);

        return extractContentFromResponse(aiResponse);
    }

    private String buildCombinedPrompt(List<ReviewRequestDto> allReviews, List<ReviewRequestDto> friendsReviews) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Ты — помощник для анализа отзывов. Проанализируй отзывы о заведении и напиши краткий вывод (1 предложение) на русском языке. ");
        prompt.append("Учитывай:\n");
        prompt.append("1. Общий рейтинг и тон отзывов\n");
        prompt.append("2. Мнение друзей (если есть)\n");
        prompt.append("3. Основные плюсы и минусы\n\n");
        prompt.append("=== ВСЕ ОТЗЫВЫ ===\n");
        appendReviewsToPrompt(prompt, allReviews);

        if (!friendsReviews.isEmpty()) {
            prompt.append("Обрати внимание на отзывы друзей и добавь к общему мнению еще краткий вывод (1 предложение) о мнении друзей по поводу этого заведения начни с 'ваши друзья считают '");
            prompt.append("\n=== ОТЗЫВЫ ДРУЗЕЙ ===\n");
            appendReviewsToPrompt(prompt, friendsReviews);
        }

        prompt.append("\nВывод (начни с 'Заведение ' и учти мнение друзей, если оно отличается от общего):");

        return prompt.toString();
    }

    private void appendReviewsToPrompt(StringBuilder prompt, List<ReviewRequestDto> reviews) {
        for (ReviewRequestDto review : reviews) {
            prompt.append("Автор: ").append(review.authorUsername())
                    .append(", Средняя Оценка: ").append(review.generalRating()).append("/5\n")
                    .append("Отзыв: ").append(review.content()).append("\n\n");
        }
    }

    private String sendRequestToChatGPT(String prompt) throws IOException {
        String url = "https://openrouter.ai/api/v1/chat/completions";

        String requestBody = String.format("""
        {
            "model": "openai/gpt-3.5-turbo",
            "messages": [{"role": "user", "content": "%s"}],
            "temperature": 0.7,
            "max_tokens": 300
        }
        """, escapeJson(prompt));

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + apiKeyAI);
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining());
        } finally {
            connection.disconnect();
        }
    }

    private String extractContentFromResponse(String jsonResponse) {
        try {
            int start = jsonResponse.indexOf("\"content\":\"") + 11;
            int end = jsonResponse.indexOf("\"", start);
            return jsonResponse.substring(start, end)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"");
        } catch (Exception e) {
            return "Не удалось сгенерировать отзыв. Попробуйте позже.";
        }
    }

    private String escapeJson(String input) {
        return input.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}