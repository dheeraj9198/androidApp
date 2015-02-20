package com.example.dheeraj.superprofs;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dheeraj.superprofs.fakeData.FakeDataJsonStrings;
import com.example.dheeraj.superprofs.models.Attachment;
import com.example.dheeraj.superprofs.models.Course;
import com.example.dheeraj.superprofs.models.CourseMeta;
import com.example.dheeraj.superprofs.models.CourseReview;
import com.example.dheeraj.superprofs.models.Language;
import com.example.dheeraj.superprofs.models.Lecture;
import com.example.dheeraj.superprofs.models.Professor;
import com.example.dheeraj.superprofs.models.ProfessorEducation;
import com.example.dheeraj.superprofs.models.Profile;
import com.example.dheeraj.superprofs.models.Section;
import com.example.dheeraj.superprofs.models.User;
import com.example.dheeraj.superprofs.utils.BoonJsonHandler;
import com.example.dheeraj.superprofs.utils.Device;
import com.example.dheeraj.superprofs.utils.JsonHandler;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends ActionBarActivity {

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

        private class DataFetcher extends AsyncTask<Void, String, Course> {

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Course doInBackground(Void... params) {
                publishProgress("started");
                Course course = JsonHandler.parseToBaseResponse(FakeDataJsonStrings.getCourse(), Course.class);
                publishProgress("ended");
                return course;
            }

            protected void onProgressUpdate(String... s) {
                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Toast.makeText(getActivity(), s[0] + " at " + mydate, Toast.LENGTH_LONG).show();
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
             *set progress bar %completed scaled out of max course duration;
             */
            ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.course_progress_bar);
            progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
            progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);

            try {
                progressBar.setMax(course.getCourseMetas().get(0).getTotal_duration());
                progressBar.setProgress(course.getCourseMetas().get(0).getAvailable_content_duration());
            } catch (Exception e) {
                Log.e(TAG, "caught exception while setting up progress bar ", e);
            }

            /**
             * set course details
             */
            parseAndInflateCourse(rootView, course);

            //professor info
            try {
                CircleImageView profCircleImageView = (CircleImageView) rootView.findViewById(R.id.professor_profile_image);
                profCircleImageView.setImageBitmap(course.getProfessor().getUser().getProfiles().get(0).getBitmap());
            } catch (Exception e) {
                Log.e(TAG, "unable to set professor image", e);
            }

            TextView profNameTextView1 = (TextView) rootView.findViewById(R.id.prof_name_1);
            profNameTextView1.setText(course.getProfessor().getUser().getFullName());

            try {
                TextView collegeTextView = (TextView) rootView.findViewById(R.id.college);
                collegeTextView.setText(course.getProfessor().getProfessorEducations().get(0).getCollege());
            } catch (Exception e) {
                Log.e(TAG, "unable to find college", e);
            }

            //course sections
            LinearLayout courseSectionLinearLayout = (LinearLayout) rootView.findViewById(R.id.course_sections);
            for (Section section : course.getSections()) {
                View sectionHead = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_section_head, null);
                TextView textViewSectionTopic = (TextView) sectionHead.findViewById(R.id.section_title);
                textViewSectionTopic.setText(section.getName().toUpperCase());
                textViewSectionTopic.setTextColor(getResources().getColor(R.color.black));
                textViewSectionTopic.setBackgroundColor(getResources().getColor(R.color.light_gray));
                courseSectionLinearLayout.addView(sectionHead);

                for (Lecture lecture : section.getLectures()) {
                    View lectureView = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_syllabus_lecture, null);
                    TextView textView = (TextView) lectureView.findViewById(R.id.lecture_name);
                    ImageView imageView = (ImageView) lectureView.findViewById(R.id.iv_lecture_list);
                    if (!lecture.isPublic()) {
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


            //about the course
            TextView courseDescription = (TextView) rootView.findViewById(R.id.description);
            courseDescription.setText(Html.fromHtml(course.getDescription()));

            //course students
            LinearLayout courseStudentLinearLayout = (LinearLayout) rootView.findViewById(R.id.course_student_layout);
            int numComponents = 4;
            for (int x = 0; x < Math.min(numComponents, course.getStudents().size()); x++) {
                LinearLayout courseStudent1 = (LinearLayout) getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_course_student, null);
                try {
                    CircleImageView circleImageView = (CircleImageView) courseStudent1.findViewById(R.id.course_student_image);
                    circleImageView.setImageBitmap(course.getStudents().get(x).getProfiles().get(0).getBitmap());
                } catch (Exception e) {
                    Log.e(TAG, "caught exception while setting student image ", e);
                }
                try {
                    TextView textView = (TextView) courseStudent1.findViewById(R.id.course_student_name);
                    textView.setText(course.getStudents().get(x).getFullName());
                } catch (Exception e) {
                    Log.e(TAG, "caught exception while setting student name ", e);
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                courseStudent1.setLayoutParams(layoutParams);
                courseStudentLinearLayout.addView(courseStudent1);
            }

            /**
             * view pager for horizontal scrolling
             */

            ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.review_view_pager);
            MyPagerAdapter1 myPagerAdapter = new MyPagerAdapter1(getActivity(), course.getCourseReviews());
            viewPager.setAdapter(myPagerAdapter);

            /**
             * similar courses
             */

            LinearLayout linearLayout1 = (LinearLayout) rootView.findViewById(R.id.similar_courses);
            View view = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_similar_course, null);
            linearLayout1.addView(view);


            return rootView;
        }


        private void parseAndInflateCourse(View rootView, Course course) {
            try {
                ImageView imageViewSubject = (ImageView) rootView.findViewById(R.id.iv_subject);
                imageViewSubject.setImageBitmap(course.getImageBitmap());
            } catch (Exception e) {
                Log.e(TAG, "caught exception while loading image", e);
            }

            TextView courseNameTextView = (TextView) rootView.findViewById(R.id.course_name);
            courseNameTextView.setText(course.getName());

            TextView profNameTextView = (TextView) rootView.findViewById(R.id.prof_name);
            profNameTextView.setText(course.getProfessor().getUser().getFullName());

            TextView durationTextView = (TextView) rootView.findViewById(R.id.duration);

            try {
                durationTextView.setText(course.getCourseMetas().get(0).getDurationString());
            } catch (Exception e) {
                Log.e(TAG, "caught exception while getting course duration from course meta", e);
            }

            TextView languageTextView = (TextView) rootView.findViewById(R.id.language);
            try {
                languageTextView.setText(course.getAllLanguages());
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }

            try {
                TextView courseRatingTextView = (TextView) rootView.findViewById(R.id.course_rating);
                courseRatingTextView.setText(course.getCourseMetas().get(0).getCumulativeRatingString());
            } catch (Exception e) {

            }
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


            /**
             * view pager for horizontal scrolling
             */

            ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.review_view_pager);
            MyPagerAdapter1 myPagerAdapter = new MyPagerAdapter1(getActivity(), null);
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
        private ArrayList<CourseReview> courseReviews;

        public MyPagerAdapter1(Activity a, ArrayList<CourseReview> c) {
            super();
            activity = a;
            courseReviews = c;
            numberOfPages = Math.min(numberOfPages, courseReviews.size());
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

            View[] views = new View[3];
            views[0] = linearLayout.findViewById(R.id.bar1);
            views[1] = linearLayout.findViewById(R.id.bar2);
            views[2] = linearLayout.findViewById(R.id.bar3);

            for (int x = 0; x < views.length; x++) {
                if (x < numberOfPages) {
                    views[x].setBackgroundColor(activity.getResources().getColor(R.color.gray));
                } else {
                    views[x].setBackgroundColor(activity.getResources().getColor(R.color.white));
                }
            }

            try {
                CircleImageView circleImageView = (CircleImageView) linearLayout.findViewById(R.id.student_image);
                circleImageView.setImageBitmap(courseReviews.get(position).getUser().getProfiles().get(0).getBitmap());
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }

            try {
                TextView reviewTextView = (TextView) linearLayout.findViewById(R.id.review_text);
                reviewTextView.setText(courseReviews.get(position).getReview());
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }

            try {
                TextView studentNameTextView = (TextView) linearLayout.findViewById(R.id.student_name);
                studentNameTextView.setText(courseReviews.get(position).getUser().getFullName());
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }

            try {
                views[position].setBackgroundColor(activity.getResources().getColor(R.color.orange));
            } catch (Exception e) {
                Log.e(TAG, "", e);
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
