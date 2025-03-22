package com.bhasaka.newsportal.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface GoRestPageConfiguration {

    @AttributeDefinition(name = "Cron Expression")
    public String cronExpression() default "*/5 * * ? * *";

    @AttributeDefinition(name = "Scheduler Name")
    public String schedulerName() default "gorestPage-scheduler";

    @AttributeDefinition(name = "Enable or disable scheduler")
    public boolean enable() default true;

    @AttributeDefinition(name = "Article Rest API URL")
    public String articleApiUri() default "https://gorest.co.in/public/v2/posts";
}
