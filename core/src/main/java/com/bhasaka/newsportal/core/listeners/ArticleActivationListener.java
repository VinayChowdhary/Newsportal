package com.bhasaka.newsportal.core.listeners;

import com.bhasaka.newsportal.core.services.NPUtilService;
import com.day.cq.replication.ReplicationAction;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(service = EventHandler.class,
        property = {
                EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
                EventConstants.EVENT_FILTER+"=(&(type=ACTIVATE)(paths=/content/newsportal/us/en/products/*))"
        },
        immediate = true
)       //property="event.topics=com/day/cq/replication"
public class ArticleActivationListener implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleActivationListener.class);

    @Reference
    NPUtilService npUtilService;

    @Override
    public void handleEvent(Event event) {
        String[] paths = (String[])event.getProperty("paths"); //typecasting object to string array
        ResourceResolver resolver = npUtilService.getResourceResolver();
        for(String path:paths){
            Resource contentResource = resolver.getResource(path+"/jcr:content");

            ModifiableValueMap mprops = contentResource.adaptTo(ModifiableValueMap.class);
            mprops.put("pageActivated","true");
        }
        try {
            resolver.commit();
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }

        LOG.info("Inside handle Event method....");
    }
}
