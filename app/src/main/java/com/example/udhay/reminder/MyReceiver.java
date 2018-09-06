package com.example.udhay.reminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        if(intent.getAction() == MainActivity.ACTION_NOTIFY){

            new AsyncTask<Void , Void , Notification>(){
                @Override
                protected Notification doInBackground(Void... voids) {

                    SQLiteDatabase database = new ReminderOpenHelper(context).getReadableDatabase();

                    Cursor cursor = database.query(ReminderContract.ReminderTable.TABLE_NAME , null , null , null
                     ,null , null , ReminderContract.ReminderTable.COLUMN_IMPORTANCE + " DESC ");

                    cursor.moveToFirst();
                    Intent intent = new Intent(context , MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context , 0 , intent , 0);


                    Notification.Builder builder;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        builder = new Notification.Builder(context, "DEFAULT_CHANNEL");
                    }else {
                        builder = new Notification.Builder(context);
                    }
                    builder.setContentTitle("Reminder")
                            .setContentText(cursor.getString(cursor.getColumnIndex(ReminderContract.ReminderTable.COLUMN_MESSAGE)))
                            .setContentIntent(pendingIntent)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_launcher_foreground);



                    return builder.build();
                }

                @Override
                protected void onPostExecute(Notification notification) {
                    super.onPostExecute(notification);
                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                    managerCompat.notify(24 , notification);
                }
            }.execute();

        }
    }
}
