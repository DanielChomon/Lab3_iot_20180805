package com.example.l3_iot_20180805;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.l3_iot_20180805.dto.movie;
import com.example.l3_iot_20180805.services.OmdbApiService;

public class MainActivity extends AppCompatActivity {


    private EditText editTextMovieId;

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final OmdbApiService omdbApiService = retrofit.create(OmdbApiService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        checkInternetConnection();
        Toast.makeText(this, "Estás en la pantalla principal", Toast.LENGTH_SHORT).show();

        Button btnVisualizar = findViewById(R.id.button1);
        btnVisualizar.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, PrimeCounter.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextMovieId = findViewById(R.id.idPelicula);
        Button btnBuscar = findViewById(R.id.button2);

        btnBuscar.setOnClickListener(view -> {
            String movieId = editTextMovieId.getText().toString();
            getMovie(movieId);
        });

    }
    private void getMovie(String imdbId) {
        if (imdbId.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce un ID de película.", Toast.LENGTH_SHORT).show();
            return;
        }

        omdbApiService.getMovieDetails(imdbId, OmdbApiService.API_KEY).enqueue(new Callback<movie>() {
            @Override
            public void onResponse(@NonNull Call<movie> call,@NonNull Response<movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movie movie = response.body();

                    Intent intent = new Intent(MainActivity.this, MovieObtainer.class);
                    intent.putExtra("EXTRA_MOVIE_DETAILS", (CharSequence) movie);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Código incorrecto o película no encontrada", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<movie> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error en la conexión o al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            Toast.makeText(this, "Conexión a Internet disponible", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No hay conexión a Internet", Toast.LENGTH_LONG).show();
        }
    }

}