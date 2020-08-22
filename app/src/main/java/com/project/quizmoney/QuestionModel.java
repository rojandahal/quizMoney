package com.project.quizmoney;

import android.app.Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionModel {

    private String question, optionA, optionB, optionC, optionD, correctAns;
    private List<String> options = new ArrayList<>();
    private int number;

    public QuestionModel(String question, String optionA, String optionB, String optionC, String optionD, String correctAns) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAns = correctAns;

    }

    public QuestionModel() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }

    public int getNumber() {
        return number;
    }

    public void setQuestionData(String que, String correctAnswer, String optA, String optB, String optC, int num){

        number = num;
        question = que;
        correctAns = correctAnswer;
        options.add(optA);
        options.add(optB);
        options.add(optC);
        options.add(correctAns);
    }

    public void shuffleQuestion(){

        Collections.shuffle(options);
        optionA = options.get(0);
        optionB = options.get(1);
        optionC =options.get(2);
        optionD = options.get(3);
    }
}
