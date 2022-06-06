package com.example.mynewcalendarpager;

import android.provider.BaseColumns;

public final class ScheduleContract {
    public static final String DB_NAME="schedule.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private ScheduleContract() {}
    public static class Schedules implements BaseColumns {
        public static final String TABLE_NAME="Schedule";
        public static final String KEY_TITLE="Title";
        public static final String KEY_DATE="Date";
        public static final String KEY_START ="Start";
        public static final String KEY_END ="End";
        public static final String KEY_PLACE="Place";
        public static final String KEY_MEMO="Memo";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_TITLE + TEXT_TYPE + COMMA_SEP +
                KEY_DATE + TEXT_TYPE + COMMA_SEP +
                KEY_START + TEXT_TYPE + COMMA_SEP +
                KEY_END + TEXT_TYPE + COMMA_SEP +
                KEY_PLACE + TEXT_TYPE + COMMA_SEP +
                KEY_MEMO + TEXT_TYPE +  " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
