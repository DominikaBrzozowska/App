package com.example.one.tas;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String address;
    private String city;
    private String postal_code;
    private String avatar;

    public User(){
        this.email="guest";
    }
    public User(String email,String password){
        this.email=email;
        this.password=password;
    }
    public User( String name, String surname, String email, String password, String address, String city, String postal_code) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.city = city;
        this.postal_code = postal_code;
        this.avatar="default.png";
    }
    public User( String id, String name, String surname, String email, String password, String address, String city, String postal_code) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.city = city;
        this.postal_code = postal_code;
        this.avatar="default.png";
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {return password;}

    public String getName() { return name; }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getAvatar() {
        return avatar;
    }
}
