 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idempotent.coma.maps.component;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Image;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.idempotent.coma.maps.listener.ControlsListener;
import com.idempotent.coma.maps.provider.GoogleStreetViewProvider;
import java.io.IOException;

/**
 *
 * @author aardvocate
 */
public class GoogleStreetViewMap extends Container {
    //MapComponent

    GoogleStreetViewProvider provider;
    /**
     * fov
     */
    private int zoom = 90;
    private int heading = -360;
    private int pitch = 0;
    private Container imageContainer;

    public GoogleStreetViewMap(GoogleStreetViewProvider provider) throws IOException {
        this.provider = provider;

        setLayout(new BorderLayout());

        layoutComponents();
        getAndShowImage();
    }

    public void zoomOut(boolean repaint) {
        zoom = provider.getFov() + GoogleStreetViewProvider.INCREMENT_FOV;
        if (repaint) {
            getAndShowImage();
        }
    }

    public void zoomIn(boolean repaint) {
        zoom = provider.getFov() - GoogleStreetViewProvider.INCREMENT_FOV;
        if (repaint) {
            getAndShowImage();
        }
    }

    public void moveRight(boolean repaint) {
        heading = provider.getHeading() + GoogleStreetViewProvider.INCREMENT_HEADING;
        if (repaint) {
            getAndShowImage();
        }
    }

    public void moveLeft(boolean repaint) {
        heading = provider.getHeading() - GoogleStreetViewProvider.INCREMENT_HEADING;
        if (repaint) {
            getAndShowImage();
        }
    }

    public void moveUp(boolean repaint) {
        pitch = provider.getPitch() + GoogleStreetViewProvider.INCREMENT_PITCH;
        if (repaint) {
            getAndShowImage();
        }
    }

    public void moveDown(boolean repaint) {
        pitch = provider.getPitch() - GoogleStreetViewProvider.INCREMENT_PITCH;
        if (repaint) {
            getAndShowImage();
        }
    }

    private void getAndShowImage() {
        provider.setFov(zoom);
        provider.setHeading(heading);
        provider.setPitch(pitch);

        provider.getStreetView(imageContainer);
    }

    private void layoutComponents() throws IOException {
        Container base = new Container(new LayeredLayout());
        addComponent(BorderLayout.CENTER, base);

        imageContainer = new Container(new BorderLayout());
        base.addComponent(imageContainer);

        Container controlsContainer = new Container(new BorderLayout());
        base.addComponent(controlsContainer);

        Style buttonStyle = new Style();
        buttonStyle.setBgTransparency(0);

        Container zoomBar = new Container(new FlowLayout(Component.RIGHT));
        controlsContainer.addComponent(BorderLayout.SOUTH, zoomBar);

        Button out = new Button(Image.createImage("/minus.png"));
        out.setRolloverIcon(Image.createImage("/minus_s.png"));
        out.setUIID("Label");
        zoomBar.addComponent(out);

        Button in = new Button(Image.createImage("/plus.png"));
        in.setRolloverIcon(Image.createImage("/plus_s.png"));
        in.setUIID("Label");
        zoomBar.addComponent(in);

        Container directionsContainer = new Container(new FlowLayout(Component.RIGHT));
        Container directionsBar = new Container(new BorderLayout());
        directionsContainer.addComponent(directionsBar);

        controlsContainer.addComponent(BorderLayout.EAST, directionsContainer);

        Button up = new Button(Image.createImage("/up.png"));
        up.setUIID("Label");
        up.setRolloverIcon(Image.createImage("/up_s.png"));

        Button down = new Button(Image.createImage("/down.png"));
        down.setUIID("Label");
        down.setRolloverIcon(Image.createImage("/down_s.png"));

        Button right = new Button(Image.createImage("/right.png"));
        right.setUIID("Label");
        right.setRolloverIcon(Image.createImage("/right_s.png"));

        Button left = new Button(Image.createImage("/left.png"));
        left.setUIID("Label");
        left.setRolloverIcon(Image.createImage("/left_s.png"));

        Container upContainer = new Container(new FlowLayout(Component.CENTER));
        upContainer.addComponent(up);

        Container downContainer = new Container(new FlowLayout(Component.CENTER));
        downContainer.addComponent(down);

        directionsBar.addComponent(BorderLayout.NORTH, upContainer);
        directionsBar.addComponent(BorderLayout.SOUTH, downContainer);
        directionsBar.addComponent(BorderLayout.EAST, right);
        directionsBar.addComponent(BorderLayout.WEST, left);

        up.setSelectedStyle(buttonStyle);
        up.setUnselectedStyle(buttonStyle);

        down.setSelectedStyle(buttonStyle);
        down.setUnselectedStyle(buttonStyle);

        left.setSelectedStyle(buttonStyle);
        left.setUnselectedStyle(buttonStyle);

        right.setSelectedStyle(buttonStyle);
        right.setUnselectedStyle(buttonStyle);

        in.setSelectedStyle(buttonStyle);
        in.setUnselectedStyle(buttonStyle);

        out.setSelectedStyle(buttonStyle);
        out.setUnselectedStyle(buttonStyle);
        
        ControlsListener listener = new ControlsListener(this, up, down, right, left, in, out);
        up.addActionListener(listener);
        down.addActionListener(listener);
        right.addActionListener(listener);
        left.addActionListener(listener);
        in.addActionListener(listener);
        out.addActionListener(listener);
    }
}