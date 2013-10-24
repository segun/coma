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
 */
public interface CallNext {
    public void onError(Object retVal);
    public void onSuccess(Object retVal);
}
