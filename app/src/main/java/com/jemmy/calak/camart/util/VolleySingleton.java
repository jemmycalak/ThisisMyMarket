package com.jemmy.calak.camart.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Created by Jemmy Calak on 5/12/2017.
 */

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleySingleton(Context context1){
        context = context1;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());

        }
        return requestQueue;
    }

    public static synchronized VolleySingleton getmInstance(Context context1){
        if(mInstance==null){
            mInstance = new VolleySingleton(context1);
        }
        return mInstance;
    }

    public<T> void addToRequestque(Request<T> request){
        requestQueue.add(request);
    }
}
