/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.maps.listener;

import com.codename1.ui.Button;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.idempotent.coma.maps.component.GoogleStreetViewMap;

/**
 *
 * @author aardvocate
 */
public class ControlsListener implements ActionListener {

    private Button up, down, right, left, in, out;
    private GoogleStreetViewMap map;

    public ControlsListener(GoogleStreetViewMap map, Button up, Button down, Button right, Button left, Button in, Button out) {
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.in = in;
        this.out = out;
        this.map = map;
    }
        
    public void actionPerformed(ActionEvent evt) {
        Button b = (Button) evt.getSource();
        
        if(b.equals(in)) {
            map.zoomIn(true);
        }
        
        if(b.equals(out)) {
            map.zoomOut(true);
        }
        
        if(b.equals(left)) {
            map.moveLeft(true);
        }
        
        if(b.equals(right)) {
            map.moveRight(true);
        }
        
        if(b.equals(up)) {
            map.moveUp(true);
        }
        
        if(b.equals(down)) {
            map.moveDown(true);
        }
        
    }
    
}
