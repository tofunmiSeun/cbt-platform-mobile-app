package com.unilorin.vividmotion.pre_cbtapp.managers.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.unilorin.vividmotion.pre_cbtapp.models.Course;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tofunmi on 26/12/2016.
 */

public class CourseDBHelper extends SQLiteOpenHelper {
    private static String TAG = "CourseDBHelper";
    private static String dbName = "courses.db";
    private static int dbVersion = 1;

    public CourseDBHelper(Context c){
        super(c, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    private void createTable(SQLiteDatabase db){
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + DBContract.CoursesEntry.tableName +
                " ( " + DBContract.CoursesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.CoursesEntry.idColumn + " BIG INTEGER NOT NULL, " +
                DBContract.CoursesEntry.courseTitleColumn + " TEXT NOT NULL, " +
                DBContract.CoursesEntry.courseCodeColumn + " TEXT NOT NULL, " +
                DBContract.CoursesEntry.departmentIdColumn + " BIG INTEGER NOT NULL, " +
                DBContract.CoursesEntry.levelValueColumn + " INTEGER NOT NULL, UNIQUE " +
                " ( " + DBContract.CoursesEntry._ID + " ) ON CONFLICT REPLACE );";

        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.CoursesEntry.tableName);
        onCreate(db);
    }

    public void registerNewCourse(Course newCourse){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.CoursesEntry.idColumn, newCourse.getId());
        contentValues.put(DBContract.CoursesEntry.courseTitleColumn, newCourse.getCourseTitle());
        contentValues.put(DBContract.CoursesEntry.courseCodeColumn, newCourse.getCourseCode());
        contentValues.put(DBContract.CoursesEntry.departmentIdColumn, newCourse.getDepartmentId());
        contentValues.put(DBContract.CoursesEntry.levelValueColumn, newCourse.getLevelValue());

        database.insert(DBContract.CoursesEntry.tableName, null, contentValues);
        database.close();
    }

    public void registerNewCourses(List<Course> courses){
        for (Course c : courses){
            registerNewCourse(c);
        }
    }

    public List<Course> getAssignedCourses(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DBContract.CoursesEntry.tableName,
                new String[]{
                        DBContract.CoursesEntry.idColumn,
                        DBContract.CoursesEntry.courseTitleColumn,
                        DBContract.CoursesEntry.courseCodeColumn,
                        DBContract.CoursesEntry.departmentIdColumn,
                        DBContract.CoursesEntry.levelValueColumn
                }, null, null, null, null, null);

        cursor.moveToFirst();
        List<Course> assignedCourses = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++){
            Course c = new Course();
            c.setId(cursor.getLong(cursor.getColumnIndex(DBContract.CoursesEntry.idColumn)));
            c.setCourseTitle(cursor.getString(cursor.getColumnIndex(DBContract.CoursesEntry.courseTitleColumn)));
            c.setCourseCode(cursor.getString(cursor.getColumnIndex(DBContract.CoursesEntry.courseCodeColumn)));
            c.setDepartmentId(cursor.getLong(cursor.getColumnIndex(DBContract.CoursesEntry.departmentIdColumn)));
            c.setLevelValue(cursor.getInt(cursor.getColumnIndex(DBContract.CoursesEntry.levelValueColumn)));

            cursor.moveToNext();
            assignedCourses.add(c);
        }
        cursor.close();
        database.close();

        return assignedCourses;
    }

    public void updateCourse(Course course){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.CoursesEntry.idColumn, course.getId());
        contentValues.put(DBContract.CoursesEntry.courseTitleColumn, course.getCourseTitle());
        contentValues.put(DBContract.CoursesEntry.courseCodeColumn, course.getCourseCode());
        contentValues.put(DBContract.CoursesEntry.departmentIdColumn, course.getDepartmentId());
        contentValues.put(DBContract.CoursesEntry.levelValueColumn, course.getLevelValue());

        database.update(DBContract.CoursesEntry.tableName, contentValues, DBContract.CoursesEntry.courseCodeColumn+"=?",
                new String[]{course.getCourseCode()});

        database.close();
    }

    public List<Long> getIdsForAssignedCourses(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DBContract.CoursesEntry.tableName, new String[]{
                DBContract.CoursesEntry.idColumn
        }, null, null, null, null, null);

        cursor.moveToFirst();
        List<Long> courseIds = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++){
            courseIds.add(cursor.getLong(cursor.getColumnIndex(DBContract.CoursesEntry.idColumn)));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();

        return courseIds;
    }

    public List<String> getCodesForAssignedCourses(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DBContract.CoursesEntry.tableName, new String[]{
                DBContract.CoursesEntry.courseCodeColumn
        }, null, null, null, null, null);

        cursor.moveToFirst();
        List<String> courseCodes = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++){
            courseCodes.add(cursor.getString(cursor.getColumnIndex(DBContract.CoursesEntry.courseCodeColumn)));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();

        return courseCodes;
    }

    public void emptyDatabase(){
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            database.delete(DBContract.CoursesEntry.tableName, null, null);
            database.close();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }
}
