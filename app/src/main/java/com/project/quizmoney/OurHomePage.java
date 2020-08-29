package com.project.quizmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.quizmoney.Utils.LoginDetailsAPI;
import com.project.quizmoney.data.QuestionBank;

public class OurHomePage extends AppCompatActivity implements View.OnClickListener {

    /**
     * This is a tag string which is used in debug
     */
    private String TAG = "Our HomePage";

    /**
     * This is the logout button but this might be temporary as this is just for checking that
     * our app logout and another user can login
     */
    private Button logout;

    /**
     * Firebase authentication instance is declared here. This auth will help to get the
     * current user and the current authentication in the app
     */
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * LoginDetailsAPI contains the details of the user that can be accessed from anywhere in the application
     * This instance can be used retrieve the user details
     */
    LoginDetailsAPI loginDetailsAPI = LoginDetailsAPI.getInstance();

    /**
     * This text view is used to get welcome user text in the screen
     */
    private TextView welcomeText;

    /**
     * This is the start game button
     * This button is used to start game
     */
    private Button startGame;

    /**
     * This method is used to transfer the activity to the profile activity
     */
    private Button myProfile;

    /**
     * This button is used to get to the credits and the details activity
     */
    private Button credits;

    /**
     * This button is used to exit the game
     */
    private Button exitGame;

    /**
     * This is for coin
     */
    private TextView coinView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ourhomepage);

        welcomeText = findViewById(R.id.welcome_note);
        logout = findViewById(R.id.logout);
        startGame = findViewById(R.id.startGame);
        myProfile = findViewById(R.id.myProfile);
        credits = findViewById(R.id.credits);
        exitGame = findViewById(R.id.exitGame);
        coinView = findViewById(R.id.scorePoints);

        welcomeText.setText("Welcome to game " + loginDetailsAPI.getFirstName() + " !");
        coinView.setText(String.valueOf(loginDetailsAPI.getCoin()));

        //Setting the on click listeners
        startGame.setOnClickListener(this);
        logout.setOnClickListener(this);
        myProfile.setOnClickListener(this);
        credits.setOnClickListener(this);
        exitGame.setOnClickListener(this);

        Log.d(TAG, "Inside Home Page: " + loginDetailsAPI.getUserID() + " " +
                loginDetailsAPI.getFirstName() + " " +
                loginDetailsAPI.getLastName() + " " +
                loginDetailsAPI.getEmail() + " " +
                loginDetailsAPI.getPhoneNumber());


    }

    /**
     * This method is used to send the user to the Main activity i.e after the logout button is clicked
     */
    private void sendUserToMain(){

        Intent homeIntent = new Intent(OurHomePage.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            //When start game is clicked
            case R.id.startGame:
                //Code which transfer to the start game should be included
                Intent startGameIntent = new Intent(this, InGame.class);
                startActivity(startGameIntent);
                Log.d(TAG, "onClick: Start Game");
                break;

            //When myProfile is clicked
            case R.id.myProfile:
                //Code to open profile
                startActivity(new Intent(this,ProfileActivity.class));
                Log.d(TAG, "onClick: Profile");
                break;

            //When credits is clicked
            case R.id.credits:
                //Code to open credits
                Log.d(TAG, "onClick: Credits");
                break;

            //When exit game is clicked
            case R.id.exitGame:
                //Code to exit game
                Log.d(TAG, "onClick: Exit Game");
                break;

            //When logout is clicked
            case R.id.logout:
                mAuth.signOut();
                sendUserToMain();
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onResume: Resumed");
        new QuestionBank().getQuestions();
    }

    @Override
    protected void onStop() {
        super.onStop();
        loginDetailsAPI.resetQuestionModel();
    }
}