/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.stringshelper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public class MStrings {

    public static boolean contains(String from, String check) {
        return from.indexOf(check) >= 0 ? true : false;
    }

    public static List<String> splitString(String string, String delimiter) {
        List retValue = new ArrayList<String>();
        String myString = string + delimiter;
        while (myString.length() > 0) {
            try {
                String one = myString.substring(0, myString.indexOf(delimiter));
                retValue.add(one);
                myString = myString.substring(one.length() + delimiter.length(), myString.length());
            } catch (StringIndexOutOfBoundsException siobe) {
                break;
            }
        }
        return retValue;
    }

    public static String[] sort(String toSort[], boolean asc) {
        String[] retVal = toSort;
        for (int i = 0; i < retVal.length; i++) {
            for (int j = i; j < retVal.length; j++) {
                if (asc) {
                    if (retVal[i].compareTo(retVal[j]) > 0) {
                        String temp = retVal[i];
                        retVal[i] = retVal[j];
                        retVal[j] = temp;
                    }
                } else {
                    if (retVal[i].compareTo(retVal[j]) < 0) {
                        String temp = retVal[i];
                        retVal[i] = retVal[j];
                        retVal[j] = temp;
                    }
                }
            }
        }
        return retVal;
    }

    public static boolean isNumeric(String a) {
        try {
            Long.parseLong(a);
            return true;
        } catch (NumberFormatException nfe) {
            try {
                Integer.parseInt(a);
                return true;
            } catch (NumberFormatException nfe2) {
                try {
                    Double.parseDouble(a);
                    return true;
                } catch(NumberFormatException nfe3) {
                    return false;
                }
            }
        }
    }
}
