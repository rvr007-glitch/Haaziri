package com.hackslash.haaziri.models;

public class UserProfile {
    public String Name;
    public String email;
    public String mobile;
    public String userProfileImageUrl;

  public  UserProfile(){

    }

   public  UserProfile(String Name,String email,String mobile,String userProfileImageUrl)
     {
        this.Name=Name;
        this.email=email;
        this.mobile=mobile;
        this.userProfileImageUrl=userProfileImageUrl;
     }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }
}
