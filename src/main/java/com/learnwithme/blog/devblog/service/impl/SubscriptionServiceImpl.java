package com.learnwithme.blog.devblog.service.impl;

import com.learnwithme.blog.devblog.dto.BlogPostDto;
import com.learnwithme.blog.devblog.exception.BadRequestException;
import com.learnwithme.blog.devblog.model.Subscriber;
import com.learnwithme.blog.devblog.repository.SubscriberRepository;
import com.learnwithme.blog.devblog.service.EmailService;
import com.learnwithme.blog.devblog.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public Subscriber subscribe(String email) {
        // Check if the email is already subscribed
        if (subscriberRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("This email address is already subscribed.");
        }
        Subscriber newSubscriber = new Subscriber();
        newSubscriber.setEmail(email);
        newSubscriber.setSubscribedAt(LocalDateTime.now());
        return subscriberRepository.save(newSubscriber);
    }

    @Override
    public void notifySubscribers(BlogPostDto newPost) {
        List<Subscriber> subscribers = subscriberRepository.findAll();

        if (subscribers.isEmpty()) {
            System.out.println("No subscribers to notify.");
            return;
        }

        String subject = "New Post on Our Blog: " + newPost.getTitle();
        String textBody = String.format(
                "Hello,\n\nA new post has been published!\n\nTitle: %s\n\nRead it here: %s\n\nThanks for subscribing!",
                newPost.getTitle(),
                "https://your-frontend-url.com/blog/" + newPost.getSlug() // <-- अपनी वेबसाइट का सही URL यहाँ डालें
        );

        System.out.printf("Sending notification for post '%s' to %d subscribers...%n", newPost.getTitle(), subscribers.size());

        for (Subscriber subscriber : subscribers) {
            try {
                emailService.sendSimpleMessage(subscriber.getEmail(), subject, textBody);
            } catch (Exception e) {
                // Log error and continue to next subscriber
                System.err.printf("Failed to send email to %s. Error: %s%n", subscriber.getEmail(), e.getMessage());
            }
        }
    }
}