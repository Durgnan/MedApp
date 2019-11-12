package com.jce.medapp;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("fRq67Y1p5bjmDl9DhVo0aonbENr62pnCJQ7oMHz8")
                .clientKey("Lbon3mN4ICocEkT0375DYGfxtAXnivM07jqTnTZB")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
