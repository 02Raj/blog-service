package com.learnwithme.blog.devblog.config;


import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "djudxjlyc");
        config.put("api_key", "167844856964621");
        config.put("api_secret", "iAy1MWSnFekt_4ZXgjWPq8peX3U");
        return new Cloudinary(config);
    }
}
