package com.project.quizmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.quizmoney.ui.login.RegisterActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }
}
