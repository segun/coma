/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.maps.provider;

import com.codename1.io.services.ImageDownloadService;
import com.codename1.location.Location;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.idempotent.coma.stringshelper.MStrings;
import com.idempotent.coma.urlhelper.URLConstants;

/**
 *
 * @author aardvocate
 *
 * @since 1.0
 *
 * This class provides a google street view
 */
public class GoogleStreetViewProvider {

    private String apiKey;
    private String language;
    private boolean sensor = true;
    private int tileWidth = 256;
    private int tileHeight = 256;
    private int fov = 90;
    private int heading = -1;
    private int pitch = 0;
    private boolean useDisplaySizeAsTileSize = false;
    private String url;
    private Location location;

    public GoogleStreetViewProvider(String apiKey, Location location) {
        this(apiKey, location, false);
    }

    public GoogleStreetViewProvider(String apiKey, Location location, boolean useDisplaySizeAsTileSize) {
        this.url = URLConstants.STREET_VIEW_API_URL;
        this.apiKey = apiKey;
        this.location = location;
        this.useDisplaySizeAsTileSize = useDisplaySizeAsTileSize;
    }

    public void getImageAndDraw(final GoogleStreetViewMap mapComponent) {
        String urlToCall = constructURL();
        System.out.println(urlToCall);
        Label l = new Label();
        
        ImageDownloadService.createImageToStorage(urlToCall, l, getCacheId(), new Dimension(getTileWidth(), getTileHeight()));
        mapComponent.addComponent(BorderLayout.CENTER, l);
        mapComponent.animateLayout(750);
    }
    
    private String constructURL() {
        StringBuilder sb = new StringBuilder(url);

        sb.append("location=");
        sb.append(location.getLatitude());
        sb.append(",");
        sb.append(location.getLongitude());
        sb.append("&fov=");
        sb.append(getFov());
        sb.append("&size=");
        sb.append(getTileWidth());
        sb.append("x");
        sb.append(getTileHeight());
        sb.append("&sensor=");
        sb.append(sensor);
        sb.append("&pitch=");
        sb.append(pitch);
        if (language != null) {
            sb.append("&language=");
            sb.append(language);
        }

        if (heading != -1) {
            sb.append("&heading=");
            sb.append(heading);
        }

        sb.append("&key=");
        sb.append(apiKey);

        return sb.toString();
    }

    public int getTileWidth() {
        if (useDisplaySizeAsTileSize) {
            tileWidth = Display.getInstance().getDisplayWidth();
        }
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        if (useDisplaySizeAsTileSize) {
            tileHeight = Display.getInstance().getDisplayHeight();
        }
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Determines the horizontal field of view of the image. The field of view
     * is expressed in degrees, with a maximum allowed value of 120. When
     * dealing with a fixed-size viewport, as with a Street View image of a set
     * size, field of view in essence represents zoom, with smaller numbers
     * indicating a higher level of zoom Default is 90
     *
     * @return
     */
    public int getFov() {
        return fov;
    }

    public void setFov(int fov) {
        this.fov = fov;
        if (this.fov > 120) {
            this.fov = 120;
        }

        if (this.fov < 0) {
            this.fov = 0;
        }
    }

    /**
     * Indicates the compass heading of the camera. Accepted values are from 0
     * to 360 (both values indicating North, with 90 indicating East, and 180
     * South). If no heading is specified, a value will be calculated that
     * directs the camera towards the specified location, from the point at
     * which the closest photograph was taken.
     *
     * @return
     */
    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
        if(this.heading > 360) {
            this.heading = 360;
        }
        
        if(this.heading < 0) {
            this.heading = 0;
        }        
    }

    /**
     * Specifies the up or down angle of the camera relative to the Street View
     * vehicle. This is often, but not always, flat horizontal. Positive values
     * angle the camera up (with 90 degrees indicating straight up); negative
     * values angle the camera down (with -90 indicating straight down). Default
     * is 0
     *
     * @return
     */
    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        if(this.pitch < -90) {
            this.pitch = -90;
        }
        
        if(this.pitch > 90) {
            this.pitch = 90;
        }
        this.pitch = pitch;
    }

    public boolean isUseDisplaySizeAsTileSize() {
        return useDisplaySizeAsTileSize;
    }

    public void setUseDisplaySizeAsTileSize(boolean useDisplaySizeAsTileSize) {
        this.useDisplaySizeAsTileSize = useDisplaySizeAsTileSize;
    }

    private String getCacheId() {
        return MStrings.encode(getFov() + "/" + getPitch() + "/" + getTileHeight() + "/"  + getTileWidth() + "/" + getHeading());
    }
}