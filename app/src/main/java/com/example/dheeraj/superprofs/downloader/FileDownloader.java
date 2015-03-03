package com.example.dheeraj.superprofs.downloader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dheeraj on 27/2/15.
 */
public class FileDownloader {
    private static long totalsize = 0;
    private static long size = 0;
    private static boolean run = true;

    public static void main(String[] args) throws IOException {
        URL url = new URL("https://s3-ap-southeast-1.amazonaws.com/media.coursehub.tv/120/4114/61868/61868xyz1xyz0.flv?X-Amz-Date=20150227T144618Z&X-Amz-Expires=300&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Signature=e0520a0638eeda13d52eb55b50a786291946592a6b5e0ed6aa39cf74c04afbe9&X-Amz-Credential=ASIAJGLE4SGHEDY2VJFQ/20150227/ap-southeast-1/s3/aws4_request&X-Amz-SignedHeaders=Host&x-amz-security-token=AQoDYXdzEC0a0ALm0f6sdcsl5B%2Bmf44I2uTLNx/Bl9w7DGg1HdtisotOeoygDWpiYd3qAS4c/IHwOi68UiPfGcGOBQM1y1MlaUzzGXpp5mtGAmlvD2OoTlqJhqM7K7tTf8407xmdTTzZ2it%2BlCQrW5ZqYbts1AkW5eYd1UxHq7SBjmP4n9kXALqIJ6U8IOHJHn2eNbrFhpx9AcNgEQWRAhvJx1q/msXVLOPwXAkKUlGgGET7GZnHxbODPLka6QOFbco0ujoiglmhaVjHpOB2Z1nq8YpqRaz0hJ87PE%2BiDdB144lVgNdZZsgrI9KsXVhcAPJInmj9TNTalcGDaki65aMG3bOCUPiN9zuotBbcjMyfhsUffeVxNplmHA//id/HFQL%2BNXVlyNOYxu3GU8hJ/chGas1QovhwSEEPQptP%2B%2BeEVwJH9bO6uoez6qWblrUOPW87u%2BUZnPeQV0QgrffApwU%3D");
        URLConnection urlConnection = url.openConnection();
        totalsize = urlConnection.getContentLength();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }
                    System.out.println(size * 100 / totalsize + " %");
                }
            }
        }).start();

        InputStream in = null;
        FileOutputStream fileOutputStream = null;

        byte[] bytes = new byte[1024];
        int k;
        try {
            in = url.openStream();
            fileOutputStream = new FileOutputStream(new File("/home/dheeraj/Desktop/bigVideo.mp4"));
            while ((k = in.read(bytes)) != -1) {
                size = size + k;
                fileOutputStream.write(bytes);
            }
        } finally {
            run = false;
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
