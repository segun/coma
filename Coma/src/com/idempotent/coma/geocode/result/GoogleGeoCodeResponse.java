/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.geocode.result;

import java.util.List;

/**
 *
 * @author aardvocate
 */
public class GoogleGeoCodeResponse {
    private  String status;
    private String type;
    private  Geometry geometry;
    private  String formattedAddress;
    private  List<AddressComponent> addressComponents;
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }        
}