package ognjen.popovic.eventsapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Event {

    private String serverEventId;

    private String name;
    private String description;
    private String location;
    private String dateTime;
    private String category;
    private int imageResId;

    private boolean isPromoted;
    private int capacity;
    private int attendingCount;

    private double averageRating;
    private int ratingCount;

    // Konstruktor za promoted eventove
    public Event(String name, String description, String location,
                 String dateTime, String category, int imageResId,
                 boolean isPromoted, int capacity, int attendingCount) {

        this.serverEventId = "";

        this.name = name;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.category = category;
        this.imageResId = imageResId;
        this.isPromoted = isPromoted;
        this.capacity = capacity;
        this.attendingCount = attendingCount;
        this.averageRating = 0;
        this.ratingCount = 0;
    }

    // Konstruktor za regular eventove
    public Event(String name, String description, String location,
                 String dateTime, String category, int imageResId) {

        this.serverEventId = "";

        this.name = name;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.category = category;
        this.imageResId = imageResId;
        this.isPromoted = false;
        this.capacity = 0;
        this.attendingCount = 0;
        this.averageRating = 0;
        this.ratingCount = 0;
    }

    // Konstruktor za eventove koji dolaze sa servera ili iz baze
    public Event(String serverEventId, String name, String description, String location,
                 String dateTime, String category, int imageResId,
                 boolean isPromoted, int capacity, int attendingCount,
                 double averageRating, int ratingCount) {

        this.serverEventId = serverEventId;

        this.name = name;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.category = category;
        this.imageResId = imageResId;
        this.isPromoted = isPromoted;
        this.capacity = capacity;
        this.attendingCount = attendingCount;
        this.averageRating = averageRating;
        this.ratingCount = ratingCount;
    }

    public boolean isPast() {
        SimpleDateFormat sdf =
                new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

        try {
            Date eventDate = sdf.parse(dateTime);
            Date currentDate = new Date();

            return eventDate.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void addRating(int rating) {
        double total = averageRating * ratingCount;
        total += rating;
        ratingCount++;
        averageRating = total / ratingCount;
    }

    public String getServerEventId() {
        return serverEventId;
    }

    public void setServerEventId(String serverEventId) {
        this.serverEventId = serverEventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAttendingCount() {
        return attendingCount;
    }

    public void setAttendingCount(int attendingCount) {
        this.attendingCount = attendingCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
}