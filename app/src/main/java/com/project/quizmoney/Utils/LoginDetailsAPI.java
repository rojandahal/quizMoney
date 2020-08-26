package com.project.quizmoney.Utils;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.project.quizmoney.QuestionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to store the data of the user which is frequently used like userID
 * phone number and name.
 * This class is static and it extends through the application
 * This static instance is declared so the value in this class doesn't change through out the application
 */
public class LoginDetailsAPI extends Application {

    private static final String TAG = LoginDetailsAPI.class.getSimpleName();
    /**
     * Information data of the user
     */
    private String userID;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;

    private List<QuestionModel> _qBankList = new ArrayList<>();

    /**
     * ************************Score related data of the user***************************
     */

    private int coin =0;
    private int level=1;
    private int score = 0;
    private int xp = 0;
    private int totalQuestionAttempt = 0;
    private int totalQuestionsSolved= 0;
    private int totalSetsSolved= 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score + this.score;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp + this.xp;
    }

    public int getTotalQuestionAttempt() {
        return totalQuestionAttempt;
    }

    public void setTotalQuestionAttempt(int totalQuestionAttempt) {
        this.totalQuestionAttempt = totalQuestionAttempt + this.totalQuestionAttempt;
    }

    public int getTotalQuestionsSolved() {
        return totalQuestionsSolved;
    }

    public void setTotalQuestionsSolved(int totalQuestionsSolved) {
        this.totalQuestionsSolved = totalQuestionsSolved + this.totalQuestionsSolved;
    }

    public int getTotalSetsSolved() {
        return totalSetsSolved;
    }

    public void setTotalSetsSolved(int totalSetsSolved) {
        this.totalSetsSolved = totalSetsSolved + this.totalSetsSolved;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    /** **********************************Score Related Data Enclosed*********************************************** **/

    /**
     * Static instance of the class
     */
    private  static LoginDetailsAPI instance;

    /**
     * This method created a new instance of the class if no any instance is found
     * and if an instance is found then it will return an instance which is used to retrieve or sava
     * data to the strings in this class
     */
    public static LoginDetailsAPI getInstance(){

        if(instance==null)
            instance = new LoginDetailsAPI();
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LoginDetailsAPI() {

    }

    /**
     * This method is used in question Bank to add a new questionModel object to the question model list
     */
    public void addToQuestionList (QuestionModel question){

        Log.d(TAG, "addToQuestionList: " + question.getQuestion() + "\n" +
                question.getOptionA() + "\n" + question.getNumber());
        _qBankList.add(question);
    }

    /**
     * This method will return a list of QuestionModel object
     */
    public List<QuestionModel> get_qBankList() {
        return _qBankList;
    }

    /**
     * The Code BELOW HERE IS FOR THE QUESTION BANK class to access the volley request to get
     * the questions from the question bank
      */
    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        //set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
