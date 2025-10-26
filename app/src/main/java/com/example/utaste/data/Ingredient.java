package com.example.utaste.data;

public class Ingredient {
    private int id;
    private String name;
    private String qrCode;

    public Ingredient(int id, String name, String qrCode){
        this.id=id;
        this.name=name;
        this.qrCode=qrCode;
    }

    public int getId() {return  id;}
    public String getName() { return name;}
    public String getQrCode() { return qrCode; }
    
}
