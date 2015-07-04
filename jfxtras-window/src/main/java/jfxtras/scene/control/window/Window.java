/**
 * Window.java
 *
 * Copyright (c) 2011-2015, JFXtras
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

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import jfxtras.util.NodeUtil;

/**
 * Window control. A window control is a window node as known from Swing, e.g
 * {@link javax.swing.JInternalFrame}. It can be used to realize MDI based
 * applications. See <a href=https://github.com/miho/VFXWindows-Samples>
 * https://github.com/miho/VFXWindows-Samples</a> for sample code.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class Window extends Control implements SelectableNode {

    /**
     * Default css style.
     */
    public static final String DEFAULT_STYLE =
            "/jfxtras/scene/control/window/default.css";
    /**
     * Default style class for css.
     */
    public static final String DEFAULT_STYLE_CLASS = "window";
    /**
     * Defines whether window is moved to front when user clicks on it
     */
    private boolean moveToFront = true;
    /**
     * Window title property (used by titlebar)
     */
    private final StringProperty titleProperty = new SimpleStringProperty("Title");
    /**
     * Minimize property (defines whether to minimize the window,performed by
     * skin)
     */
    private final BooleanProperty minimizeProperty = new SimpleBooleanProperty();
    /**
     * Resize property (defines whether is the window resizeable,performed by
     * skin)
     */
    private final BooleanProperty resizableProperty = new SimpleBooleanProperty(true);
    /**
     * Resize property (defines whether is the window movable,performed by skin)
     */
    private final BooleanProperty movableProperty = new SimpleBooleanProperty(true);
    /**
     * Content pane property. The content pane is the pane that is responsible
     * for showing user defined nodes/content.
     */
    private final Property<Pane> contentPaneProperty = new SimpleObjectProperty<>();
    /**
     * List of icons shown on the left. TODO replace left/right with more
     * generic position property?
     */
    private final ObservableList<WindowIcon> leftIcons =
            FXCollections.observableArrayList();
    /**
     * List of icons shown on the right. TODO replace left/right with more
     * generic position property?
     */
    private final ObservableList<WindowIcon> rightIcons = FXCollections.observableArrayList();
    /**
     * Defines the width of the border /area where the user can grab the window
     * and resize it.
     */
    private final DoubleProperty resizableBorderWidthProperty = new SimpleDoubleProperty(5);
    /**
     * Defines the titlebar class name. This can be used to define css
     * properties specifically for the titlebar, e.g., background.
     */
    private final StringProperty titleBarStyleClassProperty =
            new SimpleStringProperty("window-titlebar");
    /**
     * defines the action that shall be performed before the window is closed.
     */
    private final ObjectProperty<EventHandler<ActionEvent>> onCloseActionProperty =
            new SimpleObjectProperty<>();
    /**
     * defines the action that shall be performed after the window has been
     * closed.
     */
    private final ObjectProperty<EventHandler<ActionEvent>> onClosedActionProperty =
            new SimpleObjectProperty<>();
    /**
     * defines the transition that shall be played when closing the window.
     */
    private final ObjectProperty<Transition> closeTransitionProperty =
            new SimpleObjectProperty<>();
    /**
     * Selected property (defines whether this window is selected.
     */
    private final BooleanProperty selectedProperty = new SimpleBooleanProperty(false);
    /**
     * Selectable property (defines whether this window is selectable.
     */
    private final BooleanProperty selectableProperty = new SimpleBooleanProperty(true);

    /**
     * Constructor.
     */
    public Window() {
        init();
    }

    /**
     * Constructor.
     *
     * @param title window title
     */
    public Window(String title) {
        setTitle(title);
        init();
    }

    private void init() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setContentPane(new StackPane());

        closeTransitionProperty.addListener(new ChangeListener<Transition>() {
            @Override
            public void changed(ObservableValue<? extends Transition> ov, Transition t, Transition t1) {
                t1.statusProperty().addListener(new ChangeListener<Animation.Status>() {
                    @Override
                    public void changed(ObservableValue<? extends Animation.Status> observableValue,
                            Animation.Status oldValue, Animation.Status newValue) {

                        if (newValue == Animation.Status.STOPPED) {

                            // we don't fire action events twice
                            if (Window.this.getParent() == null) {
                                return;
                            }

                            if (getOnCloseAction() != null) {
                                getOnCloseAction().handle(new ActionEvent(this, Window.this));
                            }

                            // if someone manually removed us from parent, we don't
                            // do anything
                            if (Window.this.getParent() != null) {
                                NodeUtil.removeFromParent(Window.this);
                            }

                            if (getOnClosedAction() != null) {
                                getOnClosedAction().handle(new ActionEvent(this, Window.this));
                            }
                        }
                    }
                });
            }
        });

        ScaleTransition st = new ScaleTransition();
        st.setNode(this);
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(0);
        st.setToY(0);
        st.setDuration(Duration.seconds(0.2));

        setCloseTransition(st);

    }

    @Override
    public String getUserAgentStylesheet() {
        return this.getClass().getResource(DEFAULT_STYLE).toExternalForm();
    }

    /**
     * @return the content pane of this window
     */
    public Pane getContentPane() {
        return contentPaneProperty.getValue();
    }

    /**
     * Defines the content pane of this window.
     *
     * @param contentPane content pane to set
     */
    public void setContentPane(Pane contentPane) {
        contentPaneProperty.setValue(contentPane);
    }

    /**
     * Content pane property.
     *
     * @return content pane property
     */
    public Property<Pane> contentPaneProperty() {
        return contentPaneProperty;
    }

    /**
     * Defines whether this window shall be moved to front when a user clicks on
     * the window.
     *
     * @param moveToFront the state to set
     */
    public void setMoveToFront(boolean moveToFront) {
        this.moveToFront = moveToFront;
    }

    /**
     * Indicates whether the window shall be moved to front when a user clicks
     * on the window.
     *
     * @return <code>true</code> if the window shall be moved to front when a
     * user clicks on the window; <code>false</code> otherwise
     */
    public boolean isMoveToFront() {
        return moveToFront;
    }

    /**
     * Returns the window title.
     *
     * @return the title
     */
    public final String getTitle() {
        return titleProperty.get();
    }

    /**
     * Defines the window title.
     *
     * @param title the title to set
     */
    public final void setTitle(String title) {
        this.titleProperty.set(title);
    }

    /**
     * Returns the window title property.
     *
     * @return the window title property
     */
    public final StringProperty titleProperty() {
        return titleProperty;
    }

    /**
     * Returns a list that contains the icons that are placed on the left side
     * of the titlebar. Add icons to the list to add them to the left side of
     * the window titlebar.
     *
     * @return a list containing the left icons
     *
     * @see #getRightIcons()
     */
    public ObservableList<WindowIcon> getLeftIcons() {
        return leftIcons;
    }

    /**
     * Returns a list that contains the icons that are placed on the right side
     * of the titlebar. Add icons to the list to add them to the right side of
     * the window titlebar.
     *
     * @return a list containing the right icons
     *
     * @see #getLeftIcons()
     */
    public ObservableList<WindowIcon> getRightIcons() {
        return rightIcons;
    }

    /**
     * Defines whether this window shall be minimized.
     *
     * @param v the state to set
     */
    public void setMinimized(boolean v) {
        minimizeProperty.set(v);
    }

    /**
     * Indicates whether the window is currently minimized.
     *
     * @return <code>true</code> if the window is currently minimized;
     * <code>false</code> otherwise
     */
    public boolean isMinimized() {
        return minimizeProperty.get();
    }

    /**
     * Returns the minimize property.
     *
     * @return the minimize property
     */
    public BooleanProperty minimizedProperty() {
        return minimizeProperty;
    }

    /**
     * Defines whether this window shall be resizeable by the user.
     *
     * @param v the state to set
     */
    public void setResizableWindow(boolean v) {
        resizableProperty.set(v);
    }

    /**
     * Indicates whether the window is resizeable by the user.
     *
     * @return <code>true</code> if the window is resizeable; <code>false</code>
     * otherwise
     */
    public boolean isResizableWindow() {
        return resizableProperty.get();
    }

    /**
     * Returns the resize property.
     *
     * @return the minimize property
     */
    public BooleanProperty resizeableWindowProperty() {
        return resizableProperty;
    }

    /**
     * Defines whether this window shall be movable.
     *
     * @param v the state to set
     */
    public void setMovable(boolean v) {
        movableProperty.set(v);
    }

    /**
     * Indicates whether the window is movable.
     *
     * @return <code>true</code> if the window is movable; <code>false</code>
     * otherwise
     */
    public boolean isMovable() {
        return movableProperty.get();
    }

    /**
     * Returns the movable property.
     *
     * @return the minimize property
     */
    public BooleanProperty movableProperty() {
        return movableProperty;
    }

    /**
     * Returns the titlebar style class property.
     *
     * @return the titlebar style class property
     */
    public StringProperty titleBarStyleClassProperty() {
        return titleBarStyleClassProperty;
    }

    /**
     * Defines the CSS style class of the titlebar.
     *
     * @param name the CSS style class name
     */
    public void setTitleBarStyleClass(String name) {
        titleBarStyleClassProperty.set(name);
    }

    /**
     * Returns the CSS style class of the titlebar.
     *
     * @return the CSS style class of the titlebar
     */
    public String getTitleBarStyleClass() {
        return titleBarStyleClassProperty.get();
    }

    /**
     * Returns the resizable border width property.
     *
     * @return the resizable border width property
     *
     * @see #setResizableBorderWidth(double)
     */
    public DoubleProperty resizableBorderWidthProperty() {
        return resizableBorderWidthProperty;
    }

    /**
     * Defines the width of the "resizable border" of the window. The resizable
     * border is usually defined as a rectangular border around the layout
     * bounds of the window where the mouse cursor changes to "resizable" and
     * which allows to resize the window by performing a "dragging gesture",
     * i.e., the user can "grab" the window border and change the size of the
     * window.
     *
     * @param v border width
     */
    public void setResizableBorderWidth(double v) {
        resizableBorderWidthProperty.set(v);
    }

    /**
     * Returns the width of the "resizable border" of the window.
     *
     * @return the width of the "resizable border" of the window
     *
     * @see #setResizableBorderWidth(double)
     */
    public double getResizableBorderWidth() {
        return resizableBorderWidthProperty.get();
    }

    /**
     * Closes this window.
     */
    public void close() {


        // if already closed, we do nothing 
        if (this.getParent() == null) {
            return;
        }

        if (getCloseTransition() != null) {
            getCloseTransition().play();
        } else {
            if (getOnCloseAction() != null) {
                getOnCloseAction().handle(new ActionEvent(this, Window.this));
            }
            NodeUtil.removeFromParent(Window.this);
            if (getOnClosedAction() != null) {
                getOnClosedAction().handle(new ActionEvent(this, Window.this));
            }
        }
    }

    /**
     * Returns the "on-closed-action" property.
     *
     * @return the "on-closed-action" property.
     *
     * @see #setOnClosedAction(javafx.event.EventHandler)
     */
    public ObjectProperty<EventHandler<ActionEvent>> onClosedActionProperty() {
        return onClosedActionProperty;
    }

    /**
     * Defines the action that shall be performed after the window has been
     * closed.
     *
     * @param onClosedAction the action to set
     */
    public void setOnClosedAction(EventHandler<ActionEvent> onClosedAction) {
        this.onClosedActionProperty.set(onClosedAction);
    }

    /**
     * Returns the action that shall be performed after the window has been
     * closed.
     *
     * @return the action that shall be performed after the window has been
     * closed or <code>null</code> if no such action has been defined
     */
    public EventHandler<ActionEvent> getOnClosedAction() {
        return this.onClosedActionProperty.get();
    }

    /**
     * Returns the "on-close-action" property.
     *
     * @return the "on-close-action" property.
     *
     * @see #setOnCloseAction(javafx.event.EventHandler)
     */
    public ObjectProperty<EventHandler<ActionEvent>> onCloseActionProperty() {
        return onCloseActionProperty;
    }

    /**
     * Defines the action that shall be performed before the window will be
     * closed.
     *
     * @param onClosedAction the action to set
     */
    public void setOnCloseAction(EventHandler<ActionEvent> onClosedAction) {
        this.onCloseActionProperty.set(onClosedAction);
    }

    /**
     * Returns the action that shall be performed before the window will be
     * closed.
     *
     * @return the action that shall be performed before the window will be
     * closed or <code>null</code> if no such action has been defined
     */
    public EventHandler<ActionEvent> getOnCloseAction() {
        return this.onCloseActionProperty.get();
    }

    /**
     * Returns the "close-transition" property.
     *
     * @return the "close-transition" property.
     *
     * @see #setCloseTransition(javafx.animation.Transition)
     */
    public ObjectProperty<Transition> closeTransitionProperty() {
        return closeTransitionProperty;
    }

    /**
     * Defines the transition that shall be used to indicate window closing.
     *
     * @param t the transition that shall be used to indicate window closing or
     * <code>null</code> if no transition shall be used.
     */
    public void setCloseTransition(Transition t) {
        closeTransitionProperty.set(t);
    }

    /**
     * Returns the transition that shall be used to indicate window closing.
     *
     * @return the transition that shall be used to indicate window closing or
     * <code>null</code> if no such transition has been defined
     */
    public Transition getCloseTransition() {
        return closeTransitionProperty.get();
    }

    @Override
    public boolean requestSelection(boolean select) {

        if (!select) {
            selectedProperty.set(false);
        }

        if (isSelectable()) {
            selectedProperty.set(select);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the selectableProperty
     */
    public BooleanProperty selectableProperty() {
        return selectableProperty;
    }

    public void setSelectable(Boolean selectable) {
        selectableProperty.set(selectable);
    }

    public boolean isSelectable() {
        return selectableProperty.get();
    }

    /**
     * @return the selectedProperty
     */
    public ReadOnlyBooleanProperty selectedProperty() {
        return selectedProperty;
    }

    /**
     * 
     * @return {@code true} if the window is selected; {@code false} otherwise
     */
    public boolean isSelected() {
        return selectedProperty.get();
    }

}
