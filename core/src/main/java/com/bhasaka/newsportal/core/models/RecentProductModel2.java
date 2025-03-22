package com.bhasaka.newsportal.core.models;

import Utils.NewsportalUtil;
import org.apache.commons.math.stat.descriptive.summary.Product;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RecentProductModel2 {

    @ValueMapValue
    private boolean fetchRecentProducts;

    @ValueMapValue
    private int numberOfProducts;

    @SlingObject
    ResourceResolver resolver;

    private static final String PARENT_PAGE_PATH = "/content/newsportal/us/en/products/";
    private static final String PRODUCT_DETAILS_RESOURCE_TYPE = "newsportal/components/product-details";

    private List<ProductDetailsModel> productListPages;

    @PostConstruct
    void init() {
        productListPages = new ArrayList<>();
        if(fetchRecentProducts){
            Resource productResource = resolver.getResource(PARENT_PAGE_PATH);
            if(Objects.nonNull(productResource)){
                productListPages = fetchRecentProducts(productResource);
            }
        }
    }

    public List<ProductDetailsModel> fetchRecentProducts(Resource parentResource){
        List<ProductDetailsModel> recentProducts = new ArrayList<>();

        List<Resource> productPagesList = new ArrayList<>();

        for(Resource productPage : parentResource.getChildren()){
            Resource contentResource = productPage.getChild("jcr:content");
            if(Objects.nonNull(contentResource)){
                productPagesList.add(contentResource);
            }
        }
        productPagesList.sort((r1,r2)->{
            Calendar date1 = r1.getValueMap().get("cq:lastModified",Calendar.class);
            Calendar date2 = r2.getValueMap().get("cq:lastModified",Calendar.class);

            return date2.compareTo(date1);
        });
        for(Resource pageResource : productPagesList){
            Resource componentResource = NewsportalUtil.getComponentResource(pageResource,PRODUCT_DETAILS_RESOURCE_TYPE);
//            Resource componentResource = NewsportalUtil.getComponentResource(pageResource,PRODUCT_DETAILS_RESOURCE_TYPE);
            if(Objects.nonNull(componentResource)){
                recentProducts.add(componentResource.adaptTo(ProductDetailsModel.class));
            }
        }
        return recentProducts.stream().limit(numberOfProducts).collect(Collectors.toList());
    }

    public List<ProductDetailsModel> getProductListPages() {
        return productListPages;
    }
}
