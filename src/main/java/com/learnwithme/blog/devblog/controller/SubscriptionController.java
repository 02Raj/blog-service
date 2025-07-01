package com.learnwithme.blog.devblog.controller;
//package com.learnwithme.blog.devblog.controller;

import com.learnwithme.blog.devblog.dto.ApiResponseDto;
import com.learnwithme.blog.devblog.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * Endpoint to subscribe to the blog newsletter.
     * Expects a JSON payload like: {"email": "user@example.com"}
     */
    @PostMapping("/subscribe")
    public ResponseEntity<ApiResponseDto<Object>> subscribeToNewsletter(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return new ResponseEntity<>(
                    ApiResponseDto.error("A valid email address is required.", HttpStatus.BAD_REQUEST.value()),
                    HttpStatus.BAD_REQUEST
            );
        }


        subscriptionService.subscribe(email);
        return new ResponseEntity<>(
                ApiResponseDto.success("Thank you for subscribing!", HttpStatus.CREATED.value()),
                HttpStatus.CREATED
        );
    }
}
