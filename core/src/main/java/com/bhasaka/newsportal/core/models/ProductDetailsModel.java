package com.bhasaka.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {Resource.class},defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductDetailsModel {

    @ValueMapValue
    private String productTitle;

    @ValueMapValue
    private String productImage;

    @ValueMapValue
    private String productDescription;

    @ValueMapValue
    private String productPrice;

    @ValueMapValue
    private String productWeight;

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

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductWeight() {
        return productWeight;
    }

}
