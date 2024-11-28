package com.example.eliasundkyanh.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoDao {
    // Einfügen eines neuen Eintrags
    @Insert
    void insert(TodoItem todo);

    // Alle Einträge abrufen
    @Query("SELECT * FROM todo_table")
    List<TodoItem> getAllTodos();
}
