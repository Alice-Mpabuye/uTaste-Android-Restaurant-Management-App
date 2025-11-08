package com.example.utaste.data;

public class Ingredient {
    private int id;
    private String name;
    private String qrCode;

    // Nutrition info
    private double carbohydrates;
    private double fat;
    private double protein;
    private double fiber;
    private double salt;

    public Ingredient(int id, String name, String qrCode){
        this.id = id;
        this.name = name;
        this.qrCode = qrCode;
    }

    // New constructor with nutrition info
    public Ingredient(int id, String name, String qrCode, double carbohydrates, double fat, double protein, double fiber, double salt) {
        this(id, name, qrCode);
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.protein = protein;
        this.fiber = fiber;
        this.salt = salt;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getQrCode() { return qrCode; }

    public double getCarbohydrates() { return carbohydrates; }
    public double getFat() { return fat; }
    public double getProtein() { return protein; }
    public double getFiber() { return fiber; }
    public double getSalt() { return salt; }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
