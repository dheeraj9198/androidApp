package com.example.dheeraj.superprofs.utils;

import android.util.Log;

import org.boon.Str;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

/**
 * Created by dheeraj on 19/2/15.
 */
public class BoonJsonHandler {
    private static final String TAG = BoonJsonHandler.class.getSimpleName();

    private BoonJsonHandler() {

    }

    private static final ObjectMapper mapper = JsonFactory.create();

    public static final <T> T parseToBaseResponse(String jsonString, Class<T> classOfT) {

        try {
            if (jsonString == null) {
                return null;
            }
            return mapper.readValue(jsonString, classOfT);
        } catch (Exception e) {
            Log.e(TAG, "IOException Failed to parse json correctly. ", e);
            return null;
        }

    }
}
