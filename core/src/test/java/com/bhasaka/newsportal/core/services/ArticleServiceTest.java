package com.bhasaka.newsportal.core.services;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ArticleServiceTest {

    AemContext context = new AemContext();

    @Mock
    ArticleConfiguration config;

    ArticleService articleService = new ArticleService();

    @BeforeEach
    void init(){
        when(config.articleRestAPI()).thenReturn("https://gorest.co.in/public/v2/posts");
        when(config.enable()).thenReturn(true);
        when(config.clientId()).thenReturn("767678");
        articleService.activate(config);
    }

    @Test
    void testArticleServiceLifecycleMethods(){
        articleService.activate(config);
        articleService.deactivate(config);
        articleService.update(config);

        assertEquals("https://gorest.co.in/public/v2/posts",articleService.getArticleRestAPIURL());
        assertEquals("767678",articleService.getClientId());
        assertTrue(articleService.isEnable());

    }

    @Test
    void testGetArticles(){
        articleService.getArticles();
    }
}