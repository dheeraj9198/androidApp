package com.example.dheeraj.superprofs.utils;

import android.util.Log;


import com.example.dheeraj.superprofs.nanoHTTPServer.NanoHTTPD;
import com.example.dheeraj.superprofs.nanoHTTPServer.Status;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by dheeraj on 25/2/15.
 */
public class FileServer extends NanoHTTPD {
    private static final String TAG = FileServer.class.getSimpleName();
    public static final int port = 10000;
    private static NanoHTTPD nanoHTTPD;

    private String folderName;

    public FileServer(String fileName) {
        super(port);
        folderName = fileName;
    }

    public static class Mystream extends FileInputStream {

        private FileInputStream fileInputStream;

        public Mystream(String s) throws FileNotFoundException {
            super(s);
            fileInputStream = new FileInputStream(s);
        }

        @Override
        public int read(byte b[], int off, int len) throws IOException {
            byte[] bytes = new byte[b.length];
            int k = fileInputStream.read(bytes, off, len);
            if (k != -1) {
                for (int j = 0; j < k; j++) {
                    if (bytes[j] == -128) {
                        b[j] = bytes[j];
                    } else {
                        b[j] = (byte) ~bytes[j];
                    }
                }
            }
            return k;
        }
    }


    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> header, Map<String, String> parameters,
                          Map<String, String> files) {

        //Toast.makeText(context,"got request = "+uri,Toast.LENGTH_LONG).show();
        Log.e(TAG, "-------------------------------------------------------");
        Log.e(TAG, "-------------------------------------------------------");
        Log.e(TAG, "-------------------------------------------------------");
        Log.e(TAG, "got request = " + uri, new NullPointerException());
        Log.e(TAG, "-------------------------------------------------------");
        Log.e(TAG, "-------------------------------------------------------");
        Log.e(TAG, "-------------------------------------------------------");

        if (!uri.contains("manifest")) {
            InputStream fis = null;
            try {
                fis = new FileInputStream(folderName + uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (uri.contains("video"))
                return new NanoHTTPD.Response(Status.OK, "video/mp4", fis);
            else
                return new NanoHTTPD.Response(Status.OK, "audio/mp4", fis);

        }

        InputStream fis = null;

        try {
            fis = new Mystream(folderName+AppUtils.manifestFileNameEncrypted);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new NanoHTTPD.Response(Status.OK, "application/dash-xml", fis);
    }

    public static void startServer(String file) {
        nanoHTTPD = new FileServer(file);
        try {
            nanoHTTPD.start();
        } catch (Exception e) {
            Log.e(TAG, "unable to start http server", e);
        }
    }

    public static void stopServer() {
        if (nanoHTTPD != null) {
            nanoHTTPD.stop();
        }
    }
}
