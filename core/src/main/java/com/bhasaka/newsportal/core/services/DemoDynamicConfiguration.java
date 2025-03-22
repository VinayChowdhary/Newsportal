package com.bhasaka.newsportal.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface DemoDynamicConfiguration {

    @AttributeDefinition(name = "First Name")
    public String firstName() default "Vinay Kumar";

}
