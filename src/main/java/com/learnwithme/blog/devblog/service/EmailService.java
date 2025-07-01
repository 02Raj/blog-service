package com.learnwithme.blog.devblog.service;

public interface EmailService {

    /**
     * Sends a simple plain text email.
     *
     * @param to       The recipient's email address.
     * @param subject  The subject of the email.
     * @param textBody The plain text body of the email.
     */
    void sendSimpleMessage(String to, String subject, String textBody);
}

