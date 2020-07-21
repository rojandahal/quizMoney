package com.project.quizmoney.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to save the userDetails temporarily when saving the data to firebase as this class
 * executes the firebase database to store the data
 * and this method can also be used to update the data in the database if needed when
 *  changing the points or any other details
 */
public class UserDetails  {

    /**
     * This is the string tag used to debug the program
     */
    private String TAG = "User Details";

    /**
     * User details like:
     * First Name
     * Last Name
     * Email ID
     * Phone Number
     * User ID ( given by firebase authentication
     */
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String userID;

    /**
     * Firebase auth and firebase user used to access the current user and current authentication
     */
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Collection reference of the firebase database of the collection path "Users"
     * This is the instance of the Users collection made in the firebase which includes different types of data to store
     */
    private CollectionReference collectionReference = db.collection("Users");

    /**
     * Empty constructor
     */
    public UserDetails() {
    }

    /**
     * Constructor to set the values to the users if we want to set values directly without
     * making the object and invoking method
     */
    public UserDetails(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * This method is used to save data to the variables in this class and
     * the data saved or the variable value saved in this class is valid only in the class
     * from which it is call and if that activity closes then these values will also reset
     */
    public void setValues(String firstName, String lastName, String email, String phoneNumber) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;

    }

    /**
     * This method is used to set the userID as while registering the userID is not available
     * and after registration while adding the data to database this can be used to add the user id after the authentication is
     * complete
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * This method is used to add the data to the firebase database
     * Firebase database can be added using a hashMap object so here HashMap object is created and the data is
     * put into the hashMap and the HashMap object is added to the firebase database
     */
    public void runFireStore() {

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();

            Map<String,String > userObj = new HashMap<>();

            userObj.put("userID",userID);
            userObj.put("firstName",firstName);
            userObj.put("lastName",lastName);
            userObj.put("email",email);
            userObj.put("phoneNumber",phoneNumber);

            collectionReference.add(userObj)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            documentReference.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Log.d(TAG, "onComplete: " + firebaseUser.getUid());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + "Authentication Failed");
                        }
                    });
        }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
