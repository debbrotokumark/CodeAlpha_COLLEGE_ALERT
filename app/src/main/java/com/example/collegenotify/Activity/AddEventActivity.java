package com.example.collegenotify.Activity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegenotify.R;
import com.example.collegenotify.Services.DatabaseHelper;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextTime;
    private DatabaseHelper databaseHelper;
    private String eventTitle, eventDescription, eventTime;
    boolean isUpdate = false;
    int eventId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);
        ImageView imgBack = findViewById(R.id.imgBack);
        ImageView imgDelete = findViewById(R.id.imgDelete);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextTime = findViewById(R.id.editTextTime);
        TextView txtTitle = findViewById(R.id.txtTitle);


        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra("isupdate", false);
        eventId = intent.getIntExtra("eventId", -1); // Default value is -1 if not found
        String eventName = intent.getStringExtra("eventName");
        String eventDescription = intent.getStringExtra("eventDescription");
        String eventTime = intent.getStringExtra("eventTime");
        if (isUpdate) {
            imgDelete.setVisibility(View.VISIBLE);
            editTextTitle.setText(eventName);
            editTextDescription.setText(eventDescription);
            editTextTime.setText(eventTime);
            txtTitle.setText("Update Event");
        }


        imgBack.setOnClickListener(v ->
        {
            if (!isUpdate) {
                saveEvent();
            } else {
                Update();
            }
            finish();
        });
        imgDelete.setOnClickListener(v -> {
            if (isUpdate) {
                databaseHelper.deleteEvent(eventId);
                Toast.makeText(this, "Delete event successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        // Time Picker Dialog
        editTextTime.setOnClickListener(v -> showTimePickerDialog());
    }

    // Show Time Picker
    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (TimePicker view, int hourOfDay, int minute1) -> {
                    @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", hourOfDay, minute1);
                    editTextTime.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    // Handle Back Press and Save Event
    @Override
    public void onBackPressed() {
        if (!isUpdate) {
            saveEvent();
        } else {
            Update();
        }
        super.onBackPressed();
    }

    // Save Event to Database
    private void saveEvent() {
        eventTitle = editTextTitle.getText().toString().trim();
        eventDescription = editTextDescription.getText().toString().trim();
        eventTime = editTextTime.getText().toString().trim();

        if (!eventTitle.isEmpty() && !eventTime.isEmpty()) {
            boolean isInserted = databaseHelper.insertEvent(eventTitle, eventDescription, eventTime);
            if (isInserted) {
                Toast.makeText(this, "Event saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save event", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Update() {
        eventTitle = editTextTitle.getText().toString().trim();
        eventDescription = editTextDescription.getText().toString().trim();
        eventTime = editTextTime.getText().toString().trim();
        if (eventId == -1) {
            Toast.makeText(this, "Failed to Update event", Toast.LENGTH_SHORT).show();
            return;

        }
        if (!eventTitle.isEmpty() && !eventTime.isEmpty()) {
            databaseHelper.updateEvent(eventId, eventTitle, eventDescription, eventTime);
        } else {
            Toast.makeText(this, "Failed to Update event", Toast.LENGTH_SHORT).show();

        }
    }

    // Handle Toolbar Back Button

}
