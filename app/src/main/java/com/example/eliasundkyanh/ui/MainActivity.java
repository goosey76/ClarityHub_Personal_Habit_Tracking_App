package com.example.eliasundkyanh.ui;

import android.os.Bundle;
import android.view.View;

import com.example.eliasundkyanh.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase-Initalisierung
        FirebaseApp.initializeApp(this);


        setContentView(R.layout.activity_main);

        // Bottom Navigation Setup
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Verknüpfen der Navigation mit der Bottom Navigation View
        NavigationUI.setupWithNavController(bottomNav, navController);

        // FloatingActionButton Setup
        FloatingActionButton fabAddTodo = findViewById(R.id.fab_add_todo);

        // Sichtbarkeit des FAB basierend auf dem aktuellen Ziel
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.todoFragment) {
                fabAddTodo.setVisibility(View.VISIBLE); // FAB sichtbar auf der Todo-Seite
            } else {
                fabAddTodo.setVisibility(View.GONE); // FAB ausblenden auf anderen Seiten
            }
        });
        // Navigieren zu AddTodoFragment beim Klicken auf den FAB
        fabAddTodo.setOnClickListener(view -> navController.navigate(R.id.addTodoFragment));
    }


}