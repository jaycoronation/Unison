package com.unisonpharmaceuticals.service;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.unisonpharmaceuticals.classes.SessionManager;

public class FireBaseInstanceIDService extends FireBaseMessagingService {
    private static final String TAG = "MyFirebaseIIDService";


    @Override
    public void onNewToken(String fcmToken) {
        super.onNewToken(fcmToken);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.saveTokenId(fcmToken);
        Log.d(TAG, "Refreshed token: "+fcmToken);
    }

}