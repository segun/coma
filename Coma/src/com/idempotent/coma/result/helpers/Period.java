/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result.helpers;

/**
 *
 * @author aardvocate
 * 
 * @since 1.0
 */
public class Period {
    Time open;
    Time close;

    public Time getOpen() {
        return open;
    }

    public void setOpen(Time open) {
        this.open = open;
    }

    public Time getClose() {
        return close;
    }

    public void setClose(Time close) {
        this.close = close;
    }        
}
