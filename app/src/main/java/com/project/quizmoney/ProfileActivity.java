package com.project.quizmoney;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.quizmoney.Utils.LoginDetailsAPI;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 1;
    private ImageView profilePic;
    private ImageView insertImage;


    private TextView coinValue;

    private TextView name;
    private TextView phoneNumber;

    private TextView shareButton;
    private TextView username;

    private Button paymentButton;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private StorageReference mStorageRef;

    private Uri imageUri;

    LoginDetailsAPI loginDetailsAPI = LoginDetailsAPI.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = findViewById(R.id.profilePic);
        insertImage = findViewById(R.id.insertImage);
        coinValue = findViewById(R.id.coinValue);
        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);
        username = findViewById(R.id.username);

        shareButton = findViewById(R.id.shareButton);
        paymentButton = findViewById(R.id.paymentButton);

        name.setText(loginDetailsAPI.getFirstName() + loginDetailsAPI.getLastName());
        phoneNumber.setText(loginDetailsAPI.getPhoneNumber());
        username.setText(loginDetailsAPI.getFirstName());

        insertImage.setOnClickListener(this);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && requestCode == RESULT_OK) {

            if(data!=null){
                imageUri = data.getData();
                profilePic.setImageURI(imageUri);

            }

        }
    }

    private void saveProfilePic(){

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE);

    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.insertImage:
                //inserting profile pic here
                saveProfilePic();
                break;
        }
    }
}