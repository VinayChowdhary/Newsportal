package com.bhasaka.newsportal.core.services.Impl;

import com.bhasaka.newsportal.core.services.ExampleConfiguration;
import com.bhasaka.newsportal.core.services.ExampleService;
import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        immediate = true,
        service = ExampleService.class
)
@Slf4j
@Designate(ocd = ExampleConfiguration.class)
public class ExampleServiceImpl implements ExampleService {


    private static final Logger log = LoggerFactory.getLogger(ExampleServiceImpl.class);

    String firstName;

    @Activate
    public void activate(ExampleConfiguration config){
        firstName =  config.firstName();
        log.info("Inside activate Method");
        log.info(getFirstName());
        log.info(getLastName());
    }

    @Deactivate
    public void deactivate(){

    }

    @Modified
    public void update(){
        log.info("Inside Update Method");
        log.info(getFirstName());
        log.info(getLastName());
    }


    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return "Polina";
    }
}
