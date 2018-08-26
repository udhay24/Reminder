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
        customAdapter = new CustomAdapter(this);
        refreshCursor();
        prepareRecyclerView();
        prepareFab();


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCursor();
        Toast.makeText(this, "cursor count"+cursor.getCount() , Toast.LENGTH_LONG).show();

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
                Log.v("position" , position+"");
                cursor.moveToPosition(position);

                long id = cursor.getLong(cursor.getColumnIndex(ReminderContract.ReminderTable._ID)) ;
                Log.v("cursor position" , cursor.getPosition()+" cursor count" + cursor.getCount() + "id :"+id);

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




    private void insertDummyData(){
        SQLiteDatabase database = new ReminderOpenHelper(this).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReminderContract.ReminderTable.COLUMN_IMPORTANCE , 1250);
        contentValues.put(ReminderContract.ReminderTable.COLUMN_MESSAGE , "HELLO");
        long id = database.insert(ReminderContract.ReminderTable.TABLE_NAME , null , contentValues);
        Log.v("Custom ID" , Long.toString(id));
        database.close();
    }

    private void refreshCursor(){
        cursor = customAdapter.getCursor();
    }
}
