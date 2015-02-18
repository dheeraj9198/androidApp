package com.example.dheeraj.superprofs;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dheeraj.superprofs.fakeData.FakeDataJsonStrings;
import com.example.dheeraj.superprofs.models.Attachment;
import com.example.dheeraj.superprofs.models.Course;
import com.example.dheeraj.superprofs.models.Lecture;
import com.example.dheeraj.superprofs.models.Section;
import com.example.dheeraj.superprofs.utils.JsonHandler;

import junit.framework.Test;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    private static final String test = "Now that the elections are over, Sanjay Singh, member of AAP National Executive, has made a statement that AAP never promised 15 Lakh CCTV cameras. On a TV channel, Sanjay Singh made the following statement:";
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SpinnerFragment())
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


    public static class SpinnerFragment extends Fragment {
        public SpinnerFragment() {
        }

        @Override
        public void onStart() {
            super.onStart();
            DataFetcher dataFetcher = new DataFetcher();
            dataFetcher.execute();
        }

        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstance) {
            View rootView = layoutInflater.inflate(R.layout.loading, container, false);
            return rootView;
        }

        private class DataFetcher extends AsyncTask<Void, Void, Course> {

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Course doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "caught execption", Toast.LENGTH_LONG).show();
                }
                Course course = JsonHandler.parse(FakeDataJsonStrings.courseData, Course.class);
                return  course;
            }

            @Override
            protected void onPostExecute(Course course) {
                PlaceholderFragment placeholderFragment = new PlaceholderFragment();
                placeholderFragment.setCourse(course);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, placeholderFragment)
                        .commit();
            }
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private Course course;

        private void setCourse(Course c) {
            this.course = c;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_course_details, container, false);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v.findViewById(R.id.lecture_name);
                    Toast.makeText(getActivity(), textView.getText(), Toast.LENGTH_LONG).show();
                }
            };

            //add view dynamically here

            /**
             *set progress bar %completed scaled out of 10;
             */
            ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.courseProgressBar);
            progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
            progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);

            try {
                progressBar.setMax(course.getCourseMetas().get(0).getTotal_duration());
                progressBar.setProgress(course.getCourseMetas().get(0).getAvailable_content_duration());
            }catch (Exception e){
                Log.e(TAG,"caught exception while setting up progress bar ",e);
            }

            /**
             * set course details
             */

            try {
                ImageView imageViewSubject = (ImageView) rootView.findViewById(R.id.iv_subject);
                imageViewSubject.setImageBitmap(course.getImageBitmap());
            }catch (Exception e){
                Log.e(TAG,"caught exception while loading image",e);
            }

            TextView courseNameTextView = (TextView) rootView.findViewById(R.id.course_name);
            courseNameTextView.setText(course.getName());

            TextView profNameTextView = (TextView)rootView.findViewById(R.id.prof_name);
            profNameTextView.setText(course.getProfessor().getUser().getFullName());

            TextView durationTextView = (TextView)rootView.findViewById(R.id.duration);

            try {
                durationTextView.setText(course.getCourseMetas().get(0).getDurationString());
            }catch (Exception e){
                Log.e(TAG,"caught exception while getting course duration from course meta",e);
            }

            TextView languageTextView = (TextView)rootView.findViewById(R.id.language);
            languageTextView.setText(course.getAllLanguages());

            try {
                TextView courseRatingTextView = (TextView) rootView.findViewById(R.id.course_rating);
                courseRatingTextView.setText(course.getCourseMetas().get(0).getCumulative_rating() + "");
            }catch (Exception e){

            }

            //course sections
            LinearLayout courseSectionLinearLayout = (LinearLayout) rootView.findViewById(R.id.course_sections);
            for (Section section : course.getSections()) {
                View sectionHead = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_section_head, null);
                TextView textViewSectionTopic = (TextView) sectionHead.findViewById(R.id.section_title);
                textViewSectionTopic.setText(section.getName());
                courseSectionLinearLayout.addView(sectionHead);

                for (Lecture lecture : section.getLectures()) {
                    View lectureView = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_syllabus_lecture, null);
                    TextView textView = (TextView) lectureView.findViewById(R.id.lecture_name);
                    ImageView imageView = (ImageView) lectureView.findViewById(R.id.iv_lecture_list);
                    if(!lecture.isPublic()){
                        imageView.setImageResource(R.drawable.iv_lock_button);
                    }
                    textView.setText(lecture.getName());
                    lectureView.setOnClickListener(onClickListener);
                    courseSectionLinearLayout.addView(lectureView);
                }
            }

            //attachments
            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.attachment_items);
            for (Attachment attachment : course.getAttachments()) {
                View attachmentView = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_attachment, null);
                TextView textViewAttachmentName = (TextView) attachmentView.findViewById(R.id.head);
                textViewAttachmentName.setText(attachment.getName());
                TextView textViewAttachmentFile = (TextView) attachmentView.findViewById(R.id.file);
                textViewAttachmentFile.setText(attachment.getCompleteFileName());
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
            View view = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_similar_course, null);
            linearLayout1.addView(view);


            return rootView;
        }
    }

    public static class PlaceholderFragmentFake extends Fragment {

        public PlaceholderFragmentFake() {
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
                TextView textViewAttachment = (TextView) attachmentView.findViewById(R.id.head);
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
            View view = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_similar_course, null);
            linearLayout1.addView(view);


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

}
