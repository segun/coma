/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.google.api;

import com.codename1.io.ConnectionRequest;
import com.codename1.location.Location;
import com.codename1.processing.Result;
import com.idempotent.coma.Coma;
import com.idempotent.coma.callback.CallNext;
import com.idempotent.coma.result.GooglePlaceDetailsResult;
import com.idempotent.coma.result.helpers.AddressComponent;
import com.idempotent.coma.result.helpers.AspectRating;
import com.idempotent.coma.result.helpers.Event;
import com.idempotent.coma.result.helpers.Geometry;
import com.idempotent.coma.result.helpers.OpeningHours;
import com.idempotent.coma.result.helpers.Period;
import com.idempotent.coma.result.helpers.Photo;
import com.idempotent.coma.result.helpers.Review;
import com.idempotent.coma.result.helpers.SinglePlaceDetails;
import com.idempotent.coma.result.helpers.Time;
import com.idempotent.coma.urlhelper.URLConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Please read https://developers.google.com/places/documentation/details before
 * proceeding to use the API
 *
 * @author aardvocate
 * 
 * @since 1.0
 */
public class PlaceDetails {

    Coma coma;
    String apiKey;

    public PlaceDetails(Coma coma, String apiKey) {
        this.coma = coma;
        this.apiKey = apiKey;
    }

