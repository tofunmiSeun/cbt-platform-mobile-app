package com.unilorin.vividmotion.pre_cbtapp.managers.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.models.Question;
import com.unilorin.vividmotion.pre_cbtapp.utils.AttemptedQuestionsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tofunmi on 06/01/2017.
 */

public class QuestionDBHelper extends SQLiteOpenHelper {
    private static String TAG = "QuestionDBHelper";
    private static String dbName = "questions.db";
    private static int dbVersion = 1;

    private Context appContext;

    public QuestionDBHelper(Context c) {
        super(c, dbName, null, dbVersion);
        appContext = c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.QuestionEntry.tableName);
        onCreate(db);
    }

    public void saveQuestion(Question question) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.QuestionEntry.idColumn, question.getId());
        contentValues.put(DBContract.QuestionEntry.questionColumn, question.getQuestion());
        contentValues.put(DBContract.QuestionEntry.optionsColumn, new Gson().toJson(question.getOptions()));
        contentValues.put(DBContract.QuestionEntry.correctAnswerIndexColumn, question.getCorrectAnswerIndex());
        contentValues.put(DBContract.QuestionEntry.courseIdColumn, question.getCourseId());

        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(DBContract.QuestionEntry.tableName, null, contentValues);

        database.close();
    }

    public void saveQuestions(List<Question> questions) {
        for (Question q : questions) {
            saveQuestion(q);
        }
    }

    public List<Question> getFreshQuestionsForCourse(Long courseId, int count) {
        // here find a way to get a list of question ids that have been answered
        SQLiteDatabase database = this.getReadableDatabase();

        AttemptedQuestionsUtils attemptedQuestionsUtils = new AttemptedQuestionsUtils(appContext);
        List<Long> idsOfAttemptedQuestions = attemptedQuestionsUtils.getIdsOfAttemptedQuestions();
        Cursor cursor = database.query(DBContract.QuestionEntry.tableName, new String[]{
                        DBContract.QuestionEntry.idColumn,
                        DBContract.QuestionEntry.questionColumn,
                        DBContract.QuestionEntry.optionsColumn,
                        DBContract.QuestionEntry.correctAnswerIndexColumn
                }, DBContract.QuestionEntry.courseIdColumn + " = " +courseId + " AND " + DBContract.QuestionEntry.idColumn +
                " NOT IN " + AttemptedQuestionsUtils.getInClauseStringFormat(idsOfAttemptedQuestions)
                , null, null, null, "RANDOM()", String.valueOf(count));

        List<Question> freshQuestions = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {

            Long id = cursor.getLong(cursor.getColumnIndex(DBContract.QuestionEntry.idColumn));
            String question = cursor.getString(cursor.getColumnIndex(DBContract.QuestionEntry.questionColumn));
            String[] options = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DBContract.QuestionEntry.optionsColumn))
                    , String[].class);
            int correctAnswerIndex = cursor.getInt(cursor.getColumnIndex(DBContract.QuestionEntry.correctAnswerIndexColumn));

            Question q = new Question();
            q.setId(id);
            q.setQuestion(question);
            q.setOptions(options);
            q.setCorrectAnswerIndex(correctAnswerIndex);
            q.setCourseId(courseId);

            freshQuestions.add(q);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return freshQuestions;
    }

    public List<Question> getQuestionsForCourse(Long courseId, int count, List<Long> idListOfQuestionsToExempt) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(DBContract.QuestionEntry.tableName, new String[]{
                        DBContract.QuestionEntry.idColumn,
                        DBContract.QuestionEntry.questionColumn,
                        DBContract.QuestionEntry.optionsColumn,
                        DBContract.QuestionEntry.correctAnswerIndexColumn
                }, DBContract.QuestionEntry.courseIdColumn + " = " +courseId + " AND " + DBContract.QuestionEntry.idColumn +
                        " NOT IN " + AttemptedQuestionsUtils.getInClauseStringFormat(idListOfQuestionsToExempt)
                , null, null, null, "RANDOM()", String.valueOf(count));

        List<Question> freshQuestions = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {

            Long id = cursor.getLong(cursor.getColumnIndex(DBContract.QuestionEntry.idColumn));
            String question = cursor.getString(cursor.getColumnIndex(DBContract.QuestionEntry.questionColumn));
            String[] options = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DBContract.QuestionEntry.optionsColumn))
                    , String[].class);
            int correctAnswerIndex = cursor.getInt(cursor.getColumnIndex(DBContract.QuestionEntry.correctAnswerIndexColumn));

            Question q = new Question();
            q.setId(id);
            q.setQuestion(question);
            q.setOptions(options);
            q.setCorrectAnswerIndex(correctAnswerIndex);
            q.setCourseId(courseId);

            freshQuestions.add(q);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return freshQuestions;
    }

    public List<Long> getIdsForQuestionsWithCourseId(Long courseId) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DBContract.CoursesEntry.tableName, new String[]{
                DBContract.QuestionEntry.idColumn
        }, DBContract.QuestionEntry.courseIdColumn + "=?", new String[]{String.valueOf(courseId)}, null, null, null);

        cursor.moveToFirst();
        List<Long> courseIds = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            courseIds.add(cursor.getLong(cursor.getColumnIndex(DBContract.CoursesEntry.idColumn)));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();

        return courseIds;
    }

    public List<Question> getQuestionsForCourse(Long courseId, int count) {
        SQLiteDatabase database = this.getReadableDatabase();

        String rawQuery = "SELECT ( " + DBContract.QuestionEntry.idColumn + ", " + DBContract.QuestionEntry.questionColumn +
                ", " + DBContract.QuestionEntry.optionsColumn + ", " + DBContract.QuestionEntry.correctAnswerIndexColumn + " ) FROM " +
                DBContract.QuestionEntry.tableName + " WHERE " + DBContract.QuestionEntry.courseIdColumn + " = " + courseId +
                " ORDER BY RANDOM() LIMIT " + count + ";";

        Cursor cursor = database.rawQuery(rawQuery, null);

        List<Question> freshQuestions = new ArrayList<>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {

            Long id = cursor.getLong(cursor.getColumnIndex(DBContract.QuestionEntry.idColumn));
            String question = cursor.getString(cursor.getColumnIndex(DBContract.QuestionEntry.questionColumn));
            String[] options = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DBContract.QuestionEntry.optionsColumn))
                    , String[].class);
            int correctAnswerIndex = cursor.getInt(cursor.getColumnIndex(DBContract.QuestionEntry.correctAnswerIndexColumn));

            Question q = new Question();
            q.setId(id);
            q.setQuestion(question);
            q.setOptions(options);
            q.setCorrectAnswerIndex(correctAnswerIndex);
            q.setCourseId(courseId);

            freshQuestions.add(q);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return freshQuestions;
    }

    public void emptyDatabase() {
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            database.delete(DBContract.QuestionEntry.tableName, null, null);
            database.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
