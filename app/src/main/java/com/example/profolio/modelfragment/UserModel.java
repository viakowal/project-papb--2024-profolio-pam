package com.example.profolio.modelfragment;

public class UserModel {
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String seniorHighSchool;
    private String seniorHighSchoolPeriod;
    private String university;
    private String universityPeriod;
    private String skills;
    private String selfDescription;
    private String key;
    private String imageProfile;

    public UserModel() {

    }

    public UserModel(String username, String firstName, String lastName, String phone,
                     String email, String seniorHighSchool, String seniorHighSchoolPeriod, String university,
                     String universityPeriod, String skills, String selfDescription, String imageProfile) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.seniorHighSchool = seniorHighSchool;
        this.seniorHighSchoolPeriod = seniorHighSchoolPeriod;
        this.university = university;
        this.universityPeriod = universityPeriod;
        this.skills = skills;
        this.selfDescription = selfDescription;
        this.imageProfile = imageProfile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSeniorHighSchool() {
        return seniorHighSchool;
    }

    public void setSeniorHighSchool(String seniorHighSchool) {
        this.seniorHighSchool = seniorHighSchool;
    }

    public String getSeniorHighSchoolPeriod() {
        return seniorHighSchoolPeriod;
    }

    public void setSeniorHighSchoolPeriod(String seniorHighSchoolPeriod) {
        this.seniorHighSchoolPeriod = seniorHighSchoolPeriod;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getUniversityPeriod() {
        return universityPeriod;
    }

    public void setUniversityPeriod(String universityPeriod) {
        this.universityPeriod = universityPeriod;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getSelfDescription() {
        return selfDescription;
    }

    public void setSelfDescription(String selfDescription) {
        this.selfDescription = selfDescription;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
}
