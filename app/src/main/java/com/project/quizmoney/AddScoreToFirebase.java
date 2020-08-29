package com.project.quizmoney;

import android.os.AsyncTask;
import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.quizmoney.Utils.LoginDetailsAPI;

import static com.android.volley.VolleyLog.TAG;

/**
 * This class will add score to the Firebase firestore
 */
public class AddScoreToFirebase {

    public void addPointsToFirebase(){
        new AddPointsToFirebaseAsync().execute();
    }

    /**
     * This is a static async class which will execute in background and it will access the firebase
     * to set the score and update the score data to the firebase server.
     * This will update the data in the document of the userID
     */
    private static class AddPointsToFirebaseAsync extends AsyncTask<Void, Void, Void> {

        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        private CollectionReference collectionReference = db.collection("Users");

        private LoginDetailsAPI loginDetailsAPI = LoginDetailsAPI.getInstance();
        private int coin = loginDetailsAPI.getCoin();
        private int level = loginDetailsAPI.getLevel();
        private int score = loginDetailsAPI.getScore();
        private int xp = loginDetailsAPI.getXp();
        private int totalQuestionAttempt = loginDetailsAPI.getTotalQuestionAttempt();
        private int totalQuestionsSolved = loginDetailsAPI.getTotalQuestionsSolved();
        private int totalSetsSolved = loginDetailsAPI.getTotalSetsSolved();

        @Override
        protected Void doInBackground(Void... voids) {

            collectionReference.document(loginDetailsAPI.getDocumentID())
                    .update(
                            "score",String.valueOf(score),
                            "xp",String.valueOf(xp),
                            "totalQuestionAttempt",String.valueOf(totalQuestionAttempt),
                            "totalQuestionSolved",String.valueOf(totalQuestionsSolved),
                            "totalSetsSolved",String.valueOf(totalSetsSolved),
                            "coin",String.valueOf(coin),
                            "level",String.valueOf(level)
                    );
            Log.d(TAG, "onComplete: Question Attempt: " + totalQuestionAttempt);
            return null;
        }
    }
}
