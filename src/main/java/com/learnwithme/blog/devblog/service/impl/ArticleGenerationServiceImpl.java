package com.learnwithme.blog.devblog.service.impl;

import com.learnwithme.blog.devblog.dto.BlogPostDto;
import com.learnwithme.blog.devblog.service.ArticleGenerationService;
import com.learnwithme.blog.devblog.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;
import java.util.Collections;

@Service
public class ArticleGenerationServiceImpl implements ArticleGenerationService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BlogPostService blogPostService;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    /**
     * AI का उपयोग करके दिए गए केटेगरी पर एक नया ब्लॉग पोस्ट जेनरेट करता है
     * और उसे ड्राफ़्ट के रूप में सेव करता है।
     * @param categoryName जिस केटेगरी के लिए आर्टिकल जेनरेट करना है।
     * @return जेनरेट और सेव किया गया BlogPostDto.
     */
    @Override
    public BlogPostDto createGeneratedPost(String categoryName) {
        // 1. AI के लिए एक विस्तृत प्रॉम्प्ट तैयार करें
        String prompt = createDetailedPrompt(categoryName);

        // 2. Gemini API के लिए रिक्वेस्ट बॉडी तैयार करें
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );


        String fullApiUrl = apiUrl + "?key=" + apiKey;
        Map<String, Object> apiResponse = restTemplate.postForObject(fullApiUrl, requestBody, Map.class);


        String generatedContent = parseApiResponse(apiResponse);


        BlogPostDto generatedPostDto = createDtoFromGeneratedContent(generatedContent, categoryName);


        return blogPostService.createPost(generatedPostDto, null);
    }

    private String createDetailedPrompt(String categoryName) {
        return String.format(
                "You are an expert tech blogger. Your task is to write a high-quality blog post about a fresh and relevant topic within the '%s' category." +
                        " The output must be a single block of text with clear separators." +
                        " Use the following structure EXACTLY:" +
                        "\n\nTITLE: [An engaging and SEO-friendly title here]" +
                        "\n\nSUMMARY: [A concise summary of about 50-60 words]" +
                        "\n\nCONTENT: [The full article content in Markdown format, around 700-800 words. Use H2 and H3 for headings.]",
                categoryName
        );
    }

    private String parseApiResponse(Map<String, Object> apiResponse) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) apiResponse.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            // बेहतर एरर हैंडलिंग के लिए यहाँ लॉगिंग जोड़ें
            throw new RuntimeException("Failed to parse response from AI API", e);
        }
    }

    private BlogPostDto createDtoFromGeneratedContent(String text, String categoryName) {
        String title = text.substring(text.indexOf("TITLE:") + 6, text.indexOf("SUMMARY:")).trim();
        String summary = text.substring(text.indexOf("SUMMARY:") + 8, text.indexOf("CONTENT:")).trim();
        String content = text.substring(text.indexOf("CONTENT:") + 8).trim();

        BlogPostDto dto = new BlogPostDto();
        dto.setTitle(title);
        dto.setSummary(summary);
        dto.setContent(content);
        dto.setPublished(false); // हमेशा ड्राफ़्ट के रूप में सेव करें
        // आपको categoryId को categoryName से ढूंढने का लॉजिक लिखना होगा
        // dto.setCategoryId("ID_OF_" + categoryName);
        return dto;
    }
}