    /**
     * 
     * @param reference You get this from calling the PlacesSearch api
     * @param callNext call onSuccess method will be called with an instance of GooglePlaceDetailsResult
     * @param otherParameters 
     * @see com.idempotent.coma.result.GooglePlaceDetailsResult
     */
    public void getDetails(String reference, CallNext callNext, String... otherParameters) {
        String query = "&key=" + apiKey
                + "&reference=" + reference;

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
                GooglePlaceDetailsResult placeDetailsResult = parsePlaceDetailsResult(res);
                callNext.onSuccess(placeDetailsResult);
            }
        };

        request.setUrl(url);
        request.setPost(false);
        request.setDuplicateSupported(true);

        coma.getNetworkManager().addToQueue(request);
    }

    /**
     * This method is public only because we want to test it. 
     * You can make it private if you want.
     * @param result
     * @return GooglePlaceDetailsResult
     */    
    public GooglePlaceDetailsResult parsePlaceDetailsResult(Result result) {
        GooglePlaceDetailsResult placeDetailsResult = new GooglePlaceDetailsResult();

        placeDetailsResult.setRaw(result);
        placeDetailsResult.setHtmlAttributions(result.getAsString("html_attributions"));
        placeDetailsResult.setStatus(result.getAsString("status"));

        SinglePlaceDetails singlePlaceDetails = new SinglePlaceDetails();
        List<AddressComponent> addressComponents = new ArrayList<AddressComponent>();
        int acs = result.getSizeOfArray("result/address_components");

        for (int i = 0; i < acs; i++) {
            AddressComponent addressComponent = new AddressComponent();
            addressComponent.setLongName(result.getAsString("result/address_components[" + i + "]/long_name"));
            addressComponent.setShortName(result.getAsString("result/address_components[" + i + "]/short_name"));
            addressComponent.setTypes(result.getAsStringArray("result/address_components[" + i + "]/types"));
            addressComponents.add(addressComponent);
        }

        List<Event> events = new ArrayList<Event>();
        int es = result.getSizeOfArray("result/events");

        for (int i = 0; i < es; i++) {
            Event event = new Event();
            event.setEventID(result.getAsString("result/events[" + i + "]/event_id"));
            event.setSummary(result.getAsString("result/events[" + i + "]/summary"));
            event.setUrl(result.getAsString("result/events[" + i + "]/url"));
            event.setStartTime(result.getAsLong("result/events[" + i + "]/start_time"));
            events.add(event);
        }

        singlePlaceDetails.setFormattedAddress(result.getAsString("result/formatted_address"));
        singlePlaceDetails.setFormattedPhoneNumber(result.getAsString("result/formatted_phone_number"));

        Geometry geometry = new Geometry();
        Location location = new Location();
        location.setLatitude(result.getAsDouble("result/geometry/location/lat"));
        location.setLongitude(result.getAsDouble("result/geometry/location/lng"));
        geometry.setLocation(location);

        singlePlaceDetails.setIcon(result.getAsString("result/icon"));
        singlePlaceDetails.setId(result.getAsString("result/id"));
        singlePlaceDetails.setInternationalPhoneNumber(result.getAsString("result/international_phone_number"));
        singlePlaceDetails.setName(result.getAsString("result/name"));

        OpeningHours openingHours = new OpeningHours();
        openingHours.setOpenNow(result.getAsBoolean("result/opening_hours/open_now"));
        List<Period> periods = new ArrayList<Period>();

        int ps = result.getSizeOfArray("result/opening_hours/periods");
        for (int i = 0; i < ps; i++) {
            Period period = new Period();
            Time openTime = new Time();
            openTime.setDay(result.getAsInteger("result/opening_hours/periods[" + i + "]/open/day"));
            openTime.setTime(result.getAsInteger("result/opening_hours/periods[" + i + "]/open/time"));

            Time closeTime = new Time();
            closeTime.setDay(result.getAsInteger("result/opening_hours/periods[" + i + "]/close/day"));
            closeTime.setTime(result.getAsInteger("result/opening_hours/periods[" + i + "]/close/time"));

            period.setClose(closeTime);
            period.setOpen(openTime);
            periods.add(period);
        }
        openingHours.setPeriods(periods);

        List<Photo> photos = new ArrayList<Photo>();
        int phs = result.getSizeOfArray("result/photos");

        for (int i = 0; i < phs; i++) {
            Photo photo = new Photo();

            photo.setHeight(result.getAsDouble("result/photos[" + i + "]/height"));
            photo.setWidth(result.getAsDouble("result/photos[" + i + "]/width"));
            photo.setHtmlAttributions(result.getAsStringArray("result/photos[" + i + "]/html_attributions"));
            photo.setPhotoReference(result.getAsString("result/photos[" + i + "]/photo_reference"));
            photos.add(photo);
        }

        singlePlaceDetails.setPriceLevel((int) result.getAsDouble("result/price_level"));
        singlePlaceDetails.setRating(result.getAsDouble("result/rating"));
        singlePlaceDetails.setReference(result.getAsString("result/reference"));
        
        List<Review> reviews = new ArrayList<Review>();
        int rvs = result.getSizeOfArray("result/reviews");
        
        for(int i = 0; i < rvs; i++) {
            Review review = new Review();
            review.setTime((long) result.getAsDouble("result/reviews[" + i +"]/time"));
            review.setText(result.getAsString("result/reviews[" + i +"]/text"));
            review.setAuthorURL(result.getAsString("result/reviews[" + i +"]/author_url"));
            review.setAuthorName(result.getAsString("result/reviews[" + i +"]/author_name"));
            
            List<AspectRating> aspectRatings = new ArrayList<AspectRating>();
            int rar = result.getSizeOfArray("result/reviews[" + i + "]/aspects");
            for(int j = 0; j < rar; j++) {
                AspectRating rating = new AspectRating();
                rating.setRating((int) result.getAsDouble("result/reviews[" + i + "]/aspects["+ j + "]/rating"));                     
                rating.setType(result.getAsString("result/reviews[" + i + "]/aspects["+ j + "]/type"));                     
                aspectRatings.add(rating);
            }
            review.setAspects(aspectRatings);
            reviews.add(review);
        }
        
        
        singlePlaceDetails.setVicinity(result.getAsString("result/vicinity"));
        singlePlaceDetails.setWebsite(result.getAsString("result/website"));
        singlePlaceDetails.setUrl(result.getAsString("result/url"));
        singlePlaceDetails.setTypes(result.getAsStringArray("result/types"));
        singlePlaceDetails.setUTCOffset(result.getAsString("result/utc_offset"));
        singlePlaceDetails.setReviews(reviews);
        singlePlaceDetails.setPhotos(photos);
        singlePlaceDetails.setOpeningHours(openingHours);
        singlePlaceDetails.setGeometry(geometry);
        singlePlaceDetails.setEvents(events);
        singlePlaceDetails.setAddressComponents(addressComponents);
        placeDetailsResult.setSinglePlaceDetails(singlePlaceDetails);

        return placeDetailsResult;
    }
}
