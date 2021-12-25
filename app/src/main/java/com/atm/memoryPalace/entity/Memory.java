package com.atm.memoryPalace.entity;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;
import java.util.Date;

public class Memory implements Serializable {
    private String id;
    public String title;
    public String description;

    public Bitmap bitmap;
    public LatLng location;
    public Date date;
    public Date createDate;

    public Memory(String id, String title, String description, Bitmap bitmap, String lat, String lng, Date date, Date createDate) {
        try {
            this.id = id;
            this.title = title;
            this.description = description;
            this.bitmap = bitmap;
            this.location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            this.date = date;
            this.createDate = createDate;
        } catch (Exception e) {
            System.out.println("Memory e: " + e);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Memory{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", createDate=" + createDate +
                '}';
    }
}
