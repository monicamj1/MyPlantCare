package com.example.monicamj1.myplantcare;

import java.util.Date;

public class Plant {
    private String name;
    private String scientific_name;
    private String web_url;
    private Date birthday;
    private int reminder;
    private Date last_watering_day;
    private String images_url[];
    private String profile;

    public Plant(String name, String scientific_name, String web_url, Date birthday, int reminder, Date last_watering_day, String[] images_url, String profile) {
        this.name = name;
        this.scientific_name = scientific_name;
        this.web_url = web_url;
        this.birthday = birthday;
        this.reminder = reminder;
        this.last_watering_day = last_watering_day;
        this.images_url = images_url;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    public Date getLast_watering_day() {
        return last_watering_day;
    }

    public void setLast_watering_day(Date last_watering_day) {
        this.last_watering_day = last_watering_day;
    }

    public String[] getImages_url() {
        return images_url;
    }

    public void setImages_url(String[] images_url) {
        this.images_url = images_url;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
