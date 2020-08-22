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

import com.project.quizmoney.Utils.LoginDetailsAPI;

import java.util.ArrayList;
import java.util.List;

public class InGame extends AppCompatActivity {
    private TextView question,noIndicator;
    private LinearLayout options;
    private Button nextBtn;
    private int count = 0;
    private List<QuestionModel> list = new ArrayList<>();
    private int position=0;
    private int score = 0;

    private QuestionModel questionModel = new QuestionModel();
    private LoginDetailsAPI questionListAPI = LoginDetailsAPI.getInstance();
    private List<QuestionModel> questionModelsList = questionListAPI.get_qBankList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingame);

        question = findViewById(R.id.question);
        noIndicator = findViewById(R.id.no_indicator);
        options = findViewById(R.id.options);
        nextBtn = findViewById(R.id.nxtbtn);

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
//        list.add(new QuestionModel("Question 1", "a", "b", "c", "d","b" ));
//        list.add(new QuestionModel("Question 2", "a", "b", "c", "d","a" ));
//        list.add(new QuestionModel("Question 3", "a", "b", "c", "d","d" ));
//        list.add(new QuestionModel("Question 4", "a", "b", "c", "d","c" ));
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
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                nextBtn.setEnabled(false);
                nextBtn.setAlpha(0.4f);
                enableoption(true);
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
    private void checkanswer(Button selectedoption){
        enableoption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if (selectedoption.getText().toString().equals(list.get(position).getCorrectAns())){
            //correct
            selectedoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));


        }
        else {
            //incorrect
            selectedoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctoption = (Button) options.findViewWithTag(list.get(position).getCorrectAns());
            correctoption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableoption(boolean enable){
        for (int i =0; i<4; i++){
            options.getChildAt(i).setEnabled(enable);
            if (enable){
                options.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0B0101")));

            }
        }

    }

}