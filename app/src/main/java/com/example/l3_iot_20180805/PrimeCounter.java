package com.example.l3_iot_20180805;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.l3_iot_20180805.dto.prime;
import com.example.l3_iot_20180805.services.Typicodeservice;

import java.util.concurrent.ExecutorService;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.l3_iot_20180805.databinding.ActivityMainBinding;

public class PrimeCounter extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Typicodeservice typicodeService;
    private ExecutorService executorService;
    private Handler handler = new Handler(Looper.getMainLooper());

    private boolean isCountingUp = true;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toast.makeText(this, "Estás en el contador de números primos", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.prime_counter);

        Button descenderButton = findViewById(R.id.down_button);
        descenderButton.setOnClickListener(v -> toggleCountingDirection());

        Button pausarButton = findViewById(R.id.pause_button);
        pausarButton.setOnClickListener(v -> togglePauseAndResume());

        EditText editTextBuscarPrimo = findViewById(R.id.buscar_primo);
        Button botonBuscar = findViewById(R.id.search_button);

        botonBuscar.setOnClickListener(v -> {
            String ordenTexto = editTextBuscarPrimo.getText().toString();
            if (!ordenTexto.isEmpty()) {
                int orden = Integer.parseInt(ordenTexto);
                if (orden >= 1 && orden <= 999) {
                    currentIndex = orden - 1;
                    isCountingUp = true;
                    updateCounterState();
                    handler.removeCallbacks(updateRunnable);
                    handler.post(updateRunnable);
                } else {
                    Toast.makeText(this, "Por favor, introduce un número entre 1 y 999.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://prime-number-api.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        typicodeService = retrofit.create(Typicodeservice.class);

        AppThreads application = (AppThreads) getApplication();
        executorService = application.executorService;

        if (isConnectedToInternet()) {
            fetchPrimes();
        } else {
            Toast.makeText(this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
        }
        resetAndStartCounting();
    }

    private int currentIndex = 0;
    private prime[] primesArray;
    private final long PRIME_NUMBER_DISPLAY_INTERVAL = 1000;

    private void fetchPrimes() {
        typicodeService.getPrime().enqueue(new Callback<prime[]>() {
            @Override
            public void onResponse(Call<prime[]> call, Response<prime[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    primesArray = response.body();
                    currentIndex = isCountingUp ? 0 : primesArray.length - 1;
                    handler.post(updateRunnable);
                }
            }

            @Override
            public void onFailure(Call<prime[]> call, Throwable t) {
            }
        });

    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (primesArray != null && currentIndex >= 0 && currentIndex < primesArray.length) {
                updatePrimeNumberInView(primesArray[currentIndex].getNumber());
                currentIndex += isCountingUp ? 1 : -1;
                if (currentIndex >= primesArray.length || currentIndex < 0) {
                    currentIndex = isCountingUp ? 0 : primesArray.length - 1;
                }
            } else {
                handler.removeCallbacks(this);
            }
            if (!isPaused) {
                handler.postDelayed(this, PRIME_NUMBER_DISPLAY_INTERVAL);
            }
        }
    };

    private void updatePrimeNumberInView(int primeNumber) {
        TextView primoResultado = findViewById(R.id.primo_resultado);
        primoResultado.setText(String.valueOf(primeNumber));
    }

    private void toggleCountingDirection() {
        isCountingUp = !isCountingUp;
        updateCounterState();
    }

    private void togglePauseAndResume() {
        isPaused = !isPaused;
        updateCounterState();
    }

    private void resetAndStartCounting() {
        currentIndex = 0;
        isCountingUp = true;
        isPaused = false;
        updateCounterState();
        handler.post(updateRunnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(updateRunnable);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateRunnable);
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void updateCounterState() {
        Button descenderButton = findViewById(R.id.down_button);
        Button pausarButton = findViewById(R.id.pause_button);
        TextView mensajeTextView = findViewById(R.id.msg);

        if (isPaused) {
            pausarButton.setText("Reiniciar");
            mensajeTextView.setText("Actualmente el contador está pausado");
            handler.removeCallbacks(updateRunnable);
        } else {
            pausarButton.setText("Pausar");
            descenderButton.setText(isCountingUp ? "Descender" : "Ascender");
            mensajeTextView.setText(isCountingUp ? "Actualmente el contador está ascendiendo" : "Actualmente el contador está descendiendo");
            handler.post(updateRunnable);
        }
    }


}