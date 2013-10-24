/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma;

import com.codename1.processing.Result;
import com.idempotent.coma.callback.CallNext;
import com.idempotent.coma.geocode.result.GoogleGeoCodeResponse;
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

    /**
     * Test of geocode method, of class Coma.
     */
    @Test
    public void testGeocode() {
        System.out.println("geocode");
        String street = "";
        String stateOrProvince = "";
        String country = "";
        String latlng = "";
        boolean doReverse = false;
        CallNext callNext = null;
        String otherParameters = "";
        Coma instance = new Coma();
        //instance.geocode(street, stateOrProvince, country, latlng, doReverse, callNext, otherParameters);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        assertTrue(true);
    }
    
    @Test
    public void testParse() throws IllegalArgumentException, IOException {
        System.out.println("parse");
        InputStream is = ComaTest.class.getResourceAsStream("/geocode.json");
        Result result = Result.fromContent(is, Result.JSON);
        Coma coma = new Coma();
        List<GoogleGeoCodeResponse> googleGeoCodeResponses = coma.parse(result);
        
        assertEquals(googleGeoCodeResponses.size(), 10);
        assertEquals(googleGeoCodeResponses.get(0).getAddressComponents().size(), 9);
        assertEquals(googleGeoCodeResponses.get(1).getAddressComponents().get(0).getLongName(), "Gramercy Park");
        assertEquals(googleGeoCodeResponses.get(2).getAddressComponents().get(5).getShortName(), "US");
        assertEquals(googleGeoCodeResponses.get(3).getFormattedAddress(), "Midtown, New York, NY, USA");
        assertEquals(googleGeoCodeResponses.get(0).getGeometry().getLocationType(), "ROOFTOP");
        assertEquals(googleGeoCodeResponses.get(0).getGeometry().getLocation().getLatitude() + "", "40.737342");
        assertEquals(googleGeoCodeResponses.get(1).getGeometry().getLocation().getLongitude() + "", "-73.9844722");
        assertEquals(googleGeoCodeResponses.get(1).getGeometry().getViewPort().getNorthEast().getLatitude() + "", "40.74018510000001");
        assertEquals(googleGeoCodeResponses.get(2).getGeometry().getBounds().getSouthWest().getLongitude() + "", "-73.994028");
        assertEquals(googleGeoCodeResponses.get(2).getStatus(), "OK");
        assertEquals(googleGeoCodeResponses.get(1).getType(), "neighborhood");
        assertTrue(true);
    }
}
