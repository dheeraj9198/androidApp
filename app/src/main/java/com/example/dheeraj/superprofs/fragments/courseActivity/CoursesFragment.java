package com.example.dheeraj.superprofs.fragments.courseActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.CaptioningManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dheeraj.superprofs.CourseActivity;
import com.example.dheeraj.superprofs.PlayerActivity;
import com.example.dheeraj.superprofs.ProfessorActivity;
import com.example.dheeraj.superprofs.R;
import com.example.dheeraj.superprofs.db.DatabaseHelper;
import com.example.dheeraj.superprofs.db.DbHandler;
import com.example.dheeraj.superprofs.db.tables.CourseJson;
import com.example.dheeraj.superprofs.db.tables.LectureDownloadStatus;
import com.example.dheeraj.superprofs.exoplayer.DashRendererBuilder;
import com.example.dheeraj.superprofs.exoplayer.DefaultRendererBuilder;
import com.example.dheeraj.superprofs.exoplayer.DemoPlayer;
import com.example.dheeraj.superprofs.exoplayer.DemoUtil;
import com.example.dheeraj.superprofs.exoplayer.EventLogger;
import com.example.dheeraj.superprofs.exoplayer.HlsRendererBuilder;
import com.example.dheeraj.superprofs.exoplayer.SmoothStreamingRendererBuilder;
import com.example.dheeraj.superprofs.exoplayer.SmoothStreamingTestMediaDrmCallback;
import com.example.dheeraj.superprofs.exoplayer.UnsupportedDrmException;
import com.example.dheeraj.superprofs.exoplayer.WidevineTestMediaDrmCallback;
import com.example.dheeraj.superprofs.fakeData.FakeDataJsonStrings;
import com.example.dheeraj.superprofs.models.Attachment;
import com.example.dheeraj.superprofs.models.Course;
import com.example.dheeraj.superprofs.models.CourseReview;
import com.example.dheeraj.superprofs.models.Lecture;
import com.example.dheeraj.superprofs.models.Profile;
import com.example.dheeraj.superprofs.models.Section;
import com.example.dheeraj.superprofs.services.DownloaderService;
import com.example.dheeraj.superprofs.utils.AppUtils;
import com.example.dheeraj.superprofs.utils.Device;
import com.example.dheeraj.superprofs.utils.JsonHandler;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.VideoSurfaceView;
import com.google.android.exoplayer.metadata.TxxxMetadata;
import com.google.android.exoplayer.text.CaptionStyleCompat;
import com.google.android.exoplayer.text.SubtitleView;
import com.google.android.exoplayer.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by windows 7 on 3/8/2015.
 */
