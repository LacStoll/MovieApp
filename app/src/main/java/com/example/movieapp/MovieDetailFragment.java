package com.example.movieapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailFragment extends BottomSheetDialogFragment {

    // Declare variables
    private static final String ARG_MOVIE_ID = "movie_id";
    private String title;

    private Integer movieId;
    private TextView titleTextView, releaseDateTextView, overviewTextView, homePageTextView,movieRatingTextView;
    private ImageView posterImageView;
    private ProgressBar progressBar;
    private Button addToWatchListButton;

    // Create instance of fragment with movie ID as argument
    public static MovieDetailFragment newInstance(int movieId) {
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // Get movie ID from arguments bundle
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getInt(ARG_MOVIE_ID);
        }
    }

    // Create view for bottom sheet dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        // Find views
        titleTextView = view.findViewById(R.id.movieTitle);
        releaseDateTextView = view.findViewById(R.id.movieReleaseDate);
        overviewTextView = view.findViewById(R.id.movieOverview);
        movieRatingTextView = view.findViewById(R.id.movieRating);
        homePageTextView = view.findViewById(R.id.movieHomepage);
        posterImageView = view.findViewById(R.id.moviePoster);
        progressBar = view.findViewById(R.id.progressBar);

        // Make API call to get movie details
        String url = "https://api.themoviedb.org/3/movie/" + movieId +
                "?api_key=5134b3f56c2cae575bb0ad435f0be5ee&language=en-US";

        // Log object URL
        Log.d(TAG,"This is the object URL: " +url);

        // Get movie details from API response
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);

                    Log.d(TAG,"Response: " + response);

                    // Parse movie details from response
                    String title = response.optString("title");
                    String releaseDate = response.optString("release_date");
                    String overview = response.optString("overview");
                    String movieRating = response.optString("vote_average");

                    // Format movie rating to one decimal place
                    double convertRatingToDouble = Double.parseDouble(movieRating);
                    String formattedMovieRating = String.format("%.1f",convertRatingToDouble);

                    String homepage = response.optString("homepage");
                    Log.d(TAG,homepage);
                    String posterPath = response.optString("poster_path");

                    // Set movie details to views
                    titleTextView.setText("Title: " +title);
                    releaseDateTextView.setText("Release Date: " +releaseDate);
                    overviewTextView.setText("Synopsis: " +overview);
                    movieRatingTextView.setText("Rating: " + formattedMovieRating + " /10");
                    homePageTextView.setText("Website: " +homepage);

                    // Load movie poster using Picasso
                    String posterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
                    Picasso.get().load(posterUrl).into(posterImageView);

                    // Get reference to add to watch list button
                    addToWatchListButton = view.findViewById(R.id.addToWatchListButton);

                    // Add movie to watch list on button click
                    addToWatchListButton.setOnClickListener(v -> {

                        // Get current watch list
                        List<String> watchList = WatchList.getInstance().getWatchList();

                        // Check if movie is already in watch list
                        if(watchList.contains(title)){

                            // Show a toast indicating that the movie is already in the watch list
                            Toast.makeText(getContext(), "Movie is Already in the Watch List:" + title, Toast.LENGTH_SHORT).show();

                        } else {

                            // Add the movie to the watch list
                            WatchList.getInstance().addMovieToWatchList(title);

                            // Show a toast indicating that the movie has been added to the watch list
                            Toast.makeText(getContext(), "Added to Watch List:" + title, Toast.LENGTH_SHORT).show();

                        }
                    });
                },
                error -> Log.e(TAG, "Error occurred while getting movie details: " + error.getMessage()));

        // Add request to Volley request queue
        VolleySingleton.getInstance(requireContext()).addToRequestQueue(jsonObjectRequest);

        return view;
    }

}