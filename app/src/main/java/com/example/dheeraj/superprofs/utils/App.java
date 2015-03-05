package com.example.dheeraj.superprofs.utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by dheeraj on 5/3/15.
 */
public class App {
    public static final String manifestFileNameUnencrypted = "manifest.mpd";
    public static final String manifestFileNameEncrypted = "manifestE.mpd";
    public static final String lectureFolderName = "lectures"; 
    
    private App(){
    }
    
    public static String getLectureFolderName(String lectureID){
        String name =  Device.getDir()+ File.separator+lectureFolderName+File.separator+lectureID;
        makeDirIfNotExists(name);
        return name;
    }
    
    private static void makeDirIfNotExists(String file){
        File file1 = new File(file);
        if(!file1.exists()){
            file1.mkdirs();
        }
    }
    
}
