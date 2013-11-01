/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma;

import com.codename1.io.NetworkManager;
import com.codename1.location.Location;
import com.idempotent.coma.callback.CallNext;
import com.idempotent.coma.google.api.Direction;
import com.idempotent.coma.google.api.Geocode;
import com.idempotent.coma.google.api.PlaceDetails;
import com.idempotent.coma.google.api.PlacesSearch;
import com.idempotent.coma.result.GoogleDirectionResult;
import com.idempotent.coma.result.GoogleGeocodeResult;

/** 
 * This class provides the API method calls.
 * 
 * @author aardvocate
 * @since 1.0
 */
public class Coma {

    private NetworkManager networkManager;
    private CountryCodes countryCodes;

    /**
     *
     * @return the internal CountryCode object
     * @see com.idempotent.coma.CountryCodes
     */
    public CountryCodes getCountryCodes() {
        if (countryCodes == null) {
            countryCodes = new CountryCodes();
        }
        return countryCodes;
    }

    /**
     *
     * @return the internal NetworkManager object
     * @see com.codename1.io.NetworkManager
     */
    public NetworkManager getNetworkManager() {
        if (networkManager == null) {
            networkManager = NetworkManager.getInstance();
        }
        return networkManager;
    }

    /**
     * You can use this method to set the com.codename1.io.NetworkManager to be
     * used in servicing requests
     *
     * @param manager
     */
    public void setNeworkManager(NetworkManager manager) {
        this.networkManager = manager;
    }

    /**
     * This method can do both geocoding and reverse geocoding. <br>To do a
     * reverse geocoding, set all variable to null except latlng and doReverse.
     * <br>To do geocoding, fill in all variables except latlng
     *
     * @param street
     * @param stateOrProvince
     * @param country
     * @param location
     * @param doReverse
     * @param callNext onError, CallNext.onError() is called and a HashMap is
     * passed in. <br>The HashMap contains two keys, code and value.
     * <br>onSuccess, CallNext.onSuccess is called and the response from the
     * geocode/reverse geocode call is passed. The response is a
     * GoogleGeoCodeResult object.
     * @param otherParameters - The api takes lots of other parameters that can
     * help you streamline the result. Pass the other parameters here e.g
     * region=es
     * @see com.idempotent.coma.result.GoogleGeocodeResult
     */
    public void geocode(String street, String stateOrProvince, String country, Location location, boolean doReverse, final CallNext callNext, String... otherParameters) {
        Geocode geocode = new Geocode(this);
        geocode.geocode(street, stateOrProvince, country, location, doReverse, callNext, otherParameters);
    }

    /**
     *
     * @param from
     * @param to
     * @param how
     * @param avoidTolls
     * @param avoidHighways
     * @param callNext onError, CallNext.onError() is called and a HashMap is
     * passed in. <br>The HashMap contains two keys, code and value.
     * <br>onSuccess, CallNext.onSuccess is called and the response from the
     * geocode/reverse geocode call is passed. The response is a
     * GoogleGeoCodeResult object.
     * @param otherParameters - The api takes lots of other parameters that can
     * help you streamline the result. Pass the other parameters here e.g
     * region=es
     * @see com.idempotent.coma.result.GoogleDirectionResult
     */
    public void getDirections(String from, String to, String how, boolean avoidTolls, boolean avoidHighways, final CallNext callNext, String... otherParameters) {
        Direction direction = new Direction(this);
        direction.getDirections(from, to, how, avoidTolls, avoidHighways, callNext, otherParameters);
    }

    /**
     *
     * @param apiKey
     * @param location
     * @param radius
     * @param callNext onError, CallNext.onError() is called and a HashMap is
     * passed in. <br>The HashMap contains two keys, code and value.
     * <br>onSuccess, CallNext.onSuccess is called and the response from the
     * geocode/reverse geocode call is passed. The response is a
     * GoogleGeoCodeResult object.
     * @param otherParameters The api takes lots of other parameters that can
     * help you streamline the result. Pass the other parameters here e.g
     * region=es
     * @see com.idempotent.coma.result.GooglePlacesSearchResult
     */
    public void getPlacesRadarSearch(String apiKey, Location location, int radius, final CallNext callNext, String... otherParameters) {
        PlacesSearch places = new PlacesSearch(this, apiKey);
        places.getRadarSearch(location, radius, callNext, otherParameters);
    }

    /**
     *
     * @param apiKey
     * @param searchString
     * @param callNextonError, CallNext.onError() is called and a HashMap is
     * passed in. <br>The HashMap contains two keys, code and value.
     * <br>onSuccess, CallNext.onSuccess is called and the response from the
     * geocode/reverse geocode call is passed. The response is a
     * GoogleGeoCodeResult object.
     * @param otherParameters The api takes lots of other parameters that can
     * help you streamline the result. Pass the other parameters here e.g
     * region=es
     * @see com.idempotent.coma.result.GooglePlacesSearchResult
     */
    public void getPlacesTextSearch(String apiKey, String searchString, final CallNext callNext, String... otherParameters) {
        PlacesSearch places = new PlacesSearch(this, apiKey);
        places.getTextSearch(searchString, callNext, otherParameters);
    }

    /**
     *
     * @param apiKey
     * @param location
     * @param radius
     * @param onError, CallNext.onError() is called and a HashMap is passed in.
     * <br>The HashMap contains two keys, code and value. <br>onSuccess,
     * CallNext.onSuccess is called and the response from the geocode/reverse
     * geocode call is passed. The response is a GoogleGeoCodeResult object.
     * @param otherParameters The api takes lots of other parameters that can
     * help you streamline the result. Pass the other parameters here e.g
     * region=es
     * @see com.idempotent.coma.result.GooglePlacesSearchResult
     */
    public void getPlacesNearby(String apiKey, Location location, int radius, final CallNext callNext, String... otherParameters) {
        PlacesSearch places = new PlacesSearch(this, apiKey);
        places.getNearby(location, radius, callNext, otherParameters);
    }

    /**
     *
     * @param apiKey
     * @param location
     * @param radius
     * @param rankBy
     * @param onError, CallNext.onError() is called and a HashMap is passed in.
     * <br>The HashMap contains two keys, code and value. <br>onSuccess,
     * CallNext.onSuccess is called and the response from the geocode/reverse
     * geocode call is passed. The response is a GoogleGeoCodeResult object.
     * @param otherParameters The api takes lots of other parameters that can
     * help you streamline the result. Pass the other parameters here e.g
     * region=es
     * @see com.idempotent.coma.result.GooglePlacesSearchResult
     */
    public void getPlacesNearby(String apiKey, Location location, int radius, PlacesSearch.RankBy rankBy, final CallNext callNext, String... otherParameters) {
        PlacesSearch places = new PlacesSearch(this, apiKey);
        places.getNearby(location, radius, rankBy, callNext, otherParameters);
    }

    /**
     * 
     * @param apiKey
     * @param reference
     * @param CallNext.onError() is called and a HashMap is passed in.
     * <br>The HashMap contains two keys, code and value. <br>onSuccess,
     * CallNext.onSuccess is called and the response from the geocode/reverse
     * geocode call is passed. The response is a GoogleGeoCodeResult object.
     * @param otherParameters The api takes lots of other parameters that can
     * help you streamline the result. Pass the other parameters here e.g
     * region=es
     * @see com.idempotent.coma.result.GooglePlaceDetailsResult
     */
    public void getPlaceDetails(String apiKey, final String reference, final CallNext callNext, final String... otherParameters) {
        PlaceDetails details = new PlaceDetails(this, apiKey);
        details.getDetails(reference, callNext, otherParameters);
    }
}