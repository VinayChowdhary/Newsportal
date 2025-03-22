package com.bhasaka.newsportal.core.servlets;

import com.bhasaka.newsportal.core.services.NPUtilService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

@Component(service = Servlet.class)
@SlingServletPaths("/newsportal/page-info")
public class PageInfoServlet extends SlingAllMethodsServlet {

    @Reference
    NPUtilService npUtilService;

    @Override
    protected void doGet(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException {

        ResourceResolver resolver = npUtilService.getResourceResolver() ;
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        Page articlePage = pageManager.getPage("/content/newsportal/us/en/");
        JsonArrayBuilder pagesJson = Json.createArrayBuilder();  //[]

        if(articlePage !=null){
           Iterator<Page> childPages = articlePage.listChildren();
            while (childPages.hasNext()) {
                Page childPage = childPages.next();
                JsonObjectBuilder pageJson = Json.createObjectBuilder(); // {}
                pageJson.add("title",childPage.getTitle());
                pageJson.add("page",childPage.getPath());
                pagesJson.add(pageJson);
            }
        }
        response.setContentType("application/json");
        response.getWriter().write(pagesJson.build().toString());
    }

    @Override
    protected void doPost(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resolver = request.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);

        String pageName = request.getParameter("pageName");
        String pageTitle = request.getParameter("pageTitle");

        try {
            pageManager.create(
                    "/content/newsportal/us/en",
                    pageName,
                    "/conf/newsportal/settings/wcm/templates/article-template",
                    pageTitle
            );    //don't hardcode the values , give dynamically by request object
        } catch (WCMException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().write("Page created successfully..");
    }

    @Override
    protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resolver = request.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        String pageName = request.getParameter("pageName");

        Page articlePage = pageManager.getPage("/content/newsportal/us/en/"+pageName);
        if(articlePage!=null){
            try {
                pageManager.delete(articlePage,false);

            } catch (WCMException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
