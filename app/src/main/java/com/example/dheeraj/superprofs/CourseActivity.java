package com.example.dheeraj.superprofs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dheeraj.superprofs.exoplayer.DemoUtil;
import com.example.dheeraj.superprofs.fakeData.FakeDataJsonStrings;
import com.example.dheeraj.superprofs.models.Attachment;
import com.example.dheeraj.superprofs.models.Course;
import com.example.dheeraj.superprofs.models.CourseReview;
import com.example.dheeraj.superprofs.models.Lecture;
import com.example.dheeraj.superprofs.models.Profile;
import com.example.dheeraj.superprofs.models.Section;
import com.example.dheeraj.superprofs.models.User;
import com.example.dheeraj.superprofs.utils.JsonHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


public class CourseActivity extends ActionBarActivity {

    private static final String TAG = CourseActivity.class.getSimpleName();
    public static final String PROFESSOR_JSON_DATA = "professor_json_data";

    private static Course course;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new SpinnerFragment())
                        .commit();
            }
        }
    }


    public static class SpinnerFragment extends Fragment {

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstance) {
            View rootView = layoutInflater.inflate(R.layout.loading, container, false);
            DataFetcher dataFetcher = new DataFetcher();
            dataFetcher.execute();
            return rootView;
        }

        private class DataFetcher extends AsyncTask<Void, String, Course> {

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Course doInBackground(Void... params) {
                // publishProgress("started");
                final Course course = JsonHandler.parse(FakeDataJsonStrings.getCourse(), Course.class);
                // publishProgress("ended");
                // download images
                // download images in multi threads to reduce loading time
                // publishProgress("started");
                ExecutorService executorService = Executors.newFixedThreadPool(4);
                try {
                    if (course != null) {
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                course.downloadBitmap();
                            }
                        });
                        for (final Profile profile : course.getProfessor().getUser().getProfiles()) {
                            executorService.submit(new Runnable() {
                                @Override
                                public void run() {
                                    profile.downloadBitmap();
                                }
                            });
                        }
                        for (User user : course.getStudents()) {
                            for (final Profile profile : user.getProfiles()) {
                                executorService.submit(new Runnable() {
                                    @Override
                                    public void run() {
                                        profile.downloadBitmap();
                                    }
                                });
                            }
                        }
                        for (final Course course1 : course.getSimilarCourses()) {
                            executorService.submit(new Runnable() {
                                @Override
                                public void run() {
                                    course1.downloadBitmap();
                                }
                            });
                        }

                        for (CourseReview courseReview : course.getCourseReviews()) {
                            for (final Profile profile : courseReview.getUser().getProfiles()) {
                                executorService.submit(new Runnable() {
                                    @Override
                                    public void run() {
                                        profile.downloadBitmap();
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "exception in downloading images", e);
                }

                executorService.shutdown();
                try {
                    executorService.awaitTermination(100, TimeUnit.SECONDS);
                } catch (Exception e) {
                    Log.e(TAG, "executor.awaitTermination interrupted", e);
                }
                //  publishProgress("ended");
                return course;
            }

            protected void onProgressUpdate(String... s) {
                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Toast.makeText(getActivity(), s[0] + " at " + mydate, Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onPostExecute(Course c) {
                course = c;
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new CoursesFragment())
                        .commit();
            }
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CoursesFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_course_details, container, false);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Lecture lecture = null;
                    String lectureName = String.valueOf(((TextView) v.findViewById(R.id.lecture_name)).getText());
                    //TODO do something better here
                    for (Section section : course.getSections()) {
                        for (Lecture lecture1 : section.getLectures()) {
                            if (lecture1.getName().equals(lectureName)) {
                                lecture = lecture1;
                                break;
                            }
                            if (lecture != null) {
                                break;
                            }
                        }
                    }

                    //TextView textView = (TextView) v.findViewById(R.id.lecture_name);
                    //Toast.makeText(getActivity(), textView.getText(), Toast.LENGTH_LONG).show();
                    if (lecture != null && lecture.isPublic()) {
                        Intent mpdIntent = new Intent(getActivity(), PlayerActivity.class)
                                .setData(Uri.parse(FakeDataJsonStrings.getVideoUrl()))
                                .putExtra(PlayerActivity.CONTENT_ID_EXTRA, /*sample.contentId*/"")
                                .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, /*sample.type*/DemoUtil.TYPE_DASH);
                        startActivity(mpdIntent);
                    } else {
                        DialogFragment dialogFragment = new PurchaseDialogFragment();
                        dialogFragment.show(getActivity().getFragmentManager(), "PurchaseDialogFragment");
                    }
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

            /**
             * professor info
             */

            LinearLayout profInfoLinearLayout = (LinearLayout) rootView.findViewById(R.id.prof_info);
            profInfoLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProfessorActivity.class);
                    // bitmap makes data too large for passing it to activity
                    for (Profile profile : course.getProfessor().getUser().getProfiles()) {
                        profile.eraseBitmap();
                    }
                    intent.putExtra(PROFESSOR_JSON_DATA, JsonHandler.stringify(course.getProfessor()));
                    startActivity(intent);
                }
            });

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

            Iterator<Course> courseIterator = course.getSimilarCourses().iterator();
            while (courseIterator.hasNext()) {
                Course course1 = courseIterator.next();
                View view = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_similar_course, null);
                parseAndInflateCourse(view, course1);
                linearLayout1.addView(view);
                if (courseIterator.hasNext()) {
                    View view1 = new View(getActivity());
                    view1.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));
                    view1.setBackgroundColor(getResources().getColor(R.color.apricot));
                    linearLayout1.addView(view1);
                }
            }
            return rootView;
        }


        private void parseAndInflateCourse(View rootView, Course course) {
            //Toast.makeText(getActivity(), course.toString(), Toast.LENGTH_LONG).show();
            RelativeLayout mainCourseLinearLayout =(RelativeLayout) rootView.findViewById(R.id.main_course);
            mainCourseLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO replace by actual video
                    Intent mpdIntent = new Intent(getActivity(), PlayerActivity.class)
                            .setData(Uri.parse(FakeDataJsonStrings.getVideoUrl()))
                            .putExtra(PlayerActivity.CONTENT_ID_EXTRA, /*sample.contentId*/"")
                            .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, /*sample.type*/DemoUtil.TYPE_DASH);
                    startActivity(mpdIntent);
                }
            });

            try {
                ImageView imageViewSubject = (ImageView) rootView.findViewById(R.id.iv_subject);
                imageViewSubject.setImageBitmap(course.getBitmap());
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
                Log.e(TAG, "caught exception while filling course rating ", e);
            }
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

    public static final class PurchaseDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(getActivity().getLayoutInflater().inflate(R.layout.purchase_dialog, null))
                    .setPositiveButton("UPGRADE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getActivity(), PaymentActivity.class);
                            getActivity().startActivityForResult(intent, 1);
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(Typeface.DEFAULT_BOLD);
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                }
            });
            return alertDialog;

        }

    }

}
