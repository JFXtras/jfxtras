package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;

/**
 * Utility methods for FXML files
 * 
 * @author David Bal
 *
 */
public class FXMLUtilities
{
    /** Convenience method to load FXML files */
    protected static void loadFxml(URL fxmlFile, Object rootController)
    {
        FXMLLoader loader = new FXMLLoader(fxmlFile);
        loader.setController(rootController);
        loader.setRoot(rootController);
        loader.setResources(Settings.resources);
        try {
            loader.load();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