public class CoursesFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener,
        DemoPlayer.Listener, DemoPlayer.TextListener, DemoPlayer.Id3MetadataListener {
    public static final String TAG = CoursesFragment.class.getSimpleName();
    public static final String PROFESSOR_JSON_DATA = "professor_json_data";
    private boolean isAttachmentExpanededList = false;

    private boolean keepRunning = false;

    private View.OnClickListener mDeleteListener = null;
    private View.OnClickListener mDownloadPauseListener = null;

    //player related stuff
    private EventLogger eventLogger;
    private MediaController mediaController;
    private View debugRootView;
    private View shutterView;
    private VideoSurfaceView surfaceView;
    private TextView debugTextView;
    private TextView playerStateTextView;
    private SubtitleView subtitleView;
    private Button videoButton;
    private Button audioButton;
    private Button textButton;
    private Button retryButton;

    private DemoPlayer player;
    private boolean playerNeedsPrepare;

    private long playerPosition;
    private boolean enableBackgroundAudio;

    private Uri contentUri = Uri.parse("http://frontend.test.superprofs.com:1935/vod_android/mp4:sp_high_4.mp4/manifest.mpd");
    private int contentType = DemoUtil.TYPE_DASH;
    private String contentId = null;

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            showControls();
        }
        String text = "playWhenReady=" + playWhenReady + ", playbackState=";
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                text += "buffering";
                break;
            case ExoPlayer.STATE_ENDED:
                text += "ended";
                break;
            case ExoPlayer.STATE_IDLE:
                text += "idle";
                break;
            case ExoPlayer.STATE_PREPARING:
                text += "preparing";
                break;
            case ExoPlayer.STATE_READY:
                text += "ready";
                break;
            default:
                text += "unknown";
                break;
        }
        playerStateTextView.setText(text);
        updateButtonVisibilities();
    }

    @Override
    public void onVideoSizeChanged(int width, int height, float pixelWidthAspectRatio) {
        shutterView.setVisibility(View.GONE);
        surfaceView.setVideoWidthHeightRatio(
                height == 0 ? 1 : (width * pixelWidthAspectRatio) / height);
    }

    @Override
    public void onError(Exception e) {
        if (e instanceof UnsupportedDrmException) {
            // Special case DRM failures.
            UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
            int stringId = unsupportedDrmException.reason == UnsupportedDrmException.REASON_NO_DRM
                    ? R.string.drm_error_not_supported
                    : unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                    ? R.string.drm_error_unsupported_scheme
                    : R.string.drm_error_unknown;
            Toast.makeText(getActivity().getApplicationContext(), stringId, Toast.LENGTH_LONG).show();
        }
        playerNeedsPrepare = true;
        updateButtonVisibilities();
        showControls();
    }

    @Override
    public void onId3Metadata(Map<String, Object> metadata) {
        for (int i = 0; i < metadata.size(); i++) {
            if (metadata.containsKey(TxxxMetadata.TYPE)) {
                TxxxMetadata txxxMetadata = (TxxxMetadata) metadata.get(TxxxMetadata.TYPE);
                Log.i(TAG, String.format("ID3 TimedMetadata: description=%s, value=%s",
                        txxxMetadata.description, txxxMetadata.value));
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (player != null) {
            player.setSurface(holder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Do nothing.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player != null) {
            player.blockingClearSurface();
        }
    }

    @Override
    public void onText(String text) {
        if (TextUtils.isEmpty(text)) {
            subtitleView.setVisibility(View.INVISIBLE);
        } else {
            subtitleView.setVisibility(View.VISIBLE);
            subtitleView.setText(text);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == retryButton) {
            preparePlayer();
        }
    }

    private DemoPlayer.RendererBuilder getRendererBuilder() {
        String userAgent = DemoUtil.getUserAgent(getActivity().getApplicationContext());
        switch (contentType) {
            case DemoUtil.TYPE_SS:
                return new SmoothStreamingRendererBuilder(userAgent, contentUri.toString(), contentId,
                        new SmoothStreamingTestMediaDrmCallback(), debugTextView);
            case DemoUtil.TYPE_DASH:
                return new DashRendererBuilder(userAgent, contentUri.toString(), contentId,
                        new WidevineTestMediaDrmCallback(contentId), debugTextView);
            case DemoUtil.TYPE_HLS:
                return new HlsRendererBuilder(userAgent, contentUri.toString(), contentId);
            default:
                return new DefaultRendererBuilder(getActivity().getApplicationContext(), contentUri, debugTextView);
        }
    }

    private void preparePlayer() {
        if (player == null) {
            player = new DemoPlayer(getRendererBuilder());
            player.addListener(this);
            player.setTextListener(this);
            player.setMetadataListener(this);
            player.seekTo(playerPosition);
            playerNeedsPrepare = true;
            mediaController.setMediaPlayer(player.getPlayerControl());
            mediaController.setEnabled(true);
            eventLogger = new EventLogger();
            eventLogger.startSession();
            player.addListener(eventLogger);
            player.setInfoListener(eventLogger);
            player.setInternalErrorListener(eventLogger);
        }
        if (playerNeedsPrepare) {
            player.prepare();
            playerNeedsPrepare = false;
            updateButtonVisibilities();
        }
        player.setSurface(surfaceView.getHolder().getSurface());
        player.setPlayWhenReady(true);
    }

    private void updateButtonVisibilities() {
        retryButton.setVisibility(playerNeedsPrepare ? View.VISIBLE : View.GONE);
        videoButton.setVisibility(haveTracks(DemoPlayer.TYPE_VIDEO) ? View.VISIBLE : View.GONE);
        audioButton.setVisibility(haveTracks(DemoPlayer.TYPE_AUDIO) ? View.VISIBLE : View.GONE);
        textButton.setVisibility(haveTracks(DemoPlayer.TYPE_TEXT) ? View.VISIBLE : View.GONE);
    }

    private boolean haveTracks(int type) {
        return player != null && player.getTracks(type) != null;
    }

    @Override
    public void onDestroy() {
        keepRunning = false;
        releasePlayer();
        super.onDestroy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_course_details, container, false);


        keepRunning = true;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (keepRunning) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (((CourseActivity) getActivity()).bound && DownloaderService.isRunning && DownloaderService.currentLectureDownloader != null) {
                                try {
                                    DownloaderService.DownloadStats downloadStats = ((CourseActivity) getActivity()).downloaderService.getDownloadStats();
                                    View view = rootView.findViewById(downloadStats.getLectureId());
                                    ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.circularProgressbar);
                                    progressBar.setProgress(downloadStats.getPercent());
                                    if (downloadStats.getPercent() == 100) {
                                        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.download_icon_section);
                                        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
                                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.trash));
                                        if (mDeleteListener != null) {
                                            linearLayout.setOnClickListener(mDeleteListener);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "caught exception on ui updater thread", e);
                                }
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }
            }
        });


        View.OnClickListener lectureOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playVideo("", 1, rootView);
                if (true) return;

                Lecture lecture = (Lecture) v.getTag();
                Toast.makeText(getActivity(), "lecture id = " + lecture.getId(), Toast.LENGTH_LONG).show();
                if (lecture != null && lecture.isPublic()) {
                    Intent mpdIntent = new Intent(getActivity(), PlayerActivity.class)
                            .setData(Uri.parse(FakeDataJsonStrings.getVideoUrl()))
                            .putExtra(CourseActivity.LECTURE_ID, lecture.getId())
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
            progressBar.setMax(CourseActivity.course.getCourseMetas().get(0).getTotal_duration());
            progressBar.setProgress(CourseActivity.course.getCourseMetas().get(0).getAvailable_content_duration());
        } catch (Exception e) {
            Log.e(TAG, "caught exception while setting up progress bar ", e);
        }

        /**
         * set course details
         */
        parseAndInflateCourse(rootView, CourseActivity.course);

        View view11 = (RelativeLayout) rootView.findViewById(R.id.main_course);
        ViewGroup.LayoutParams layoutParams11 = view11.getLayoutParams();
        layoutParams11.width = Device.getWidth(getActivity());
        layoutParams11.height = Device.getWidth(getActivity()) * 9 / 16;
        view11.setLayoutParams(layoutParams11);
        /**
         * professor info
         */

        LinearLayout profInfoLinearLayout = (LinearLayout) rootView.findViewById(R.id.prof_info);
        profInfoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfessorActivity.class);
                // bitmap makes data too large for passing it to activity
                for (Profile profile : CourseActivity.course.getProfessor().getUser().getProfiles()) {
                    profile.eraseBitmap();
                }
                intent.putExtra(PROFESSOR_JSON_DATA, JsonHandler.stringify(CourseActivity.course.getProfessor()));
                startActivity(intent);
            }
        });

        try {
            CircleImageView profCircleImageView = (CircleImageView) rootView.findViewById(R.id.professor_profile_image);
            profCircleImageView.setImageBitmap(CourseActivity.course.getProfessor().getUser().getProfiles().get(0).getBitmap());
        } catch (Exception e) {
            Log.e(TAG, "unable to set professor image", e);
        }

        TextView profNameTextView1 = (TextView) rootView.findViewById(R.id.prof_name_1);
        profNameTextView1.setText(CourseActivity.course.getProfessor().getUser().getFullName());

        try {
            TextView collegeTextView = (TextView) rootView.findViewById(R.id.college);
            collegeTextView.setText(CourseActivity.course.getProfessor().getProfessorEducations().get(0).getCollege());
        } catch (Exception e) {
            Log.e(TAG, "unable to find college", e);
        }

        //course sections
        LinearLayout courseSectionLinearLayout = (LinearLayout) rootView.findViewById(R.id.course_sections);
        for (Section section : CourseActivity.course.getSections()) {
            View sectionHead = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_section_head, null);
            TextView textViewSectionTopic = (TextView) sectionHead.findViewById(R.id.section_title);
            textViewSectionTopic.setText(section.getName().toUpperCase());
            textViewSectionTopic.setTextColor(getResources().getColor(R.color.black));
            textViewSectionTopic.setBackgroundColor(getResources().getColor(R.color.light_gray));
            courseSectionLinearLayout.addView(sectionHead);

            for (Lecture lecture : section.getLectures()) {
                View lectureView = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_syllabus_lecture, null);
                /**
                 * make id unique
                 */
                lectureView.setId(lecture.getId());
                lectureView.setTag(lecture);
                final LinearLayout linearLayout = (LinearLayout) lectureView.findViewById(R.id.linear_layout_lecture_data);
                TextView textView = (TextView) linearLayout.findViewById(R.id.lecture_name);
                ImageView imageView = (ImageView) lectureView.findViewById(R.id.iv_lecture_list);
                if (!lecture.isPublic()) {
                    imageView.setImageResource(R.drawable.iv_lock_button);
                }
                textView.setText(lecture.getName());

                /**
                 * set listeners
                 */
                imageView.setTag(lecture);
                linearLayout.setTag(lecture);
                imageView.setOnClickListener(lectureOnClickListener);
                linearLayout.setOnClickListener(lectureOnClickListener);


                /**
                 * download icon section
                 */
                final LinearLayout linearLayoutDownloads = (LinearLayout) lectureView.findViewById(R.id.download_icon_section);
                linearLayoutDownloads.setTag(lecture);
                //listeners

                final View.OnClickListener downloadResumeOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Lecture lecture1 = (Lecture) v.getTag();
                        DbHandler.getDbHandler().saveStatusById(lecture1.getId(), LectureDownloadStatus.STATUS_PENDING);

                        LinearLayout linearLayout1 = (LinearLayout) v;
                        linearLayout1.removeAllViews();
                        View view = getLayoutInflater(null).inflate(R.layout.circular_progress_with_image, null);
                        ImageView imageView1 = (ImageView) view.findViewById(R.id.image_view);
                        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.cancel));
                        linearLayout1.addView(view);
                        linearLayout1.setOnClickListener(mDownloadPauseListener);

                        ((CourseActivity) getActivity()).startAndBindToDownloadService();
                    }
                };

                final View.OnClickListener downloadPauseListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Lecture lecture1 = (Lecture) v.getTag();
                        DbHandler.getDbHandler().saveStatusById(lecture1.getId(), LectureDownloadStatus.STATUS_PAUSED);
                        if (DownloaderService.currentLectureDownloader != null &&
                                DownloaderService.currentLectureDownloader.getLectureId() == lecture1.getId()) {
                            DownloaderService.currentLectureDownloader.cancel();

                            LinearLayout linearLayout1 = (LinearLayout) v;
                            linearLayout1.removeAllViews();
                            View view = getLayoutInflater(null).inflate(R.layout.circular_progress_with_image, null);
                            ImageView imageView1 = (ImageView) view.findViewById(R.id.image_view);
                            imageView1.setImageDrawable(getResources().getDrawable(R.drawable.resume_download));
                            linearLayout1.addView(view);
                            linearLayout1.setOnClickListener(downloadResumeOnClickListener);
                        }
                    }
                };

                mDownloadPauseListener = downloadPauseListener;

                final View.OnClickListener downloadOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Lecture lecture = (Lecture) v.getTag();
                        //add lecture in download queue and start the downloader service if not running
                        Toast.makeText(getActivity(), "Added lecture in download queue " + lecture.getId(), Toast.LENGTH_SHORT).show();
                        //make db entry
                        DbHandler.getDbHandler().saveCourseJson(new CourseJson(CourseActivity.course.getId(), CourseActivity.courseJsonStringOF));
                        DbHandler.getDbHandler().saveLectureDownloadStatus(new LectureDownloadStatus(lecture.getId(),
                                CourseActivity.course.getId(),
                                    /*TODO dash url*/"http://frontend.test.superprofs.com:1935/vod_android/mp4:sp_high_4.mp4/manifest.mpd",
                                LectureDownloadStatus.STATUS_PENDING,
                                0, false
                        ));

                        LinearLayout linearLayout1 = (LinearLayout) v;

                        linearLayout1.removeAllViews();
                        View view = getLayoutInflater(null).inflate(R.layout.circular_progress_with_image, null);
                        linearLayout1.addView(view);
                        linearLayout1.setOnClickListener(downloadPauseListener);
                        ((CourseActivity) getActivity()).startAndBindToDownloadService();
                    }
                };


                View.OnClickListener deleteOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Lecture lecture1 = (Lecture) v.getTag();
                        AppUtils.deleteAllFilesAndFolderInNewThread(lecture1.getId());
                        DbHandler.getDbHandler().deleteLectureDownloadStatus(lecture1.getId());
                        Toast.makeText(getActivity(), "deleted files", Toast.LENGTH_SHORT).show();

                        //change icon and listener
                        LinearLayout linearLayout1 = (LinearLayout) v;
                        linearLayout1.removeAllViews();
                        View view = getLayoutInflater(null).inflate(R.layout.download_lecture_layout, null);
                        linearLayout1.addView(view);
                        linearLayout1.setOnClickListener(downloadOnClickListener);
                    }
                };
                mDeleteListener = deleteOnClickListener;


                LectureDownloadStatus lectureDownloadStatus = DbHandler.getDbHandler().getLectureDownloadStatusById(lecture.getId());
                if (lectureDownloadStatus != null) {
                    linearLayoutDownloads.removeAllViews();
                    if (lectureDownloadStatus.getStatus() == LectureDownloadStatus.STATUS_PENDING ||
                            lectureDownloadStatus.getStatus() == LectureDownloadStatus.STATUS_RUNNING) {
                        View view = getLayoutInflater(null).inflate(R.layout.circular_progress_with_image, null);
                        linearLayoutDownloads.addView(view);
                        linearLayoutDownloads.setOnClickListener(downloadPauseListener);
                    } else if (lectureDownloadStatus.getStatus() == LectureDownloadStatus.STATUS_FINISHED) {
                        View view = getLayoutInflater(null).inflate(R.layout.circular_progress_with_image, null);
                        ImageView imageView1 = (ImageView) view.findViewById(R.id.image_view);
                        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.trash));
                        ProgressBar progressBar1 = (ProgressBar) view.findViewById(R.id.circularProgressbar);
                        progressBar1.setProgress(100);
                        linearLayoutDownloads.setOnClickListener(deleteOnClickListener);
                        linearLayoutDownloads.addView(view);
                    } else if (lectureDownloadStatus.getStatus() == LectureDownloadStatus.STATUS_PAUSED) {
                        View view = getLayoutInflater(null).inflate(R.layout.circular_progress_with_image, null);
                        ImageView imageView1 = (ImageView) view.findViewById(R.id.image_view);
                        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.resume_download));
                        view.setTag(lecture);
                        ProgressBar progressBar1 = (ProgressBar) view.findViewById(R.id.circularProgressbar);
                        progressBar1.setProgress(lectureDownloadStatus.getPercentCompleted());
                        linearLayoutDownloads.addView(view);
                        linearLayoutDownloads.setOnClickListener(downloadResumeOnClickListener);
                    }
                } else if (lectureDownloadStatus == null || lectureDownloadStatus.getStatus() == LectureDownloadStatus.STATUS_ERROR) {
                    linearLayoutDownloads.removeAllViews();
                    View view = getLayoutInflater(null).inflate(R.layout.download_lecture_layout, null);
                    linearLayoutDownloads.addView(view);
                    linearLayoutDownloads.setOnClickListener(downloadOnClickListener);
                }
                courseSectionLinearLayout.addView(lectureView);
            }
        }

        //attachments
        final LinearLayout attachmentMasterRow = (LinearLayout) rootView.findViewById(R.id.attachment_master_row);
        attachmentMasterRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAttachmentExpanededList) {
                    isAttachmentExpanededList = false;
                    ImageView imageView = (ImageView) attachmentMasterRow.findViewById(R.id.attachment_drop_down);
                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.drop_down));
                    removeAttachments(rootView);
                } else {
                    addAttachments(rootView, savedInstanceState);
                    isAttachmentExpanededList = true;
                    ImageView imageView = (ImageView) attachmentMasterRow.findViewById(R.id.attachment_drop_down);
                    imageView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.drop_up));
                }
            }
        });

        //about the course
        TextView courseDescription = (TextView) rootView.findViewById(R.id.description);
        courseDescription.setText(Html.fromHtml(CourseActivity.course.getDescription()));

        //course students
        LinearLayout courseStudentLinearLayout = (LinearLayout) rootView.findViewById(R.id.course_student_layout);
        int numComponents = 4;
        for (int x = 0; x < Math.min(numComponents, CourseActivity.course.getStudents().size()); x++) {
            LinearLayout courseStudent1 = (LinearLayout) getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_course_student, null);
            try {
                CircleImageView circleImageView = (CircleImageView) courseStudent1.findViewById(R.id.course_student_image);
                circleImageView.setImageBitmap(CourseActivity.course.getStudents().get(x).getProfiles().get(0).getBitmap());
            } catch (Exception e) {
                Log.e(TAG, "caught exception while setting student image ", e);
            }
            try {
                TextView textView = (TextView) courseStudent1.findViewById(R.id.course_student_name);
                textView.setText(CourseActivity.course.getStudents().get(x).getFullName());
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
        MyPagerAdapter1 myPagerAdapter = new MyPagerAdapter1(getActivity(), CourseActivity.course.getCourseReviews());
        viewPager.setAdapter(myPagerAdapter);

        /**
         * similar courses
         */

        LinearLayout linearLayout1 = (LinearLayout) rootView.findViewById(R.id.similar_courses);

        Iterator<Course> courseIterator = CourseActivity.course.getSimilarCourses().iterator();
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

    private void playVideo(String url, int lectureId, View rootView) {
        RelativeLayout playerView = (RelativeLayout) rootView.findViewById(R.id.main_course);
        ViewGroup.LayoutParams layoutParams11 = playerView.getLayoutParams();
        layoutParams11.width = Device.getWidth(getActivity());
        layoutParams11.height = Device.getWidth(getActivity()) * 9 / 16;
        playerView.setLayoutParams(layoutParams11);

        if (mediaController != null && mediaController.isShowing()) {
            mediaController.hide();
        }
        mediaController = null;
        
        playerView.removeAllViews();
        releasePlayer();
        
        
        View playerMainView = getLayoutInflater(null).inflate(R.layout.player_activity, null);

        playerMainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    toggleControlsVisibility();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.performClick();
                }
                return true;
            }
        });

        shutterView = playerMainView.findViewById(R.id.shutter);
        debugRootView = playerMainView.findViewById(R.id.controls_root);

        surfaceView = (VideoSurfaceView) playerMainView.findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(this);
        debugTextView = (TextView) playerMainView.findViewById(R.id.debug_text_view);

        playerStateTextView = (TextView) playerMainView.findViewById(R.id.player_state_view);
        subtitleView = (SubtitleView) playerMainView.findViewById(R.id.subtitles);

        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(playerMainView);
        retryButton = (Button) playerMainView.findViewById(R.id.retry_button);
        retryButton.setOnClickListener(this);
        videoButton = (Button) playerMainView.findViewById(R.id.video_controls);
        audioButton = (Button) playerMainView.findViewById(R.id.audio_controls);
        textButton = (Button) playerMainView.findViewById(R.id.text_controls);

        DemoUtil.setDefaultCookieManager();

        configureSubtitleView();
        preparePlayer();
        playerView.addView(playerMainView);

    }
    
        private void releasePlayer() {
        if (player != null) {
            playerPosition = 0L;//player.getCurrentPosition();
            player.release();
            player = null;
            eventLogger.endSession();
            eventLogger = null;
        }
    }
    
    private void configureSubtitleView() {
        CaptionStyleCompat captionStyle;
        float captionTextSize = getCaptionFontSize();
        if (Util.SDK_INT >= 19) {
            captionStyle = getUserCaptionStyleV19();
            captionTextSize *= getUserCaptionFontScaleV19();
        } else {
            captionStyle = CaptionStyleCompat.DEFAULT;
        }
        subtitleView.setStyle(captionStyle);
        subtitleView.setTextSize(captionTextSize);
    }

    private float getCaptionFontSize() {
        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        return Math.max(getResources().getDimension(R.dimen.subtitle_minimum_font_size),
                CAPTION_LINE_HEIGHT_RATIO * Math.min(displaySize.x, displaySize.y));
    }

    private static final float CAPTION_LINE_HEIGHT_RATIO = 0.0533f;

    @TargetApi(19)
    private float getUserCaptionFontScaleV19() {
        CaptioningManager captioningManager =
                (CaptioningManager) getActivity().getSystemService(Context.CAPTIONING_SERVICE);
        return captioningManager.getFontScale();
    }

    @TargetApi(19)
    private CaptionStyleCompat getUserCaptionStyleV19() {
        CaptioningManager captioningManager =
                (CaptioningManager) getActivity().getSystemService(Context.CAPTIONING_SERVICE);
        return CaptionStyleCompat.createFromCaptionStyle(captioningManager.getUserStyle());
    }

    private void toggleControlsVisibility() {
        if (mediaController.isShowing()) {
            mediaController.hide();
            debugRootView.setVisibility(View.GONE);
        } else {
            showControls();
        }
    }


    private void showControls() {
        mediaController.show(0);
        debugRootView.setVisibility(View.VISIBLE);
    }

    private void addAttachments(View rootView, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.attachment_items);
        for (Attachment attachment : CourseActivity.course.getAttachments()) {
            View attachmentView = getLayoutInflater(savedInstanceState).inflate(R.layout.list_item_attachment, null);
            TextView textViewAttachmentName = (TextView) attachmentView.findViewById(R.id.head);
            textViewAttachmentName.setText(attachment.getName());
            TextView textViewAttachmentFile = (TextView) attachmentView.findViewById(R.id.file);
            textViewAttachmentFile.setText(attachment.getCompleteFileName());
            linearLayout.addView(attachmentView);
        }
    }

    private void removeAttachments(View rootView) {
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.attachment_items);
        linearLayout.removeAllViews();
    }


    private void parseAndInflateCourse(View rootView, Course course) {
        //Toast.makeText(getActivity(), course.toString(), Toast.LENGTH_LONG).show();
        RelativeLayout mainCourseLinearLayout = (RelativeLayout) rootView.findViewById(R.id.main_course);
        mainCourseLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO replace by actual video
                Intent mpdIntent = new Intent(getActivity(), PlayerActivity.class)
                        .setData(Uri.parse(FakeDataJsonStrings.getVideoUrl()))
                        .putExtra(PlayerActivity.CONTENT_ID_EXTRA, /*sample.contentId*/"")
                        .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, /*sample.type*/DemoUtil.TYPE_DASH);
                getActivity().startActivity(mpdIntent);
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