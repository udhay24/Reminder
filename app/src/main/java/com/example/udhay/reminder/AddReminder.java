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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        editText = findViewById(R.id.messageEditText);

        FloatingActionButton actionButton = findViewById(R.id.floating_action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEntry();
            }
        });

        spinner = findViewById(R.id.spinner);
        setUpSpinner();

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
                Toast.makeText(AddReminder.this , ""+importance , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
