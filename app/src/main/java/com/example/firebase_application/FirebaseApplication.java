package com.example.firebase_application;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicializa o Firebase
        FirebaseApp.initializeApp(this);

       // Habilita a persistência dos dados offline
       FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}