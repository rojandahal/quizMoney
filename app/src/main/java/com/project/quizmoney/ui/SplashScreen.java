package com.project.quizmoney.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.quizmoney.MainActivity;
import com.project.quizmoney.ui.login.RegisterActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen.this.startActivity(new Intent(this, MainActivity.class));
        finish();

    }
}
