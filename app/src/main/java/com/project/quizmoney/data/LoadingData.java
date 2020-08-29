package com.project.quizmoney.data;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.project.quizmoney.Utils.LoginDetailsAPI;
import com.project.quizmoney.model.UserDetails;

import java.util.Objects;

/**
 * This class is used to load data from the database to the LoginDetailsAPI
 * i.e userID , name, email and phoneNumber
 * If the user is not created then this class will create the user and put it in firebase database and also put its details
 * in LoginDetailsApi
 *
 * If the User already exists then the details of the user is got.
 * The document is firebase is looped to get the data and store here
 *
 */
public class LoadingData extends AppCompatActivity{

    /**
     * This is a TAG string used to debug
     */
    private String TAG = "Loading Activity";

    /**
     * This is the signout button
     * This button is  invisible. This button was created to check the validity of the class whether
     * it worked or not and what happend after the signout
     */
    private Button signOut;

    /**
     * This is the circular progress bar which works until the result from the database is not got
     * if the database has data then the progressbar works until the data is retrieved and
     * if the database doesn't have data then the progressbar works until the data is got
     */
    private ProgressBar progressBar;

    /**
     * This loading text is shown until the progress bar continues to work
     * and the successFulText is not used currently which was created to debug the successful login of the
     * firebase user and completion of authentication
     */
    private TextView loadingText, successfulText;

    /**
     * This is a global instance of the LoginDetailsAPI class
     * This is used to store the data of the class until the app is opened.
     * This instance decreases the work to access database everytime the app is running
     * to display the user details or for any other work
     */
    private LoginDetailsAPI loginDetailsAPI = LoginDetailsAPI.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        signOut = findViewById(R.id.signout);
        progressBar = findViewById(R.id.loadingFirebase);
        loadingText = findViewById(R.id.loadingText);
        successfulText = findViewById(R.id.successText);

        //Creating an instance of the background Thread class and starting the thread
        BackgroundDatabaseAccess dbAccess = new BackgroundDatabaseAccess();
        dbAccess.start();

        //This method is not used currently. It is used to get logout from the firebase authentication
//        signout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                sendUserToMain();
//            }
//        });
    }

    /**
     * This method is used to send user to the Main activity if the user pressed the signout button and
     * after the user is signout
     */
    private void sendUserToMain() {

        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    /**
     * This is a class made to work on worker thread or background thread
     * The work inside this class is executed in a background ( according to me)
     * I don't know much about background thread execution only some theory stuff so I searched and cameup with
     * this idea to execute task in a background thread but the thread executes and if we log thread count then the thread reaches from 12 to 20
     * i.e the thread doesn't stop (maybe) in future if someone sees this comment then help to execute this and
     * work in other classes too in background thread
     */
    class BackgroundDatabaseAccess extends Thread {

        private FirebaseUser firebaseUser;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        private UserDetails userDetails = new UserDetails();

        private String fstName;
        private String lstName;
        private String email;
        private String phoneNumber;
        private String documentId;

        private int coin;
        private int level;
        private int score;
        private int xp ;
        private int totalQuestionAttempt;
        private int totalQuestionsSolved;
        private int totalSetsSolved;

        private Boolean idFound = false;

        /**
         * This constructor is used to set the details of the user for further use when adding the user authentication
         * details in the firebase database
         * and also instantiating the firebaseAuth and firebaseUser
         */
        public BackgroundDatabaseAccess() {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();

            fstName = loginDetailsAPI.getFirstName();
            lstName = loginDetailsAPI.getLastName();
            email = loginDetailsAPI.getEmail();
            phoneNumber = loginDetailsAPI.getPhoneNumber();
            score = loginDetailsAPI.getScore();
            xp = loginDetailsAPI.getXp();
            totalQuestionAttempt = loginDetailsAPI.getTotalQuestionAttempt();
            totalQuestionsSolved = loginDetailsAPI.getTotalQuestionsSolved();
            totalSetsSolved = loginDetailsAPI.getTotalSetsSolved();
            coin = loginDetailsAPI.getCoin();
            level= loginDetailsAPI.getLevel();

            loginDetailsAPI.setUserID(firebaseUser.getUid());

        }

        @Override
        public void run() {

            Log.d(TAG, "doInBackground: databaseAccess " + Thread.currentThread() + " " + Thread.activeCount());
            Log.d(TAG, "run: " + firebaseUser.getUid());

            /*This selects the collection in Firebase database and loops through the entire database in search of the
             userID to match it with the userID from the FirebaseCurrentUser i.e the user loggedIn in the phone and
             if the user exists then it is added to LoginDetailsAPI if not the it is created in the database
            */
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
                                        idFound = true; //if the userID from currentUser Matches with the database then it is set to true

                                        loginDetailsAPI.setDocumentID(document.getId());
                                        loginDetailsAPI.setUserID(document.getData().get("userID").toString());
                                        loginDetailsAPI.setFirstName(document.getData().get("firstName").toString());
                                        loginDetailsAPI.setLastName(document.getData().get("lastName").toString());
                                        loginDetailsAPI.setEmail(document.getData().get("email").toString());
                                        loginDetailsAPI.setPhoneNumber(document.getString("phoneNumber"));

                                        loginDetailsAPI.setInitialData(
                                                Integer.parseInt(document.getString("coin")),
                                                Integer.parseInt(document.getString("level")),
                                                Integer.parseInt(document.getString("score")),
                                                Integer.parseInt(document.getString("totalQuestionAttempt")),
                                                Integer.parseInt(document.getString("totalQuestionSolved")),
                                                Integer.parseInt(document.getString("totalSetsSolved")),
                                                Integer.parseInt(document.getString("xp"))
                                        );

                                        //Log.d(TAG, "onComplete: Phone: " + document.getData().get("firstName") + " Document ID: " + documentId);
//                                        Log.d(TAG, "onComplete: Account Firestore exists " + document.getData().get("phoneNumber"));

                                    }else {
                                        Log.d(TAG, "onInComplete: " + " userID not Found");
                                    }
                                }

                                //If the id matches then it is set true and this only sends the user to another activity
                                if(idFound){
                                    disableButtons();
                                    sendUserToMainMenu();

                                    Log.d(TAG, "run: " + "user Already Exists");

                                }else if (!idFound){

                                    //If user is NOT found then the userDetails is added to the firebase database
                                    userDetails.setUserID(firebaseUser.getUid());
                                    userDetails.setValues(fstName,lstName,email,phoneNumber,score,xp,totalQuestionAttempt,
                                            totalQuestionsSolved,totalSetsSolved,coin,level);
                                    userDetails.runFireStore();
                                    disableButtons();
                                    sendUserToMainMenu();

                                }

                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });

          }

        /**
         * This method sends the user to Main Menu Activity where user can start game , view profile and the
         * real game starts
         */
        private void sendUserToMainMenu(){

            Intent homeIntent = new Intent(LoadingData.this, OurHomePage.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);
            finish();

        }

        /**
         * This method disables the progressbar and the loading text when the database is accessed
         */
        private void disableButtons(){

            loadingText.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

//          successfulText.setVisibility(View.VISIBLE);
//          signout.setVisibility(View.VISIBLE);

            Log.d(TAG, "disableButtons: " + loginDetailsAPI.getUserID() + " " +
                    loginDetailsAPI.getFirstName() + " " +
                    loginDetailsAPI.getLastName() + " " +
                    loginDetailsAPI.getEmail() + " " +
                    loginDetailsAPI.getPhoneNumber());

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

