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
import com.idempotent.coma.result.GoogleGeocodeResult;
import com.idempotent.coma.result.helpers.AddressComponent;
import com.idempotent.coma.result.helpers.Bounds;
import com.idempotent.coma.result.helpers.Geometry;
import com.idempotent.coma.result.helpers.SingleResult;
import com.idempotent.coma.result.helpers.ViewPort;
import com.idempotent.coma.urlhelper.URLConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Please read https://developers.google.com/maps/documentation/geocoding/
 * before proceeding to use the API
 *
 * @author aardvocate
 */
public class Geocode {

    Coma coma;

    public Geocode(Coma coma) {
        this.coma = coma;
    }

    public void geocode(String street, String stateOrProvince, String country, Location location, boolean doReverse, final CallNext callNext, String... otherParameters) {
        String countryCodesString = "NG";

        String query = "";
        if (doReverse) {
            if (location == null) {
                HashMap<String, String> errorMap = new HashMap<String, String>();
                errorMap.put("code", 500 + "");
                errorMap.put("message", "Location Variable can not be null if doReverse is set to true");
                callNext.onError(errorMap);
            } else {
                query = "&latlng=" + Util.encodeUrl(location.getLatitude() + "," + location.getLongitude());
            }
        } else {
            countryCodesString = coma.getCountryCodes().getCountryCodes();

            query = "address=" + Util.encodeUrl(street + ", " + stateOrProvince);

            if (!countryCodesString.equals("")) {
                query += "&components=country:" + countryCodesString;
            }
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

        String url = URLConstants.GEOCODE_API_URL + query;

        System.out.println(url);
        connect(url, callNext);
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

                addressComponent.setTypes(result.getAsStringArray("results[" + rs + "]/address_components[" + i + "]/types"));

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

            singleResult.setTypes(result.getAsStringArray("results[" + rs + "]/types"));

            allResults.add(singleResult);
        }

        geocodeResult.setResults(allResults);
        return geocodeResult;
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
                GoogleGeocodeResult geocodeResult = parseGeoCodeResult(res);
                callNext.onSuccess(geocodeResult);
            }
        };

        request.setUrl(url);
        request.setPost(false);
        request.setDuplicateSupported(false);

        coma.getNetworkManager().addToQueue(request);        
    }
}
