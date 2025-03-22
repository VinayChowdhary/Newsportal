package com.bhasaka.newsportal.core.models;

import com.bhasaka.newsportal.core.services.PageManagerService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticlePagesModel {

    @OSGiService
    PageManagerService pageManagerService;

    List<ArticlePagesModel> articles;

    @JsonProperty("id")
    private int id;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @PostConstruct
    protected void init() {
        articles = pageManagerService.getArticles();
        if (articles == null) {
            articles = Collections.emptyList();
        }
    }

    public PageManagerService getPageManagerService() {
        return pageManagerService;
    }

    public List<ArticlePagesModel> getArticles() {
        return articles;
    }

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
        return (body.length() > 30) ? body.substring(0, 30) : body;
    }
}
