package com.example.utaste;

import android.app.Application;
import com.example.utaste.data.RecipeRepository;
import com.example.utaste.data.UserRepository;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize repositories at application startup
        UserRepository.init(this);
        RecipeRepository.init(this);
    }
}
