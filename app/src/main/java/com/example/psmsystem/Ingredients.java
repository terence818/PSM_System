package com.example.psmsystem;

public class Ingredients {

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String getIngredient_categories() {
        return ingredient_categories;
    }

    public void setIngredient_categories(String ingredient_categories) {
        this.ingredient_categories = ingredient_categories;
    }

    public Ingredients(String ingredient_name, String ingredient_categories, String ingredient_unit) {
        this.ingredient_name = ingredient_name;
        this.ingredient_categories = ingredient_categories;
        this.ingredient_unit = ingredient_unit;
    }

    private String ingredient_name;
    private String ingredient_categories;

    public String getIngredient_unit() {
        return ingredient_unit;
    }

    public void setIngredient_unit(String ingredient_unit) {
        this.ingredient_unit = ingredient_unit;
    }

    private String ingredient_unit;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    private String quantity;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public Ingredients() {
        this.key = "";
        this.ingredient_name = "";
        this.ingredient_categories = "";
        this.ingredient_unit = "";

    }

    public String toString() {
        return ingredient_name;
    }
}
