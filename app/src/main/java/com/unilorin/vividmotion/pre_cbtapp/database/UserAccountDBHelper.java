package com.unilorin.vividmotion.pre_cbtapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.models.StudentProfile;
import com.unilorin.vividmotion.pre_cbtapp.models.User;

/**
 * Created by Tofunmi on 24/12/2016.
 */

public class UserAccountDBHelper extends SQLiteOpenHelper {
    private String TAG = "UserAccountDBHelper";

    private static String dbName = "user.db";
    private static int dbVersion = 1;

    public UserAccountDBHelper(Context context){
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + DBContract.UserContract.tableName + " ( " +
                DBContract.UserContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DBContract.UserContract.userObjectColumn +
                "  TEXT NOT NULL, UNIQUE ( " + DBContract.UserContract._ID + " ) ON CONFLICT REPLACE );";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.UserContract.tableName);
        onCreate(db);
    }

    public void insertUser(User user){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.UserContract.userObjectColumn, new Gson().toJson(user));

        database.insert(DBContract.UserContract.tableName, null, contentValues);
    }

    public void updateUser(User user){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.UserContract.userObjectColumn, new Gson().toJson(user));

        String currentUserInJson = new Gson().toJson(getUser());

        database.update(DBContract.UserContract.tableName, contentValues, DBContract.UserContract.userObjectColumn+"=?",
                new String[]{currentUserInJson});
    }

    public User getUser(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(DBContract.UserContract.tableName, new String[]{ DBContract.UserContract.userObjectColumn},
                null, null, null, null, null);

        if (cursor.getCount() == 0){
            return null;
        }

        cursor.moveToFirst();
        String userObjectInJson = cursor.getString(cursor.getColumnIndex(DBContract.UserContract.userObjectColumn));
        User theUser = new Gson().fromJson(userObjectInJson, User.class);

        cursor.close();
        database.close();

        return theUser;
    }

    public void emptyDatabase(){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.delete(DBContract.UserContract.tableName, null, null);
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage() + "aborting...");
        }
        finally{
            database.close();
        }
    }
}
