/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxtras.scene.control.window.test;

import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.window.Window;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class WindowToFrontTest extends GuiTest {

    private Pane pane;
    private Window windowOne;
    private Window windowTwo;

    @Override
    public Parent getRootNode() {

        // creates a pane that contains one window
        pane = new Pane();
        pane.setPrefSize(1024, 768);

        windowOne = new Window("Window Title 1");
        windowOne.setId("window-control-1");

        windowOne.setPrefSize(600, 400);
        windowOne.setLayoutX(100);
        windowOne.setLayoutY(100);

        windowTwo = new Window("Window Title 2");
        windowTwo.setId("window-control-2");

        windowTwo.setPrefSize(600, 400);
        windowTwo.setLayoutX(500);
        windowTwo.setLayoutY(280);

        pane.getChildren().add(windowOne);
        pane.getChildren().add(windowTwo);

        return pane;
    }

    @Test
    public void windowShouldComeToFront() {

        assertTrue("Pane must contain a window",
                pane.getChildren().stream().filter(w -> w instanceof Window).
                findFirst().isPresent());

        assertTrue("First window must be window 1",
                pane.getChildren().stream().filter(w -> w instanceof Window).
                findFirst().get().getId().equals("window-control-1"));

        click(windowOne, MouseButton.PRIMARY).sleep(1000);

        assertTrue("After clicking on first window, first window in list must be window 2",
                pane.getChildren().stream().filter(w -> w instanceof Window).
                findFirst().get().getId().equals("window-control-2"));

    }

}
