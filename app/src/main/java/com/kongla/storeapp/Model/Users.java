package com.kongla.storeapp.Model;

public class Users
{
    private String email, name, phone, password,status;

    public Users()
    {

    }

    public Users(String email, String name, String phone,String status) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.status = status;
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

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getStatus() { return status; }

    public void setStatus(String password) { this.status = status; }
}
