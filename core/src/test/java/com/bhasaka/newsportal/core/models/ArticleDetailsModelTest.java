package com.bhasaka.newsportal.core.models;

import com.bhasaka.newsportal.core.services.ArticleService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ArticleDetailsModelTest {

    AemContext context = new AemContext();

    ArticleDetailsModel articleDetailsModel;

    @Mock
    ArticleService articleService;

    @BeforeEach
    void init() {
        context.addModelsForClasses(ArticleDetailsModel.class);

        context.load().json("/com.bhasaka.newsportal.core.models.ArticleDetailsModel.json", "/content");

        Resource articleJsonResource = context.currentResource("/content/article-details");
        articleDetailsModel = articleJsonResource.adaptTo(ArticleDetailsModel.class);

     /*
       lenient().when(articleService.getClientId()).thenReturn("mock-client-id");

        context.registerService(ArticleService.class, articleService);

        Map<String, Object> props = new HashMap<>();
        props.put("articleTitle", "Title 1");
        props.put("articleDesc", "Dummy Description");
        props.put("articleImage", "/content/newsportal/dam/vinay/vinay-image/");

        Resource resource = context.create().resource("/content/newsportal/article-details", props);
        articleDetailsModel = resource.adaptTo(ArticleDetailsModel.class); */

    }

    @Test
    void articlePropsTest() {
        assertEquals("Title 1", articleDetailsModel.getArticleTitle());
        assertEquals("Dummy Description", articleDetailsModel.getArticleDesc());
        assertEquals("/content/newsportal/dam/vinay/vinay-image/", articleDetailsModel.getArticleImage());
//        assertEquals("mock-client-id", articleDetailsModel.getClientId());
        assertNotNull(articleDetailsModel.getArticleExpiry());
        assertFalse(articleDetailsModel.isArticleIsExpired());
    }

    @Test
    void articleExpiryTest() {

        Resource articleJsonResource = context.currentResource("/content/expired-article-details");
        articleDetailsModel = articleJsonResource.adaptTo(ArticleDetailsModel.class);

        /*
        Map<String, Object> props = new HashMap<>();
        props.put("articleTitle", "Title 1");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -15);

        props.put("articleExpiry", cal);

        Resource resource = context.create().resource("/content/newsportal/article-details/expiry-date", props);
        articleDetailsModel = resource.adaptTo(ArticleDetailsModel.class);
                                                                                                */
        assertNotNull(articleDetailsModel.getArticleExpiry());
        assertTrue(articleDetailsModel.isArticleIsExpired());


    }

}