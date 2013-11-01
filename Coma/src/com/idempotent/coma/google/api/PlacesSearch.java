/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.google.api;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Util;
import com.codename1.location.Location;
import com.codename1.processing.Result;
import com.idempotent.coma.Coma;
import com.idempotent.coma.callback.CallNext;
import com.idempotent.coma.result.helpers.Geometry;
import com.idempotent.coma.result.helpers.OpeningHours;
import com.idempotent.coma.result.helpers.Photo;
import com.idempotent.coma.result.GooglePlacesSearchResult;
import com.idempotent.coma.result.helpers.SinglePlace;
import com.idempotent.coma.urlhelper.URLConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Please read https://developers.google.com/places/documentation/search before
 * proceeding to use the API
 *
 * @author aardvocate
 */
public class PlacesSearch {

    public enum RankBy {
        PROMINENCE, DISTANCE
    };
    Coma coma;
    String apiKey;

    public PlacesSearch(Coma coma, String apiKey) {
        this.coma = coma;
        this.apiKey = apiKey;
    }

    public void getRadarSearch(Location location, int radius, final CallNext callNext, String... otherParameters) {
        String query = "&key=" + apiKey
                + "&location=" + Util.encodeUrl(location.getLatitude() + "," + location.getLongitude())
                + "&radius=" + radius;

        if (otherParameters != null) {
            for (String otherParam : otherParameters) {
                if (otherParam.indexOf("&") == 0) {
                    query += otherParam;
                } else {
                    query += "&" + otherParam;
                }
            }
        }

        String url = URLConstants.PLACES_RADAR_SEARCH_API_URL + query;

        System.out.println(url);
        connect(url, callNext);
    }

    public void getTextSearch(String searchString, final CallNext callNext, String... otherParameters) {
        String query = "&key=" + apiKey
                + "&query=" + Util.encodeUrl(searchString);

        if (otherParameters != null) {
            for (String otherParam : otherParameters) {
                if (otherParam.indexOf("&") == 0) {
                    query += otherParam;
                } else {
                    query += "&" + otherParam;
                }
            }
        }

        String url = URLConstants.PLACES_TEXT_SEARCH_API_URL + query;

        System.out.println(url);
        connect(url, callNext);
    }

    public void getNearby(Location location, int radius, final CallNext callNext, String... otherParameters) {
        RankBy rankBy = RankBy.PROMINENCE;
        getNearby(location, radius, rankBy, callNext, otherParameters);
    }

    public void getNearby(Location location, int radius, RankBy rankBy, final CallNext callNext, String... otherParameters) {
        boolean rankSettingsOk = false;
        if (rankBy.equals(RankBy.DISTANCE)) {
            for (String otherParam : otherParameters) {
                if (otherParam.indexOf("keyword") >= 0 || otherParam.indexOf("name") >= 0 || otherParam.indexOf("types") >= 0) {
                    rankSettingsOk = true;
                }
            }
        } else {
            rankSettingsOk = true;
        }

        if (!rankSettingsOk) {
            HashMap<String, String> errorMap = new HashMap<String, String>();
            errorMap.put("code", 500 + "");
            errorMap.put("message", "If you select rank by distance, you must also provide at least one of keyword=[keyword], name=[name] and types=[in_type] in the otherParameters");
            callNext.onError(errorMap);
            return;
        }

        String query = "&key=" + apiKey
                + "&location=" + Util.encodeUrl(location.getLatitude() + "," + location.getLongitude());

        if (rankBy.equals(RankBy.PROMINENCE)) {
            query += "&radius=" + radius;
        }

        if (otherParameters != null) {
            for (String otherParam : otherParameters) {
                if (otherParam.indexOf("&") == 0) {
                    query += otherParam;
                } else {
                    query += "&" + otherParam;
                }
            }
        }

        String url = URLConstants.PLACES_NEARBY_SEARCH_API_URL + query;

        System.out.println(url);
        connect(url, callNext);
    }

