package com.example.l3_iot_20180805.services;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.l3_iot_20180805.dto.movie;

public interface OmdbApiService {
    // Define tu API key aquí
    String API_KEY = "bf81d461";

    @GET("/")
    Call<movie> getMovieDetails(@Query("i") String imdbId, @Query("apikey") String apiKey);
}