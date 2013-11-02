/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.maps.provider;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;

/**
 *
 * @author aardvocate
 */
public class GoogleStreetViewMap extends Container {

    GoogleStreetViewProvider provider;
    private int zoom = 90;

    public GoogleStreetViewMap(GoogleStreetViewProvider provider) {
        this.provider = provider;

        setLayout(new BorderLayout());
        Container buttonsbar = new Container(new FlowLayout(Component.RIGHT));
        Button out = new Button("-");
        out.setUIID("MapZoomOut");

        out.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                zoomOut(true);
            }
        });
        buttonsbar.addComponent(out);
        Button in = new Button("+");
        in.setUIID("MapZoomIn");
        in.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                zoomIn(true);
            }
        });
        buttonsbar.addComponent(in);
        addComponent(BorderLayout.SOUTH, buttonsbar);

        getAndShowImage();
    }

    public void zoomOut(boolean repaint) {
        zoom = provider.getFov() + 10;
        if (repaint) {
            getAndShowImage();
        }
    }

    public void zoomIn(boolean repaint) {
        zoom = provider.getFov() - 10;
        if (repaint) {
            getAndShowImage();
        }
    }

    public void keyPressed(int keyCode) {
        int oldZoom = zoom;

        if (isLeftKey(keyCode)) {
            //moveLeft(false);
        } else if (isRightKey(keyCode)) {
            //moveRight(false);
        } else if (isDownKey(keyCode)) {
            //moveDown(false);
        } else if (isUpKey(keyCode)) {
            //moveUp(false);
        }

        if (isZoomInKey(keyCode)) {
            zoomIn(false);
        }
        if (isZoomOutKey(keyCode)) {
            zoomOut(false);
        }

        super.keyPressed(keyCode);
    }

    /**
     * Returns true if this is a left keycode
     *
     * @param keyCode
     * @return true if this is a left keycode
     */
    protected boolean isLeftKey(int keyCode) {
        int game = Display.getInstance().getGameAction(keyCode);
        return game == Display.GAME_LEFT;
    }

    /**
     * Returns true if this is a right keycode
     *
     * @param keyCode
     * @return
     */
    protected boolean isRightKey(int keyCode) {
        int game = Display.getInstance().getGameAction(keyCode);
        return game == Display.GAME_RIGHT;
    }

    /**
     * Returns true if this is a down keycode
     *
     * @param keyCode
     * @return
     */
    protected boolean isDownKey(int keyCode) {
        int game = Display.getInstance().getGameAction(keyCode);
        return game == Display.GAME_DOWN;
    }

    /**
     * Returns true if this is a up keycode
     *
     * @param keyCode
     * @return
     */
    protected boolean isUpKey(int keyCode) {
        int game = Display.getInstance().getGameAction(keyCode);
        return game == Display.GAME_UP;
    }

    /**
     * Returns true if this is a zoom in keycode
     *
     * @param keyCode
     * @return
     */
    protected boolean isZoomInKey(int keyCode) {
        return keyCode == '1';
    }

    /**
     * Returns true if this is a zoom out keycode
     *
     * @param keyCode
     * @return
     */
    protected boolean isZoomOutKey(int keyCode) {
        return keyCode == '3';
    }

    /**
     * Returns true if this is a zoom to layers keycode
     *
     * @param keyCode
     * @return
     */
    protected boolean isZoomToLayersKey(int keyCode) {
        return keyCode == '5';
    }

    private void getAndShowImage() {
        provider.setFov(zoom);
        provider.getImageAndDraw(this);
    }
}
