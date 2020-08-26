package com.project.quizmoney.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.quizmoney.Utils.LoginDetailsAPI;
import com.project.quizmoney.data.LoadingData;
import com.project.quizmoney.R;


public class OtpActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "OtpActivity";
    /**
     * This is the editText object for otp number which is used to extract the otp from the edit text and verify
     */
    private EditText mOtpNumber;

    /**
     * This button is used to verify the otp when the button is clicked then the onclickListener will verify after click
     */
    private Button verifyOtp;

    /**
     * This is errorMessage text which is displayed if the otp is wrong or verification is failed
     */
    private TextView errorMessage;

    /**
     * This is verificationID which is passed when the code is sent and this is used to verify the credentials
     */
    private String verificationId;

    /**
     * ProgressBar shows the progress when the button is clicked
     */
    private ProgressBar otpProgress;

    /**
     * These are the country code and the phone number view of the activity from where
     * the phone number and the country code is acquired
     */
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    /**
     * This method is executed when the activity is created
     */

    private TextView countdown;
    private int counter=59;
    private TextView resendCode;

    private String fstName;
    private String lstName;
    private String email;
    private String phoneNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();  //This will get the object of the currentUser

        mOtpNumber = findViewById(R.id.otpNumber);
        verifyOtp = findViewById(R.id.verifyOTP);
        errorMessage = findViewById(R.id.error_text_display_otp);
        otpProgress = findViewById(R.id.otpProgress);
        countdown = findViewById(R.id.countdown);
        resendCode = findViewById(R.id.resendCode);

       setCountDownTimer();

        //Extracting verification id from the extra put in RegisterActivity when calling this activity
       verificationId = getIntent().getStringExtra("verificationID");
//        fstName = getIntent().getStringExtra("fname");
//        lstName = getIntent().getStringExtra("lname");
//        email = getIntent().getStringExtra("email");
//        phoneNumber = getIntent().getStringExtra("phoneNumber");

        verifyOtp.setOnClickListener(this);
    }

    /**
     * This method is called when the verify otp button is pressed
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.verifyOTP:
               setVerifyOtp();
                break;
            case R.id.resendCode:
                setResendCode();
                break;
        }
    }

    /**
     * This method is used to signIn with the credentials or phone number into the app after verifying the otp
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signIn", "signInWithCredential:success");
                            sendUserToHome();
                            OtpActivity.this.finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        errorMessage.setText(R.string.error_verifying_otp);
                                        verifyOtp.setEnabled(true);
                                        errorMessage.setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                        }
                    }
                });
    }


    /**
     * This method sends the user to the MainActivity Page when the verification is successful
     */
    private void sendUserToHome(){
        Intent homeIntent = new Intent(this, LoadingData.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        homeIntent.putExtra("phoneNumber",phoneNumber);
//        homeIntent.putExtra("fname",fstName);
//        homeIntent.putExtra("lname",lstName);
//        homeIntent.putExtra("email",email);
        startActivity(homeIntent);

    }

    /**
     * When resend button is clicked this method is invoked which will send the activity to the register activity
     * and the user needs to enter all the values once again to again register and be in the otp activity
     */
    public void setResendCode(){

        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(registerIntent);
    }

    /**
     * This method is used to set the otp to the verificationID and signIn using the credentials set using the verification
     * i.e signInWithAuthCredentials checks the otp with the otp send by the firebase and verifies the authentication
     */
    private void setVerifyOtp(){

        final String otp = mOtpNumber.getText().toString();

        if (otp.isEmpty()) {
            //Error Message is shown when the otp is empty
            errorMessage.setText(R.string.error_display);
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            //When the otp is filled then it will check with the credentials or Otp passed as sms
            otpProgress.setVisibility(View.VISIBLE);
            verifyOtp.setEnabled(false);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }
            }).start();
        }

    }

    /**
     * This method is used to set the timer to 60 second to click the resend button if the otp is not received
     * in 60 seconds then the resend button can be clicked to send the otp again
     */
    public void setCountDownTimer(){
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText(String.valueOf(counter));
                counter--;
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFinish() {
                //When the 60 seconds is finished then the resendCode color is changed and it can be clicked

                Log.d(TAG, "onFinish: " + counter + " finished");
                resendCode.setEnabled(true);
                resendCode.setTextColor(R.color.theme_red);
                resendCode.setOnClickListener(OtpActivity.this);
            }
        }.start();

    }

    /**
     * On activity start if the user is signed in i.e current user is not null
     * then this activity is finished as this happens when the user is authenticated directly from the register activity
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "doInBackground: on Start " + Thread.currentThread());
        // Check if user is signed in (non-null).
        if(mCurrentUser !=null){
           this.finish();
        }else Log.d(TAG, "onStart: " + "No User Signed In");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop: " + LoginDetailsAPI.getInstance().getUserID() + LoginDetailsAPI.getInstance().getFirstName());
        finish();
    }
}
