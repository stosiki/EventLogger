package com.stosiki.tutorials.eventlogger;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.stosiki.tutorials.eventlogger.CreateEventLineDialogFragment.CreateEventLineDialogListener;

import java.sql.SQLException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
    implements CreateEventLineDialogListener{

    private static final String TAG = MainActivity.class.getName();

    private EventLinesDataSource linesDao;
    private EventsDataSource eventsDao;

    //private ArrayList<EventLine> data;
    private MainListArrayAdapter eventLinesAdapter;
    private ListView listView;

    private View undoContainer;
    // Action Mode lock
    private Object actionMode;
    private int selectedItemIndex = -1;
    // undo support
    private int lastDeletedItemIndex = -1;
    private EventLine lastDeletedEventLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linesDao = new EventLinesDataSource(this);
        eventsDao = new EventsDataSource(this);
        try {
            linesDao.open();
        } catch (SQLException sqle) {
            Log.e(TAG, sqle.getMessage());
            finish();
        }
        try {
            eventsDao.open();
        } catch (SQLException sqlee) {
            Log.e(TAG, sqlee.getMessage());
            finish();
        }

        undoContainer = (View)findViewById(R.id.undo_bar);

        listView = (ListView) findViewById(android.R.id.list);

        Cursor eventLinesCursor = linesDao.getEventLinesCursor();
        eventLinesAdapter = new MainListArrayAdapter(this, eventLinesCursor);
        listView.setAdapter(eventLinesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Log.d(TAG, "onListItemClick position=" +
                        String.valueOf(position) + ", id=" + String.valueOf(id));
                final EventLine item = (EventLine) listView.getItemAtPosition(position);
                item.addEvent(new AppEvent());
                view.animate().setDuration(1000).scaleX(new Float(1.2)).scaleY(new Float(1.2))
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
//                                list.remove(item);
                                eventLinesAdapter.notifyDataSetChanged();
//                                view.setAlpha(1);
                                view.setScaleX(new Float(1.0));
                                view.setScaleY(new Float(1.0));
                            }
                        });
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Long List Item Click");
                if (actionMode != null) {
                    return false;
                }
                selectedItemIndex = position;
                actionMode = MainActivity.this.startActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        MenuInflater menuInflater = mode.getMenuInflater();
                        menuInflater.inflate(R.menu.row_selection, menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_item_delete_eventline:
                                deleteEventLine();
                                lastDeletedItemIndex = selectedItemIndex;
                                mode.finish();
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        actionMode = null;
                        selectedItemIndex = -1;
                    }
                });
                view.setSelected(true);
                return true;
            }
        });

        FloatingActionButton addLineButton =
                (FloatingActionButton)findViewById(R.id.add_event_line_widget);
        addLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddLineDialog();
            }
        });
    }

    private void showAddLineDialog() {
        DialogFragment eventLineTypeEntryFragment = new CreateEventLineDialogFragment();
        eventLineTypeEntryFragment.show(getFragmentManager(), "Line Type");
    }

    private void deleteEventLine() {
        EventLine eventLine = (EventLine)listView.getItemAtPosition(selectedItemIndex);
        linesDao.deleteLine(eventLine);
//        data.remove(eventLine);
        lastDeletedItemIndex = selectedItemIndex;
        lastDeletedEventLine = eventLine;
        eventLinesAdapter.notifyDataSetChanged();
        showUndo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        showUndo();

        return true;
    }

    public void onUndoClick(View view) {
//        linesDao.createEventLine(lastDeletedItemIndex, lastDeletedEventLine);
        eventLinesAdapter.notifyDataSetChanged();
        undoContainer.setVisibility(View.GONE);
    }

    private void showUndo() {
        undoContainer.setVisibility(View.VISIBLE);
        undoContainer.setAlpha(1.0f);
        undoContainer.animate().alpha(0.4f).setDuration(5000)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        undoContainer.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        linesDao.close();
        eventsDao.close();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        int selectedType = ((CreateEventLineDialogFragment)dialog).getSelectedType();
        String title = ((CreateEventLineDialogFragment)dialog).getTitle();
        createEventLine(selectedType, title);
    }

    private void createEventLine(int selectedType, String title) {
        //TODO: check that title is unique
        linesDao.createEventLine(selectedType, title);
        runOnUiThread(new Runnable() {
            public void run() {
                eventLinesAdapter.notifyDataSetChanged();
            }
        });

        Log.d(TAG, "Event line successfully created");
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
