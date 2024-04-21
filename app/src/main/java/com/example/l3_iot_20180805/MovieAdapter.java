package com.example.l3_iot_20180805;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.l3_iot_20180805.dto.movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<movie> movieList;
    private final LayoutInflater inflater;

    public MovieAdapter(Context context, List<movie> movieList) {
        this.inflater = LayoutInflater.from(context);
        this.movieList = movieList;
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        movie movie = movieList.get(position);
        holder.bind(movie);

        CheckBox checkBoxConfirm = holder.itemView.findViewById(R.id.checkbox);
        Button buttonReturn = holder.itemView.findViewById(R.id.return_button);

        buttonReturn.setEnabled(false);

        checkBoxConfirm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            buttonReturn.setEnabled(isChecked);
        });

        buttonReturn.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewPoster;
        private final TextView textViewTitle;
        private final TextView textViewDirector;
        private final TextView textViewActors;
        private final TextView textViewReleaseDate;
        private final TextView textViewGenre;
        private final TextView textViewWriters;
        private final TextView textViewPlot;
        private final TextView textViewIMDbRating;
        private final TextView textViewRottenTomatoesRating;
        private final TextView textViewMetacriticRating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.item_movie_poster);
            textViewTitle = itemView.findViewById(R.id.item_movie_title);
            textViewDirector = itemView.findViewById(R.id.item_movie_director);
            textViewActors = itemView.findViewById(R.id.item_movie_actors);
            textViewReleaseDate = itemView.findViewById(R.id.item_movie_release_date);
            textViewGenre = itemView.findViewById(R.id.item_movie_genre);
            textViewWriters = itemView.findViewById(R.id.item_movie_writers);
            textViewPlot = itemView.findViewById(R.id.item_movie_plot);
            textViewIMDbRating = itemView.findViewById(R.id.item_movie_rating_imdb);
            textViewRottenTomatoesRating = itemView.findViewById(R.id.item_movie_rating_rotten_tomatoes);
            textViewMetacriticRating = itemView.findViewById(R.id.item_movie_rating_metacritic);

        }
        public void bind(movie movie) {
            textViewTitle.setText(movie.getTitle());
            textViewDirector.setText("Director: " + movie.getDirector());
            textViewActors.setText("Actores: " + movie.getActors());
            textViewReleaseDate.setText("Fecha de Estreno: " + movie.getReleased());
            textViewGenre.setText("Género: " + movie.getGenre());
            textViewWriters.setText("Escritores: " + movie.getWriter());
            textViewPlot.setText(movie.getPlot());

            if (movie.getRatings() != null && !movie.getRatings().isEmpty()) {
                for (movie.RatingDTO rating : movie.getRatings()) {
                    String source = rating.getSource();
                    String value = rating.getValue();

                    switch (source) {
                        case "Internet Movie Database":
                            textViewIMDbRating.setText("IMDb Rating: " + value);
                            break;
                        case "Rotten Tomatoes":
                            textViewRottenTomatoesRating.setText("Rotten Tomatoes Rating: " + value);
                            break;
                        case "Metacritic":
                            textViewMetacriticRating.setText("Metacritic Rating: " + value);
                            break;
                    }
                }
            }
            else {
                textViewIMDbRating.setText("IMDb Rating: N/A");
                textViewRottenTomatoesRating.setText("Rotten Tomatoes Rating: N/A");
                textViewMetacriticRating.setText("Metacritic Rating: N/A");
            }
        }
    }
}
