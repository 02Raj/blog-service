package com.learnwithme.blog.devblog.service;

import com.learnwithme.blog.devblog.dto.BlogPostDto;
import com.learnwithme.blog.devblog.model.Subscriber;

public interface SubscriptionService {

    /**
     * Subscribes a new user with the given email.
     * @param email The email of the user to subscribe.
     * @return The saved Subscriber entity.
     */
    Subscriber subscribe(String email);

    /**
     * Notifies all subscribers about a new blog post.
     * @param newPost The new blog post DTO.
     */
    void notifySubscribers(BlogPostDto newPost);
}
