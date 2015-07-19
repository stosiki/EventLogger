package com.stosiki.tutorials.eventlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.w3c.dom.Comment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 7/18/2015.
 */
public class EventsDataSource {
    private SQLiteDatabase database;
    private EventsDbHelper dbHelper;

    private String[] allColumns = {
            EventsDbHelper.COLUMN_ID,
            EventsDbHelper.COLUMN_LINE_ID,
            EventsDbHelper.COLUMN_TIMESTAMP,
            EventsDbHelper.COLUMN_NUMVALUE,
            EventsDbHelper.COLUMN_COMMENT
    };

    private Context context;

    public EventsDataSource(Context context) {
        dbHelper = new EventsDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * TODO: add handling for numeric and string events, meanwhile only basic event is supported
     */
    public AppEvent createEvent(long lineId) {
        ContentValues values = new ContentValues();
        values.put(EventsDbHelper.COLUMN_LINE_ID, lineId);
        long eventId = database.insert(EventsDbHelper.TABLE_EVENTS, null, values);
        Cursor cursor = database.query(EventsDbHelper.TABLE_EVENTS, allColumns,
                EventsDbHelper.COLUMN_ID + " = " + eventId, null, null, null, null);
        cursor.moveToFirst();
        AppEvent event = cursorToEvent(cursor);
        cursor.close();
        return event;
    }

    public void deleteEvent(AppEvent event) {
        long id = event.getId();
        database.delete(EventsDbHelper.TABLE_EVENTS, EventsDbHelper.COLUMN_ID + " = " + id, null);
    }

    public List<AppEvent> getEvents(long eventLineId) {
        List<AppEvent> events = new ArrayList<>();
        Cursor cursor = database.query(EventsDbHelper.TABLE_EVENTS, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            AppEvent event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        cursor.close();
        return events;
    }

    public Cursor getEventsCursor(long eventLineId) {
        Cursor cursor = database.query(EventsDbHelper.TABLE_EVENTS, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    private AppEvent cursorToEvent(Cursor cursor) {
        AppEvent event =  new AppEvent();
        event.setId(cursor.getLong(0));
        event.setLineId(cursor.getLong(1));
        return event;
    }
}
