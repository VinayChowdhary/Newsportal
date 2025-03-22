package com.bhasaka.newsportal.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface NPEmailServiceConfiguration2 {

    @AttributeDefinition(name = "SMTP Host")
    String host() default "smtp.gmail.com";

    @AttributeDefinition(name = "SMTP Port")
    int port() default 587;

    @AttributeDefinition(name = "Sender Email")
    String userName() default "your-email@gmail.com";

    @AttributeDefinition(name = "Sender Password (App Password)")
    String password() ;
}
