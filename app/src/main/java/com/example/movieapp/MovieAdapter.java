package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private OnMovieClickListener onMovieClickListener;

    private OnWatchListClickListener OnWatchListClickListener;


    // Interface for watchlist button click listener
    public interface OnWatchListClickListener {
        void onWatchlistClicked(Movie movie);
    }

    // Setter for movie click listener
    public void setOnMovieClickListener(OnMovieClickListener onMovieClickListener) {
        this.onMovieClickListener = onMovieClickListener;
    }

    // Setter for movie list
    public void setMovies(List<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }

    // Constructor for MovieAdapter
    public MovieAdapter(Context context, List<Movie> movieList, OnMovieClickListener onMovieClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.onMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the movie_item layout
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // Get the movie at the specified position
        Movie movie = movieList.get(position);

        // Set the movie data to the views in the ViewHolder
        holder.titleTextView.setText(movie.getTitle());
        holder.releaseDateTextView.setText(movie.getReleaseDate());
        holder.overviewTextView.setText(movie.getOverview());

        // Load the movie poster image using Picasso library
        String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Picasso.get().load(posterUrl).into(holder.posterImageView);

        // Set the click listener for the entire item view
        holder.itemView.setOnClickListener(v -> {
            if (onMovieClickListener != null) {
                onMovieClickListener.onMovieClick(movie); // Call onMovieClick method on listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // ViewHolder class for movie items
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImageView;
        public TextView titleTextView;
        public TextView releaseDateTextView;
        public TextView overviewTextView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.moviePoster);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            releaseDateTextView = itemView.findViewById(R.id.movieReleaseDate);
            overviewTextView = itemView.findViewById(R.id.movieOverview);
        }
    }

    // Interface for movie item click listener
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    // Interface for watchlist button click listener
    public interface WatchListClickListener {
        void onWatchlistClicked(Movie movie);
    }
}

