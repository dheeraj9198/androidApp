package com.example.dheeraj.superprofs.handlers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class CourseActivityDownloaderServiceHandler extends Handler {
    private Context context;

    public CourseActivityDownloaderServiceHandler(Context c) {
        super();
    }

    @Override
    public void handleMessage(Message message) {
        int state = message.arg1;
        Toast.makeText(context, "got message from service : " + state, Toast.LENGTH_LONG).show();
    }
}