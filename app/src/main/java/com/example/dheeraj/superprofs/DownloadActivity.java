package com.example.dheeraj.superprofs;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dheeraj.superprofs.events.DownloadProgressEvent;
import com.example.dheeraj.superprofs.models.Lecture;
import com.example.dheeraj.superprofs.models.Section;
import com.example.dheeraj.superprofs.services.DownloaderService;
import com.example.dheeraj.superprofs.utils.JsonHandler;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class DownloadActivity extends ActionBarActivity {
    public static final String TAG = DownloadActivity.class.getSimpleName();

    public class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            int state = message.arg1;
            int lectureId = message.arg2;
            onDownloadProgressEvent(new DownloadProgressEvent(lectureId, state ));
        }
    }


    public static Handler messageHandler;

    public static final String LECTURE_ID = "lectureId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(messageHandler == null) {
            messageHandler = new MessageHandler();
        }
        setContentView(R.layout.activity_download);
        View.OnClickListener lectureOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lecture lecture = (Lecture) v.getTag();
                if (lecture != null) {
                    Intent intent = new Intent(getBaseContext(), DownloaderService.class);
                    intent.putExtra("MESSENGER", new Messenger(messageHandler));

                    intent.putExtra(LECTURE_ID, lecture.getId());
                    startService(intent);
                }
            }
        };

        String sectionString = (String) getIntent().getExtras().get("lectures");
        Section[] sections = JsonHandler.parse(sectionString, Section[].class);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.section_lectures);
        for (Section section : sections) {
            View sectionView = getLayoutInflater().inflate(R.layout.list_item_section_head, null);
            TextView textView = (TextView) sectionView.findViewById(R.id.section_title);
            textView.setText(section.getDescription());
            linearLayout.addView(sectionView);
            for (Lecture lecture : section.getLectures()) {
                View lectureView = getLayoutInflater().inflate(R.layout.list_item_download_lecture, null);
                lectureView.setId(lecture.getId());
                TextView textViewLecture = (TextView) lectureView.findViewById(R.id.title);
                textViewLecture.setText(lecture.getName());
                if (lecture.getId() % 2 == 0) {
                    ImageView imageView = (ImageView) lectureView.findViewById(R.id.icon);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.download_complete));
                }
                lectureView.setTag(lecture);
                lectureView.setOnClickListener(lectureOnClickListener);
                linearLayout.addView(lectureView);
            }

        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download, menu);
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStop();
    }

    public void onDownloadProgressEvent(DownloadProgressEvent downloadProgressEvent) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.section_lectures);
        View view = linearLayout.findViewById(downloadProgressEvent.getLectureId());
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.download_progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
        progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
        progressBar.setProgress(downloadProgressEvent.getPercent());
    }
}
