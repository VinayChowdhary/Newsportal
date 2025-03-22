package com.bhasaka.newsportal.core.services;

import com.bhasaka.newsportal.core.models.ArticlePagesModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;


import java.io.IOException;
import java.util.List;

@Component(service = PageManagerService.class)
@Designate(ocd = PageManagerConfiguration.class)
public class PageManagerService {

    private String articleApiUri;

    @Activate
    public void activate(PageManagerConfiguration config) {
        articleApiUri = config.articleRestAPI();
    }

    @Modified
    public void update(PageManagerConfiguration config) {
        articleApiUri = config.articleRestAPI();
    }

    public List<ArticlePagesModel> getArticles() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(articleApiUri);
            try {
                CloseableHttpResponse response = client.execute(getRequest);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());

                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(jsonResponse, new TypeReference<List<ArticlePagesModel>>() {
                    });
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getRestArticles() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(articleApiUri);
            try {
                CloseableHttpResponse response = client.execute(getRequest);
                if (response.getStatusLine().getStatusCode() == 200) {
                    return EntityUtils.toString(response.getEntity());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}