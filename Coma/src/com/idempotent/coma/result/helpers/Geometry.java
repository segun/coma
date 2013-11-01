/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result.helpers;

import com.idempotent.coma.result.helpers.Bounds;
import com.idempotent.coma.result.helpers.ViewPort;
import com.codename1.location.Location;

/**
 *
 * @author aardvocate
 */
public class Geometry {
    private  Location location;
    private  String locationType;
    private  ViewPort viewPort;
    private  Bounds bounds;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public ViewPort getViewPort() {
        return viewPort;
    }

    public void setViewPort(ViewPort viewPort) {
        this.viewPort = viewPort;
    }        

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }    
}
