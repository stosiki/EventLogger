package com.stosiki.tutorials.eventlogger;

import java.util.Date;

/**
 * Created by mike on 7/15/2015.
 */
public class AppEvent {
    private Date timeStamp;
    private long id;
    private long lineId;
    private int numValue;
    private String comment;

    public AppEvent() {
        timeStamp = new Date();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public AppEvent(int value) {
        numValue = value;
        comment = null;
    }

    public AppEvent(String comment) {
        comment = comment;
        numValue = -1;
    }

    public long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public int getNumValue() {
        return numValue;
    }
}
