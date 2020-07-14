package com.project.quizmoney.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.quizmoney.MainActivity;
import com.project.quizmoney.R;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * These are the country code and the phone number view of the activity from where
     * the phone number and the country code is acquired
     */
    private EditText mCountryCode;
    private EditText mPhoneNumber;

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
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

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

    private PhoneAuthProvider.ForceResendingToken resendingToken;

    private String totalPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance(); //Instancing the firebase authentication object
        mCurrentUser = mAuth.getCurrentUser();      //Getting the current user object from the firebase

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

        totalPhoneNumber = "+" + country_code + phone_number;

        Log.d(TAG, "onClick: PhoneNumber " + totalPhoneNumber );

        if (country_code.isEmpty() || phone_number.isEmpty()) {
            //If the country code or the phone number is empty the an error message is shown
            errorText.setText(R.string.error_display);
            errorText.setVisibility(View.VISIBLE);
        } else {
            //If the user types correct phone number then the progressBar is set to visible and
            //the register button is set to disable
            loginProcess.setVisibility(View.VISIBLE);
            registerBtn.setEnabled(false);

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
        // Check if user is signed in (non-null).
        if(mCurrentUser !=null){
            Log.d(TAG, "onStart: " + mCurrentUser.getPhoneNumber());
            Intent homeIntent = new Intent(this,MainActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);
            this.finish();

        }else Log.d(TAG, "onStart: " + "No User Signed In");
    }


    private void setCallbacks(){
        // This will set the callBack when the code is sent or verification is complete or the verification is failed
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                resendingToken = forceResendingToken;
                Log.d(TAG, "onCodeSent: " + " Code is Sent");
                Intent otpIntent = new Intent(RegisterActivity.this,OtpActivity.class);
                otpIntent.putExtra("verificationID",s);
                otpIntent.putExtra("phoneNumber",totalPhoneNumber);
                startActivity(otpIntent);
            }
        };
    }
}
