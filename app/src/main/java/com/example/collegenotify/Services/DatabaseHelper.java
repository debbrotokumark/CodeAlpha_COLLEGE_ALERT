package com.example.collegenotify.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.collegenotify.Model.NotificationModel;
import com.example.collegenotify.Model.EventModel;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "collegenotify.db";
    private static final int DATABASE_VERSION = 2; // Incremented version due to schema change

    // Notifications Table
    private static final String TABLE_NOTIFICATIONS = "notifications";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_LINK = "link";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    // Event Management Table
    private static final String TABLE_EVENTS = "event_management";
    private static final String COLUMN_EVENT_ID = "id";
    private static final String COLUMN_EVENT_NAME = "name";
    private static final String COLUMN_EVENT_DESCRIPTION = "description";
    private static final String COLUMN_EVENT_TIME = "event_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Notifications Table
        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_BODY + " TEXT, " +
                COLUMN_LINK + " TEXT, " +
                COLUMN_TIMESTAMP + " TEXT)";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);

        // Create Event Management Table
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EVENT_NAME + " TEXT, " +
                COLUMN_EVENT_DESCRIPTION + " TEXT, " +
                COLUMN_EVENT_TIME + " TEXT)";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    // ✅ Insert Notification
    public boolean insertNotification(String title, String body, String link) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_BODY, body);
        values.put(COLUMN_LINK, link);
        String formattedTimestamp = new SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.getDefault()).format(new Date());
        values.put(COLUMN_TIMESTAMP, formattedTimestamp);

        long result = db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
        return result != -1;
    }

    // ✅ Get All Notifications
    public List<NotificationModel> getAllNotifications() {
        List<NotificationModel> notificationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTIFICATIONS + " ORDER BY " + COLUMN_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                NotificationModel notification = new NotificationModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                notificationList.add(notification);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notificationList;
    }

    // ✅ Clear All Notifications
    public void clearAllNotifications() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTIFICATIONS);
        db.close();
    }

    // ================= Event Management Functions ====================

    // ✅ Insert Event
    public boolean insertEvent(String name, String description, String eventTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, name);
        values.put(COLUMN_EVENT_DESCRIPTION, description);
        values.put(COLUMN_EVENT_TIME, eventTime);

        long result = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return result != -1;
    }

    // ✅ Get All Events
    public List<EventModel> getAllEvents() {
        List<EventModel> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTS + " ORDER BY " + COLUMN_EVENT_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                EventModel event = new EventModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventList;
    }

    // ✅ Delete a Specific Event
    public boolean deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_EVENTS, COLUMN_EVENT_ID + "=?", new String[]{String.valueOf(eventId)});
        db.close();
        return result > 0;
    }

    // ✅ Update an Event
    public boolean updateEvent(int eventId, String name, String description, String eventTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, name);
        values.put(COLUMN_EVENT_DESCRIPTION, description);
        values.put(COLUMN_EVENT_TIME, eventTime);

        int result = db.update(TABLE_EVENTS, values, COLUMN_EVENT_ID + "=?", new String[]{String.valueOf(eventId)});
        db.close();
        return result > 0;
    }
}
