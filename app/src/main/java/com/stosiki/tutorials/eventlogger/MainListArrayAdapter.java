package com.stosiki.tutorials.eventlogger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mike on 7/15/2015.
 */
public class MainListArrayAdapter extends ArrayAdapter<EventLine> {
    private Context context;
    private ArrayList<EventLine> eventLines;

    public MainListArrayAdapter(Context context, ArrayList<EventLine> eventLines) {
        super(context, R.layout.main_list_item, eventLines);
        this.context = context;
        this.eventLines = eventLines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.main_list_item, parent, false);
        TextView titleText = (TextView) rowView.findViewById(R.id.event_line_title);
        titleText.setText(eventLines.get(position).getTitle());
//        titleText.setText(String.valueOf(position));
        TextView counterText = (TextView) rowView.findViewById(R.id.event_line_count);
        counterText.setText(String.valueOf(eventLines.get(position).getCount()));

        return rowView;
    }
}
