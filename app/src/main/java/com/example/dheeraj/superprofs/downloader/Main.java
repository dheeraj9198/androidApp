package com.example.dheeraj.superprofs.downloader;

import com.example.dheeraj.superprofs.models.MPDModels.AdaptationSetBase;
import com.example.dheeraj.superprofs.models.MPDModels.MPDParser;
import com.example.dheeraj.superprofs.models.MPDModels.SBase;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.net.SocketException;
import java.net.URL;

public class Main {

    private static void encrypt(String src,String dst)throws IOException{
        final byte[] buf = new byte[1024];
        byte test;
        final InputStream is = new FileInputStream(src);
        final OutputStream os = new FileOutputStream(dst);
        while (true) {
            int n = is.read(buf);
            if (n == -1) break;
            for(int k =0;k<n;k++) {
                test = buf[k];
                if(test != -128) {
                    buf[k] = (byte)~test;
                }
            }
            os.write(buf, 0, n);
        }
        os.close();
        is.close();
    }
    
    private static void deleteFile(String src){
        //TODO remove this return
        if(true)return;
        File file = new File(src);
        if(file.delete()){
            System.out.println("file deleted successfully : "+file);
        }else{
            System.err.println("unable to delete file : " + file);
        }
        
    }
    
    
    private static void downloadFile(URL url,File file){
        try {
            if(file.exists()){
                System.out.println("skipping file : " + file);
                return;
            }
            System.out.println("downloading file : " + file);
            FileUtils.copyURLToFile(url, file);
        }catch (SocketException e){
            System.err.println("-----------------------------------------------");
            e.printStackTrace();
        }catch (IOException e){
            System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
            e.printStackTrace();
        }
    }
    
    public static int PRETTY_PRINT_INDENT_FACTOR = 4;
    public static String folder = /*"/home/dheeraj/Desktop/offlineContent/"*/"/home/ubuntu/dash/";

    public static void main(String[] args) throws IOException {

        File file = new File(folder+"manifestE.mpd");
        if(!file.exists()) {
            downloadFile(new URL("http://54.86.202.143:1935/vod_android/mp4:sample.mp4/manifest.mpd"), new File(folder + "manifest.mpd"));
        }else{
            System.out.println("manifest already exists , decrypting and using the same");
            encrypt(folder + "manifestE.mpd",folder + "manifest.mpd");
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(folder + "manifest.mpd")));
        StringBuilder stringBuilder = new StringBuilder();
        String test;
        while ((test = bufferedReader.readLine()) != null) {
            stringBuilder.append(test);
        }
        test = stringBuilder.toString();
        bufferedReader.close();
        
        encrypt(folder + "manifest.mpd",folder + "manifestE.mpd");
        deleteFile(folder+"manifest.mpd");
        
        //System.out.println(test);
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(test);
            String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
            //System.out.println(jsonPrettyPrintString);
            MPDParser mpdParser = JsonHandler.parse(jsonPrettyPrintString, MPDParser.class);
            //System.out.println(JsonHandler.stringify(mpdParser));
            String location = mpdParser.getMPD().getLocation();
            location = location.substring(0, location.lastIndexOf("/")) + "/";
            //System.out.println(location);
            /**
             * test was created using new by string builder
             * remove reference to release it
             */
            test = null;

            for (AdaptationSetBase adaptationSetBase : mpdParser.getMPD().getPeriod().getAdaptationSet()) {
                String repId = adaptationSetBase.getRepresentation().getId();
                String init = adaptationSetBase.getSegmentTemplate().getInitialization();
                //"initialization": "chunk_ctvideo_cfm4s_rid$RepresentationID$_cinit_w664894557_mpd.m4s",
                init = init.replace("$RepresentationID$", repId);
               downloadFile(new URL(location+init), new File(folder + init));

                //"media": "chunk_ctvideo_cfm4s_rid$RepresentationID$_cs$Time$_w664894557_mpd.m4s",
                String media = adaptationSetBase.getSegmentTemplate().getMedia();
                media = media.replace("$RepresentationID$",repId);
                
                long time = 0;
                boolean firstGone = false;
                for(SBase s :adaptationSetBase.getSegmentTemplate().getSegmentTimeline().getS()){
                    if(!firstGone){
                        firstGone = true;
                        String mediaFinal  = media.replace("$Time$",time+"");
                        downloadFile(new URL(location+mediaFinal), new File(folder + mediaFinal));
                    }
                    time = time + Long.parseLong(s.getD());
                    String mediaFinal  = media.replace("$Time$",time+"");
                    downloadFile(new URL(location+mediaFinal), new File(folder + mediaFinal));
                }
            }

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}