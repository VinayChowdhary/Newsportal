package com.bhasaka.newsportal.core.servlets;

import Utils.NewsportalConstants;
import Utils.NewsportalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import com.google.gson.JsonObject;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Objects;

@Component(service = Servlet.class)
@SlingServletPaths(value = {"/newsportal/copyrights"})
public class CopyRightServlet extends SlingAllMethodsServlet {

    private static final String COPYRIGHTS_RESOURCE_TYPE = "newsportal/components/copy-rights";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String currentPagePath = request.getParameter("pagePath");
        ResourceResolver resolver = request.getResourceResolver();
//        Resource resource = resolver.getResource(currentPagePath + "/jcr:content");
        Resource resource = resolver.getResource(currentPagePath + NewsportalConstants.JCR_CONTENT);


        JsonObject jsonResponse = new JsonObject();
//        Resource componentResource = findCopyRights(resource);
        Resource componentResource = NewsportalUtil.getComponentResource(resource,COPYRIGHTS_RESOURCE_TYPE);

        if (Objects.nonNull(componentResource)) {
            ValueMap props = componentResource.getValueMap();
            jsonResponse.addProperty("componentText", props.get("componentText", String.class));
            jsonResponse.addProperty("copyrightText", props.get("copyrightText", String.class));
        } else {
            jsonResponse.addProperty("error", "Component not found");
        }
        response.getWriter().write(jsonResponse.toString());
    }
}

