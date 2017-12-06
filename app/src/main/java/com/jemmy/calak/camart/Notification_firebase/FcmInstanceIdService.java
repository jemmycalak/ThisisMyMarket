package com.example.jemmycalak.thisismymarket.Notification_firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


//class ini untuk meminta token ke Firebase dan akan di save di shared preferences
public class FcmInstanceIdService extends FirebaseInstanceIdService {

    private static final String FCM_PREF="fcm_pref";
    private static final String FCM_TOKEN="fcm_token";
    private final String TAG = "TAG";
    @Override
    public void onTokenRefresh() {

        //untuk membuat token baru tanpa buat class ini pun bisa
        String recent_token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "TokenFirebase====>"+recent_token);
//        SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences(FCM_PREF, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(FCM_TOKEN , recent_token);
//        editor.commit();
    }
}
