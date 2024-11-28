package com.example.eliasundkyanh.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Room-Entität für die Tabelle 'todo_table'
@Entity(tableName = "todo_table")
public class TodoItem {
    @PrimaryKey(autoGenerate = true)
    private int id; // Automatisch generierte ID

    private String title;       // Titel der Aufgabe
    private String description; // Beschreibung
    private String priority;    // Priorität
    private String category;    // Kategorie
    private String deadline;    // Fälligkeitsdatum
    private int repetition; // Haeufigkeit


    // Getter und Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }
}
