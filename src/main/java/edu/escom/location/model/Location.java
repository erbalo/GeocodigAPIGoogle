package edu.escom.location.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Location {

    private String country;
    private String state;
    private String city;
    private String district;
    private String postalCode;
    private String addressType;
    private String street;
    private String dependentAddress;
    private Integer streetNumber;
    private String streetNumberComplement;
    private String fullAddress; // formatted street
    private Coordinate coordinate; // Coordinates
    private boolean centroide;
    private String precision;
    private String placeId;
    private String shortState;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDependentAddress() {
        return dependentAddress;
    }

    public void setDependentAddress(String dependentAddress) {
        this.dependentAddress = dependentAddress;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public boolean isCentroide() {
        return centroide;
    }

    public void setCentroide(boolean centroide) {
        this.centroide = centroide;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getPlaceId(){
        return placeId;
    }

    public void setPlaceId(String placeId){
        this.placeId = placeId;
    }

    public String getShortState() {
        if(shortState == null){
            this.shortState = "";
        }

        return shortState;
    }

    public void setShortState(String shortState) {
        this.shortState = shortState;
    }

    public String toString(){
        String returned;
        ObjectMapper mapper = new ObjectMapper();
        try {
            returned = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            returned = null;
        }

        return returned;
    }

    public String getStreetNumberComplement() {
        return streetNumberComplement;
    }

    public void setStreetNumberComplement(String streetNumberComplement) {
        this.streetNumberComplement = streetNumberComplement;
    }
}
