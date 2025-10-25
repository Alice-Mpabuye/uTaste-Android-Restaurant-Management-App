package com.example.utaste.model;

public class Recipe {
    private int id;
    private String name;
    private String imageName;
    private String description;

    public Recipe(int id, String name, String imageName, String description){
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.description = description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getImageName() { return imageName; }
    public String getDescription() { return description; }
}
