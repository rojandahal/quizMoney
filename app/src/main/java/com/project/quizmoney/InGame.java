package com.project.quizmoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class InGame extends AppCompatActivity {
    private TextView question,noIndicator;
    private LinearLayout options;
    private Button nextBtn;
    private int count = 0;
    private List<Questionmodel> list;
    private int position=0;
    private int score = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingame);

        question = findViewById(R.id.question);
        noIndicator = findViewById(R.id.no_indicator);
        options = findViewById(R.id.options);
        nextBtn = findViewById(R.id.nxtbtn);
        list = new ArrayList<>();
        list.add(new Questionmodel("Question 1", "a", "b", "c", "d","b" ));
        list.add(new Questionmodel("Question 2", "a", "b", "c", "d","a" ));
        list.add(new Questionmodel("Question 3", "a", "b", "c", "d","d" ));
        list.add(new Questionmodel("Question 4", "a", "b", "c", "d","c" ));
        for (int i =0; i<4; i++){
            options.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    checkanswer((Button) view);

                }
            });
        }


        playAnim(question,0, list.get(position).getQuestion());
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtn.setEnabled(true);
                nextBtn.setAlpha(1);
                position++;
                if (position == list.size()){
                    ///scoreactivity

                    return;
                }
                count = 0;

                playAnim(question, 0,list.get(position).getQuestion());
            }
        });
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

                }catch (ClassCastException ex){
                    ((Button)view).setText(data);
                    view.setTag(data);
                }
                if (value == 0){
                    playAnim(view,1,data);
                }

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
    private void checkanswer(Button selectedoption){
        enableoption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if (selectedoption.getText().toString().equals(list.get(position).getCorrectAns())){
            //coorect
            selectedoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            score++;

        }
        else {
            //incorrect
            selectedoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("ff0000")));
            Button correctopt =(Button) options.findViewWithTag(list.get(position).getCorrectAns())
            correctopt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }

    }
    private void enableoption(boolean enable){
        for (int i =0; i<4; i++){
            options.getChildAt(i).setEnabled(enable);
        }

    }

}