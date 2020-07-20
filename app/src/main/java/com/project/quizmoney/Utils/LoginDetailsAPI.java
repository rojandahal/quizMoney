package com.project.quizmoney.Utils;

import android.app.Application;
import android.widget.EditText;

/**
 * This class is used to store the data of the user which is frequently used like userID
 * phone number and name.
 * This class is static and it extends through the application
 * This static instance is declared so the value in this class doesn't change through out the application
 */
public class LoginDetailsAPI extends Application {

    /**
     * Information data of the user
     */
    private String userID;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;

    /**
     * Static instance of the class
     */
    private  static LoginDetailsAPI instance;

    /**
     * This method created a new instance of the class if no any instance is found
     * and if an instance is found then it will return an instance which is used to retrieve or sava
     * data to the strings in this class
     */
    public static LoginDetailsAPI getInstance(){

        if(instance==null)
            instance = new LoginDetailsAPI();
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public LoginDetailsAPI() {

    }
}
