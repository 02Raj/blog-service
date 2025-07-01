package com.learnwithme.blog.devblog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class AwsConfig {

    // Access Key और Secret Key वाले फील्ड्स को यहाँ से हटा दिया गया है।

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public SesClient sesClient() {
        // क्रेडेंशियल्स बनाने वाला कोड हटा दिया गया है।
        // AWS SDK अब अपने आप EC2 इंस्टेंस से जुड़े IAM Role से परमिशन ले लेगा।
        return SesClient.builder()
                .region(Region.of(region))
                .build();
    }
}