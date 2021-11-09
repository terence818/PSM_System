package com.example.psmsystem;

import android.graphics.Bitmap;

public class Recipes {



    public Recipes(String name,  String calories, String serve, String url_image, Bitmap bitmap, String categories) {
        this.name = name;
        this.calories = calories;
        this.serve = serve;
        this.categories = categories;
        this.url_image = url_image;
        this.bitmap = bitmap;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getServe() {
        return serve;
    }

    public void setServe(String serve) {
        this.serve = serve;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private String calories;
    private String serve;
    private String url_image;
    private Bitmap bitmap;
    private String name;


    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    private String categories;

    public Recipes() {

        this.url_image = "";
        this.calories = "";
        this.serve = "";
        this.name = "";
        this.categories ="";

    }


}
