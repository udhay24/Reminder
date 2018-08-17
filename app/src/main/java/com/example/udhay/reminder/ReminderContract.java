package com.example.udhay.reminder;

import android.provider.BaseColumns;

public  class ReminderContract {

    public static final String DATABASE_NAME = "reminder.db";
    public static final int VERSION = 1;

    private ReminderContract(){}

    public static final class ReminderTable implements BaseColumns{

        public static final String TABLE_NAME = "reminder";

        public static final String COLUMN_IMPORTANCE = "importance";
        public static final String COLUMN_MESSAGE = "message";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + _ID + " NUMBER PRIMARY KEY AUTOINCREMENT " +
                COLUMN_IMPORTANCE + " TEXT " + COLUMN_MESSAGE + "TEXT " + " ) ";

    }
}
