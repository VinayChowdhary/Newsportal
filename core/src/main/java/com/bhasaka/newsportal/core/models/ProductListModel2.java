package com.bhasaka.newsportal.core.models;

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
public class ProductListModel2 {

    @ValueMapValue
    private String[] productPages;

    @SlingObject
    ResourceResolver resolver;

    private static final String PRODUCT_DETAILS_RESOURCE_TYPE = "newsportal/components/product-details";
    private static final String PRODUCT_BUTTON_RESOURCE_TYPE = "newsportal/components/product-button";
    private static final String JCR_CONTENT_ROOT = "/jcr:content";

    private List<ProductDetailsModel> productListPages;

    @PostConstruct
    public void init() {
        productListPages = new ArrayList<>();
        if (Objects.nonNull(productPages)) {
            for (String page : productPages) {
                Resource productsResource = resolver.getResource(page + JCR_CONTENT_ROOT);
                if (Objects.nonNull(productsResource)) {
                    findProductDetails(productsResource);
                }
            }
        }
    }

    private void findProductDetails(Resource resource) {
        for (Resource child : resource.getChildren()) {
            if (PRODUCT_DETAILS_RESOURCE_TYPE.equals(child.getResourceType())) {
                if(PRODUCT_BUTTON_RESOURCE_TYPE.equals( child.getResourceType())){
                    ProductButtonModel productButtonModel = child.adaptTo(ProductButtonModel.class);
                }
                ProductDetailsModel productDetails = child.adaptTo(ProductDetailsModel.class);
                if (Objects.nonNull(productDetails)) {
                    productListPages.add(productDetails);
                }
            } else {
                findProductDetails(child);
            }
        }
    }


    public List<ProductDetailsModel> getProductListPages() {
        return productListPages;
    }

    public String[] getProductPages() {
        return productPages;
    }
}
