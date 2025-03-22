package com.bhasaka.newsportal.core.listeners;

import com.bhasaka.newsportal.core.services.NPUtilService;
import com.day.cq.dam.api.DamEvent;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import java.util.Objects;
import java.util.UUID;

@Component(
        service = EventHandler.class,
        immediate = true,
        property = {
                EventConstants.EVENT_TOPIC + "=" + DamEvent.EVENT_TOPIC,
                EventConstants.EVENT_FILTER + "=(&(type=ASSET_CREATED)(paths=/content/dam/newsportal/pdfs/))"
        }
)
public class PdfAssetUploadListener implements EventHandler {

    @Reference
    NPUtilService npUtilService;

    private static final String META_DATA = "/jcr:content/metadata";

    @Override
    public void handleEvent(Event event) {
        String path = (String) event.getProperty("assetPath");

        if (path == null || !path.endsWith(".pdf")) {
            return;
        }

        ResourceResolver resolver = npUtilService.getResourceResolver();
        if (Objects.nonNull(resolver)) {
            Resource contentResource = resolver.getResource(path + META_DATA);
            if (Objects.nonNull(contentResource)) {
                ModifiableValueMap mprops = contentResource.adaptTo(ModifiableValueMap.class);
                if (Objects.nonNull(mprops)) {
                    mprops.put("UniqueId", generateUniqueId());
                    try {
                        resolver.commit();
                    } catch (PersistenceException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}


