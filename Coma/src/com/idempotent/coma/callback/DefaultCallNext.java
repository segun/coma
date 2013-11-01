/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.callback;

/**
 * A CallNext instance that do nothing.
 * 
 * @author aardvocate
 * 
 * @since 1.0
 */
public class DefaultCallNext implements CallNext{

    public void onError(Object retVal) {
        //do something with retval
    }

    public void onSuccess(Object retVal) {
        //do something with retval
    }
    
}
