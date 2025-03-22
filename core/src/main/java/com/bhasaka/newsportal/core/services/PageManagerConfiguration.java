package com.bhasaka.newsportal.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface PageManagerConfiguration {

    @AttributeDefinition(name="Article Rest API")
    public String articleRestAPI() default "https://gorest.co.in/public/v2/posts";

}

