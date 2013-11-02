/**
 * Your application code goes here
 */

package userclasses;

import com.codename1.location.LocationManager;
import generated.StateMachineBase;
import com.codename1.ui.*; 
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.Resources;
import com.idempotent.coma.maps.provider.GoogleStreetViewMap;
import com.idempotent.coma.maps.provider.GoogleStreetViewProvider;
import java.io.IOException;

/**
 *
 * @author Your name here
 */
public class StateMachine extends StateMachineBase {
    public StateMachine(String resFile) {
        super(resFile);
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }
    
    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    protected void initVars(Resources res) {
    }

    @Override
    protected void beforeMain(Form f) {
        try {
            GoogleStreetViewProvider provider = new GoogleStreetViewProvider("AIzaSyBy9QJoaWlNuv1eWLj1twPzyk5ymWACcl4", LocationManager.getLocationManager().getCurrentLocation());
            GoogleStreetViewMap map = new GoogleStreetViewMap(provider);
            f.addComponent(BorderLayout.CENTER, map);
            
            super.beforeMain(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    
}
