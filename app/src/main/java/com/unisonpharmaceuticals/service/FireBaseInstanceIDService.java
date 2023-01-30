package com.unisonpharmaceuticals.service;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.unisonpharmaceuticals.classes.SessionManager;

public class FireBaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh()
    {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.saveTokenId(refreshedToken);
        Log.d(TAG, "Refreshed token: "+refreshedToken);
    }
}