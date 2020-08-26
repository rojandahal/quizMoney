package com.project.quizmoney.data;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.quizmoney.QuestionModel;
import com.project.quizmoney.Utils.LoginDetailsAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class QuestionBank extends Thread {

    @Override
    public synchronized void start() {
        super.start();
    }

    final String TAG = "Question Bank";

    private QuestionModel questionModel;
    LoginDetailsAPI questionListAPI = LoginDetailsAPI.getInstance();

    private int set;
    private  int number;
    private String queName;
    private String correct;
    private String optA;
    private String optB;
    private String optC;

    // This is the url from where we get the question answer array as json array
    private String url = "https://raw.githubusercontent.com/rojandahal/questions/master/questionQuiz.json";

    public void getQuestions(){

        /** This method will get question from the JsonRequest and returns a list of questions to the caller
         * The AsyncQuestionList parameter is an interface which is used to indicate the ending of process and passing
         * the array of the list of question to the caller
         * */

        Log.d(TAG, "doInBackground: Inside Background");
        //Fetching all the data from the url json array

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                         //Log.d(TAG, "onResponse: Response" + response.toString());

                        try {

                            JSONArray questions = response.getJSONArray("results");

                            for (int i = 0 ; i<questions.length() ; i++){

                                JSONObject q = questions.getJSONObject(i);

                                set = Integer.parseInt(q.getString("set"));
                                number = Integer.parseInt(q.getString("number"));
                                queName = q.getString("question");
                                correct = q.getString("correct_answer");

//                                Log.d(TAG, "onResponse: Set: " + q.getString("set"));
//                                Log.d(TAG, "onResponse: Number: " + q.getString("number"));
//                                Log.d(TAG, "onResponse: Question: " + q.getString("question"));
//                                Log.d(TAG, "onResponse: Correct_answer: " + q.getString("correct_answer"));
//
                                JSONObject incorrect = q.getJSONObject("incorrect_answers");
                                optA = incorrect.getString("first");
                                optB = incorrect.getString("second");
                                optC = incorrect.getString("third");

//                                Log.d(TAG, "onResponse: First: " + incorrect.getString("first"));
//                                Log.d(TAG, "onResponse: Second: " + incorrect.getString("second"));
//                                Log.d(TAG, "onResponse: Third: " + incorrect.getString("third"));

                                Random random = new Random();
//                                if(random.nextInt(3) == Integer.parseInt(q.getString("set"))){
//                                 //Save the QuestionModel and the model to QuestionList (whose add function is in LoginDetailsAPI
//                                }

                                /*
                                These lines below are used to add question to the question list. Now we need to pick a
                                random number and match it wih set and also match the number of the question with the number sequence and add the data from that
                                extracted set to the question list
                                 */
                                    questionModel = new QuestionModel();
                                    questionModel.setQuestionData(queName,correct,optA,optB,optC,number);
                                    questionModel.shuffleQuestion();
                                    questionListAPI.addToQuestionList(questionModel);
                            }

                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: Catch Error");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: Response Error");
            }
        });

        LoginDetailsAPI.getInstance().addToRequestQueue(jsonObjectRequest);

    }
}
