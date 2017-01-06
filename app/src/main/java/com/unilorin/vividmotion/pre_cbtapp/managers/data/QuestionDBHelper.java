package com.unilorin.vividmotion.pre_cbtapp.managers.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;

import java.util.List;

/**
 * Created by Tofunmi on 06/01/2017.
 */

public class QuestionDBHelper extends SQLiteOpenHelper {
    private static String dbName = "questions.db";
    private static int dbVersion = 1;

    public QuestionDBHelper (Context c){
        super(c, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String execSQL = "CREATE TABLE IF NOT EXISTS " + DBContract.QuestionEntry.tableName + " ( " +
                DBContract.QuestionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.QuestionEntry.idColumn + " BIG INTEGER NOT NULL, " +
                DBContract.QuestionEntry.questionColumn + " TEXT NOT NULL, " +
                DBContract.QuestionEntry.optionsColumn + " TEXT NOT NULL, " +
                DBContract.QuestionEntry.correctAnswerIndexColumn + " INTEGER NOT NULL, " +
                DBContract.QuestionEntry.courseIdColumn + " BIF INTEGER NOT NULL, UNIQUE ( " +
                DBContract.QuestionEntry._ID + " ) ON CONFLICT REPLACE );";

        db.execSQL(execSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.QuestionEntry.tableName);
        onCreate(db);
    }

    public void saveQuestion(Question question){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.QuestionEntry.idColumn, question.getId());
        contentValues.put(DBContract.QuestionEntry.questionColumn, question.getQuestion());
        contentValues.put(DBContract.QuestionEntry.optionsColumn, new Gson().toJson(question.getOptions()));
        contentValues.put(DBContract.QuestionEntry.correctAnswerIndexColumn, question.getCorrectAnswerIndex());
        contentValues.put(DBContract.QuestionEntry.courseIdColumn, question.getCourseId());

        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(DBContract.QuestionEntry.tableName, null, contentValues);
    }

    public void saveQuestions(List<Question> questions){
        for (Question q : questions){
            saveQuestion(q);
        }
    }
}
