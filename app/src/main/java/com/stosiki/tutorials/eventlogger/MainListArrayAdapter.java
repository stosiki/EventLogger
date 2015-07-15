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
 *
 * Uses Holder pattern as a performance optimization
 */
public class MainListArrayAdapter extends ArrayAdapter<EventLine> {
    private Context context;
    private ArrayList<EventLine> eventLines;

    static class ViewHolder {
        TextView titleText;
        TextView counterText;
    }

    public MainListArrayAdapter(Context context, ArrayList<EventLine> eventLines) {
        super(context, R.layout.main_list_item, eventLines);
        this.context = context;
        this.eventLines = eventLines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.main_list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.titleText = (TextView) rowView.findViewById(R.id.event_line_title);
            holder.counterText = (TextView) rowView.findViewById(R.id.event_line_count);
            rowView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder)rowView.getTag();
        holder.counterText.setText(String.valueOf(eventLines.get(position).getCount()));
        holder.titleText.setText(eventLines.get(position).getTitle());

        return rowView;
    }
}
