package com.atm.memoryPalace.entity;

import java.util.Date;

public class Memory{
    public String id;
    public String title;
    public String description;
    public String location;
    public Date date;
    public Date createDate;

    public Memory(String id, String title, String description, String location, Date date, Date createDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.createDate = createDate;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
