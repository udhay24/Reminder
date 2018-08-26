package com.example.udhay.reminder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
//                Cursor cursor = new ReminderOpenHelper(MainActivity.this).getReadableDatabase().query(ReminderContract.ReminderTable.TABLE_NAME ,
//                        null , ReminderContract.ReminderTable._ID+" =? " ,
//                        new String[]{Long.toString(id)} , null , null , null);
//                cursor.moveToFirst();
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCursor();
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
        actionButton.setRippleColor(getResources().getColor(R.color.colorPrimary));
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , AddReminder.class);
                startActivity(intent);
            }
        });
    }


    private void refreshCursor(){
        cursor = customAdapter.getCursor();
    }
}
