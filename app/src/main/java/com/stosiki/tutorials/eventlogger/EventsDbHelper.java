package com.stosiki.tutorials.eventlogger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mike on 7/18/2015.
 *
 * events table(_id, line_id, timestamp, numValue, comment)
 * lines table(_id, line_type, title)
 *
 * This db scheme is not normalized according to the event type
 */
public class EventsDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "events";
    private static final int DB_VERSION = 1;

    public static final String TABLE_EVENTS = "events";
    public static final String TABLE_LINES = "lines";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LINE_ID = "line_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_NUMVALUE = "numvalue";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_LINE_TYPE = "line_type";
    public static final String COLUMN_LINE_TITLE = "line_title";


    private static final String TABLE_EVENTS_CREATE =
            "CREATE TABLE " + TABLE_EVENTS +
            "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_LINE_ID + " INTEGER, " +
            COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
            COLUMN_NUMVALUE + " INTEGER, " +
            COLUMN_COMMENT + " TEXT" +
            ");";

    private static final String TABLE_LINES_CREATE =
            "CREATE TABLE " + TABLE_LINES +
            "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_LINE_TYPE + " INTEGER, " +
            COLUMN_LINE_TITLE + " VARCHAR(64)" +
            ");";

    public EventsDbHelper(Context context)  {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_EVENTS_CREATE);
        db.execSQL(TABLE_LINES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINES);
        onCreate(db);
    }
}
