package com.learnwithme.blog.devblog.dto;

import java.util.List;

public class GeneratedArticleContentDto {
    private String title;
    private String content; // Markdown format
    private String summary;
    private List<String> tags;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}