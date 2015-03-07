package com.example.dheeraj.superprofs.fragments.courseActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dheeraj.superprofs.CourseActivity;
import com.example.dheeraj.superprofs.R;
import com.example.dheeraj.superprofs.fakeData.FakeDataJsonStrings;
import com.example.dheeraj.superprofs.models.Course;
import com.example.dheeraj.superprofs.models.CourseReview;
import com.example.dheeraj.superprofs.models.Profile;
import com.example.dheeraj.superprofs.models.User;
import com.example.dheeraj.superprofs.utils.JsonHandler;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by windows 7 on 3/8/2015.
 */
public class SpinnerFragment extends Fragment {
    private static final String TAG = SpinnerFragment.class.getSimpleName();
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
            // TODO api call
            String courseJsonString = FakeDataJsonStrings.getCourse();
            CourseActivity.courseJsonStringOF = courseJsonString;
            final Course course = JsonHandler.parse(courseJsonString, Course.class);
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
            CourseActivity.course = c;
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new CoursesFragment())
                    .commit();
        }
    }

}