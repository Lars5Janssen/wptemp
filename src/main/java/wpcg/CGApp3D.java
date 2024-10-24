package wpcg;

import com.jme3.system.AppSettings;
import wpcg.lab.a1.TriangleMeshScene;
import wpcg.ui.jmonkey.CG3DApplication;

/**
 * Base application for all 3D scenes.
 */
public class CGApp3D extends CG3DApplication {
    public CGApp3D() {
        // Assignment 1
        setScene3D(new TriangleMeshScene());
    }

    public static void main(String[] args) {
        // Setup JME app
        var app = new CGApp3D();
        AppSettings appSettings = new AppSettings(true);
        appSettings.setTitle("HAW Hamburg: 2D and 3D Graphics");
        appSettings.setResolution(800, 600);
        appSettings.setFullscreen(false);
        appSettings.setAudioRenderer(null);
        app.setSettings(appSettings);
        app.setShowSettings(false);
        app.setDisplayStatView(false);
        app.start();
    }
}
