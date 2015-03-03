package com.example.dheeraj.superprofs.downloader;

import android.util.Log;

import com.example.dheeraj.superprofs.models.MPDModels.AdaptationSetBase;
import com.example.dheeraj.superprofs.models.MPDModels.MPDParser;
import com.example.dheeraj.superprofs.models.MPDModels.SBase;
import com.example.dheeraj.superprofs.utils.Device;
import com.example.dheeraj.superprofs.utils.JsonHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LectureDownloader {
    private static final String TAG = LectureDownloader.class.getSimpleName();
    private static int PRETTY_PRINT_INDENT_FACTOR = 4;
    public static final String manifestFileNameUnencrypted = "manifest.mpd";
    public static final String manifestFileNameEncrypted = "manifestE.xml";
    public static final String lectureFolderName = "lectures";

    private static long totalsize = 0;
    private static long size = 0;

    private LectureDownloader() {

    }

    private static void encrypt(String src, String dst) throws IOException {
        final byte[] buf = new byte[1024];
        byte test;
        final InputStream is = new FileInputStream(src);
        final OutputStream os = new FileOutputStream(dst);
        while (true) {
            int n = is.read(buf);
            if (n == -1) break;
            for (int k = 0; k < n; k++) {
                test = buf[k];
                if (test != -128) {
                    buf[k] = (byte) ~test;
                }
            }
            os.write(buf, 0, n);
        }
        os.close();
        is.close();
    }

    private static void deleteFile(String src) {
        File file = new File(src);
        if (file.delete()) {
            Log.i(TAG, "file deleted successfully : " + file);
        } else {
            Log.e(TAG, "unable to delete file : " + file);
        }

    }

    public static void downloadLecture(URL url, int lectureId) throws IOException {

        String folder = Device.getDir() +
                File.separator + lectureFolderName +
                File.separator + lectureId + File.separator;
        File file = new File(folder + manifestFileNameEncrypted);
        if (!file.exists()) {
            downloadFile(url, new File(folder + manifestFileNameUnencrypted));
        } else {
            Log.i(TAG, "manifest already exists , decrypting and using the same");
            encrypt(folder + manifestFileNameEncrypted, folder + manifestFileNameUnencrypted);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(folder + "manifest.mpd")));
        StringBuilder stringBuilder = new StringBuilder();
        String test;
        while ((test = bufferedReader.readLine()) != null) {
            stringBuilder.append(test);
        }
        test = stringBuilder.toString();
        bufferedReader.close();

        encrypt(folder + manifestFileNameUnencrypted, folder + manifestFileNameEncrypted);
        deleteFile(folder + manifestFileNameUnencrypted);

        try {
            JSONObject xmlJSONObj = XML.toJSONObject(test);
            String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
            MPDParser mpdParser = JsonHandler.parse(jsonPrettyPrintString, MPDParser.class);
            String location = mpdParser.getMPD().getLocation();
            location = location.substring(0, location.lastIndexOf("/")) + "/";
            /**
             * test was created using new by string builder
             * remove reference to release it
             */
            test = null;
            calculateTotalFileSize(mpdParser.getMPD().getPeriod().getAdaptationSet(), location);
            for (AdaptationSetBase adaptationSetBase : mpdParser.getMPD().getPeriod().getAdaptationSet()) {
                String repId = adaptationSetBase.getRepresentation().getId();
                String init = adaptationSetBase.getSegmentTemplate().getInitialization();
                //"initialization": "chunk_ctvideo_cfm4s_rid$RepresentationID$_cinit_w664894557_mpd.m4s",
                init = init.replace("$RepresentationID$", repId);
                downloadFile(new URL(location + init), new File(folder + init));

                //"media": "chunk_ctvideo_cfm4s_rid$RepresentationID$_cs$Time$_w664894557_mpd.m4s",
                String media = adaptationSetBase.getSegmentTemplate().getMedia();
                media = media.replace("$RepresentationID$", repId);

                long time = 0;
                boolean firstGone = false;
                for (SBase s : adaptationSetBase.getSegmentTemplate().getSegmentTimeline().getS()) {
                    if (!firstGone) {
                        firstGone = true;
                        String mediaFinal = media.replace("$Time$", time + "");
                        downloadFile(new URL(location + mediaFinal), new File(folder + mediaFinal));
                    }
                    time = time + Long.parseLong(s.getD());
                    String mediaFinal = media.replace("$Time$", time + "");
                    downloadFile(new URL(location + mediaFinal), new File(folder + mediaFinal));
                }
            }

        } catch (JSONException je) {
            Log.e(TAG, "caught exception", je);
        }
    }

    /**
     * actual file downloader
     */
    private static void calculateTotalFileSize(ArrayList<AdaptationSetBase> adaptationSetBases, String location) {
        for (AdaptationSetBase adaptationSetBase : adaptationSetBases) {
            String repId = adaptationSetBase.getRepresentation().getId();
            String init = adaptationSetBase.getSegmentTemplate().getInitialization();
            //"initialization": "chunk_ctvideo_cfm4s_rid$RepresentationID$_cinit_w664894557_mpd.m4s",
            init = init.replace("$RepresentationID$", repId);
            try {
                totalsize = totalsize + getFileLength(new URL(location + init));
            } catch (MalformedURLException e) {

            }
            //"media": "chunk_ctvideo_cfm4s_rid$RepresentationID$_cs$Time$_w664894557_mpd.m4s",
            String media = adaptationSetBase.getSegmentTemplate().getMedia();
            media = media.replace("$RepresentationID$", repId);

            long time = 0;
            boolean firstGone = false;
            for (SBase s : adaptationSetBase.getSegmentTemplate().getSegmentTimeline().getS()) {
                if (!firstGone) {
                    firstGone = true;
                    String mediaFinal = media.replace("$Time$", time + "");
                    try {
                        totalsize = time + getFileLength(new URL(location + mediaFinal));
                    } catch (MalformedURLException e) {

                    }
                }
                time = time + Long.parseLong(s.getD());
                String mediaFinal = media.replace("$Time$", time + "");
                try {
                    totalsize = totalsize + getFileLength(new URL(location + mediaFinal));
                } catch (MalformedURLException e) {

                }
            }
        }
    }

    /**
     * actual file downloader
     */
    public static void downloadFile(URL url, File file) {
        URLConnection urlConnection = null;
        InputStream in = null;
        FileOutputStream fileOutputStream = null;
        try {
            urlConnection = url.openConnection();
            totalsize = urlConnection.getContentLength();
            byte[] bytes = new byte[1024];
            int k;
            in = url.openStream();
            fileOutputStream = new FileOutputStream(new File("/home/dheeraj/Desktop/bigVideo.mp4"));
            while ((k = in.read(bytes)) != -1) {
                size = size + k;
                fileOutputStream.write(bytes);
            }
        } catch (SocketException e) {
            // caught when there is no net connection
            Log.e(TAG, "caught socket connection, may be net problem", e);
        } catch (IOException e) {
            // caught when file does not exist at http server
            Log.e(TAG, "caught IO connection, may be net problem", e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                //
            }
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                //
            }
        }
    }

    /**
     * get remote file length
     */
    public static int getFileLength(URL url) {
        try {
            URLConnection urlConnection = url.openConnection();
            return urlConnection.getContentLength();
        } catch (IOException e) {
            //TODO do something better here
            Log.e(TAG, "", e);
            return 0;
        }
    }

}