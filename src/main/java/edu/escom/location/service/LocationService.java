package edu.escom.location.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.OverDailyLimitException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import edu.escom.location.model.Coordinate;
import edu.escom.location.model.Location;
import edu.escom.location.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class LocationService {

    private static LocationService locationService;
    private Logger logger = LoggerFactory.getLogger(LocationService.class);
    private String API_KEY = "AIzaSyBEBgmIWMqNvnQeuB_yQYqZL3C_pGSTlxM";
    private GeoApiContext context;

    private LocationService(){
        context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();
    }

    public static synchronized LocationService getInstance(){
        if(Objects.isNull(locationService)){
            locationService = new LocationService();
        }

        return locationService;
    }

    public void setApiKey(String apiKey) throws RuntimeException {
        if(StringUtils.isBlank(apiKey)){
            throw new RuntimeException("API KEY must not be null");
        }

        context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    public Location searchByString(String stringAddress) throws ApiException, InterruptedException, IOException {
        GeocodingResult[] results;

        results = GeocodingApi.newRequest(context).address(stringAddress).region("MX").language("ES").await();

        if( results.length == 0 ){
            return null;
        }

        GeocodingResult result;
        Location model;

        if( results.length > 1 ){
            result = bestChoice( results );
        }else{
            result = results[0];
        }

        model = handleModel(result);

        handleCity(model);

        return model;
    }

    private void handleCity(Location model) throws InterruptedException, ApiException, IOException {
        String lat = model.getCoordinate().getLatitude().toString();
        String lon = model.getCoordinate().getLongitude().toString();

        LatLng latLng = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
        GeocodingResult[] results = GeocodingApi.newRequest(context).latlng(latLng).region("MX").language("ES").await();

        if(results.length > 0){
            GeocodingResult result = results[0];
            AddressComponent[] components = result.addressComponents;

            for(AddressComponent component: components){
                String city = StringUtil.cleanCity(component.longName);
                if( model.getShortState().equalsIgnoreCase("cdmx")){
                    if( search(component, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_3)){
                        model.setCity(city);
                        continue;
                    }
                }else{
                    //BUSCAR OTRA FORMA DE LOCALITY
                    if( search(component, AddressComponentType.LOCALITY)){
                        model.setCity(city);
                        continue;
                    }

                    if( search(component, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2)){
                        model.setCity(city);
                        continue;
                    }
                }
            }

        }
    }

    private Location handleModel(GeocodingResult result){
        AddressComponent[] components = result.addressComponents;

        Location model = new Location();
        model.setFullAddress(result.formattedAddress);
        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(result.geometry.location.lat);
        coordinate.setLongitude(result.geometry.location.lng);

        model.setCoordinate(coordinate);
        model.setPrecision(result.geometry.locationType.name());
        model.setPlaceId(result.placeId);

        for(AddressComponent component: components){
            if (search(component, AddressComponentType.STREET_NUMBER)) {
                handleStreetNumber(component.longName, model);
                continue;
            }

            if (search(component, AddressComponentType.COUNTRY)) {
                model.setCountry(component.longName);
                continue;
            }

            if (search(component, AddressComponentType.POSTAL_CODE)) {
                model.setPostalCode(component.longName);
                continue;
            }

            if (search(component, AddressComponentType.ROUTE)) {
                model.setStreet(component.longName);
                continue;
            }

            if (search(component, AddressComponentType.STREET_ADDRESS)) {
                model.setStreet(component.longName);
                continue;
            }

            if (search(component, AddressComponentType.SUBLOCALITY)) {
                model.setDistrict(component.longName);
                continue;
            }

            if(search(component, AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1)) {
                model.setState(component.longName);
                model.setShortState(component.shortName);
                continue;
            }

        }

        handleVoidStreetNumber(model);

        return model;
    }

    private void handleStreetNumber(String value, Location model){
        String[] tokenizedNumber = value.split("-");

        if(org.apache.commons.lang.StringUtils.isNotBlank(value)){
            model.setStreetNumberComplement(value);
        }

        if (Objects.nonNull(tokenizedNumber) && tokenizedNumber.length > 1) {
            value = tokenizedNumber[0];
        }

        value = value.replaceAll("[^\\d.]", "");

        try {
            model.setStreetNumber(Integer.valueOf(value));
        } catch (Exception e) {
            handleVoidStreetNumber(model);
        }
    }

    private void handleVoidStreetNumber(Location model) {
        if(Objects.isNull(model.getStreetNumber())){
            model.setStreetNumber(-1);
        }
    }

    private boolean search(AddressComponent component, AddressComponentType tag){
        return Arrays.asList(component.types).stream().
                anyMatch(type -> type.equals(tag));
    }

    private GeocodingResult bestChoice(GeocodingResult[] results) {
        return results[0];
    }

}
