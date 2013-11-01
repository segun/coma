/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result.helpers;

import com.idempotent.coma.result.helpers.Polyline;
import com.codename1.maps.Coord;
import java.util.List;

/**
 *
 * @author aardvocate
 */
public class SingleRoute {

    private Bounds bounds;
    private String copyrights;
    private List<Leg> legs;
    private Polyline overviewPolyline;
    private String summary;
    private List<Coord> decodedPolyline;

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public Polyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(Polyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * NOTE: Use this to plot the direction on the map.
     */
    public List<Coord> getDecodedPolyline() {
        return decodedPolyline;
    }

    public void setDecodedPolyline(List<Coord> decodedPolyline) {
        this.decodedPolyline = decodedPolyline;
    }
}
