package com.example.eliasundkyanh.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eliasundkyanh.R;
import com.example.eliasundkyanh.adapter.TodoAdapter;
import com.example.eliasundkyanh.model.TodoDatabase;
import com.example.eliasundkyanh.model.TodoItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodoFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<TodoItem> todoList;
    private TodoAdapter adapter;

    private FirebaseFirestore firestore;

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate das Layout für das Fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        // RecyclerView initialisieren
        recyclerView = view.findViewById(R.id.recycler_view_todo);
        if (recyclerView != null) {
            Log.d("TodoFragment", "RecyclerView erfolgreich gefunden.");
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            Log.e("TodoFragment", "RecyclerView ist NULL. Überprüfe die ID in fragment_todo.xml.");
        }

        // Firestore initialisieren
        firestore = FirebaseFirestore.getInstance();

        // Liste und Adapter initialisieren
        todoList = new ArrayList<>();
        adapter = new TodoAdapter(todoList);
        recyclerView.setAdapter(adapter);

        // Daten aus Firestore laden
        loadTodosFromFirestore();

        // Floating Action Button für neue Aufgaben
        FloatingActionButton fab = view.findViewById(R.id.fab_add_todo);
        if (fab != null) {
            Log.d("TodoFragment", "FloatingActionButton erfolgreich gefunden.");
            fab.setOnClickListener(v -> {
                Log.d("TodoFragment", "FloatingActionButton wurde geklickt.");
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_todoFragment_to_addTodoFragment);
            });
        } else {
            Log.e("TodoFragment", "FloatingActionButton ist NULL. Überprüfe die ID in fragment_todo.xml.");
        }
        return view;
    }


    private void loadTodosFromFirestore() {
        firestore.collection("todos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        todoList.clear(); // Clear previous entries
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("FirestoreTest", "Dokument-ID: " + document.getId());

                            // Überprüfung von deadline
                            if (document.contains("deadline")) {
                                Log.d("FirestoreTest", "Deadline: " + document.getTimestamp("deadline"));
                            } else {
                                Log.w("FirestoreTest", "Deadline fehlt im Dokument: " + document.getId());
                            }

                            // Rest des Codes für das Hinzufügen in die Liste
                            TodoItem todo = new TodoItem();
                            todo.setTitle(document.getString("title"));
                            todo.setDescription(document.getString("description"));
                            todo.setPriority(document.getString("priority"));
                            todo.setCategory(document.getString("category"));

                            // Convert deadline
                            com.google.firebase.Timestamp timestamp = document.getTimestamp("deadline");
                            if (timestamp != null) {
                                Date date = timestamp.toDate();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                todo.setDeadline(formatter.format(date));
                            } else {
                                todo.setDeadline(null); // Handle null or missing deadline
                            }

                            todoList.add(todo);
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreTest", "Fehler beim Abrufen: " + task.getException().getMessage());
                    }
                });
    }

    private void showAddTodoDialog() {
        // Dialog anzeigen, um eine neue Aufgabe hinzuzufügen
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Neue Aufgabe hinzufügen");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_todo, null);
        builder.setView(dialogView);

        EditText inputTitle = dialogView.findViewById(R.id.input_todo_title);
        EditText inputDescription = dialogView.findViewById(R.id.input_todo_description);
        EditText inputRepetitions = dialogView.findViewById(R.id.input_repetitions);
        Spinner spinnerPriority = dialogView.findViewById(R.id.spinner_priority);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        Button btnDatePicker = dialogView.findViewById(R.id.btn_date_picker);
        Button btnTimePicker = dialogView.findViewById(R.id.btn_time_picker);

        // Calendar instance for date and time
        Calendar calendar = Calendar.getInstance();

        // Date selection button
        btnDatePicker.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Time selection button
        btnTimePicker.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (view, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true // Use 24-hour format
            );
            timePickerDialog.show();
        });

        // Handle positive button click
        builder.setPositiveButton("Hinzufügen", (dialog, which) -> {
            String title = inputTitle.getText().toString();
            String description = inputDescription.getText().toString();
            String repetitionsText = inputRepetitions.getText().toString();
            String priority = spinnerPriority.getSelectedItem().toString();
            String category = spinnerCategory.getSelectedItem().toString();
            String deadline = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.getTime());

            Log.d("AddTodoDialog", "Eingegebene Daten: " +
                    "Title: " + title +
                    ", Description: " + description +
                    ", Priority: " + priority +
                    ", Category: " + category +
                    ", Deadline: " + deadline +
                    ", Repetitions: " + repetitionsText);

            if (!title.isEmpty() && !description.isEmpty()) {
                int repetitions = repetitionsText.isEmpty() ? 0 : Integer.parseInt(repetitionsText);

                // Create a new TodoItem
                TodoItem newTodo = new TodoItem();
                newTodo.setTitle(title);
                newTodo.setDescription(description);
                newTodo.setPriority(priority);
                newTodo.setCategory(category);
                newTodo.setDeadline(deadline);
                newTodo.setRepetition(repetitions);

                // Save the new TodoItem to Firestore
                saveTodoToFirestore(newTodo);
            } else {
                Toast.makeText(getContext(), "Bitte alle Felder ausfüllen!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Abbrechen", (dialog, which) -> {
            dialog.dismiss();
            Log.d("AddTodoDialog", "Dialog abgebrochen.");

        });

        builder.create().show();
    }

    private void saveTodoToFirestore(TodoItem todo) {
        // Calendar für Datum und Uhrzeit
        Calendar calendar = Calendar.getInstance();

        // Aufgabe als Map speichern
        Map<String, Object> todoData = new HashMap<>();
        todoData.put("title", todo.getTitle());
        todoData.put("description", todo.getDescription());
        todoData.put("priority", todo.getPriority());
        todoData.put("category", todo.getCategory());
        todoData.put("deadline", calendar.getTime()); // Hier wird das Datum gespeichert
        todoData.put("repetitions", todo.getRepetition());

        firestore.collection("todos")
                .add(todoData)
                .addOnSuccessListener(documentReference -> {
                    todoList.add(todo); // Neue Aufgabe zur Liste hinzufügen
                    adapter.notifyItemInserted(todoList.size() - 1); // UI aktualisieren
                    Toast.makeText(getContext(), "Aufgabe hinzugefügt!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Fehler beim Speichern: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
