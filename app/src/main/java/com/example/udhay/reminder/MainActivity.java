package com.example.udhay.reminder;

import android.content.ContentValues;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareRecyclerView();
        prepareFab();



    }

    private void prepareRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.reminder_recyclerView);
        recyclerView.setAdapter(customAdapter = new CustomAdapter(this));
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
                new ReminderOpenHelper(MainActivity.this).getWritableDatabase().delete(ReminderContract.ReminderTable.TABLE_NAME ,
                        ReminderContract.ReminderTable._ID + " =? " , new String[]{Long.toString(i)});
                customAdapter.notifyDataSetChanged();
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




    private void insertDummyData(){
        SQLiteDatabase database = new ReminderOpenHelper(this).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReminderContract.ReminderTable.COLUMN_IMPORTANCE , 1250);
        contentValues.put(ReminderContract.ReminderTable.COLUMN_MESSAGE , "HELLO");
        long id = database.insert(ReminderContract.ReminderTable.TABLE_NAME , null , contentValues);
        Log.v("Custom ID" , Long.toString(id));
        database.close();
    }
}
