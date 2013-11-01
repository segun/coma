/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result;

import com.codename1.processing.Result;

/**
 *
 * @author aardvocate
 * 
 * @since 1.0
 */
public abstract class GenericResult {
    protected String status;
    protected Result raw;

    public abstract String getStatus();        
    public abstract void setStatus(String status);        

    public abstract Result getRaw();

    public abstract void setRaw(Result raw);
}
