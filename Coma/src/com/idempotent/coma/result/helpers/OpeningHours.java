/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result.helpers;

import com.idempotent.coma.result.helpers.Period;
import java.util.List;

/**
 *
 * @author aardvocate
 */
public class OpeningHours {
    private boolean openNow;
    private List<Period> periods;

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }        

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }    
}
