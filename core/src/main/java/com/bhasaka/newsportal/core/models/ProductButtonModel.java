package com.bhasaka.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductButtonModel {

    @ValueMapValue
    private String ctaLabel;

    @ValueMapValue
    private String ctaLink;

    public String getCtaLabel() {
        return ctaLabel;
    }

    public String getCtaLink() {
        return ctaLink;
    }
}
