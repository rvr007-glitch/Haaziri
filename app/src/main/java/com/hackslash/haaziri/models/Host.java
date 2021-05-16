package com.hackslash.haaziri.models;

public class Host {
    public String name;
    public String email;
    public String phone;
    public String uld;

    public Host() {
    }

    public Host(String a,String b,String c){
        a = "";
        b = "";
        c = "";
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPhone(){
        return phone;
    }

    public void setName(String newName){ name = newName; }

    public void setEmail(String newEmail){
        email = newEmail;
    }

    public void setPhone(String newPhone){
        phone = newPhone;
    }
}
