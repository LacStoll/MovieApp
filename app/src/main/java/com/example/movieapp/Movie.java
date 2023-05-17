package com.example.movieapp;

import com.google.gson.annotations.SerializedName;

public class Movie {

    // Define private member variables for the movie's id, title, overview, poster path, and release date.
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    // Define public getter and setter methods for each member variable.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    // Define a constructor that takes the movie's title as a parameter and sets the title member variable.
    public Movie(String title) {
        this.title = title;
    }

    // The @SerializedName annotation is used to map JSON keys to the member variables.
    // This is used when deserializing JSON responses from an API into Java objects using a library like GSON.
}