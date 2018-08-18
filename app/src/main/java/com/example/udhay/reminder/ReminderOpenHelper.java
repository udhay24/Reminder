package com.example.udhay.reminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ReminderOpenHelper extends SQLiteOpenHelper {

    public ReminderOpenHelper(Context context){
        super(context , ReminderContract.DATABASE_NAME , null ,ReminderContract.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ReminderContract.ReminderTable.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
