package com.stosiki.tutorials.eventlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by mike on 7/18/2015.
 */
public class EventLinesDataSource {
    private static final String TAG = EventLinesDataSource.class.getName();

    private SQLiteDatabase database;
    private EventsDbHelper dbHelper;

    private String[] allColumns = {
        EventsDbHelper.COLUMN_ID, EventsDbHelper.COLUMN_LINE_TYPE, EventsDbHelper.COLUMN_LINE_TITLE
    };

    public EventLinesDataSource(Context context) {
        dbHelper = new EventsDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public EventLine createEventLine(int type, String title) {
        Log.d(TAG, "Creating event line");
        ContentValues values = new ContentValues();
        values.put(EventsDbHelper.COLUMN_LINE_TYPE, type);
        values.put(EventsDbHelper.COLUMN_LINE_TITLE, title);
        long lineId = database.insert(EventsDbHelper.TABLE_LINES, null, values);
        Cursor cursor = database.query(EventsDbHelper.TABLE_LINES, allColumns,
                EventsDbHelper.COLUMN_ID + " = " + lineId, null, null, null, null);
        cursor.moveToFirst();
        EventLine eventLine = cursorToLine(cursor);
        return eventLine;
    }

    public Cursor getEventLinesCursor() {
        Cursor cursor = database.query(EventsDbHelper.TABLE_LINES, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    private EventLine cursorToLine(Cursor cursor) {
        long lineId = cursor.getLong(0);
        int lineType = cursor.getInt(1);
        String lineTitle = cursor.getString(2);
        EventLine eventLine = new EventLine(lineId, lineType, lineTitle);
        return eventLine;
    }

    public void deleteLine(EventLine eventLine) {
        long id = eventLine.getId();
        database.delete(EventsDbHelper.TABLE_LINES, EventsDbHelper.COLUMN_ID + " = " + id, null);
    }
}