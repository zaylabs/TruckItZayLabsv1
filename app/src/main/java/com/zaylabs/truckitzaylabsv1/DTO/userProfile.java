package com.zaylabs.truckitzaylabsv1.DTO;

public class userProfile {

    private String name;
    private String email;
    private String dpURL;
    private String phone;
    private String createDate;
    private String profileUpdatedatetime;


    public userProfile(){

    }
    public userProfile(String name,String email, String dpURL, String phone, String createDate,String profileUpdatedatetime){

        this.name = name;
        this.email = email;
        this.dpURL = dpURL;
        this.phone = phone;
        this.createDate = createDate;
        this.profileUpdatedatetime = profileUpdatedatetime;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDpURL() {
        return dpURL;
    }

    public void setDpURL(String dpURL) {
        this.dpURL = dpURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getProfileUpdatedatetime() {
        return profileUpdatedatetime;
    }

    public void setProfileUpdatedatetime(String profileUpdatedatetime) {
        this.profileUpdatedatetime = profileUpdatedatetime;
    }
}