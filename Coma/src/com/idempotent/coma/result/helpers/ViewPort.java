/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result.helpers;

import com.codename1.location.Location;

/**
 *
 * @author aardvocate
 * 
 * @since 1.0
 */
public class ViewPort {
    private  Location northEast;
    private  Location southWest;

    public Location getNorthEast() {
        return northEast;
    }

    public void setNorthEast(Location northEast) {
        this.northEast = northEast;
    }

    public Location getSouthWest() {
        return southWest;
    }

    public void setSouthWest(Location southWest) {
        this.southWest = southWest;
    }        
}
