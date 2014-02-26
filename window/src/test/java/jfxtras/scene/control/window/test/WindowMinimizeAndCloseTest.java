/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxtras.scene.control.window.test;

import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.window.CloseIcon;
import jfxtras.scene.control.window.MinimizeIcon;
import jfxtras.scene.control.window.Window;
import jfxtras.scene.control.window.WindowIcon;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class WindowMinimizeAndCloseTest extends GuiTest {

    private Pane pane;
    private Window window;
    private WindowIcon minimizeIcon;
    private WindowIcon closeIcon;

    @Override
    public Parent getRootNode() {
        
        // creates a pane that contains one window
        pane = new Pane();
        pane.setPrefSize(1024, 768);
        window = new Window("Window Title");
        window.setId("window-control-1");
        minimizeIcon = new MinimizeIcon(window);
        closeIcon = new CloseIcon(window);
        window.getLeftIcons().addAll(closeIcon, minimizeIcon);
        window.setPrefSize(600, 400);
        window.setLayoutX(100);
        window.setLayoutY(100);

        pane.getChildren().add(window);

        return pane;
    }

    @Test
    public void windowShouldMinimize() {

        assertFalse("Window must not be minimized", window.isMinimized());

        click(minimizeIcon, MouseButton.PRIMARY).sleep(1000);

        assertTrue("After clicking the minimze icon the window must be minimized",
                window.isMinimized());
        
        click(minimizeIcon, MouseButton.PRIMARY).sleep(1000);

        assertFalse("After clicking the minimze icon again the window must be maximized",
                window.isMinimized());
    }

    @Test
    public void windowShouldClose() {

        assertTrue("Pane must contain one window control",
                pane.getChildren().stream().filter(n -> n instanceof Window).count() == 1);

        click(closeIcon, MouseButton.PRIMARY).sleep(1000);

        assertTrue("After clicking the close icon the pane must not contain window controls",
                pane.getChildren().stream().filter(n -> n instanceof Window).count() == 0);
    }
}
