package com.example.utaste.data;

public class Sale {
    private long id;
    private int recipeId;
    private String recipeName; // convenience for queries that join recipe
    private int rating; // 1..5
    private String note;
    private long timestamp;

    public Sale(long id, int recipeId, String recipeName, int rating, String note, long timestamp) {
        this.id = id;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.rating = rating;
        this.note = note;
        this.timestamp = timestamp;
    }

    public long getId() { return id; }
    public int getRecipeId() { return recipeId; }
    public String getRecipeName() { return recipeName; }
    public int getRating() { return rating; }
    public String getNote() { return note; }
    public long getTimestamp() { return timestamp; }
}
