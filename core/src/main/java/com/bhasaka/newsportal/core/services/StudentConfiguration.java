package com.bhasaka.newsportal.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface StudentConfiguration {

    @AttributeDefinition(name = "First Name")
    public String firstName() default "Sathyanarayana";

    @AttributeDefinition(name = "Last Name")
    public String lastName() default "Polina";

    @AttributeDefinition(name = "Gender")
    public String gender() default "male";

    @AttributeDefinition(name = "Age")
    public int age() default 22;
}
