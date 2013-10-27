/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.io.Util;
import com.codename1.location.Location;
import com.codename1.maps.Coord;
import com.codename1.processing.Result;
import com.idempotent.coma.callback.CallNext;
import com.idempotent.coma.geocode.result.AddressComponent;
import com.idempotent.coma.geocode.result.Bounds;
import com.idempotent.coma.geocode.result.Distance;
import com.idempotent.coma.geocode.result.Duration;
import com.idempotent.coma.geocode.result.Geometry;
import com.idempotent.coma.geocode.result.GoogleDirectionResult;
import com.idempotent.coma.geocode.result.GoogleGeocodeResult;
import com.idempotent.coma.geocode.result.Leg;
import com.idempotent.coma.geocode.result.Polyline;
import com.idempotent.coma.geocode.result.SingleResult;
import com.idempotent.coma.geocode.result.SingleRoute;
import com.idempotent.coma.geocode.result.Step;
import com.idempotent.coma.geocode.result.ViewPort;
import com.idempotent.coma.urlhelper.URLConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author aardvocate
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
     * This method can do noth geocoding and reverse geocoding. <br>To do a
     * reverse geocoding, set all variable to null except latlng and doReverse.
     * <br>To do geocoding, fill in all variables except latlng
     *
     * @param street
     * @param stateOrProvince
     * @param country
     * @param latlng
     * @param doReverse
     * @param callNext onError, CallNext.onError() is called and a HashMap is
     * passed in. <br>The HashMap contains two keys, code and value.
     * <br>onSuccess, CallNext.onSuccess is called and the response from the
     * geocode/reverse geocode call is passed. The response is a
     * GoogleGeoCodeResult object.
     * @param otherParameters - The api takes lots of other parameters that can
     * help you streamline the result. Pass the other parameters here e.g
     * region=es
     * @see com.idempotent.coma.geocode.result.GeoCodeResult
     */
    public void geocode(String street, String stateOrProvince, String country, String latlng, boolean doReverse, final CallNext callNext, String otherParameters) {
        String countryCodesString = "NG";

        String query = "";
        if (doReverse) {
            if (latlng == null) {
                HashMap<String, String> errorMap = new HashMap<String, String>();
                errorMap.put("code", 500 + "");
                errorMap.put("message", "LatLng Variable can not be null if doReverse is set to true");
                callNext.onError(errorMap);
            } else {
                query = "&latlng=" + Util.encodeUrl(latlng);
            }
        } else {
            countryCodesString = getCountryCodes().getCountryCodes();

            query = "address=" + Util.encodeUrl(street + ", " + stateOrProvince);

            if (!countryCodesString.equals("")) {
                query += "&components=country:" + countryCodesString;
            }
        }

        if (otherParameters != null) {
            if (otherParameters.indexOf("&") == 0) {
                query += otherParameters;
            } else {
                query += "&" + otherParameters;
            }
        }

        String url = URLConstants.GEOCODE_API_URL + query;

        System.out.println(url);

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
                GoogleGeocodeResult geocodeResult = parseGeoCodeResult(res);
                callNext.onSuccess(geocodeResult);
            }
        };

        request.setUrl(url);
        request.setPost(false);
        request.setDuplicateSupported(false);

        getNetworkManager().addToQueue(request);
    }

    public GoogleGeocodeResult parseGeoCodeResult(Result result) {

        GoogleGeocodeResult geocodeResult = new GoogleGeocodeResult();
        geocodeResult.setStatus(result.getAsString("status"));
        geocodeResult.setRaw(result);

        List<SingleResult> allResults = new ArrayList<SingleResult>();

        int size = result.getSizeOfArray("results");

        for (int rs = 0; rs < size; rs++) {
            SingleResult singleResult = new SingleResult();

            singleResult.setFormattedAddress(result.getAsString("results[" + rs + "]/formatted_address"));

            List<AddressComponent> addressComponents = new ArrayList<AddressComponent>();
            int addressComponentsSize = result.getSizeOfArray("results[" + rs + "]/address_components");

            for (int i = 0; i < addressComponentsSize; i++) {
                AddressComponent addressComponent = new AddressComponent();
                addressComponent.setLongName(result.getAsString("results[" + rs + "]/address_components[" + i + "]/long_name"));
                addressComponent.setShortName(result.getAsString("results[" + rs + "]/address_components[" + i + "]/short_name"));

                addressComponent.setType(result.getAsString("results[" + rs + "]/address_components[" + i + "]/types"));

                System.out.println("TYPE: " + addressComponent.getType());
                addressComponents.add(addressComponent);
            }

            singleResult.setAddressComponents(addressComponents);

            Geometry geometry = new Geometry();

            Location location = new Location();
            location.setLatitude(result.getAsDouble("results[" + rs + "]/geometry/location/lat"));
            location.setLongitude(result.getAsDouble("results[" + rs + "]/geometry/location/lng"));
            geometry.setLocation(location);
            geometry.setLocationType(result.getAsString("results[" + rs + "]/geometry/location_type"));

            ViewPort viewPort = new ViewPort();
            Location vpNorthEastLocation = new Location(), vpSouthWestLocation = new Location();
            vpNorthEastLocation.setLatitude(result.getAsDouble("results[" + rs + "]/geometry/viewport/northeast/lat"));
            vpNorthEastLocation.setLongitude(result.getAsDouble("results[" + rs + "]/geometry/viewport/northeast/lng"));
            vpSouthWestLocation.setLatitude(result.getAsDouble("results[" + rs + "]/geometry/viewport/southwest/lat"));
            vpSouthWestLocation.setLongitude(result.getAsDouble("results[" + rs + "]/geometry/viewport/southwest/lng"));
            viewPort.setNorthEast(vpNorthEastLocation);
            viewPort.setSouthWest(vpSouthWestLocation);
            geometry.setViewPort(viewPort);

            Bounds bounds = new Bounds();
            Location bNorthEastLocation = new Location(), bSouthWestLocation = new Location();
            bNorthEastLocation.setLatitude(result.getAsDouble("results[" + rs + "]/geometry/bounds/northeast/lat"));
            bNorthEastLocation.setLongitude(result.getAsDouble("results[" + rs + "]/geometry/bounds/northeast/lng"));
            bSouthWestLocation.setLatitude(result.getAsDouble("results[" + rs + "]/geometry/bounds/southwest/lat"));
            bSouthWestLocation.setLongitude(result.getAsDouble("results[" + rs + "]/geometry/bounds/southwest/lng"));
            bounds.setNorthEast(bNorthEastLocation);
            bounds.setSouthWest(bSouthWestLocation);
            geometry.setBounds(bounds);

            singleResult.setGeometry(geometry);

            singleResult.setType(result.getAsString("results[" + rs + "]/types"));

            allResults.add(singleResult);
        }

        geocodeResult.setResults(allResults);
        return geocodeResult;
    }

    public void getDirections(String from, String to, String how, boolean avoidTolls, boolean avoidHighways, String otherParameters, final CallNext callNext) {

        String url = URLConstants.DIRECTIONS_API_URL;

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
                Result res = Result.fromContent(input, Result.JSON);
                GoogleDirectionResult directionResult = parseDirectionsResult(res);
                callNext.onSuccess(directionResult);
            }
        };

        String query = "origin=" + Util.encodeUrl(from) + "&destination=" + Util.encodeUrl(to) + "&mode=" + Util.encodeUrl(how);

        if (avoidTolls) {
            query += "&avoid=tolls";
        }

        if (avoidHighways) {
            if (query.indexOf("avoid") >= 0) {
                query += Util.encodeUrl("|") + "highways";
            } else {
                query += "&avoid=highways";
            }
        }

        url += query;

        if (otherParameters != null) {
            if (otherParameters.indexOf("&") == 0) {
                url += otherParameters;
            } else {
                url += "&" + otherParameters;
            }
        }

        System.out.println(url);
        request.setUrl(url);
        request.setPost(false);
        request.setDuplicateSupported(true);

        getNetworkManager().addToQueue(request);
    }

    public GoogleDirectionResult parseDirectionsResult(Result result) {
        GoogleDirectionResult directionResult = new GoogleDirectionResult();
        directionResult.setStatus(result.getAsString("status"));
        directionResult.setRaw(result);

        List<SingleRoute> routes = new ArrayList<SingleRoute>();
        int size = result.getSizeOfArray("routes");

        for (int rs = 0; rs < size; rs++) {
            SingleRoute singleRoute = new SingleRoute();

            singleRoute.setCopyrights(result.getAsString("routes[" + rs + "]/copyrights"));
            singleRoute.setSummary(result.getAsString("routes[" + rs + "]/summary"));
            Polyline overviewPolyline = new Polyline();
            overviewPolyline.setPoints(result.getAsString("routes[" + rs + "]/overview_polyline"));
            singleRoute.setOverviewPolyline(overviewPolyline);
            singleRoute.setDecodedPolyline(decodePolyLine(overviewPolyline.getPoints()));

            Bounds bounds = new Bounds();
            Location bNorthEastLocation = new Location(), bSouthWestLocation = new Location();
            bNorthEastLocation.setLatitude(result.getAsDouble("routes[" + rs + "]/bounds/northeast/lat"));
            bNorthEastLocation.setLongitude(result.getAsDouble("routes[" + rs + "]/bounds/northeast/lng"));
            bSouthWestLocation.setLatitude(result.getAsDouble("routes[" + rs + "]/bounds/southwest/lat"));
            bSouthWestLocation.setLongitude(result.getAsDouble("routes[" + rs + "]/bounds/southwest/lng"));
            bounds.setNorthEast(bNorthEastLocation);
            bounds.setSouthWest(bSouthWestLocation);
            singleRoute.setBounds(bounds);

            List<Leg> legs = new ArrayList<Leg>();
            int legsSize = result.getSizeOfArray("routes[" + rs + "]/legs");

            for (int li = 0; li < legsSize; li++) {
                Leg leg = new Leg();
                leg.setEndAddress(result.getAsString("routes[" + rs + "]/legs[" + li + "]/end_address"));
                leg.setStartAddress(result.getAsString("routes[" + rs + "]/legs[" + li + "]/start_address"));

                Location startLocation = new Location(), endLocation = new Location();
                startLocation.setLatitude(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/start_location/lat"));
                endLocation.setLatitude(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/end_location/lat"));
                startLocation.setLongitude(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/start_location/lng"));
                endLocation.setLongitude(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/end_location/lng"));

                leg.setStartLocation(startLocation);
                leg.setEndLocation(endLocation);

                Distance distance = new Distance();
                distance.setText(result.getAsString("routes[" + rs + "]/legs[" + li + "]/distance/text"));
                distance.setValue(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/distance/value"));
                leg.setDistance(distance);

                Duration duration = new Duration();
                duration.setText(result.getAsString("routes[" + rs + "]/legs[" + li + "]/duration/text"));
                duration.setValue(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/duration/value"));
                leg.setDuration(duration);

                List<Step> steps = new ArrayList<Step>();

                int stepsSize = result.getSizeOfArray("routes[" + rs + "]/legs[" + li + "]/steps");

                for (int ss = 0; ss < stepsSize; ss++) {
                    Step step = new Step();
                    Distance stepDistance = new Distance();
                    stepDistance.setText(result.getAsString("routes[" + rs + "]/legs[" + li + "]/steps[" + ss + "]/distance/text"));
                    stepDistance.setValue(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/steps[" + ss + "]/distance/value"));
                    step.setDistance(stepDistance);

                    Duration stepDuration = new Duration();
                    stepDuration.setText(result.getAsString("routes[" + rs + "]/legs[" + li + "]/steps[" + ss + "]/duration/text"));
                    stepDuration.setValue(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/steps[" + ss + "]/duration/value"));
                    step.setDuration(stepDuration);

                    Location stepStartLoc = new Location(), stepEndLoc = new Location();
                    stepStartLoc.setLatitude(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/steps[" + ss + "]/start_location/lat"));
                    stepEndLoc.setLatitude(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/steps[" + ss + "]/end_location/lat"));
                    stepStartLoc.setLongitude(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/steps[" + ss + "]/start_location/lng"));
                    stepEndLoc.setLongitude(result.getAsDouble("routes[" + rs + "]/legs[" + li + "]/steps[" + ss + "]/end_location/lng"));

                    step.setEndLocation(stepEndLoc);
                    step.setStarLocation(stepStartLoc);

                    step.setHtmlInstructions(result.getAsString("routes[" + rs + "]/legs[" + li + "]/steps[" + ss + "]/html_instructions"));

                    Polyline polyline = new Polyline();
                    polyline.setPoints(result.getAsString("routes[" + rs + "]/legs[" + li + "]/steps[" + ss + "]/polyline/points"));
                    step.setPolyline(polyline);

                    step.setTravelMode(result.getAsString("routes[" + rs + "]/legs[" + li + "]/steps[" + ss + "]/travel_mode"));

                    steps.add(step);
                }

                leg.setSteps(steps);

                legs.add(leg);
            }

            singleRoute.setLegs(legs);
            
            routes.add(singleRoute);
        }
        directionResult.setRoutes(routes);
        return directionResult;
    }

    private List<Coord> decodePolyLine(String encoded) {
        List<Coord> poly = new ArrayList<Coord>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            Coord p = new Coord((double) lat / (double) 1E5, (double) lng / (double) 1E5);

            poly.add(p);
        }

        return poly;
    }
}
