package com.example.eliasundkyanh.model;

public class TodoItem {

    private String title;
    private String description;

    /**
     *
     * @param title
     * @param description
     */
    public TodoItem(String title, String description) {

        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
