/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma;

import com.codename1.location.Location;
import com.codename1.maps.Coord;
import com.codename1.processing.Result;
import com.idempotent.coma.geocode.result.Distance;
import com.idempotent.coma.geocode.result.Duration;
import com.idempotent.coma.geocode.result.GoogleDirectionResult;
import com.idempotent.coma.geocode.result.GoogleGeocodeResult;
import com.idempotent.coma.geocode.result.Leg;
import com.idempotent.coma.geocode.result.SingleResult;
import com.idempotent.coma.geocode.result.SingleRoute;
import com.idempotent.coma.geocode.result.Step;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author aardvocate
 */
public class ComaTest {

    public ComaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Setting Up Class");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Tearing Down Class");
    }

    @Before
    public void setUp() {
        System.out.println("Setting Up Test");
    }

    @After
    public void tearDown() {
        System.out.println("Tearing Down Test");
    }

    @Test
    public void testParseGeocode() throws IllegalArgumentException, IOException {
        System.out.println("parseGeoCode");
        InputStream is = ComaTest.class.getResourceAsStream("/geocode.json");
        Result result = Result.fromContent(is, Result.JSON);
        Coma coma = new Coma();
        GoogleGeocodeResult geocodeResult = coma.parseGeoCodeResult(result);
        List<SingleResult> results = geocodeResult.getResults();

        assertEquals(results.size(), 10);
        assertEquals(results.get(0).getAddressComponents().size(), 9);
        assertEquals(results.get(1).getAddressComponents().get(0).getLongName(), "Gramercy Park");
        assertEquals(results.get(2).getAddressComponents().get(5).getShortName(), "US");
        assertEquals(results.get(3).getFormattedAddress(), "Midtown, New York, NY, USA");
        assertEquals(results.get(0).getGeometry().getLocationType(), "ROOFTOP");
        assertEquals(results.get(0).getGeometry().getLocation().getLatitude() + "", "40.737342");
        assertEquals(results.get(1).getGeometry().getLocation().getLongitude() + "", "-73.9844722");
        assertEquals(results.get(1).getGeometry().getViewPort().getNorthEast().getLatitude() + "", "40.74018510000001");
        assertEquals(results.get(2).getGeometry().getBounds().getSouthWest().getLongitude() + "", "-73.994028");
        assertEquals(geocodeResult.getStatus(), "OK");
        assertEquals(results.get(1).getType(), "neighborhood");
        assertTrue(geocodeResult.getRaw() instanceof Result);
    }

    @Test
    public void testParseReverseGeocode() throws IllegalArgumentException, IOException {
        System.out.println("parseReverseGeoCode");
        InputStream is = ComaTest.class.getResourceAsStream("/reverse_geocode.json");
        Result result = Result.fromContent(is, Result.JSON);
        Coma coma = new Coma();

        GoogleGeocodeResult geocodeResult = coma.parseGeoCodeResult(result);

        List<SingleResult> results = geocodeResult.getResults();

        assertEquals(results.size(), 12);
        assertEquals(results.get(0).getAddressComponents().size(), 8);
        assertEquals(results.get(1).getAddressComponents().get(0).getLongName(), "Grand St/Bedford Av");
        assertEquals(results.get(2).getAddressComponents().get(5).getShortName(), "US");
        assertEquals(results.get(3).getFormattedAddress(), "Bedford Av/Grand St, Brooklyn, NY 11211, USA");
        assertEquals(results.get(0).getGeometry().getLocationType(), "ROOFTOP");
        assertEquals(results.get(0).getGeometry().getLocation().getLatitude() + "", "40.714232");
        assertEquals(results.get(1).getGeometry().getLocation().getLongitude() + "", "-73.961151");
        assertEquals(results.get(1).getGeometry().getViewPort().getNorthEast().getLatitude() + "", "40.71566998029149");
        assertEquals(geocodeResult.getStatus(), "OK");
        assertEquals(results.get(1).getType(), "bus_station");
        assertTrue(geocodeResult.getRaw() instanceof Result);
    }

    @Test
    public void testParseDirection() throws IllegalArgumentException, IOException {
        System.out.println("parseDirection");
        InputStream is = ComaTest.class.getResourceAsStream("/direction.json");
        Result result = Result.fromContent(is, Result.JSON);
        Coma coma = new Coma();

        GoogleDirectionResult directionResult = coma.parseDirectionsResult(result);

        List<SingleRoute> routes = directionResult.getRoutes();

        assertEquals(routes.size(), 1);
        assertEquals(directionResult.getStatus(), "OK");
        assertNotNull(directionResult.getRaw());
        assertTrue(directionResult.getRaw() instanceof Result);
        assertEquals(routes.get(0).getSummary(), "ON-401 E");


        Location bNorthEastLocation = new Location(), bSouthWestLocation = new Location();
        bNorthEastLocation.setLatitude(45.5101458);
        bNorthEastLocation.setLongitude(-73.55252489999999);
        bSouthWestLocation.setLatitude(43.6533103);
        bSouthWestLocation.setLongitude(-79.38373319999999);

        assertEquals(routes.get(0).getBounds().getNorthEast().getLatitude() + "", bNorthEastLocation.getLatitude() + "");
        assertEquals(routes.get(0).getBounds().getNorthEast().getLongitude() + "", bNorthEastLocation.getLongitude() + "");
        assertEquals(routes.get(0).getBounds().getSouthWest().getLatitude() + "", bSouthWestLocation.getLatitude() + "");
        assertEquals(routes.get(0).getBounds().getSouthWest().getLongitude() + "", bSouthWestLocation.getLongitude() + "");        
        
        List<Leg> legs = routes.get(0).getLegs();
        assertTrue(legs.size() == 1);
        
        Leg leg = legs.get(0);
        
        assertEquals(leg.getEndAddress(), "Montreal, QC, Canada");
        assertEquals(leg.getStartAddress(), "Toronto, ON, Canada");
        
        Location startLocation = new Location(), endLocation = new Location();
        startLocation.setLatitude(43.6533103);
        startLocation.setLongitude(-79.3827675);
        
        endLocation.setLatitude(45.5085712);
        endLocation.setLongitude(-73.5537674);   
        
        assertEquals(leg.getStartLocation().getLatitude() + "", startLocation.getLatitude() + "");
        assertEquals(leg.getStartLocation().getLongitude() + "", startLocation.getLongitude() + "");
        
        assertEquals(leg.getEndLocation().getLatitude() + "", endLocation.getLatitude() + "");
        assertEquals(leg.getEndLocation().getLongitude() + "", endLocation.getLongitude() + "");        
        
        Distance distance = new Distance();
        distance.setText("542 km");
        distance.setValue(542389);
        
        assertEquals(leg.getDistance().getText(), distance.getText());
        assertEquals(leg.getDistance().getValue() + "", distance.getValue() + "");
        
        Duration duration = new Duration();
        duration.setText("5 hours 13 mins");
        duration.setValue(18763);
        
        assertEquals(leg.getDuration().getText(), duration.getText());
        assertEquals(leg.getDuration().getValue() + "", duration.getValue() + "");        
        
        List<Step> steps = leg.getSteps();
        assertEquals(steps.size(), 13);
        
        Step step = steps.get(3);
        assertEquals(step.getDistance().getText(), "12.9 km");
        assertEquals(step.getDistance().getValue() + "", 12862.0 + "");
        
        assertEquals(step.getDuration().getText(), "9 mins");
        assertEquals(step.getDuration().getValue() + "", 534.0 + "");    
        
        assertEquals(step.getStarLocation().getLatitude() + "", 43.6636083 + "");
        assertEquals(step.getStarLocation().getLongitude() + "", -79.3554656 + "");
        
        assertEquals(step.getEndLocation().getLatitude() + "", 43.762409 + "");
        assertEquals(step.getEndLocation().getLongitude() + "", -79.33659 + "");        
        
        assertEquals(step.getTravelMode(), "DRIVING");
        
        List<Coord> decodedPolyline = routes.get(0).getDecodedPolyline();
        assertEquals(decodedPolyline.size(), 232);                
    }
}
