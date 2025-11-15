package com.example.utaste.data;

public class RecipeIngredient {
    private int ingredientId;
    private String name;
    private double quantity;

    private double carbs;
    private double protein;
    private double fat;
    private double fiber;
    private double salt;





    public RecipeIngredient(int ingredientId, String name, double quantity, double carbs, double fat, double protein, double fiber, double salt) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.quantity = quantity;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        this.fiber = fiber;
        this.salt = salt;

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

    public double getCarbs() { return carbs;}
    public double getProtein() { return protein;}
    public double getFat() { return fat;}
    public double getFiber() { return fiber;}
    public double getSalt() { return salt;}


}
