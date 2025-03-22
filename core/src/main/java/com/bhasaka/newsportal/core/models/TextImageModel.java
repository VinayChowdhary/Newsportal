package com.bhasaka.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {Resource.class})
public class TextImageModel {

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String textAlignment;

    public String getDescription() {
        return description;
    }

    public String getTextAlignment() {
        return textAlignment;
    }
}
