/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result;

import com.codename1.processing.Result;
import java.util.List;

/**
 *
 * @author aardvocate
 */
public class GoogleDirectionResult {

    private String status;
    private List<SingleRoute> routes;
    private Result raw;

    public Result getRaw() {
        return raw;
    }

    public void setRaw(Result raw) {
        this.raw = raw;
    }

    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SingleRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<SingleRoute> routes) {
        this.routes = routes;
    }        
}
