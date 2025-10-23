package com.example.utaste;

import android.app.Application;
import com.example.utaste.data.UserRepository;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialise le repository SQLite au démarrage de l'application
        UserRepository.init(this);
    }
}