    public GooglePlacesSearchResult parsePlacesResult(Result result) {
        GooglePlacesSearchResult placesResult = new GooglePlacesSearchResult();
        placesResult.setHtmlAttributions(result.getAsStringArray("html_attributions"));
        placesResult.setStatus(result.getAsString("status"));
        placesResult.setRaw(result);

        int rSize = result.getSizeOfArray("results");

        List<SinglePlace> allResults = new ArrayList<SinglePlace>();
        for (int i = 0; i < rSize; i++) {
            SinglePlace singlePlace = new SinglePlace();
            allResults.add(singlePlace);

            Geometry geometry = new Geometry();
            Location location = new Location();
            location.setLatitude(result.getAsDouble("results[" + i + "]/geometry/location/lat"));
            location.setLongitude(result.getAsDouble("results[" + i + "]/geometry/location/lng"));
            geometry.setLocation(location);
            singlePlace.setGeometry(geometry);

            singlePlace.setIcon(result.getAsString("results[" + i + "]/icon"));
            singlePlace.setId(result.getAsString("results[" + i + "]/id"));
            singlePlace.setName(result.getAsString("results[" + i + "]/name"));

            OpeningHours openingHours = new OpeningHours();
            openingHours.setOpenNow(result.getAsBoolean("results[" + i + "]/opening_hours/open_now"));
            singlePlace.setOpeningHours(openingHours);

            List<Photo> photos = new ArrayList<Photo>();
            int photosSize = result.getSizeOfArray("results[" + i + "]/photos");

            for (int j = 0; j < photosSize; j++) {
                Photo photo = new Photo();
                photo.setHeight(result.getAsDouble("results[" + i + "]/photos[" + j + "]/height"));
                photo.setWidth(result.getAsDouble("results[" + i + "]/photos[" + j + "]/width"));
                photo.setHtmlAttributions(result.getAsStringArray("results[" + i + "]/photos[" + j + "]/html_attributions"));
                photo.setPhotoReference(result.getAsString("results[" + i + "]/photos[" + j + "]/photo_reference"));
                photos.add(photo);
            }
            singlePlace.setPhotos(photos);

            singlePlace.setPriceLevel((int) result.getAsDouble("results[" + i + "]/price_level"));
            singlePlace.setRating(result.getAsDouble("results[" + i + "]/rating"));
            singlePlace.setReference(result.getAsString("results[" + i + "]/reference"));
            singlePlace.setTypes(result.getAsStringArray("results[" + i + "]/types"));
            singlePlace.setVicinity(result.getAsString("results[" + i + "]/vicinity"));
        }

        placesResult.setResults(allResults);
        return placesResult;
    }

    private void connect(String url, final CallNext callNext) {
        ConnectionRequest request = new ConnectionRequest() {
            @Override
            protected void handleErrorResponseCode(int code, String message) {
                HashMap<String, String> errorMap = new HashMap<String, String>();
                errorMap.put("code", code + "");
                errorMap.put("message", message);
                callNext.onError(errorMap);
            }

            @Override
            protected void handleException(Exception err) {
                HashMap<String, String> errorMap = new HashMap<String, String>();
                errorMap.put("code", 500 + "");
                errorMap.put("message", "Exception: " + err.getMessage());
                err.printStackTrace();
                callNext.onError(errorMap);
            }

            @Override
            protected void readResponse(InputStream input) throws IOException {
                JSONParser parser = new JSONParser();
                Hashtable result = parser.parse(new InputStreamReader(input));
                Result res = Result.fromContent(result);
                GooglePlacesSearchResult placesResult = parsePlacesResult(res);
                callNext.onSuccess(placesResult);

            }
        };

        request.setUrl(url);
        request.setPost(false);
        request.setDuplicateSupported(false);

        coma.getNetworkManager().addToQueue(request);
    }
}
