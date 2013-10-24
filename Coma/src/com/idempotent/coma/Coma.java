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
import com.codename1.processing.Result;
import com.idempotent.coma.callback.CallNext;
import com.idempotent.coma.geocode.result.AddressComponent;
import com.idempotent.coma.geocode.result.Bounds;
import com.idempotent.coma.geocode.result.Geometry;
import com.idempotent.coma.geocode.result.GoogleGeoCodeResponse;
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
     * This method can do noth geocoding and reverse geocoding. To do a reverse
     * geocoding, set all variable to null except latlng and doReverse To do
     * geocoding, fill in all variables except latlng onError, CallNext.onError
     * is called and a HashMap is passed in. The HashMap contains two keys, code
     * and value onSuccess, CallNext.onSuccess is called and the response from
     * the geocode/reverse geocode call is passed. The response is a List of
     * GoogleGeoCodeResponse objects
     *
     * @param street
     * @param stateOrProvince
     * @param country
     * @param latlng
     * @param doReverse
     * @param callNext
     * @param otherParameters - The api takes lots of other parameters that can
     * help you streamline the result. Pass the other parameters here e.g
     * region=es
     * @see com.idempotent.coma.geocode.result.GoogleGeoCodeResponse
     */
    public void geocode(String street, String stateOrProvince, String country, String latlng, boolean doReverse, final CallNext callNext, String otherParameters) {
        String countryCodes = "NG";

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
            countryCodes = getCountryCodes().getCountryCodes();

            query = "address=" + Util.encodeUrl(street + ", " + stateOrProvince);

            if (!countryCodes.equals("")) {
                query += "&components=country:" + countryCodes;
            }
        }

        if (otherParameters != null) {
            if (otherParameters.indexOf("&") == 0) {
                query += otherParameters;
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
                List<GoogleGeoCodeResponse> responses = parse(res);
                callNext.onSuccess(responses);
            }
        };

        request.setUrl(url);
        request.setPost(false);
        request.setDuplicateSupported(false);

        getNetworkManager().addToQueue(request);
    }

    public List<GoogleGeoCodeResponse> parse(Result result) {

        List<GoogleGeoCodeResponse> responses = new ArrayList<GoogleGeoCodeResponse>();

        int size = result.getSizeOfArray("results");

        for (int rs = 0; rs < size; rs++) {
            GoogleGeoCodeResponse googleGeoCodeResponse = new GoogleGeoCodeResponse();
            googleGeoCodeResponse.setStatus(result.getAsString("status"));
            googleGeoCodeResponse.setFormattedAddress(result.getAsString("results[" + rs + "]/formatted_address"));           
            googleGeoCodeResponse.setRaw(result);

            List<AddressComponent> addressComponents = new ArrayList<AddressComponent>();
            int addressComponentsSize = result.getSizeOfArray("results[" + rs + "]/address_components");

            for (int i = 0; i < addressComponentsSize; i++) {
                AddressComponent addressComponent = new AddressComponent();
                addressComponent.setLongName(result.getAsString("results[" + rs + "]/address_components[" + i + "]/long_name"));
                addressComponent.setShortName(result.getAsString("results[" + rs + "]/address_components[" + i + "]/short_name"));

                addressComponent.setType(result.getAsString("results[" + rs + "]/address_components[" + i + "]/types"));

                addressComponents.add(addressComponent);
            }

            googleGeoCodeResponse.setAddressComponents(addressComponents);

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

            googleGeoCodeResponse.setGeometry(geometry);
            
            googleGeoCodeResponse.setType(result.getAsString("results[" + rs + "]/types"));

            responses.add(googleGeoCodeResponse);
        }
        return responses;
    }
}
