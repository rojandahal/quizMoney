package com.project.quizmoney.Utils;

import android.app.Application;

import com.project.quizmoney.QuestionModel;

import java.util.List;

public class QuestionListAPI  {

    private List<QuestionModel> _qBankList;

    public static QuestionListAPI questionListAPI;

    public static QuestionListAPI getInstance(){

        if(questionListAPI == null)
            questionListAPI = new QuestionListAPI();
        return questionListAPI;
    }

    public List<QuestionModel> get_qBankList() {
        return _qBankList;
    }

    public void set_qBankList(List<QuestionModel> _qBankList) {
        this._qBankList = _qBankList;
    }

    public void addToQuestionList (QuestionModel questionModel){

        _qBankList.add(questionModel);
    }
}
