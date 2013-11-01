/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result;

import com.codename1.processing.Result;
import com.idempotent.coma.result.helpers.SingleResult;
import java.util.List;

/**
 *
 * @author aardvocate
 * 
 * @since 1.0
 */
public class GoogleGeocodeResult extends GenericResult {
    private List<SingleResult> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getRaw() {
        return raw;
    }

    public void setRaw(Result raw) {
        this.raw = raw;
    }

    public List<SingleResult> getResults() {
        return results;
    }

    public void setResults(List<SingleResult> results) {
        this.results = results;
    }        
}
