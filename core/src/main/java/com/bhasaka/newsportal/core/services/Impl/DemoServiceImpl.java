package com.bhasaka.newsportal.core.services.Impl;

import com.bhasaka.newsportal.core.services.DemoDynamicConfiguration;
import com.bhasaka.newsportal.core.services.DemoService;
import com.bhasaka.newsportal.core.services.DetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
@Slf4j
@Component(
        service = DemoService.class,
        immediate = true
)
@Designate(ocd = DemoDynamicConfiguration.class)
public class DemoServiceImpl implements DemoService {
    private static final Logger log = LoggerFactory.getLogger(DemoServiceImpl.class);


    String firstName;

    @Reference
    DetailsService detailsService;


    @Activate
    @Modified
    public void activate(DemoDynamicConfiguration config){
        firstName = config.firstName();
        log.info( config.firstName());
        getLastName();
        log.info(detailsService.getName());

    }

    @Override
    public void getLastName() {
        log.info("Polina");
    }

    @Override
    public String getArticles() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet getRequest = new HttpGet("https://gorest.co.in/public/v2/posts");
        CloseableHttpResponse response = client.execute(getRequest);
        if(response.getStatusLine().getStatusCode() == 200){
            return EntityUtils.toString(response.getEntity());
        }
        return null;
    }
}
