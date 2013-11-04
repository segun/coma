/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.maps.provider;

import com.codename1.io.FileSystemStorage;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.io.services.ImageDownloadService;
import com.codename1.location.Location;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.idempotent.coma.Coma;
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
    private int fov = 90, previousFov;
    private int heading = -1, previousHeading;
    private int pitch = 0, previousPitch;
    private boolean useDisplaySizeAsTileSize = false;
    private String url;
    private Location location;
    private Coma coma;
    /**
     * Zoom In/Out
     */
    public static final int DEFAULT_FOV = 90;
    public static final int MAX_FOV = 120;
    public static final int MIN_FOV = 0;
    public static final int INCREMENT_FOV = 5;
    /**
     * Move Up/Down
     */
    public static final int DEFAULT_PITCH = 0;
    public static final int MAX_PITCH = 90;
    public static final int MIN_PITCH = -90;
    public static final int INCREMENT_PITCH = 5;
    /**
     * Move Right/Left
     */
    public static final int DEFAULT_HEADING = -360;
    public static final int MAX_HEADING = 360;
    public static final int MIN_HEADING = 0;
    public static final int INCREMENT_HEADING = 2;

    public GoogleStreetViewProvider(Coma coma, String apiKey, Location location) {
        this(coma, apiKey, location, false);
    }

    public GoogleStreetViewProvider(Coma coma, String apiKey, Location location, boolean useDisplaySizeAsTileSize) {
        this.url = URLConstants.STREET_VIEW_API_URL;
        this.apiKey = apiKey;
        this.location = location;
        this.useDisplaySizeAsTileSize = useDisplaySizeAsTileSize;
        this.coma = coma;
    }

    public void getStreetView(Container mapContainer) {
        previousFov = getFov();
        previousPitch = getPitch();
        previousHeading = getHeading();

        String urlToCall = constructURL();
        String cacheId = getCacheId();
        System.out.println(urlToCall);
        System.out.println(cacheId);

        Label l = new Label();

        ImageDownloadService.createImageToStorage(urlToCall, l, getCacheId(), new Dimension(getTileWidth(), getTileHeight()));
        mapContainer.addComponent(BorderLayout.CENTER, l);
    }

    private String constructURL() {
        String query = "location=" + Util.encodeUrl(location.getLatitude() + "," + location.getLongitude())
                + "&fov=" + Util.encodeUrl(getFov() + "")
                + "&size=" + Util.encodeUrl(getTileWidth() + "x" + getTileHeight())
                + "&sensor=" + Util.encodeUrl(sensor + "")
                + "&pitch=" + Util.encodeUrl(pitch + "")
                + "&key=" + Util.encodeUrl(apiKey);
        if (language != null) {
            query += "&language=" + Util.encodeUrl(language);
        }

        if (heading != DEFAULT_HEADING) {
            query += "&heading=" + Util.encodeUrl(heading + "");
        }
        return url + query;
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
        if (this.fov > MAX_FOV) {
            this.fov = MAX_FOV;
        }

        if (this.fov < MIN_FOV) {
            this.fov = MIN_FOV;
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
        if (this.heading > MAX_HEADING) {
            this.heading = MAX_HEADING;
        }

        if (this.heading < MIN_HEADING && this.heading != -DEFAULT_HEADING) {
            this.heading = MIN_HEADING;
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
        if (this.pitch < -MIN_PITCH) {
            this.pitch = -MIN_PITCH;
        }

        if (this.pitch > MAX_PITCH) {
            this.pitch = MAX_PITCH;
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
        return "sv_" + MStrings.encode(getFov() + "/" + getPitch() + "/" + getTileHeight() + "/" + getTileWidth() + "/" + getHeading());
    }
}