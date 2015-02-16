package com.example.dheeraj.superprofs;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            String[] array = new String[]{"a","b","c"};
            String[] array1 = new String[]{"Chutiyapa_101.pdf","Chutiyapa_101.pdf","Chutiyapa_101.pdf"};
            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_syllabus_lecture,R.id.test);
            stringArrayAdapter.addAll(Arrays.asList(array));


            ArrayAdapter<String> stringArrayAdapter1 = new ArrayAdapter<String>(getActivity(),R.layout.list_item_attachment,R.id.list_item_id_attachment);
            stringArrayAdapter1.addAll(Arrays.asList(array1));
            View rootView = inflater.inflate(R.layout.fragment_course_details, container, false);


            /*LinearLayout linearLayout =(LinearLayout) rootView.findViewById(R.id.syllabus_list_view);

            linearLayout.addView((LinearLayout)rootView.findViewById(R.id.test_add_dynamic));
            linearLayout.addView((LinearLayout)rootView.findViewById(R.id.test_add_dynamic));
            linearLayout.addView((LinearLayout)rootView.findViewById(R.id.test_add_dynamic));
            linearLayout.addView((LinearLayout)rootView.findViewById(R.id.test_add_dynamic));*/

            ExpandedListView listView = (ExpandedListView)rootView.findViewById(R.id.syllabus_list_view);
            listView.setAdapter(stringArrayAdapter);

            ExpandedListView listView1 = (ExpandedListView)rootView.findViewById(R.id.attachment_list_view);
            listView1.setAdapter(stringArrayAdapter1);

            listView.setOnTouchListener(new ListView.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });


            listView1.setOnTouchListener(new ListView.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });


            return rootView;
        }
    }
}
