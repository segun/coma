/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.google.api;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.Util;
import com.codename1.location.Location;
import com.codename1.processing.Result;
import com.idempotent.coma.Coma;
import com.idempotent.coma.callback.CallNext;
import com.idempotent.coma.result.GoogleDirectionResult;
import com.idempotent.coma.result.helpers.Bounds;
import com.idempotent.coma.result.helpers.Distance;
import com.idempotent.coma.result.helpers.Duration;
import com.idempotent.coma.result.helpers.Leg;
import com.idempotent.coma.result.helpers.Polyline;
import com.idempotent.coma.result.helpers.SingleRoute;
import com.idempotent.coma.result.helpers.Step;
import com.idempotent.coma.urlhelper.URLConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Please read https://developers.google.com/maps/documentation/directions/
 * before proceeding to use the API
 *
 * @author aardvocate
 */
public class Direction {

    Coma coma;

    public Direction(Coma coma) {
        this.coma = coma;
    }

    public void getDirections(String from, String to, String how, boolean avoidTolls, boolean avoidHighways, final CallNext callNext, String... otherParameters) {

        String url = URLConstants.DIRECTIONS_API_URL;



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
            for (String otherParam : otherParameters) {
                if (otherParam.indexOf("&") == 0) {
                    query += otherParam;
                } else {
                    query += "&" + otherParam;
                }
            }
        }

        System.out.println(url);

        connect(url, callNext);
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
            singleRoute.setDecodedPolyline(overviewPolyline.decode());

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
                Result res = Result.fromContent(input, Result.JSON);
                GoogleDirectionResult directionResult = parseDirectionsResult(res);
                callNext.onSuccess(directionResult);
            }
        };

        request.setUrl(url);
        request.setPost(false);
        request.setDuplicateSupported(true);

        coma.getNetworkManager().addToQueue(request);
    }    
}
