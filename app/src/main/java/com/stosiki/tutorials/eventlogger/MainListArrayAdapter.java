package com.stosiki.tutorials.eventlogger;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mike on 7/15/2015.
 *
 * Uses Holder pattern as a performance optimization
 */
public class MainListArrayAdapter extends SimpleCursorAdapter {
    private static final String[] FROM = new String[]{
            EventsDbHelper.COLUMN_LINE_TITLE
    };
    private static final int[] TO = new int[]{R.id.event_line_title};

    private Context context;
    private ArrayList<EventLine> eventLines;

    static class ViewHolder {
        TextView titleText;
        TextView counterText;
    }

    public MainListArrayAdapter(Context context, Cursor cursor) {
        super(context, R.layout.main_list_item, cursor, FROM, TO, FLAG_REGISTER_CONTENT_OBSERVER);

        this.context = context;
        this.eventLines = eventLines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View rowView = convertView;

//        if(rowView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.main_list_item, parent, false);
/*
            ViewHolder holder = new ViewHolder();
            holder.titleText = (TextView) rowView.findViewById(R.id.event_line_title);
            holder.counterText = (TextView) rowView.findViewById(R.id.event_line_count);
            rowView.setTag(holder);
*/
//        }

/*
        ViewHolder holder = (ViewHolder)rowView.getTag();
        holder.counterText.setText(String.valueOf(eventLines.get(position).getCount()));
        holder.titleText.setText(eventLines.get(position).getTitle());

        return rowView;
*/
        return view;
    }
}
