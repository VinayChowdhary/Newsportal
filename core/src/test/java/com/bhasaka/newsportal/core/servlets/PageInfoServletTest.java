package com.bhasaka.newsportal.core.servlets;

import com.bhasaka.newsportal.core.services.NPUtilService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class PageInfoServletTest {

    AemContext context = new AemContext();

    @Mock
    NPUtilService npUtilService;

    MockSlingHttpServletRequest request;
    MockSlingHttpServletResponse response;

    @InjectMocks
    PageInfoServlet pageInfoServlet;

    @BeforeEach
    void init(){
        request = context.request();
        response = context.response();

        context.registerService(PageInfoServlet.class,pageInfoServlet);
        context.registerService(NPUtilService.class,npUtilService);

        context.create().page("/content/newsportal/us/en/");
    }

    @Test
    void testDoGetMethod() throws ServletException, IOException {
        when(npUtilService.getResourceResolver()).thenReturn(context.resourceResolver());
        context.create().page("/content/newsportal/us/en/article-1","demo-template","Article 1");
        context.create().page("/content/newsportal/us/en/article-2","demo-template","Article 2");
        pageInfoServlet.doGet(request,response);
    }

    @Test
    void testDoPostMethod() throws ServletException, IOException {
        request.addRequestParameter("pageName","article-22");
        request.addRequestParameter("pageTitle","Article 22");
        pageInfoServlet.doPost(request,response);
        assertNotNull(context.resourceResolver().getResource("/content/newsportal/us/en/article-22"));
    }

    @Test
    void testDoDeleteMethod() throws ServletException, IOException {
        context.create().page("/content/newsportal/us/en/article-22","demo-template","Article 22");
        request.addRequestParameter("pageName","article-22");
        pageInfoServlet.doDelete(request,response);
        assertNull(context.resourceResolver().getResource("/content/newsportal/us/en/article-22"));
    }
}