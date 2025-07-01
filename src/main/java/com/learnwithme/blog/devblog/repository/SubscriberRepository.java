package com.learnwithme.blog.devblog.repository;
import com.learnwithme.blog.devblog.model.Subscriber;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface SubscriberRepository extends MongoRepository<Subscriber, String> {

    /**
     * Finds a subscriber by their email address.
     * @param email The email to search for.
     * @return An Optional containing the subscriber if found.
     */
    Optional<Subscriber> findByEmail(String email);
}
