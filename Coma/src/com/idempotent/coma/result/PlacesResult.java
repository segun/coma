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
public class PlacesResult {
    private String[] htmlAttributions;
    private List<SinglePlace> results;
    private String status;

    public String[] getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(String[] htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<SinglePlace> getResults() {
        return results;
    }

    public void setResults(List<SinglePlace> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }        
}
