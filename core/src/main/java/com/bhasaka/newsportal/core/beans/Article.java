package com.bhasaka.newsportal.core.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Article {

    @JsonProperty("id")
    private int id;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
