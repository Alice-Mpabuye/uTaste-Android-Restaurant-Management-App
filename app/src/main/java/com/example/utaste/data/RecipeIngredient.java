package com.example.utaste.data;

public class RecipeIngredient {
    private int ingredientId;
    private String name;
    private double quantity;

    public RecipeIngredient(int ingredientId, String name, double quantity) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.quantity = quantity;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public void setQuantity(double quantity) { this.quantity = quantity; }

}
