package com.example.udhay.reminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static CustomAdapter customAdapter;
    public Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customAdapter = new CustomAdapter(this, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position, View view) {
                cursor.moveToPosition(position);
                long id = cursor.getLong(cursor.getColumnIndex(ReminderContract.ReminderTable._ID));
                String message = cursor.getString(cursor.getColumnIndex(ReminderContract.ReminderTable.COLUMN_MESSAGE));
                int importance = cursor.getInt(cursor.getColumnIndex(ReminderContract.ReminderTable.COLUMN_IMPORTANCE));

                Intent intent = new Intent(MainActivity.this , AddReminder.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ReminderContract.ReminderTable.COLUMN_IMPORTANCE , importance);
                bundle.putCharSequence(ReminderContract.ReminderTable.COLUMN_MESSAGE , message);
                bundle.putLong("id" , id);
                intent.putExtras(bundle);
                startActivity(intent)   ;
            }
        });


        refreshCursor();
        prepareRecyclerView();
        prepareFab();

        NotificationManagerCompat.from(this).notify(24 , prepareNotification());

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCursor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_acitivity_menu , menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_sort_icon:
                showDialogBox();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showDialogBox(){
        new CustomDialog().show(getSupportFragmentManager() , "dialog");
    }

    private void prepareRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.reminder_recyclerView);
        recyclerView.setAdapter(customAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                int position = viewHolder.getAdapterPosition();
                cursor.moveToPosition(position);

                long id = cursor.getLong(cursor.getColumnIndex(ReminderContract.ReminderTable._ID)) ;

              new ReminderOpenHelper(MainActivity.this).getWritableDatabase().delete(ReminderContract.ReminderTable.TABLE_NAME ,
                        ReminderContract.ReminderTable._ID +" = ? " , new String[]{Long.toString(id)} );
              customAdapter.refreshCursor();
              customAdapter.notifyDataSetChanged();
              refreshCursor();
              }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void prepareFab(){
        FloatingActionButton actionButton = findViewById(R.id.floating_action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , AddReminder.class);
                startActivity(intent);
            }
        });
    }


    private void refreshCursor(){
        customAdapter.refreshCursor();
        cursor = customAdapter.getCursor();
    }

    private Notification prepareNotification(){

        Intent intent = new Intent(this , MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0 , intent , 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this , getResources().getString(R.string.Notification_channel_id));
        cursor.moveToFirst();
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Reminder Pending")
                .setContentText(cursor.getString(cursor.getColumnIndex(ReminderContract.ReminderTable.COLUMN_MESSAGE)))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent);

       return builder.build();


    }
}
