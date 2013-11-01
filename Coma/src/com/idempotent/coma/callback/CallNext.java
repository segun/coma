/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.callback;

/**
 * This is a callback interface that has two methods. 
 * You pass an instance of this interface to methods (especially long running methods).
 * The appropriate methods are called when either an error or a success situation is encountered
 * @see com.idempotent.coma.Coma
 * @author aardvocate
 * 
 * @since 1.0
 */
public interface CallNext {
    /**
     * Implement this method. It gets called whenever there's an error in the api call.
     * @param retVal This is always an HashMap containing code and value. Code is the error code and value is the error reason     * 
     */
    public void onError(Object retVal);
    /**
     * Implement this method. It gets called on a successful api call. 
     * @param retVal 
     */    
    public void onSuccess(Object retVal);
}
