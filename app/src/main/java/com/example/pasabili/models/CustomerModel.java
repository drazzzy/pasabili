package com.example.pasabili.models;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

public class CustomerModel {

    private String userId;

    private String email;

    private String password;

    private String firstname;

    private String lastname;

    private String address;

    private Boolean verified;

    public CustomerModel(
            String email,
            String password,
            String firstname,
            String lastname,
            String address){
        setEmail(email);
        setPassword(password);
        setFirstname(firstname);
        setLastname(lastname);
        setAddress(address);
    }

    public CustomerModel(DocumentSnapshot customer){
        setEmail(customer.getString("email"));
        setPassword(customer.getString("password"));
        setFirstname(customer.getString("firstname"));
        setLastname(customer.getString("lastname"));
        setAddress(customer.getString("address"));
        setVerified(customer.getBoolean("verified"));
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
