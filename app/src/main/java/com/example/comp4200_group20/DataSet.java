package com.example.comp4200_group20;

public class DataSet {
    public String Title;
    public String Description;

    public DataSet(String title, String description) {
        this.Title = title;
        this.Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }
}
