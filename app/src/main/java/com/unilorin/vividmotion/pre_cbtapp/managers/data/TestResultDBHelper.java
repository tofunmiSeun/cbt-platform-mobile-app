package com.unilorin.vividmotion.pre_cbtapp.managers.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.models.TestResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tofunmi on 08/01/2017.
 */

public class TestResultDBHelper extends SQLiteOpenHelper{
    private static String TAG = "TestResultDBHelper";
    private static String UPLOADED_TRUE_STRING = "YES";
    private static String UPLOADED_FALSE_STRING = "NO";
    private static String dbName = "test_result.db";
    private static int dbVersion = 1;

    public TestResultDBHelper(Context context){
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase){
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + DBContract.TestResultEntry.tableName + " ( "
                + DBContract.TestResultEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.TestResultEntry.objectInJsonColumn + " TEXT NOT NULL, " +
                DBContract.TestResultEntry.uploadStatusColumn + " TEXT NOT NULL, UNIQUE ( " +
                DBContract.TestResultEntry.objectInJsonColumn + " ) ON CONFLICT REPLACE );";

        sqliteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS " + DBContract.TestResultEntry.tableName);
        onCreate(database);
    }

    public void saveTestResult(TestResult testResult){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.TestResultEntry.objectInJsonColumn, new Gson().toJson(testResult));
        contentValues.put(DBContract.TestResultEntry.uploadStatusColumn, UPLOADED_FALSE_STRING);

        database.insert(DBContract.TestResultEntry.tableName, null, contentValues);

        database.close();
    }

    public List<TestResult> getAllTestResults(){
        SQLiteDatabase database = this.getReadableDatabase();
        List<TestResult> savedTestResults = new ArrayList<>();

        Cursor cursor = database.query(DBContract.TestResultEntry.tableName, new String [] { DBContract.TestResultEntry.objectInJsonColumn},
                null, null, null, null, null);

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            String testResultJson = cursor.getString(cursor.getColumnIndex(DBContract.TestResultEntry.objectInJsonColumn));

            TestResult testResult = new Gson().fromJson(testResultJson, TestResult.class);
            savedTestResults.add(testResult);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return savedTestResults;
    }

    public List<TestResult> getTestResultsPendingUpload(){
        SQLiteDatabase database = this.getReadableDatabase();
        List<TestResult> savedTestResults = new ArrayList<>();

        Cursor cursor = database.query(DBContract.TestResultEntry.tableName, new String [] { DBContract.TestResultEntry.objectInJsonColumn},
                DBContract.TestResultEntry.uploadStatusColumn + " = '"+UPLOADED_FALSE_STRING+"'", null, null, null, null, null);

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++){
            String testResultJson = cursor.getString(cursor.getColumnIndex(DBContract.TestResultEntry.objectInJsonColumn));

            TestResult testResult = new Gson().fromJson(testResultJson, TestResult.class);
            savedTestResults.add(testResult);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return savedTestResults;
    }

    public void updateTestResultStatusToUploaded(TestResult testResult){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.TestResultEntry.uploadStatusColumn, UPLOADED_TRUE_STRING);

        database.update(DBContract.TestResultEntry.tableName, contentValues, DBContract.TestResultEntry.objectInJsonColumn+"=?",
                new String [] {new Gson().toJson(testResult)} );

        database.close();
    }

    public void updateTestResultStatusToUploaded (List<TestResult> resultList){
        for (TestResult t : resultList){
            updateTestResultStatusToUploaded(t);
        }
    }

    public void emptyDatabase(){
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            database.delete(DBContract.TestResultEntry.tableName, null, null);
            database.close();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }
}
