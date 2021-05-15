package com.hackslash.haaziri.models;

public class Guest {
    public String name;
    public String email;
    public String phone;
    public String uid;

    Guest(){
        name="";
        email="";
        phone="";
    }
    Guest(String name,String email,String phone){
        this.name=name;
        this.email=email;
        this.phone=phone;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }
    public String getEmail(){
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() {
        return uid;
    }

}
