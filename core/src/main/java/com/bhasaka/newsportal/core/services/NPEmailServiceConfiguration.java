package com.bhasaka.newsportal.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface NPEmailServiceConfiguration {

    @AttributeDefinition(name = "host Name")
    public String host() default "sandbox.smtp.mailtrap.io";

    @AttributeDefinition(name = "port Number")
    public int port() default 2525;

    @AttributeDefinition(name = "Username")
    public String userName() default "53ab2f540d6fe1";

    @AttributeDefinition(name = "password" , type = AttributeType.PASSWORD)
    public String password() default "53ab2f540d6fe1";



}
