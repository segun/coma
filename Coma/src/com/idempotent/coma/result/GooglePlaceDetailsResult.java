/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result;

import com.codename1.processing.Result;
import com.idempotent.coma.result.helpers.SinglePlaceDetails;

/**
 *
 * @author aardvocate
 */
public class GooglePlaceDetailsResult extends GenericResult {
    private SinglePlaceDetails singlePlaceDetails;
    private String htmlAttributions;
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SinglePlaceDetails getSinglePlaceDetails() {
        return singlePlaceDetails;
    }

    public void setSinglePlaceDetails(SinglePlaceDetails singlePlaceDetails) {
        this.singlePlaceDetails = singlePlaceDetails;
    }

    public String getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(String htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }        

    public Result getRaw() {
        return raw;
    }

    public void setRaw(Result raw) {
        this.raw = raw;
    }
        
}
