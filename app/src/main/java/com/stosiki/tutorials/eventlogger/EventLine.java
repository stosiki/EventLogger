package com.stosiki.tutorials.eventlogger;

import java.util.ArrayList;

/**
 * Created by mike on 7/15/2015.
 */
public class EventLine {
    private int type;
    private String title;
    private ArrayList<AppEvent> events;

    public EventLine(int type, String title) {
        this.type = type;
        this.title = title;
        events = new ArrayList<>();
    }

    public void addEvent(AppEvent event) {
        events.add(event);
    }

    public String getTitle() {
        return title;
    }

    public int getCount() {
        return events.size();
    }
}
