/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result;

import java.util.List;

/**
 *
 * @author aardvocate
 */
public class AddressComponent {
    private  String longName;
    private  String shortName;
    private  String types;

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getType() {
        return types;
    }

    public void setType(String type) {
        this.types = type;
    }

}
