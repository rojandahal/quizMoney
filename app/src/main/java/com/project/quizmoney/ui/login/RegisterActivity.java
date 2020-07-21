package com.project.quizmoney.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.quizmoney.Utils.LoginDetailsAPI;
import com.project.quizmoney.data.LodingData;
import com.project.quizmoney.R;
import com.project.quizmoney.model.UserDetails;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * These are the country code and the phone number view of the activity from where
     * the phone number and the country code is acquired
     */
    private EditText mCountryCode;
    private EditText mPhoneNumber;

    /**
     * These are Firebase authentication object and the currentUser object
     * mCurrentUser can be used to get the userId: PhoneNumber of the current logged in user in the app
     */
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    /**
     * This is the progress bar for indicating the login success or not
     */
    private ProgressBar loginProcess;

    /**
     * Register Button i.e registerBtn is used to register a new user and to send OTP
     * to the entered phone number
     */
    private Button registerBtn;

    /**
     * Error Text is shown below if user doesn't provide phone number of otp generation fails
     */
    private TextView errorText;

    /**
     * These are Firebase authentication object and the currentUser object
     * mCurrentUser can be used to get the userId: PhoneNumber of the current logged in user in the app
     */

    /**
     * This is the Authentication provider callback object
     * This object helps to get the instant when the code is sent or when the verification is complete or
     * the verification of the OTP failed
     */
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    /**
     * Tag for logging when debugging
     */
    private String TAG = "Register Activity";

    /**
     * This is the total phone number including the country code and the phone number
     * as the country code and phone number is taken in two values then they are concatenated and made into single
     * including the + sign before the country code
     */
    private String totalPhoneNumber;

    /**
     * These are the edit text vales to get the text entered into the EditText view in the
     * register activity so that they can be stored in the database
     */
    private EditText firstName;
    private EditText lastName;
    private EditText emailAddress;

    /**
     * This user details is used
     */
    private static UserDetails userDetails = new UserDetails();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        emailAddress = findViewById(R.id.emailAddress);
        mCountryCode = findViewById(R.id.editCountryCode);
        mPhoneNumber = findViewById(R.id.editPhoneNumber);
        loginProcess = findViewById(R.id.progressBar);
        registerBtn = findViewById(R.id.registerButton);
        errorText = findViewById(R.id.error_text_display);

        registerBtn.setOnClickListener(this);   //This will pass to onClick() method when the register button is clicked
        setCallbacks();

    }

    /**
     * This method is called when the register button is clicked and this method will help to extract the
     * phone number and verify the phone number according to the callback i.e code sent
     */
    @Override
    public void onClick(View v) {
        String country_code = mCountryCode.getText().toString();
        String phone_number = mPhoneNumber.getText().toString();
        String fstName = firstName.getText().toString();
        String lstName = lastName.getText().toString();
        String email = emailAddress.getText().toString();

        totalPhoneNumber = "+" + country_code + phone_number;

        Log.d(TAG, "onClick: PhoneNumber " + totalPhoneNumber );

        if (country_code.isEmpty() || phone_number.isEmpty() || fstName.isEmpty() || lstName.isEmpty() || email.isEmpty()) {
            //If the country code or the phone number is empty the an error message is shown
            errorText.setText(R.string.error_display);
            errorText.setVisibility(View.VISIBLE);
        } else {
            //If the user types correct phone number then the progressBar is set to visible and
            //the register button is set to disable
            LoginDetailsAPI loginDetailsAPI = LoginDetailsAPI.getInstance();
            loginDetailsAPI.setFirstName(fstName);
            loginDetailsAPI.setLastName(lstName);
            loginDetailsAPI.setPhoneNumber(totalPhoneNumber);
            loginDetailsAPI.setEmail(email);
            Log.d(TAG, "savingData: " + loginDetailsAPI.getUserID() + loginDetailsAPI.getFirstName());

            loginProcess = new ProgressBar(v.getContext());
            registerBtn.setEnabled(false);
            Log.d(TAG, "onClick: Sending Code");

            //This will verify the phone number and send code which can only be sent once in 60 seconds
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    totalPhoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    mCallbacks
            );
        }
    }


    /**
     * This method will invoke onStart after the onCreate and if the user has already signed in i.e not null
     * then the user is prompted directly into the MainActivity and the userId:PhoneNumber is logged
     */
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAccess fbAccess = new firebaseAccess();
        fbAccess.start();
    }


    private void setCallbacks(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                // This will set the callBack when the code is sent or verification is complete or the verification is failed
                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull final PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        errorText.setText(R.string.verification_failed);
                        errorText.setVisibility(View.VISIBLE);
                        loginProcess.setVisibility(View.INVISIBLE);
                        registerBtn.setEnabled(true);
                    }

                    @Override
                    public void onCodeSent(@NonNull final String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        loginProcess.setVisibility(View.INVISIBLE);
                                        registerBtn.setEnabled(true);

                                        Log.d(TAG, "doInBackground: Inside OnCodeSent " + Thread.currentThread() + " " + Thread.activeCount());
                                        Log.d(TAG, "onCodeSent: " + " Code is Sent");
                                        Intent otpIntent = new Intent(RegisterActivity.this,OtpActivity.class);
                                        otpIntent.putExtra("verificationID",s);
                                        startActivity(otpIntent);

                                    }
                                },10000);
                    }
                };
            }
        }).start();

    }

    /**
     *This method is used to sign in using the phone authentication.
     * If the credentials are correct and the sign is complete then this method checks it
     * if the sign in occurs or not
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        Log.d(TAG, "doInBackground: Inside signInWithPhone " + Thread.currentThread());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Username" + mCurrentUser.getUid());

                            sendUserToHome();
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                errorText.setVisibility(View.VISIBLE);
                                errorText.setText(R.string.error_verifying_otp);
                            }
                        }
                        loginProcess.setVisibility(View.INVISIBLE);
                        registerBtn.setEnabled(true);
                    }
                });
    }


//    private void savingData(){
//
//        LoginDetailsAPI loginDetailsAPI = LoginDetailsAPI.getInstance();
//
//        loginDetailsAPI.setFirstName(userDetails.getFirstName());
//        loginDetailsAPI.setLastName(userDetails.getLastName());
//        loginDetailsAPI.setPhoneNumber(userDetails.getPhoneNumber());
//        loginDetailsAPI.setEmail(userDetails.getEmail());
//        Log.d(TAG, "savingData: " + loginDetailsAPI.getUserID() + loginDetailsAPI.getFirstName());
//    }

    /**
     * This method is used to send the user to the Loading Data activity where the data is loaded or
     * added in the firebase database. This method is used after the authentication is complete and the user needs to access the
     * game and its details
     */
    private void sendUserToHome() {

        Intent homeIntent = new Intent(this, LodingData.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    /**
     * This inner class is use to execute code in the background thread. The code inside this class run()
     * method is executed in the background thread (According to me ) but I couldn't close this thread after execution
     * This thread work migh be attached to the register activity and its components.
     */
    class firebaseAccess extends Thread{

        /**
         * Getting the instance of the firebase authentication and the current user associated with the authentication
         */
        public firebaseAccess() {
            mAuth = FirebaseAuth.getInstance(); //Instancing the firebase authentication object
            mCurrentUser = mAuth.getCurrentUser();      //Getting the current user object from the firebase
        }

        @Override
        public void run() {
            Log.d(TAG, "doInBackground: on Start " + Thread.currentThread());
            // Check if user is signed in (non-null).
            if(mCurrentUser !=null){
                sendUserToHome();
            }else Log.d(TAG, "onStart: " + "No User Signed In");
        }
    }
}
