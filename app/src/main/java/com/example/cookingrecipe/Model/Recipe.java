package com.example.cookingrecipe.Model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    private String recipeName;
    private String cookedTime;
    private String forNumber;
    private String description;
    private String tutorial;
    private String imagePath;
    private String author;
    private Bitmap imageUri;
    private List<String> ingridient;

    public void setImageUri(Bitmap imageUri) {
        this.imageUri = imageUri;
    }

    public Bitmap getImageUri() {
        return imageUri;
    }

    public Recipe() {
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public Recipe(String recipeName, String cookedTime, String forNumber, String description, String tutorial, String imagePath, List<String> ingridient) {
        this.recipeName = recipeName;
        this.cookedTime = cookedTime;
        this.forNumber = forNumber;
        this.description = description;
        this.tutorial = tutorial;
        this.imagePath = imagePath;
        this.ingridient = ingridient;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setCookedTime(String cookedTime) {
        this.cookedTime = cookedTime;
    }

    public void setForNumber(String forNumber) {
        this.forNumber = forNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setIngridient(List<String> ingridient) {
        this.ingridient = ingridient;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getCookedTime() {
        return cookedTime;
    }

    public String getForNumber() {
        return forNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getTutorial() {
        return tutorial;
    }

    public String getImagePath() {
        return imagePath;
    }

    public List<String> getIngridient() {
        return ingridient;
    }
}
