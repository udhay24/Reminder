package com.example.udhay.reminder;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddReminder extends AppCompatActivity {

    EditText editText;
    Spinner spinner;
    int importance;
    String message;
    long id;

    boolean newEntryFlag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        editText = findViewById(R.id.messageEditText);
        spinner = findViewById(R.id.spinner);
        FloatingActionButton actionButton = findViewById(R.id.floating_action_button);
        setUpSpinner();

        Bundle bundle = getIntent().getExtras();
        if(bundle !=null) {
            newEntryFlag = false;
            message = (String) bundle.getCharSequence(ReminderContract.ReminderTable.COLUMN_MESSAGE);
            importance = bundle.getInt(ReminderContract.ReminderTable.COLUMN_IMPORTANCE);
            id = bundle.getLong("id");
            updateDisplay();
        }

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newEntryFlag){
                saveEntry();}
                else{
                    updateEntry(id , importance , message);
                }
            }
        });



    }

    private void saveEntry(){

        String message = editText.getText().toString();
        if(message.matches("")){
            Toast.makeText(this , "Enter The Text" , Toast.LENGTH_SHORT).show();
            return;
        }
        Reminder reminder = new Reminder(importance , message);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ReminderContract.ReminderTable.COLUMN_IMPORTANCE , reminder.getImportanceLevel());
        contentValues.put(ReminderContract.ReminderTable.COLUMN_MESSAGE , reminder.getMessage());

        new ReminderOpenHelper(this).getWritableDatabase().insert(ReminderContract.ReminderTable.TABLE_NAME , null ,contentValues);
        MainActivity.customAdapter.refreshCursor();
        MainActivity.customAdapter.notifyDataSetChanged();
        startActivity(new Intent(this , MainActivity.class));
    }

    private void setUpSpinner(){

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this , R.array.reminder , android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        importance = Reminder.IMPORTANCE_INTERMEDIATE;
                        break;
                    case 1:
                        importance = Reminder.IMPORTANCE_LOW;
                        break;
                    case 2:
                        importance = Reminder.IMPORTANCE_HIGH;

                        break;
                    default:
                        importance = Reminder.IMPORTANCE_INTERMEDIATE;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void updateEntry(long id , int importance , String message){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReminderContract.ReminderTable.COLUMN_IMPORTANCE , importance);
        contentValues.put(ReminderContract.ReminderTable.COLUMN_MESSAGE , message);

        int i = new ReminderOpenHelper(this).getWritableDatabase().update(ReminderContract.ReminderTable.TABLE_NAME  , contentValues ,
                ReminderContract.ReminderTable._ID+ " = ? " , new String[]{Long.toString(id)});
        Log.v("rows affected" , ""+i);
        MainActivity.customAdapter.refreshCursor();
        MainActivity.customAdapter.notifyDataSetChanged();
        startActivity(new Intent(this , MainActivity.class));
    }

    private void updateDisplay(){
        editText.setText(message);
        int i = 0;
        switch(importance){
            case Reminder.IMPORTANCE_LOW:
                i = 1;
                break;
            case Reminder.IMPORTANCE_HIGH:
                i = 2;
                break;
            case Reminder.IMPORTANCE_INTERMEDIATE:
                i=0;
                break;
        }
        spinner.setSelection(i);

    }
}
