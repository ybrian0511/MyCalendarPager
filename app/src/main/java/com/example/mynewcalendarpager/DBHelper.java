package com.example.mynewcalendarpager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteDBTest";

    public DBHelper(@Nullable Context context) {
        super(context, ScheduleContract.DB_NAME, null, ScheduleContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(ScheduleContract.Schedules.CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(ScheduleContract.Schedules.DELETE_TABLE);
        onCreate(db);
    }
    // 삽입
    public void insertEventBySQL(String title, String date, String start, String end, String place, String memo){
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) " +
                            "VALUES (NULL,'%s','%s','%s','%s','%s','%s')",
                    ScheduleContract.Schedules.TABLE_NAME,
                    ScheduleContract.Schedules._ID,
                    ScheduleContract.Schedules.KEY_TITLE,
                    ScheduleContract.Schedules.KEY_DATE,
                    ScheduleContract.Schedules.KEY_START,
                    ScheduleContract.Schedules.KEY_END,
                    ScheduleContract.Schedules.KEY_PLACE,
                    ScheduleContract.Schedules.KEY_MEMO,
                    title,date,start,end,place,memo);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in inserting recodes");
        }
    }
    // 삭제
    public void deleteEventBySQL(int title) {
        try {
            String sql = String.format (
                    "DELETE FROM %s WHERE %s = %s",
                    ScheduleContract.Schedules.TABLE_NAME,
                    ScheduleContract.Schedules.KEY_TITLE,
                    title);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in deleting recodes");
        }
    }
    // 수정
    public void updateEventBySQL(String _id, String title, String date, String start, String end, String place, String memo){
        try {
            String sql = String.format (
                    "UPDATE %s SET %s = '%s',%s = '%s',%s = '%s',%s = '%s',%s = '%s',%s = '%s' WHERE %s = %s",
                    ScheduleContract.Schedules.TABLE_NAME,
                    ScheduleContract.Schedules.KEY_TITLE,title,
                    ScheduleContract.Schedules.KEY_DATE,date,
                    ScheduleContract.Schedules.KEY_START,start,
                    ScheduleContract.Schedules.KEY_END,end,
                    ScheduleContract.Schedules.KEY_PLACE,place,
                    ScheduleContract.Schedules.KEY_MEMO,memo,
                    ScheduleContract.Schedules._ID,_id);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in updating recodes");
        }
    }

    public Cursor getAllEventBySQL() {
        String sql = "Select * FROM " + ScheduleContract.Schedules.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
    }
    public Cursor getAllSchedule(String date){
        String sql = "Select * FROM " + ScheduleContract.Schedules.TABLE_NAME + " WHERE Date ='" + date +"'";
        return getReadableDatabase().rawQuery(sql,null);
    }
    public Cursor searchSchedule(String date){
        String sql = "Select * FROM " + ScheduleContract.Schedules.TABLE_NAME + " WHERE Date LIKE '" + date+"%'";
        return getReadableDatabase().rawQuery(sql,null);
    }
}

