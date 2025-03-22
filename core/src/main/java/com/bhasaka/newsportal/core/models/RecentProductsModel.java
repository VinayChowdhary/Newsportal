package com.bhasaka.newsportal.core.models;

import Utils.NewsportalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.service.component.annotations.Reference;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RecentProductsModel {

    private static final String PARENT_PAGE_PATH = "/content/newsportal/us/en/products/";
    private static final String PRODUCT_DETAILS_RESOURCE_TYPE = "newsportal/components/product-details";

    private List<ProductDetailsModel> productListPages;

    @ValueMapValue
    private boolean fetchRecentProducts;

    @ValueMapValue
    private int numberOfProducts;

    @SlingObject
    ResourceResolver resolver;

    @PostConstruct
    void init() {
        productListPages = new ArrayList<>();
        if (fetchRecentProducts) {
            Resource productResource = resolver.getResource(PARENT_PAGE_PATH);
            if (Objects.nonNull(productResource)) {
                productListPages = fetchRecentProducts(productResource);
            }
        }
    }

    public List<ProductDetailsModel> fetchRecentProducts(Resource parentResource) {
        List<Resource> eligiblePages = getEligiblePages(parentResource);

        return eligiblePages.stream()
                .map(Page -> {
//                    Resource productDetailsNode = findProductDetails(Page.getChild("jcr:content"));
                    Resource productDetailsNode = NewsportalUtil.getComponentResource(Page.getChild("jcr:content"),PRODUCT_DETAILS_RESOURCE_TYPE);
                    return productDetailsNode != null ? productDetailsNode.adaptTo(ProductDetailsModel.class) : null;
                }).filter(Objects::nonNull).limit(numberOfProducts).collect(Collectors.toList());
    }

    public List<Resource> getEligiblePages(Resource parentResource) {
        List<Resource> eligiblePages = new ArrayList<>();

        for (Resource child : parentResource.getChildren()) {
            Resource childResource = child.getChild("jcr:content");
            if (childResource != null && childResource.getValueMap().containsKey("cq:lastModified")) {
                eligiblePages.add(child);
            }
           eligiblePages.addAll(getEligiblePages(child));
        }

        eligiblePages.sort((r1, r2) -> {
            Calendar date1 = Optional.ofNullable(r1.getChild("jcr:content"))
                    .map(res -> res.getValueMap().get("cq:lastModified", Calendar.class))
                    .orElse(null);

            Calendar date2 = Optional.ofNullable(r2.getChild("jcr:content"))
                    .map(res -> res.getValueMap().get("cq:lastModified", Calendar.class))
                    .orElse(null);

            return Long.compare(date2 != null ? date2.getTimeInMillis() : 0, date1 != null ? date1.getTimeInMillis() : 0);
        });
        return eligiblePages;
    }

    public List<ProductDetailsModel> getProductListPages() {
        return productListPages;
    }
}
