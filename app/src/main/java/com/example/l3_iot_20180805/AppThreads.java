package com.example.l3_iot_20180805;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppThreads extends Application {
    public ExecutorService executorService = Executors.newFixedThreadPool(5);
}

