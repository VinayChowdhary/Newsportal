package com.bhasaka.newsportal.core.schedulers;


import com.bhasaka.newsportal.core.beans.GoRestArticle;
import com.bhasaka.newsportal.core.services.NPUtilService;
import com.bhasaka.newsportal.core.services.PageManagerService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

//@Component(service = Runnable.class,
//        immediate = true
//)
//@Designate(ocd = GoRestPageConfiguration.class)
public class GoRestPageScheduler2 implements Runnable{

    private static final String PARENT_PAGE_PATH = "/content/newsportal/us/en/gorest-pages/";
    private static final String TEMPLATE = "/conf/newsportal/settings/wcm/templates/article-template/";

    private String articleApiUri;

    @Reference
    Scheduler scheduler;

    @Reference
    PageManagerService pageManagerService;

    @Reference
    NPUtilService npUtilService;

    @Activate
    @Modified
    public void activate(GoRestPageConfiguration config){
        articleApiUri = config.articleApiUri();
        if(config.enable()){
            ScheduleOptions options = scheduler.EXPR(config.cronExpression());
            options.name(config.schedulerName());
            options.canRunConcurrently(false);

            scheduler.schedule(this,options);
        }
        else{
            scheduler.unschedule(config.schedulerName());
        }
    }

    @Override
    public void run() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(articleApiUri);
            try {
                CloseableHttpResponse response = client.execute(getRequest);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    try (ResourceResolver resolver = npUtilService.getResourceResolver()) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            List<GoRestArticle> articles = objectMapper.readValue(jsonResponse, new TypeReference<List<GoRestArticle>>() {
                            });

                            for (GoRestArticle article : articles) {
                                String id = article.getId();
                                String title = article.getTitle();
                                String body = article.getBody();

                                String pagePath = PARENT_PAGE_PATH + id;
                                Resource pageResource = resolver.getResource(pagePath);
                                if (pageResource != null) {
                                    updatePage(pagePath, id, title, body, resolver);
                                } else {
                                    createPage(id, title, body, resolver);
                                }
                            }
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createPage(String id,String title,String body,ResourceResolver resolver){
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        try {
            Page articlePage = pageManager.create(PARENT_PAGE_PATH, id, TEMPLATE, title);
            if(Objects.nonNull(articlePage)){
                Resource contentResource = articlePage.getContentResource();
                if(Objects.nonNull(contentResource)){
                    ModifiableValueMap mprops = contentResource.adaptTo(ModifiableValueMap.class);
                    mprops.put("jcr:description",body);
                    try {
                        resolver.commit();
                    } catch (PersistenceException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (WCMException e) {
            throw new RuntimeException(e);
        }

    }

    public void updatePage(String pageResource,String id,String title,String body ,ResourceResolver resolver){
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        Page existingPage = pageManager.getPage(pageResource);
        if(Objects.nonNull(existingPage)){
            Resource contentResource = existingPage.getContentResource();
            if(Objects.nonNull(contentResource)){
                ModifiableValueMap mprops = contentResource.adaptTo(ModifiableValueMap.class);
                mprops.put("jcr:title",title);
                mprops.put("jcr:description",title);

                try {
                    resolver.commit();
                } catch (PersistenceException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
