package com.bhasaka.newsportal.core.models;

import com.bhasaka.newsportal.core.services.StudentService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class StudentDetailsModel {

    @ValueMapValue(name = "firstName")
    private String firstName;

    @ValueMapValue
    private String lastName;

    @ValueMapValue
    private String gender;

    @ValueMapValue
    private Date dob;

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String altText;

    @ValueMapValue
    private String mobileNumber;

    @ValueMapValue
    private String email;

    @ValueMapValue
    private String modeOfContact;

    @ValueMapValue
    private String address;

    @ValueMapValue
    private String detailsOption;

    @ValueMapValue
    private String maritalStatus;

    @ValueMapValue
    private String spouseName;

    @ValueMapValue
    private String spouseAge;

    @ValueMapValue
    private String spouseOccupation;

    @ChildResource
    List<EducationalDetailsModel> educationalDetailsGroup;

    @OSGiService
    List<StudentService> studentService;

    private Date todayDate;

    private int age = 0;

    @PostConstruct
    public void init(){
        if (dob == null) {
            age = 0;
        } else {
            LocalDate birthDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();
            age = Period.between(birthDate, currentDate).getYears();
        }
    }

    //Getters
    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public String getSpouseAge() {
        return spouseAge;
    }

    public String getSpouseOccupation() {
        return spouseOccupation;
    }

    public List<StudentService> getStudentService() {
        return studentService;
    }

    public String getFirstName() {
        return firstName;
    }

    public List<EducationalDetailsModel> getEducationalDetailsGroup() {
        return educationalDetailsGroup;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public Date getDob() {
        return dob;
    }

    public String getImage() {
        return image;
    }

    public String getAltText() {
        return altText;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getDetailsOption() {
        return detailsOption;
    }

    public String getModeOfContact() {
        return modeOfContact;
    }

    public String getAddress() {
        return address;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public int getAge() {
        return age;
    }
}
