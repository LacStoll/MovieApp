package com.example.movieapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

// Singleton class for creating a RequestQueue instance with Volley library
public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    // Constructor for VolleySingleton, which initializes the RequestQueue
    private VolleySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    // Method to get an instance of VolleySingleton, creating a new one if necessary
    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    // Method to get the RequestQueue instance, creating a new one if necessary
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    // Method to add a Request to the RequestQueue
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}