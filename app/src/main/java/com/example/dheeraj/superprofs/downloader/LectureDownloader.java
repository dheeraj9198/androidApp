package com.example.dheeraj.superprofs.downloader;

import android.util.Log;

import com.example.dheeraj.superprofs.models.MPDModels.AdaptationSetBase;
import com.example.dheeraj.superprofs.models.MPDModels.MPDParser;
import com.example.dheeraj.superprofs.models.MPDModels.SBase;
import com.example.dheeraj.superprofs.utils.App;
import com.example.dheeraj.superprofs.utils.JsonHandler;

import org.apache.commons.io.FileUtils;
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
    private static final int PRETTY_PRINT_INDENT_FACTOR = 4;


    public static final int ERROR = 0;
    public static final int COMPLETED = 1;
    public static final int INTERRUPTED = 2;

    private int totalsize = 0;
    private int downloadedSize = 0;

    private String dashUrl;
    private int lectureId;

    public LectureDownloader(String dashUrl, int lectureId) {
        this.dashUrl = dashUrl;
        this.lectureId = lectureId;
    }

    public int getDownloadedPercent() {
        try {
            return 100 * downloadedSize / totalsize;
        } catch (Exception e) {
            return 0;
        }
    }

    public int getLectureId() {
        return lectureId;
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

    public int downloadLecture() {
        URL url;
        try {
            url = new URL(dashUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "caught malformed url exception for : " + dashUrl, e);
            return ERROR;
        }

        String lectureFolder = App.getLectureFolderName(lectureId + "");

        File file = new File(lectureFolder + File.separator + App.manifestFileNameEncrypted);
        if (!file.exists()) {
            //downloadFile(url, new File(folder + manifestFileNameUnencrypted));
            try {
                FileUtils.copyURLToFile(url, new File(lectureFolder + File.separator + App.manifestFileNameUnencrypted));
            } catch (SocketException e) {
                Log.e(TAG, "caught socket exception ", e);
                return INTERRUPTED;
            } catch (IOException e) {
                Log.e(TAG, "caught io exception ", e);
                return ERROR;
            }
        } else {
            Log.i(TAG, "manifest already exists , decrypting and using the same");
            try {
                encrypt(lectureFolder + File.separator + App.manifestFileNameEncrypted, lectureFolder + File.separator + App.manifestFileNameUnencrypted);
            } catch (Exception e) {
                Log.e(TAG, "caught exception while decrypting file", e);
                return ERROR;
            }
        }
        String test;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(lectureFolder + File.separator + App.manifestFileNameUnencrypted)));
            StringBuilder stringBuilder = new StringBuilder();
            while ((test = bufferedReader.readLine()) != null) {
                stringBuilder.append(test);
            }
            test = stringBuilder.toString();
            bufferedReader.close();
            try {
                encrypt(lectureFolder + File.separator + App.manifestFileNameUnencrypted, lectureFolder + File.separator + App.manifestFileNameEncrypted);
            } catch (Exception e) {
                Log.e(TAG, "caught exception while encrypting ", e);
            }
            deleteFile(lectureFolder + File.separator + App.manifestFileNameUnencrypted);

        } catch (Exception e) {
            Log.e(TAG, "caught exception while creating string  from xml file content", e);
            return ERROR;
        }
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
            try {
                calculateTotalFileSize(mpdParser.getMPD().getPeriod().getAdaptationSet(), location);
            } catch (MalformedURLException e) {
                return ERROR;
            } catch (SocketException e) {
                return INTERRUPTED;
            } catch (IOException e) {
                return ERROR;
            }
            for (AdaptationSetBase adaptationSetBase : mpdParser.getMPD().getPeriod().getAdaptationSet()) {
                String repId = adaptationSetBase.getRepresentation().getId();
                String init = adaptationSetBase.getSegmentTemplate().getInitialization();
                //"initialization": "chunk_ctvideo_cfm4s_rid$RepresentationID$_cinit_w664894557_mpd.m4s",
                init = init.replace("$RepresentationID$", repId);
                try {
                    url = new URL(location + init);
                } catch (MalformedURLException e) {
                    Log.e(TAG,"caught malformed url exception",e);
                    return ERROR;
                }
                try {
                    file = new File(lectureFolder + File.separator + init);
                    if (file.exists() && file.length() == getFileLength(url)) {
                        downloadedSize = downloadedSize + (int)file.length();
                        Log.i(TAG, "file already downloaded : " + file.toString());
                    } else {
                        downloadFile(url, file);
                    }
                } catch (SocketException e) {
                    return INTERRUPTED;
                } catch (IOException e) {
                    return ERROR;
                }

                //"media": "chunk_ctvideo_cfm4s_rid$RepresentationID$_cs$Time$_w664894557_mpd.m4s",
                String media = adaptationSetBase.getSegmentTemplate().getMedia();
                media = media.replace("$RepresentationID$", repId);

                long time = 0;
                for (SBase s : adaptationSetBase.getSegmentTemplate().getSegmentTimeline().getS()) {
                    String mediaFinal = media.replace("$Time$", time + "");
                    try {
                        url = new URL(location + mediaFinal);
                    } catch (MalformedURLException e) {
                        Log.e(TAG, "malformed url exception", e);
                        return ERROR;
                    }
                    try {
                        file = new File(lectureFolder + File.separator + mediaFinal);
                        if (file.exists() && file.length() == getFileLength(url)) {
                            Log.i(TAG, "file already downloaded : " + file.toString());
                        } else {
                            downloadFile(url, file);
                        }
                    } catch (SocketException e) {
                        return INTERRUPTED;
                    } catch (IOException e) {
                        return ERROR;
                    }
                    time = time + Long.parseLong(s.getD());
                }
            }
        } catch (JSONException je) {
            Log.e(TAG, "caught exception", je);
        }
        return COMPLETED;
    }

    /**
     * actual file downloader
     */
    private void calculateTotalFileSize(ArrayList<AdaptationSetBase> adaptationSetBases, String location) throws MalformedURLException, SocketException, IOException {
        URL url;
        for (AdaptationSetBase adaptationSetBase : adaptationSetBases) {
            String repId = adaptationSetBase.getRepresentation().getId();
            String init = adaptationSetBase.getSegmentTemplate().getInitialization();
            //"initialization": "chunk_ctvideo_cfm4s_rid$RepresentationID$_cinit_w664894557_mpd.m4s",
            init = init.replace("$RepresentationID$", repId);
            try {
                url = new URL(location + init);
                totalsize = totalsize + getFileLength(url);
            } catch (MalformedURLException e) {
                Log.e(TAG, "caught malformed url exception", e);
                throw e;
            } catch (SocketException e) {
                Log.e(TAG, "caught socket exception", e);
                throw e;
            } catch (IOException e) {
                Log.e(TAG, "caught IO exception", e);
                throw e;
            }
            //"media": "chunk_ctvideo_cfm4s_rid$RepresentationID$_cs$Time$_w664894557_mpd.m4s",
            String media = adaptationSetBase.getSegmentTemplate().getMedia();
            media = media.replace("$RepresentationID$", repId);

            long time = 0;
            for (SBase s : adaptationSetBase.getSegmentTemplate().getSegmentTimeline().getS()) {
                String mediaFinal = media.replace("$Time$", time + "");
                try {
                    url = new URL(location + mediaFinal);
                    totalsize = totalsize + getFileLength(url);
                } catch (MalformedURLException e) {
                    Log.e(TAG, "caught malformed url exception", e);
                    throw e;
                } catch (SocketException e) {
                    Log.e(TAG, "caught socket exception", e);
                    throw e;
                } catch (IOException e) {
                    Log.e(TAG, "caught IO exception", e);
                    throw e;
                }
                time = time + Long.parseLong(s.getD());
            }
        }
    }

    /**
     * actual file downloader
     */
    private void downloadFile(URL url, File file) throws SocketException, IOException {
        Log.i(TAG,"downloading file"+file);
        InputStream in = null;
        FileOutputStream fileOutputStream = null;
        try {
            byte[] bytes = new byte[50*1024];
            int k;
            in = url.openStream();
            fileOutputStream = new FileOutputStream(file);
            while ((k = in.read(bytes)) != -1) {
                Log.i(TAG, "downloaded bytes = " + k);
                downloadedSize = downloadedSize + k;
                fileOutputStream.write(bytes);
            }
        } catch (SocketException e) {
            // caught when there is no net connection
            Log.e(TAG, "caught socket connection, may be net problem", e);
            try {
                in.close();
            } catch (Exception e1) {
                //
            }
            try {
                fileOutputStream.close();
            } catch (Exception e1) {
                //
            }
            throw e;
        } catch (IOException e) {
            // caught when file does not exist at http server
            Log.e(TAG, "caught IO connection, may be storage problem", e);
            try {
                in.close();
            } catch (Exception e1) {
                //
            }
            try {
                fileOutputStream.close();
            } catch (Exception e1) {
                //
            }
            throw e;
        }
    }

    /**
     * get remote file length
     */
    private static int getFileLength(URL url) throws SocketException, IOException {
        try {
            URLConnection urlConnection = url.openConnection();
            return urlConnection.getContentLength();
        } catch (SocketException se) {
            Log.e(TAG,"caught socket exception",se);
            throw se;
        } catch (IOException e) {
            Log.e(TAG,"caught socket exception",e);
            throw e;
        }
    }

}