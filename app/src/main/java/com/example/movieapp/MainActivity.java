package com.example.movieapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    // Define constants for API URL and API key
    private static final String API_KEY = "4b8b027096dc305e46c6e8610b49703d";
    private static final String API_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;

    // Define variables for views and adapters
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private EditText searchEditText;
    private Button searchButton;
    private Button showWatchListButton;

    // Define lists to store movies and watch list
    private List<Movie> movieList = new ArrayList<>();
    private List<Movie> watchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views and adapters
        showWatchListButton = findViewById(R.id.showWatchListButton);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progress_bar);
        movieAdapter = new MovieAdapter(this, movieList, this);
        recyclerView.setAdapter(movieAdapter);

        // Fetch top rated movies from API
        fetchMovies();

        // Set click listener for the "Show Watch List" button
        showWatchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the watch list from the WatchList singleton
                WatchList watchListService = WatchList.getInstance();
                List<String> watchList = watchListService.getWatchList();

                // Check if the watch list is empty and show a toast message if it is
                if (watchList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Watch list is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a string representation of the watch list and show it in a toast message
                StringBuilder sb = new StringBuilder();
                for (String movie : watchList) {
                    sb.append(movie);
                    sb.append("\n");
                }
                Toast.makeText(getApplicationContext(), "Watch List:\n" + sb.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchButtonClick();
            }
        });
    }

    // Fetch top rated movies from API using Volley library
    private void fetchMovies() {
        // Show progress bar while fetching movies
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                response -> {
                    try {
                        // Use Gson library to parse JSON response
                        Gson gson = new Gson();
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject movieObject = results.getJSONObject(i);
                            // Convert JSON object to Movie object using Gson
                            Movie movie = gson.fromJson(movieObject.toString(), Movie.class);
                            // Add movie to movie list
                            movieList.add(movie);

                            // Log movie details for debugging purposes
                            Log.d(TAG, "Title: " + movie.getTitle());
                            Log.d(TAG, "Release date: " + movie.getReleaseDate());
                            Log.d(TAG, "Overview: " + movie.getOverview());
                            Log.d(TAG, "Poster path: " + movie.getPosterPath());
                            Log.d(TAG, "ID: " + movie.getId());

                            // Notify adapter that data set has changed
                            movieAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        // Log error message if JSON parsing fails
                        Log.e(TAG, "JSON error: " + e.getMessage());
                    }
                    // Hide progress bar after fetching movies
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    // Log error message if Volley request fails
                    Log.e(TAG, "Volley error: " + error.getMessage());
                    // Hide progress bar after fetching movies
                    progressBar.setVisibility(View.GONE);
                }
        );
        // Add request to Volley request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    // Handle click event when a movie item is clicked in the recycler view
    @Override
    public void onMovieClick(Movie movie) {
        // Create a new instance of the MovieDetailFragment and show it
        MovieDetailFragment bottomSheetDialogFragment = MovieDetailFragment.newInstance(movie.getId());
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    // Handle click event when the search button is clicked
    private void handleSearchButtonClick() {
        // Get search query from search edit text view
        String query = searchEditText.getText().toString();
        // Show error message if search query is empty
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
            return;
        }
        // Construct search API URL with search query and API key
        String url = "https://api.themoviedb.org/3/search/movie?api_key=5134b3f56c2cae575bb0ad435f0be5ee&query=" + query;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Movie> newMovies = new ArrayList<>();
                        try {
                            Gson gson = new Gson();
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject movieObject = results.getJSONObject(i);
                                // Convert JSON object to Movie object using Gson
                                Movie movie = gson.fromJson(movieObject.toString(), Movie.class);
                                // Add movie to movie list
                                movieList.add(movie);

                                // Log movie details for debugging purposes
                                Log.d(TAG, "Title: " + movie.getTitle());
                                Log.d(TAG, "Release date: " + movie.getReleaseDate());
                                Log.d(TAG, "Overview: " + movie.getOverview());
                                Log.d(TAG, "Poster path: " + movie.getPosterPath());
                                Log.d(TAG, "ID: " + movie.getId());

                                // Add movie to new movies list
                                newMovies.add(movie);
                                // Set new movies list in adapter and notify data set has changed
                                movieAdapter.setMovies(newMovies);
                                movieAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            // Show error message if JSON parsing fails
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Show error message if Volley request fails
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error fetching movies", Toast.LENGTH_SHORT).show();
                    }
                });
        // Add request to Volley request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    // Setter method for movie list
    public void setMovies(List<Movie> movies) {
        this.movieList = movies;
    }
}