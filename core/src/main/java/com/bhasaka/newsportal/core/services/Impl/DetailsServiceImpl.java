package com.bhasaka.newsportal.core.services.Impl;

import com.bhasaka.newsportal.core.services.DetailsDynamicConfiguration;
import com.bhasaka.newsportal.core.services.DetailsService;
import com.bhasaka.newsportal.core.services.ExampleService;
import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Component
//@Designate(ocd = DetailsDynamicConfiguration.class)
public class DetailsServiceImpl implements DetailsService {

    private static final Logger log = LoggerFactory.getLogger(DetailsServiceImpl.class);
    String firstName;

    @Reference
    ExampleService exampleService;

    @Activate
    public void activate(DetailsDynamicConfiguration config){
//        firstName =  config.firstName();
//        log.info("Inside activate method");
//        getLastName();
        log.info(exampleService.getFirstName());

    }

    @Override
    public String getLastName() {
         return "Polina from Details";
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getName() {
        log.info("aem");
        return "name";
    }
}
