package com.bhasaka.newsportal.core.services;

import com.bhasaka.newsportal.core.services.Impl.DetailsServiceImpl;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = StudentService.class)
@Designate(ocd=StudentConfiguration.class)
public class StudentService {

    private String firstName;
    private String lastName;
    private String gender;
    private int age;


    @Activate
    private void activate(StudentConfiguration config){
    firstName = config.firstName();
    lastName = config.lastName();
    age = config.age();
    gender = config.gender();
    }

    @Deactivate
    private void deactivate(StudentConfiguration config){
    }

    @Modified
    private void update(StudentConfiguration config){
        firstName = config.firstName();
        lastName = config.lastName();
        age = config.age();
        gender = config.gender();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }
}
