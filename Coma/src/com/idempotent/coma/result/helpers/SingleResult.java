/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result.helpers;

import java.util.List;

/**
 *
 * @author aardvocate
 * 
 * @since 1.0
 */
public class SingleResult {
    private String[] types;
    private  Geometry geometry;
    private  String formattedAddress;
    private  List<AddressComponent> addressComponents;  

    public Geometry getGeometry() {
        return geometry;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
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