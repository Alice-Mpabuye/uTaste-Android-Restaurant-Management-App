package com.example.utaste.data;

public class RecipeIngredient {
    private int ingredientId; // NEW: Ingredient database ID
    private String name;
    private double quantity; // The field for quantity (%)

    // Builder updated to include ID (optional, but clean)
    public RecipeIngredient(int ingredientId, String name, double quantity) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.quantity = quantity;
    }

    // NEW GETTER FOR ID (CRUCIAL FOR BACKUP)
    public int getIngredientId() {
        return ingredientId;
    }

    // GETTER FOR QUANTITY (THE ONE YOU NEED)
    public double getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    // ... other getters (getName, etc.)
}
