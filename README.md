coma
====

A google maps API for Codename One.

Most of the maps/maps related API can be called via url (rest based) and they returns JSON/XML.

Parsing the returned data can be an herculean task though. This is where coma excels.

coma parses the retruned data nad returned to you java objects. For instance the below json

```javascript
"bounds" : {
	"northeast" : {
		"lat" : 45.5101458,
		"lng" : -73.55252489999999
	},
	"southwest" : {
		"lat" : 43.6533103,
		"lng" : -79.38373319999999
	}
}
```

will return an object bounds that contains two objects northEast and southWest. Each of northEast and southWest will also contain a lat and lng double variables.

You are adviced to read the API pages properly before attempting to use coma.

The below APIs have been implemented in coma.

Places
	searchPlaces
	getPlaceDetails
Geocode
Directions.
StreetView

coma contains a Google Street View map provider and a Google Street View Map component that shows how to use the provider.


Usage
-----

In the simplest form, coma can be used thus

```java
Coma coma = new Coma();
Result result = Result.fromContent(is, Result.JSON);
Geocode geocode = new Geocode(coma);
GoogleGeocodeResult geocodeResult = geocode.parseGeoCodeResult(result);
List<SingleResult> results = geocodeResult.getResults();
```
