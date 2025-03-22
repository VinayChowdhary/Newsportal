package com.bhasaka.newsportal.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import com.bhasaka.newsportal.core.services.ArticleService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.Date;

@Model(
        adaptables = Resource.class,
        resourceType = "newsportal/components/article-details",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ArticleDetailsModel {

    @ValueMapValue
    private String articleTitle;

    @ValueMapValue
    private String articleDesc;

    @ValueMapValue
    private String articleImage;

    @ValueMapValue
    private Date articleExpiry;

    private boolean articleIsExpired = false;

//    @OSGiService
//    ArticleService articleService;

    @PostConstruct
    public void init() {
        if (articleExpiry != null) {
            Date todayDate = new Date();
            if (articleExpiry.compareTo(todayDate) < 0) {
                articleIsExpired = true;
            }
        }
    }

//    public String getClientId() {
//        return articleService.getClientId();
//    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleDesc() {
        return articleDesc;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public Date getArticleExpiry() {
        return articleExpiry;
    }

    public boolean isArticleIsExpired() {
        return articleIsExpired;
    }
}
