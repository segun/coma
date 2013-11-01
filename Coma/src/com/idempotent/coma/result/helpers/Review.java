/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.result.helpers;

import java.util.List;

/**
 *
 * @author aardvocate
 * 
 * @since 1.0
 */
public class Review {
    public List<AspectRating> aspects;
    private String authorName;
    private String authorURL;
    private String text;
    private long time;

    public List<AspectRating> getAspects() {
        return aspects;
    }

    public void setAspects(List<AspectRating> aspects) {
        this.aspects = aspects;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorURL() {
        return authorURL;
    }

    public void setAuthorURL(String authorURL) {
        this.authorURL = authorURL;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }    
}
