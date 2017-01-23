/**
 * WindowMinimizeAndCloseTest.java
 *
 * Copyright (c) 2011-2016, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.scene.control.window.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.window.CloseIcon;
import jfxtras.scene.control.window.MinimizeIcon;
import jfxtras.scene.control.window.Window;
import jfxtras.scene.control.window.WindowIcon;
import jfxtras.test.JFXtrasGuiTest;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class WindowMinimizeAndCloseTest extends JFXtrasGuiTest {

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

        clickOn(minimizeIcon, MouseButton.PRIMARY).sleep(1000);

        assertTrue("After clicking the minimze icon the window must be minimized",
                window.isMinimized());
        
        clickOn(minimizeIcon, MouseButton.PRIMARY).sleep(1000);

        assertFalse("After clicking the minimze icon again the window must be maximized",
                window.isMinimized());
    }

    @Test
    public void windowShouldClose() {

        assertTrue("Pane must contain one window control",
                pane.getChildren().stream().filter(n -> n instanceof Window).count() == 1);

        clickOn(closeIcon, MouseButton.PRIMARY).sleep(1000);

        assertTrue("After clicking the close icon the pane must not contain window controls",
                pane.getChildren().stream().filter(n -> n instanceof Window).count() == 0);
    }
}
