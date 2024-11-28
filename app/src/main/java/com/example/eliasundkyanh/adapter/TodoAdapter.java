package com.example.eliasundkyanh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eliasundkyanh.R;
import com.example.eliasundkyanh.model.TodoItem;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<TodoItem> todoList;

    // Konstruktor für Adapter
    public TodoAdapter(List<TodoItem> todoList) {
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Layout für einzelne TODO-Einträge laden
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        // aktuelle Position abrufen
        TodoItem todoItem = todoList.get(position);

        if (todoItem != null) {
            // Daten werden aus dem TodoItemObjekt zugewiesen
            holder.title.setText(todoItem.getTitle());
            holder.deadline.setText("Zeitraum: " + todoItem.getDeadline());
            holder.priority.setText("Wichtigkeit: " + todoItem.getPriority());
            holder.category.setText("Kategorie: " + todoItem.getCategory());
            holder.repetitions.setText("Wiederholung: " + todoItem.getRepetition() + "x");
            holder.description.setText("Beschreibung: " + todoItem.getDescription());
        }
    }

    @Override
    public int getItemCount() {
         // Rueckgabe der Anzahl der Elemente in der Liste
        return todoList != null ? todoList.size() : 0;
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView title, deadline, priority, category, repetitions, description;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            // veknuepfe Views mit den Ids aus der todo_item.xml
            title = itemView.findViewById(R.id.todo_title);
            deadline = itemView.findViewById(R.id.todo_deadline);
            priority = itemView.findViewById(R.id.todo_priority);
            category = itemView.findViewById(R.id.todo_category);
            repetitions = itemView.findViewById(R.id.todo_repetitions);
            description = itemView.findViewById(R.id.todo_description);
        }
    }
}
