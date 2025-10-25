package com.example.utaste.ui;

public class RecipeImage {
    private String name;
    private int resId;

    public RecipeImage(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    public String getName() { return name; }
    public int getResId() { return resId; }

    @Override
    public String toString() {
        return name; // important pour l'affichage par défaut
    }
}

