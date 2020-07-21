package com.project.quizmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.quizmoney.model.UserDetails;

public class OurHomePage extends AppCompatActivity {
    private Button startgame;







        class backgroundThread extends Thread{

            private String TAG = "Our HomePage";
            private FirebaseAuth firebaseAuth;
            private FirebaseFirestore db = FirebaseFirestore.getInstance();
            private UserDetails userDetails = new UserDetails();
            private String documentID;

            public backgroundThread(){

                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                documentID = getIntent().getStringExtra("documentID");
            }


            @Override
            public void run() {

                Log.d(TAG, "doInBackground: homePageBackgroundDB " + Thread.currentThread() + " " + Thread.activeCount());

                DocumentReference documentReference = db.collection("Users").document(documentID);

                documentReference
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        Log.d(TAG, "onComplete: Douments" + document.getData());
                                    }else{
                                        Log.d(TAG, "onComplete: Document doesn't exists");
                                    }
                                }else{
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });

            }
        }

    }
}