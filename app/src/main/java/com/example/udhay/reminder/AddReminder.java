package com.example.udhay.reminder;

import android.content.ContentValues;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        EditText editText = findViewById(R.id.messageEditText);
        String message = editText.getText().toString();
        final Reminder reminder = new Reminder(0 , message);

        FloatingActionButton actionButton = findViewById(R.id.floating_action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEntry(reminder);
            }
        });
    }

    private void saveEntry(Reminder reminder){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReminderContract.ReminderTable.COLUMN_IMPORTANCE , reminder.getImportanceLevel());
        contentValues.put(ReminderContract.ReminderTable.COLUMN_MESSAGE , reminder.getMessage());

        new ReminderOpenHelper(this).getWritableDatabase().insert(ReminderContract.ReminderTable.TABLE_NAME , null ,contentValues);
    }
}
