package com.bhasaka.newsportal.core.schedulers;

import com.bhasaka.newsportal.core.services.NPUtilService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

@Component(service = Runnable.class,
        immediate = true
)
@Designate(ocd = ArticleExpiryConfiguration.class)
public class ArticleExpiryScheduler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleExpiryScheduler.class);

    private static final String ARTICLE_PARENT_PAGE_PATH = "/content/newsportal/us/en/articles0";

    @Reference
    NPUtilService npUtilService;

    @Reference
    Scheduler scheduler;

    @Activate
    @Modified
    public void activate(ArticleExpiryConfiguration config) {
        if (config.enable()) {
            //activate the scheduler
            ScheduleOptions options = scheduler.EXPR(config.cronExpression());
            options.name(config.schedulerName());
            options.canRunConcurrently(false);

            scheduler.schedule(this, options);
        } else {
            //deactivate the scheduler
            scheduler.unschedule(config.schedulerName());
        }
    }

    @Override
    public void run() {
        ResourceResolver resolver = npUtilService.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        Page articlePage = pageManager.getPage(ARTICLE_PARENT_PAGE_PATH);
        if (Objects.nonNull(articlePage)) {
            Iterator<Page> childPages = articlePage.listChildren();
            while (childPages.hasNext()) {
                Page childPage = childPages.next();
                ValueMap pageProperties = childPage.getProperties();
                Date articleExpiry = pageProperties.get("articleExpiry", Date.class);
                Date todaysDate = new Date();

                if (articleExpiry != null && articleExpiry.compareTo(todaysDate) < 0) {
                    //Deactivate the page
                }
            }
        }

        //Resource Resolver -> Page Manager -> Page
        //iterate all article pages
        //read article expiry property from jcr:content node of page
        //compare article expiry with todays date
        //deactivate programmatically if tha article is expired

        LOG.info("Inside run method from scheduler....");
    }
}
