package com.project.quizmoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.quizmoney.Utils.LoginDetailsAPI;
import com.project.quizmoney.model.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class InGame extends AppCompatActivity {

    private String TAG = "In Game Activity";
    private TextView question,noIndicator;
    private LinearLayout options;
    private Button nextBtn;
    private int count = 0;
    private List<QuestionModel> list = new ArrayList<>();
    private int position=0;
    private int correctScore = 0;
    private ProgressBar progressBar;
    private CountDownTimer mCountDownTimer;
    private int countTextNumber=20;
    private TextView countDownText;

    private QuestionModel questionModel = new QuestionModel();
    private LoginDetailsAPI loginDetailsAPI = LoginDetailsAPI.getInstance();
    private List<QuestionModel> questionModelsList = loginDetailsAPI.get_qBankList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingame);

        question = findViewById(R.id.question);
        noIndicator = findViewById(R.id.no_indicator);
        options = findViewById(R.id.options);
        nextBtn = findViewById(R.id.nxtbtn);
        progressBar = findViewById(R.id.countDownProgress);
        countDownText = findViewById(R.id.countDownText);

        progressBar.setProgress(100);
        /*
        This for loop will loop through the questionModelList which is the list of the
        QuestionModel object i.e questions of a whole set not the list of FUCKING QUESTIONS on the question model.
        This fucking code took me too much to be irritated.
        Take a QuestionModel object and then add it to the list (which is the list of a single question)
         */
        for(int i=0; i<questionModelsList.size(); i++){
            questionModel = questionModelsList.get(i);
            list.add(questionModel);
        }

        //For testing
//        list.add(new QuestionModel("Question 1", "a", "b", "c", "d","b" ));
//        list.add(new QuestionModel("Question 2", "a", "b", "c", "d","a" ));
//        list.add(new QuestionModel("Question 3", "a", "b", "c", "d","d" ));
//        list.add(new QuestionModel("Question 4", "a", "b", "c", "d","c" ));

        for (int i =0; i<4; i++){
            options.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    checkAnswer((Button) view);

                }
            });
        }

        playAnim(question,0, list.get(position).getQuestion());
        countDownMethod();
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                nextBtn.setEnabled(false);
                nextBtn.setAlpha(0.4f);
                enableOption(true);
                position++;
                countTextNumber=20;
                countDownMethod();
                if(correctScore == list.size()){
                    loginDetailsAPI.setTotalSetsSolved(1);
                    loginDetailsAPI.setCoin(1);
                }
                if (position == list.size()){
                    ///scoreActivity

                    return;
                }
                count = 0;

                playAnim(question, 0,list.get(position).getQuestion());
            }
        });
    }

    /**
     * This method is used to do the countdown for the timer during which the question needs to be answered
     */
    private void countDownMethod() {

        progressBar.setEnabled(true);

        mCountDownTimer = new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: " + millisUntilFinished);

                countDownText.setText(String.valueOf(countTextNumber-1));
                countTextNumber--;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFinish() {
                disableAllEnableNext();
                progressBar.setEnabled(false);
                countTextNumber = 20;
            }
        };
        mCountDownTimer.start();
    }

    private void playAnim(final View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                if (value == 0 && count < 4){
                    String option = "";

                    if (count==0){
                        option = list.get(position).getOptionA();

                    }else if (count==1){
                        option = list.get(position).getOptionB();

                    }else if (count==2){
                        option = list.get(position).getOptionC();

                    }else if (count==3){
                        option = list.get(position).getOptionD();

                    }
                    playAnim(options.getChildAt(count), 0,option);
                    count++;
                }

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                try {
                    ((TextView)view).setText(data);
                    noIndicator.setText(position+1+"/"+list.size());

                }catch (ClassCastException ex) {
                    ((Button) view).setText(data);
                }
                    view.setTag(data);
                    playAnim(view,1,data);
                }



            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(Button selectedOption){
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if (selectedOption.getText().toString().equals(list.get(position).getCorrectAns())){
            //correct
            loginDetailsAPI.setTotalQuestionsSolved(1);
            loginDetailsAPI.setXp(1);
            loginDetailsAPI.setTotalQuestionAttempt(1);
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));

        }
        else {
            //incorrect
            loginDetailsAPI.setTotalQuestionAttempt(1);
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctoption = (Button) options.findViewWithTag(list.get(position).getCorrectAns());
            correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableOption(boolean enable){
        for (int i =0; i<4; i++){
            options.getChildAt(i).setEnabled(enable);
            if (enable){
                options.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0B0101")));

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void disableAllEnableNext(){
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
    }

    @Override
    protected void onStop() {
        super.onStop();

        finish();
    }
}