package com.example.l3_iot_20180805.services;
import retrofit2.Call;
import retrofit2.http.GET;
import com.example.l3_iot_20180805.dto.movieDto;
public interface movieService {
    @GET()
    Call<movieDto> getMovie();

}
