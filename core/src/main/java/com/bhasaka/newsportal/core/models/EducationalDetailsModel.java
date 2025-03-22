package com.bhasaka.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Date;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EducationalDetailsModel {

    @ValueMapValue
    private String collegeName;

    @ValueMapValue
    private String percentage;

    @ValueMapValue
    private Date passedOutDate;

    @ValueMapValue
    private String backlogStatus;

    public String getCollegeName() {
        return collegeName;
    }

    public String getPercentage() {
        return percentage;
    }

    public Date getPassedOutDate() {
        return passedOutDate;
    }

    public String getBacklogStatus() {
        return backlogStatus;
    }
}
