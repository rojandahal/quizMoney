package com.project.quizmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.quizmoney.Utils.LoginDetailsAPI;

public class Statistics extends AppCompatActivity {

    private TextView level;
    private TextView xp;
    private TextView totalQuestionAttempt;
    private TextView totalQuestionSolved;
    private TextView totalSetsSolved;

    private Button backButton;

    private LoginDetailsAPI loginDetailsAPI = LoginDetailsAPI.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        level = findViewById(R.id.level);
        xp = findViewById(R.id.xpText);
        totalQuestionAttempt = findViewById(R.id.questionsAttempt);
        totalQuestionSolved = findViewById(R.id.questionSolved);
        totalSetsSolved = findViewById(R.id.setsSolved);
        backButton = findViewById(R.id.backButton);

        level.setText(String.valueOf(loginDetailsAPI.getLevel()));
        xp.setText(String.valueOf(loginDetailsAPI.getXp()));
        totalQuestionAttempt.setText(String.valueOf(loginDetailsAPI.getTotalQuestionAttempt()));
        totalSetsSolved.setText(String.valueOf(loginDetailsAPI.getTotalSetsSolved()));
        totalQuestionSolved.setText(String.valueOf(loginDetailsAPI.getTotalQuestionsSolved()));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Statistics.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}