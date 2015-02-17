package com.example.dheeraj.superprofs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    private static final String test = "Now that the elections are over, Sanjay Singh, member of AAP National Executive, has made a statement that AAP never promised 15 Lakh CCTV cameras. On a TV channel, Sanjay Singh made the following statement:";


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

            String[] sectionArray = new String[]{"section 1", "section 2", "section 3"};
            String[] lectureArray = new String[]{"lecture 1", "lecture 2", "lecture 2"};
            String[] attachmentArray = new String[]{"Chutiyapa_101.pdf", "Chutiyapa_102.pdf", "Chutiyapa_103.pdf"};

            View rootView = inflater.inflate(R.layout.fragment_course_details, container, false);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v.findViewById(R.id.lecture_name);
                    Toast.makeText(getActivity(), textView.getText(), Toast.LENGTH_LONG).show();
                }
            };

            //add view dynamically here

            //course sections
            LinearLayout courseSectionLinearLayout = (LinearLayout) rootView.findViewById(R.id.course_sections);
            for (String section : sectionArray) {
                View sectionHead = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_section_head, null);
                TextView textViewSectionTopic = (TextView) sectionHead.findViewById(R.id.section_title);
                textViewSectionTopic.setText(section);
                courseSectionLinearLayout.addView(sectionHead);

                for (String lecture : lectureArray) {
                    View lectureView = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_syllabus_lecture, null);
                    TextView textView = (TextView) lectureView.findViewById(R.id.lecture_name);
                    ImageView imageView = (ImageView) lectureView.findViewById(R.id.iv_lecture_list);

                    Random random = new Random();
                    if (random.nextInt() % 2 == 0) {
                        imageView.setImageResource(R.drawable.iv_lock_button);
                    }
                    textView.setText(lecture);

                    lectureView.setOnClickListener(onClickListener);
                    courseSectionLinearLayout.addView(lectureView);
                }
            }

            //attachments
            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.attachment_items);
            for (String attachment : attachmentArray) {
                View attachmentView = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_attachment, null);
                TextView textViewAttachment = (TextView) attachmentView.findViewById(R.id.list_item_id_attachment);
                textViewAttachment.setText(attachment);
                linearLayout.addView(attachmentView);

            }

            LinearLayout courseDescription = (LinearLayout) rootView.findViewById(R.id.about_the_course);

            for (int i = 0; i < 3; i++) {
                View descriptionView = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_description_parts, null);
                TextView textDescription = (TextView) descriptionView.findViewById(R.id.description_detail);
                textDescription.setText(test);
                courseDescription.addView(descriptionView);
            }

            /**
             * view pager for horizontal scrolling
             */

            ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.review_view_pager);
            MyPagerAdapter1 myPagerAdapter = new MyPagerAdapter1(getActivity());
            viewPager.setAdapter(myPagerAdapter);


            /**
             * similar courses
             */

            LinearLayout linearLayout1 = (LinearLayout) rootView.findViewById(R.id.similar_courses);
            View view = getLayoutInflater(savedInstanceState)



            return rootView;
        }
    }

    private static class MyPagerAdapter1 extends PagerAdapter {

        private Activity activity;
        private int numberOfPages = 3;

        public MyPagerAdapter1(Activity a) {
            super();
            activity = a;
        }

        @Override
        public int getCount() {
            return numberOfPages;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            LinearLayout linearLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.list_item_review, null);
            TextView studentNameTextView = (TextView) linearLayout.findViewById(R.id.student_name);
            studentNameTextView.setText("BY DHEERAJ SACHAN " + position);

            View view0 = (View) linearLayout.findViewById(R.id.bar1);
            View view1 = (View) linearLayout.findViewById(R.id.bar2);
            View view2 = (View) linearLayout.findViewById(R.id.bar3);

            view0.setBackgroundColor(activity.getResources().getColor(R.color.gray));
            view1.setBackgroundColor(activity.getResources().getColor(R.color.gray));
            view2.setBackgroundColor(activity.getResources().getColor(R.color.gray));

            switch (position) {
                case 0:
                    view0.setBackgroundColor(activity.getResources().getColor(R.color.orange));
                    break;
                case 1:
                    view1.setBackgroundColor(activity.getResources().getColor(R.color.orange));
                    break;
                case 2:
                    view2.setBackgroundColor(activity.getResources().getColor(R.color.orange));
                    break;
            }

            container.addView(linearLayout);
            return linearLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

    }

   /* private static class MyPagerAdapter extends PagerAdapter {

        int NumberOfPages = 5;
        private Activity activity;

        public MyPagerAdapter(Activity a) {
            super();
            activity = a;
        }


        int[] res = {
                android.R.drawable.ic_dialog_alert,
                android.R.drawable.ic_menu_camera,
                android.R.drawable.ic_menu_compass,
                android.R.drawable.ic_menu_directions,
                android.R.drawable.ic_menu_gallery};
        int[] backgroundcolor = {
                0xFF101010,
                0xFF202020,
                0xFF303030,
                0xFF404040,
                0xFF505050};

        @Override
        public int getCount() {
            return NumberOfPages;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            TextView textView = new TextView(activity);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(30);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setText(String.valueOf(position));

            ImageView imageView = new ImageView(activity);
            imageView.setImageResource(res[position]);
            ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(imageParams);

            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layout.setBackgroundColor(backgroundcolor[position]);
            layout.setLayoutParams(layoutParams);
            layout.addView(textView);
            layout.addView(imageView);

            final int page = position;
            layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(activity,
                            "Page " + page + " clicked",
                            Toast.LENGTH_LONG).show();
                }
            });

            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

    }*/


}
