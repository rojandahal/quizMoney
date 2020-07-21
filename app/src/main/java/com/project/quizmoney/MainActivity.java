package com.project.quizmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.quizmoney.data.LodingData;
import com.project.quizmoney.ui.login.RegisterActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "Main Activity";
    private Button registerButton;

    /**
     * These are Firebase authentication object and the currentUser object
     * mCurrentUser can be used to get the userId: PhoneNumber of the current logged in user in the app
     */
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        Log.d(TAG, "doInBackground: Inside onStart " + Thread.currentThread() + " " + Thread.activeCount());
        // Check if user is signed in (non-null).
        if(mCurrentUser !=null){
            sendUserToHome();

        }else Log.d(TAG, "onStart: " + "No User Signed In");
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }

    /**
     * This activity is used to send user to home if the user is already signed in
     */
    private void sendUserToHome() {

        Intent homeIntent = new Intent(this, LodingData.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
}
