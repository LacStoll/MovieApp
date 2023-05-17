package com.example.movieapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;




// Class used to manage a watch list of movies
public class WatchList extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // This service does not support binding, so return null
        return null;
    }

    // Declare variables
    private static WatchList instance;
    private ArrayList<String> watchList = new ArrayList<>();

    // Private constructor to prevent instantiation of the class from outside
    private WatchList() {}

    // Singleton pattern to ensure only one instance of the class is created
    public static WatchList getInstance() {
        if (instance == null) {
            instance = new WatchList();
        }
        return instance;
    }

    // Method to return the watch list
    public List<String> showWatchList() {
        return watchList;
    }

    // Method to add a movie to the watch list
    public void addMovieToWatchList(String title) {
        // Create a new Movie object with the given title
        //Movie movie = new Movie(title);

        // Add the movie to the watch list
        watchList.add(title);
    }

    // Method to get the watch list
    public ArrayList<String> getWatchList() {
        return watchList;
    }

    // Method to check if a movie is in the watch list
    // TODO: Implement this method, e.g. by iterating over the watch list and comparing titles
    public boolean isInWatchList(String title) {
        // TODO: Implement this method
        return false;
    }
}