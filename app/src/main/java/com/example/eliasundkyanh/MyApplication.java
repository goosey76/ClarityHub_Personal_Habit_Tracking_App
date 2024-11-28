package com.example.eliasundkyanh;

import android.app.Application;
import androidx.room.Room;
import com.example.eliasundkyanh.model.TodoDatabase;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    private static TodoDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        // Firebase initialisieren
        FirebaseApp.initializeApp(this);

        // Room-Datenbank initialisieren
        database = Room.databaseBuilder(getApplicationContext(),
                TodoDatabase.class, "todo_database").build();
    }

    public static TodoDatabase getDatabase() {
        return database;
    }
}
