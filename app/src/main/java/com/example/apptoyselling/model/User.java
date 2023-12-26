package com.example.apptoyselling.model;

public class   User {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String passWord;

    public User(int id, String name, String phone, String email, String passWord) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.passWord = passWord;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
