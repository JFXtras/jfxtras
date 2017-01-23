/**
 * WindowToFrontTest.java
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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.window.Window;
import jfxtras.test.JFXtrasGuiTest;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class WindowToFrontTest extends JFXtrasGuiTest {

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

        clickOn(windowOne, MouseButton.PRIMARY).sleep(1000);

        assertTrue("After clicking on first window, first window in list must be window 2",
                pane.getChildren().stream().filter(w -> w instanceof Window).
                findFirst().get().getId().equals("window-control-2"));

    }

}
