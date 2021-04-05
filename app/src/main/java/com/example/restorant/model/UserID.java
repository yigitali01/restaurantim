package com.example.restorant.model;

public class UserID {
    private String id;
    private String name;
    private String phone;
    UserID(){

    }

    public UserID(String id,String name,String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserID(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
