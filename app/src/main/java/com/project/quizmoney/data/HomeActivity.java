package com.project.quizmoney.data;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.quizmoney.MainActivity;
import com.project.quizmoney.OurHomePage;
import com.project.quizmoney.R;
import com.project.quizmoney.model.UserDetails;
import com.project.quizmoney.ui.login.RegisterActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private String TAG = "Home Activity";

    private Button signout;

    private ProgressBar progressBar;

    private TextView loadingText, successfulText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        signout = findViewById(R.id.signout);
        progressBar = findViewById(R.id.loadingFirebase);
        loadingText = findViewById(R.id.loadingText);
        successfulText = findViewById(R.id.successText);

        BackgroundDatabaseAccess dbAccess = new BackgroundDatabaseAccess();
        dbAccess.start();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                sendUserToMain();
            }
        });
    }

    private void sendUserToMain() {

        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    class BackgroundDatabaseAccess extends Thread {

        private String TAG = "Home Activity";
        private FirebaseAuth firebaseAuth;
        private FirebaseUser firebaseUser;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        private UserDetails userDetails = new UserDetails();

        private String fstName;
        private String lstName;
        private String email;
        private String phoneNumber;

        private String documentId;

        private Boolean idFound = false;

        public BackgroundDatabaseAccess() {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();

            fstName = getIntent().getStringExtra("fname");
            lstName = getIntent().getStringExtra("lname");
            email = getIntent().getStringExtra("email");
            phoneNumber = getIntent().getStringExtra("phoneNumber");

        }

        @Override
        public void run() {

//        // Add a new document with a generated ID
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//
            Log.d(TAG, "doInBackground: databaseAccess " + Thread.currentThread() + " " + Thread.activeCount());
            Log.d(TAG, "run: " + firebaseUser.getUid());

            db.collection("Users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                    //Log.d(TAG, "onComplete: GetUserID " + document.getData().get("userID"));

                                    if (Objects.equals(document.getData().get("userID"), firebaseUser.getUid()))
                                    {
                                        idFound = true;
                                        documentId = document.getId();
                                        Log.d(TAG, "onComplete: Phone: " + document.getData().get("firstName") + " Document ID: " + documentId);
                                        Log.d(TAG, "onComplete: Account Firestore exists " + document.getData().get("phoneNumber"));

//                                    }else if (document.getData().get("phoneNumber")!= phoneNumber){
//                                        userDetails.setUserID(firebaseUser.getUid());
//                                        userDetails.setValues(fstName,lstName,email,phoneNumber);
//                                        userDetails.runFireStore();
//                                        Log.d(TAG, "onComplete: Database Added " );

                                    }else {
                                        Log.d(TAG, "onInComplete: " + " userID not Found");
                                    }
                                }

                                if(idFound){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    loadingText.setVisibility(View.INVISIBLE);
//                                    signout.setVisibility(View.VISIBLE);
//                                    successfulText.setVisibility(View.VISIBLE);
                                    sendUserToMainMenu();
                                    Log.d(TAG, "run: " + "user Already Exists");

                                }else if (!idFound){
                                    userDetails.setUserID(firebaseUser.getUid());
                                    userDetails.setValues(fstName,lstName,email,phoneNumber);
                                    userDetails.runFireStore();
                                    loadingText.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
//                                    successfulText.setVisibility(View.VISIBLE);
//                                    signout.setVisibility(View.VISIBLE);

                                    sendUserToMainMenu();
                                }else {

                                }

                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });

        }

        private void sendUserToMainMenu(){

            Intent homeIntent = new Intent(HomeActivity.this, OurHomePage.class);
            homeIntent.putExtra("documentID",documentId);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);

        }
    }
}

