/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result;

import java.util.List;

/**
 *
 * @author aardvocate
 */
public class SinglePlace {        
    private Geometry geometry;
    private String icon;
    private String id;
    private String name;
    private OpeningHours openingHours;
    List<Photo> photos;
    private double priceLevel;
    private double rating;
    private String reference;
    private String[] types;
    private String vicinity;
    private List<Event> events;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public double getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(double priceLevel) {
        this.priceLevel = priceLevel;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }  

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }    
    
    public String getPriceLevelAsString() {
        int pl = (int) priceLevel;
        switch(pl) {
            case 1: 
                return "Free";                
            case 2: 
                return "Inexpensive";                
            case 3:
                return "Moderate";
            case 4: 
                return "Expensive";
            case 5:
                return "Very Expensive";
        }
        
        return "";
    }
}