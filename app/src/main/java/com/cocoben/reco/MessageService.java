package com.cocoben.reco;

import android.util.Log;

import com.huawei.hms.push.HmsMessageService;

public class MessageService extends HmsMessageService {

    private static final String TAG = "HMS Message Service";

    @Override
    public void onNewToken(String token) {
        Log.i(TAG, "on new token" + token);
        super.onNewToken(token);
    }
}
