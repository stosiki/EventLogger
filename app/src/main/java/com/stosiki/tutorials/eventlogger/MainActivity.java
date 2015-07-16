package com.stosiki.tutorials.eventlogger;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends ListActivity implements ActionMode.Callback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<EventLine> data;
    protected Object actionMode;
    protected int selectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String[] values = new String[] {"Niki", "Soso", "Piper", "Daynara", "Alex"};
//        final ArrayList<String> list = new ArrayList<>();
//        for(String value : values) {
//            list.add(value);
//        }


//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, list);
        final ListView listView = (ListView) findViewById(android.R.id.list);
        if(data == null) {
            createFakeData();
        }
        final MainListArrayAdapter adapter = new MainListArrayAdapter(this, data);
        listView.setAdapter(adapter);

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
                                adapter.notifyDataSetChanged();
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
                if(actionMode != null) {
                    return false;
                }
                selectedItem = position;
                actionMode = MainActivity.this.startActionMode(MainActivity.this);
                view.setSelected(true);
                return true;
            }
        });
    }

    private void createFakeData() {
        data = new ArrayList<>();
        data.add(new EventLine(1, "Laugh"));
        data.add(new EventLine(1, "Smile"));
        data.add(new EventLine(1, "Cry"));
        data.add(new EventLine(1, "Cringe"));
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_delete:
                deleteLine();
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    private void deleteLine() {
        data.remove(selectedItem);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        selectedItem = -1;
    }
}
