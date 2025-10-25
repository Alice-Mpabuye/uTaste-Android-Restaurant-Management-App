package com.example.utaste.data;

// Dans com.example.utaste.data.RecipeIngredient

public class RecipeIngredient {
    private int ingredientId; // NOUVEAU : ID BDD de l'ingrédient
    private String name;
    private double quantity; // Le champ pour la quantité (%)

    // Constructeur mis à jour pour inclure l'ID (optionnel, mais propre)
    public RecipeIngredient(int ingredientId, String name, double quantity) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.quantity = quantity;
    }

    // NOUVEAU GETTER POUR L'ID (CRUCIAL POUR LA SAUVEGARDE)
    public int getIngredientId() {
        return ingredientId;
    }

    // GETTER POUR LA QUANTITÉ (CELUI DONT VOUS AVEZ BESOIN)
    public double getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    // ... autres getters (getName, etc.)
}
