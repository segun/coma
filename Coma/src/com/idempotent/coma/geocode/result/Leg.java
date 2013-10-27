/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.geocode.result;

import com.codename1.location.Location;
import java.util.List;

/**
 *
 * @author aardvocate
 */
public class Leg {
    private Distance distance;
    private Duration duration;
    
    private String endAddress;
    private String startAddress;
    
    private Location endLocation;
    private Location startLocation;
    
    /**
     * NOTE: Do not use this to plot the directions. 
     * This is just a list of turns you must make. If you plot your directions with this, 
     * You will get a plot that doesn't follow curves neatly
     * To get a nice polyline plotted direction that follows curves 
     * Use SingleRoute.decodedPolyline
     */
    private List<Step> steps;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
        
}
