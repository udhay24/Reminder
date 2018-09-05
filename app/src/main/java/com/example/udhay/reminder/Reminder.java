package com.example.udhay.reminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Reminder {

    public static final int IMPORTANCE_LOW = 0;
    public static final int IMPORTANCE_INTERMEDIATE = 1;
    public static final int IMPORTANCE_HIGH = 2;

    public static final String SORT_BY_TIME = "time";
    public static final String SORT_BY_IMPORTANCE = "importance";

    private int importanceLevel;
    private String message;


    public Reminder(int importance , String message){
        importanceLevel = importance;
        this.message = message;
    }

    public int getImportanceLevel() {
        return importanceLevel;
    }


    public String getMessage() {
        return message;
    }


    public static Notification prepareNotification(Context context){

        Log.v("notification" , "inside method");
        Intent intent = new Intent(context , MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context , 0 , intent , 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context , context.getResources().getString(R.string.Notification_channel_id));
        MainActivity.customAdapter.getCursor().moveToFirst();
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Reminder Pending")
                .setContentText(MainActivity.customAdapter.getCursor().getString(MainActivity.customAdapter.getCursor().getColumnIndex(ReminderContract.ReminderTable.COLUMN_MESSAGE)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return builder.build();


    }

}
