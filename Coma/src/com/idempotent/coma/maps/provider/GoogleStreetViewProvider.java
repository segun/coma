/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.maps.provider;

import com.codename1.maps.BoundingBox;
import com.codename1.maps.Mercator;
import com.codename1.maps.Tile;
import com.codename1.maps.providers.TiledProvider;
import com.codename1.ui.geom.Dimension;

/**
 *
 * @author aardvocate
 * 
 * @since 1.0
 * 
 * This class provides a google street view 
 */
public class GoogleStreetViewProvider extends TiledProvider {

    private String apiKey;
    private int type;
    private String language;
    private boolean sensor;
    private static int tileSize = 256;

    public GoogleStreetViewProvider(String apiKey) {
        super("http://maps.googleapis.com/maps/api/staticmap?", new Mercator(), new Dimension(tileSize, tileSize));
        this.apiKey = apiKey;
    }

    @Override
    public Tile tileFor(BoundingBox bbox) {
        //implement this and we are good
        return super.tileFor(bbox);
    }

    
    public static int getTileSize() {
        return tileSize;
    }

    public static void setTileSize(int tileSize) {
        GoogleStreetViewProvider.tileSize = tileSize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isSensor() {
        return sensor;
    }

    public void setSensor(boolean sensor) {
        this.sensor = sensor;
    }
        
    @Override
    public int maxZoomLevel() {
        return 18;
    }

    @Override
    public String attribution() {
        return "Google";
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    
    
}
