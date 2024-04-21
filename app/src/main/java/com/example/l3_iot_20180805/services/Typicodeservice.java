package com.example.l3_iot_20180805.services;

import com.example.l3_iot_20180805.dto.prime;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Typicodeservice {

    @GET("primeNumbers?len=999&order=1")
    Call<prime[]> getPrime();
}
