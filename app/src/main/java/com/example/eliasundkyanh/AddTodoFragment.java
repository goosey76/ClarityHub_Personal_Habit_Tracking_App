package com.example.eliasundkyanh;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddTodoFragment extends Fragment {

    private EditText inputTitle, inputDescription;
    private Spinner spinnerImportance, spinnerCategory, spinnerRepetition;
    private Button btnDatePicker, btnTimePicker, btnAddTodo;
    private Timestamp selectedTimestamp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_todo, container, false);

        // UI-Komponenten verbinden
        inputTitle = view.findViewById(R.id.input_todo_title);
        inputDescription = view.findViewById(R.id.input_todo_description);
        spinnerImportance = view.findViewById(R.id.spinner_importance);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        spinnerRepetition = view.findViewById(R.id.spinner_repetition);
        btnDatePicker = view.findViewById(R.id.btn_date_picker);
        btnTimePicker = view.findViewById(R.id.btn_time_picker);
        btnAddTodo = view.findViewById(R.id.btn_add_todo);

        // Spinner-Daten initialisieren
        setupSpinners();

        // Datumsauswahl
        btnDatePicker.setOnClickListener(v -> showDatePickerDialog());
        btnTimePicker.setOnClickListener(v -> showTimePickerDialog());

        // Aufgabe hinzufügen
        btnAddTodo.setOnClickListener(v -> saveTodoToFirestore());

        return view;
    }

    private void setupSpinners() {
        // Beispielwerte für die Spinner
        String[] importanceLevels = {"Niedrig", "Mittel", "Hoch"};
        String[] categories = {"Arbeit", "Persönlich", "Studium"};
        String[] repetitions = {"Keine", "Täglich", "Wöchentlich", "Monatlich"};

        ArrayAdapter<String> adapterImportance = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, importanceLevels);
        adapterImportance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerImportance.setAdapter(adapterImportance);

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);

        ArrayAdapter<String> adapterRepetition = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, repetitions);
        adapterRepetition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepetition.setAdapter(adapterRepetition);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedTimestamp = new Timestamp(calendar.getTime());
                    btnDatePicker.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    selectedTimestamp = new Timestamp(calendar.getTime());
                    btnTimePicker.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void saveTodoToFirestore() {
        String title = inputTitle.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();
        String importance = spinnerImportance.getSelectedItem().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String repetition = spinnerRepetition.getSelectedItem().toString();

        if (title.isEmpty() || description.isEmpty() || selectedTimestamp == null) {
            Toast.makeText(getContext(), "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
            return;
        }

        // Daten vorbereiten
        Map<String, Object> todo = new HashMap<>();
        todo.put("title", title);
        todo.put("description", description);
        todo.put("importance", importance);
        todo.put("category", category);
        todo.put("repetition", repetition);
        todo.put("deadline", selectedTimestamp);

        // In Firestore speichern
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("todos")
                .add(todo)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Aufgabe hinzugefügt!", Toast.LENGTH_SHORT).show();
                    resetForm();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Fehler: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void resetForm() {
        inputTitle.setText("");
        inputDescription.setText("");
        spinnerImportance.setSelection(0);
        spinnerCategory.setSelection(0);
        spinnerRepetition.setSelection(0);
        btnDatePicker.setText("Datum wählen");
        btnTimePicker.setText("Zeit wählen");
        selectedTimestamp = null;
    }
}
