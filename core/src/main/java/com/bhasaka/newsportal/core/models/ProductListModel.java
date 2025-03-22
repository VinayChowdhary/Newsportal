package com.bhasaka.newsportal.core.models;

import Utils.NewsportalConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductListModel {

    @ValueMapValue
    private String[] productPages;

    @SlingObject
    ResourceResolver resolver;


    private static final String PRODUCT_DETAILS_RESOURCE_TYPE = "newsportal/components/product-details";
    private static final String PRODUCT_BUTTON_RESOURCE_TYPE = "newsportal/components/product-button";

    private List<Resource> productListPages;

    @PostConstruct
    public void init() {
        productListPages = new ArrayList<>();
        if (Objects.nonNull(productPages)) {
            for (String page : productPages) {
                Resource productsResource = resolver.getResource(page + NewsportalConstants.JCR_CONTENT);
                if (Objects.nonNull(productsResource)) {
                    findProductDetails(productsResource);
                }
            }
        }
    }

    private void findProductDetails(Resource resource) {
        for (Resource child : resource.getChildren()) {
            if (PRODUCT_DETAILS_RESOURCE_TYPE.equals(child.getResourceType())) {
                productListPages.add(child);
                if(PRODUCT_BUTTON_RESOURCE_TYPE.equals(child.getResourceType())){

                }
            }

            else {
                findProductDetails(child);
            }
        }
    }

    public String[] getProductPages() {
        return productPages;
    }

    public ResourceResolver getResolver() {
        return resolver;
    }

    public List<Resource> getProductListPages() {
        return productListPages;
    }




}

