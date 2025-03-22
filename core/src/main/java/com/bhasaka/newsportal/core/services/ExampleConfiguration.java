package com.bhasaka.newsportal.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface ExampleConfiguration {

    @AttributeDefinition(name = "First Name")
    public String firstName() default "Vinay Kumar";
}
