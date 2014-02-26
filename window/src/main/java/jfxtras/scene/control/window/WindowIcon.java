/**
 * WindowIcon.java
 *
 * Copyright (c) 2011-2014, JFXtras
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

package jfxtras.scene.control.window;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;

/**
 * A window icon that is the visual representation of an action that can be
 * performed on the window. Usually, icons are shown in the titlebar of a window
 * and react on clicking gestures.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class WindowIcon extends Control {

    public static final String DEFAULT_STYLE_CLASS = "window-icon";
    private final ObjectProperty<EventHandler<ActionEvent>> onActionProperty =
            new SimpleObjectProperty<>();

    /**
     * Constructor.
     */
    public WindowIcon() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    }

    /**
     * The property that defines the action that shall be performed by/ is
     * represented by this icon.
     *
     * @return the property that defines the action that shall be performed by/
     * is represented by this icon
     */
    public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onActionProperty;
    }

    /**
     * Returns the action handler that defines the action that shall be
     * performed by/ is represented by this icon.
     *
     * @return the action handler that defines the action that shall be
     * performed by/ is represented by this icon
     */
    public EventHandler<ActionEvent> getOnAction() {
        return onActionProperty.get();
    }

    /**
     * Defines the action handler that defines the action that shall be
     * performed by/ is represented by this icon.
     *
     * @param handler action handler that defines the action that shall be
     * performed by/ is represented by this icon
     */
    public void setOnAction(EventHandler<ActionEvent> handler) {
        onActionProperty.set(handler);
    }
}
