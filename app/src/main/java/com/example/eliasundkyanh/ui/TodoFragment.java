package com.example.eliasundkyanh.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eliasundkyanh.R;
import com.example.eliasundkyanh.adapter.TodoAdapter;
import com.example.eliasundkyanh.model.TodoItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TodoFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<TodoItem> todoList;
    private TodoAdapter adapter;

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflatet das fragment_todo layout
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        // RecyclerView initalisieren + LayOutManager fuer vertikale Liste
        recyclerView = view.findViewById(R.id.recycler_view_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dummy-Daten vorbereiten
        todoList = new ArrayList<>();
        todoList.add(new TodoItem("Einkaufen", "Milch, Brot, Eier"));
        todoList.add(new TodoItem("Sport", "30 Minuten Joggen"));
        todoList.add(new TodoItem("Projekt abschließen", "Navigation implementieren"));

        // Adapter initalisieren
        adapter = new TodoAdapter(todoList);
        recyclerView.setAdapter(adapter);

        // Floating Action Button initialisieren
        FloatingActionButton fab = view.findViewById(R.id.fab_add_todo);
        fab.setOnClickListener(v -> {
            // Neue Dummy Aufgabe hinzufuegen
            todoList.add(new TodoItem("Neue Aufgabe", "Beschreibung hinzufügen"));
            adapter.notifyItemInserted(todoList.size() - 1);

            // Feedback für den Benutzer
            Toast.makeText(getContext(), "Neue Aufgabe wurde hinzugeügt.", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
