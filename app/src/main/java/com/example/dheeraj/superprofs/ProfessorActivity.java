package com.example.dheeraj.superprofs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dheeraj.superprofs.fakeData.FakeDataJsonStrings;
import com.example.dheeraj.superprofs.models.Course;
import com.example.dheeraj.superprofs.models.CourseReview;
import com.example.dheeraj.superprofs.models.Professor;
import com.example.dheeraj.superprofs.models.ProfessorEducation;
import com.example.dheeraj.superprofs.models.ProfessorExperience;
import com.example.dheeraj.superprofs.models.Profile;
import com.example.dheeraj.superprofs.models.User;
import com.example.dheeraj.superprofs.utils.JsonHandler;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfessorActivity extends ActionBarActivity {

    private static final String TAG = ProfessorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SpinnerFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_professor, menu);
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

        private Professor professor;

        public void setProfessor(Professor p){
            this.professor =  p;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_professor, container, false);
            TextView numOnlineCoursesTextView = (TextView) rootView.findViewById(R.id.num_online_courses);
            numOnlineCoursesTextView.setText(professor.getNumCourses() + "");

            try {
                CircleImageView circleImageView = (CircleImageView) rootView.findViewById(R.id.professor_profile_image);
                circleImageView.setImageBitmap(professor.getUser().getProfiles().get(0).getBitmap());
            } catch (Exception e) {
                Log.e(TAG, "caught exception while setting image", e);
            }

            TextView yearsExperienceTextView = (TextView) rootView.findViewById(R.id.years_experience);
            yearsExperienceTextView.setText(professor.getTotal_experience() + "");

            TextView profName = (TextView) rootView.findViewById(R.id.prof_name);
            profName.setText(professor.getUser().getFullName());

            TextView profTagLine = (TextView) rootView.findViewById(R.id.prof_tagline);
            profTagLine.setText(professor.getTagline());


            TextView profSummary = (TextView) rootView.findViewById(R.id.prof_summary);
            profSummary.setText(professor.getSummary());

            LinearLayout experienceLinearLayout = (LinearLayout) rootView.findViewById(R.id.experience);

            int x = 0;
            for (ProfessorExperience professorExperience : professor.getProfessorExperiences()) {
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_item_education_teaching_experience, null);
                TextView experienceHead = (TextView) linearLayout.findViewById(R.id.head);
                experienceHead.setText(professorExperience.getCompany_name());
                TextView experienceDetail = (TextView) linearLayout.findViewById(R.id.detail);
                experienceDetail.setText(professorExperience.getDetail());
                if (x % 2 == 0) {
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.light_gray));
                }
                experienceLinearLayout.addView(linearLayout);
                x++;
            }

            LinearLayout eduactionLinearLayout = (LinearLayout) rootView.findViewById(R.id.education);

            x = 0;
            for (ProfessorEducation professorEducation : professor.getProfessorEducations()) {
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_item_education_teaching_experience, null);
                TextView experienceHead = (TextView) linearLayout.findViewById(R.id.head);
                experienceHead.setText(professorEducation.getDegree() + " (" + professorEducation.getGraduation_year() + ") ");
                TextView experienceDetail = (TextView) linearLayout.findViewById(R.id.detail);
                experienceDetail.setText(professorEducation.getCollege());
                if (x % 2 == 0) {
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.light_gray));
                }
                eduactionLinearLayout.addView(linearLayout);
                x++;
            }


            return rootView;
        }
    }

    public static class SpinnerFragment extends Fragment {
        public SpinnerFragment() {
        }

        @Override
        public void onStart() {
            super.onStart();
            Intent intent = getActivity().getIntent();
            String professorData = intent.getStringExtra(CourseActivity.PROFESSOR_JSON_DATA);
            DataFetcher dataFetcher = new DataFetcher();
            dataFetcher.execute(professorData);
        }

        @Override
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstance) {
            View rootView = layoutInflater.inflate(R.layout.loading, container, false);
            return rootView;
        }

        private class DataFetcher extends AsyncTask<String, String, Professor> {

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Professor doInBackground(String... professorStrings) {
                Professor professor = JsonHandler.parse(professorStrings[0], Professor.class);
                for(Profile profile : professor.getUser().getProfiles()){
                    profile.setBitmap();
                }
                return professor;
            }

            protected void onProgressUpdate(String... s) {
                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Toast.makeText(getActivity(), s[0] + " at " + mydate, Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onPostExecute(Professor professor) {
                PlaceholderFragment placeholderFragment = new PlaceholderFragment();
                placeholderFragment.setProfessor(professor);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, placeholderFragment)
                        .commit();
            }
        }

    }

}
