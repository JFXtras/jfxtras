/**
 * CirclePopupMenu.java
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

package jfxtras.scene.menu;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.util.Duration;
import jfxtras.scene.layout.CircularPane;
import jfxtras.scene.layout.CircularPane.AnimationInterpolation;

/**
 * CirclePopupMenu is a menu is intended to pop up at any place in a scene.
 * It will show the provided menu items in a circle with the origin at the point where the mouse button was clicked.
 * It is possible to, and per default will, animate the menu items in and out of view.
 * 
 * CirclePopupMenu requires a node to attach itself to, most commonly this will be the outer (largest) pane, but it is also possible to register to a specific node.
 *  
 * CirclePopupMenu uses CircularPane and this will leak through in the API. 
 * For example: it is possible to customize the animation, and the required interface to implement is the one from CircularPane.
 * 
 * @author Tom Eugelink
 *
 */
public class CirclePopupMenu {
	
	// ==================================================================================================================
	// CONSTRUCTOR

	/**
	 * 
	 * @param node the node to render upon, this probably should be a Pane
	 * @param mouseButton the mouse button on which the popup is shown (null means the coder will take care of showing and hiding)
	 */
	public CirclePopupMenu(Node node, MouseButton mouseButton)
	{
		construct(node, mouseButton);
	}

	/*
	 * 
	 */
	private void construct(Node node, MouseButton mouseButton)
	{
    	// remember node
    	this.node = node;
    	
    	// setup popup
    	// autohiding causes problems in the sample: popup.setAutoHide(true);
    	popup.setHideOnEscape(true);
    	popup.setOnHiding( windowEvent -> {
    		hide();
    	});
    	
		// add circularpane to popup
    	popup.getContent().add(circularPane);
		
    	// bind it up
    	circularPane.animationDurationProperty().bind(this.animationDurationObjectProperty);
    	circularPane.animationInterpolationProperty().bind(this.animationInterpolationObjectProperty);
    	circularPane.setPickOnBounds(false);
		// circularPane.setShowDebug(javafx.scene.paint.Color.GREEN);
    	
        // hide when the mouse moves out of the menu
    	EventHandler<MouseEvent> mouseMovedOutsideCircularPaneEventHandler = mouseEvent -> {
            if(isShown()) {
                Bounds screenBounds = circularPane.localToScreen(circularPane.getBoundsInLocal());
                if(!screenBounds.contains(mouseEvent.getScreenX(), mouseEvent.getScreenY())) {
                    hide();
                }
            }
        };
        // register to the scene when node is added there
    	node.sceneProperty().addListener((observable, oldScene, newScene) -> {
    	    if(oldScene != null) {
    	        oldScene.getRoot().removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedOutsideCircularPaneEventHandler);
    	    }
    	    if(newScene != null) {
    	        newScene.getRoot().addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedOutsideCircularPaneEventHandler);
    	    }
    	});
		
		// setup the animation
		circularPane.setOnAnimateOutFinished( (actionEvent) -> {
			popup.hide();
		});
		
    	// react to the mouse button
    	node.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
    		if (mouseButton != null && mouseButton.equals(mouseEvent.getButton())) {
    			if (isShown()) { 
    				hide();
    			}
    			else {
    				show(mouseEvent);
    			}
    		}
    	});
    	
    	// listen to items and modify circular pane's children accordingly
		getItems().addListener( (ListChangeListener.Change<? extends MenuItem> change) -> {
			while (change.next())
			{
				for (MenuItem lMenuItem : change.getRemoved())
				{
					for (javafx.scene.Node lNode : new ArrayList<javafx.scene.Node>(circularPane.getChildren())) {
						if (lNode instanceof CirclePopupMenuNode) {
							CirclePopupMenuNode lCirclePopupMenuNode = (CirclePopupMenuNode)lNode;
							if (lCirclePopupMenuNode.menuItem == lMenuItem) {
								circularPane.remove(lCirclePopupMenuNode);
							}
						}
					}
				}
				for (MenuItem lMenuItem : change.getAddedSubList()) 
				{
					circularPane.add(new CirclePopupMenuNode(lMenuItem) );
				}
			}
		});	
    	
		// default status
		setShown(false);
	}
	private Node node = null;
	private Popup popup = new Popup();
    private CircularPane circularPane = new CircularPane();
	

	// ==================================================================================================================
	// PROPERTIES
	
	/** items */
    private final ObservableList<MenuItem> items = FXCollections.observableArrayList(); 
    public final ObservableList<MenuItem> getItems() {
        return items;
    }
    
	/** shown */
	public final ReadOnlyBooleanProperty shownProperty() { return shown.getReadOnlyProperty(); }
    private void setShown(boolean value) { shown.set(value); }
    public final boolean isShown() { return shownProperty().get(); }
    private ReadOnlyBooleanWrapper shown = new ReadOnlyBooleanWrapper(this, "shown");
	
    // ----------------------
    // CircularPane API
    
	/** animationDuration */
	public ObjectProperty<Duration> animationDurationProperty() { return animationDurationObjectProperty; }
	final private ObjectProperty<Duration> animationDurationObjectProperty = new SimpleObjectProperty<Duration>(this, "animationDuration", Duration.millis(500));
	public Duration getAnimationDuration() { return animationDurationObjectProperty.getValue(); }
	public void setAnimationDuration(Duration value) { animationDurationObjectProperty.setValue(value); }
	public CirclePopupMenu withAnimationDuration(Duration value) { setAnimationDuration(value); return this; } 

	/** animationInterpolation: calculate the position of a node during the animation (default: move from origin), use node.relocate to position node (or manually apply layoutBounds.minX/Y) */
	public ObjectProperty<AnimationInterpolation> animationInterpolationProperty() { return animationInterpolationObjectProperty; }
	final private ObjectProperty<AnimationInterpolation> animationInterpolationObjectProperty = new SimpleObjectProperty<AnimationInterpolation>(this, "animationInterpolation", CircularPane::animateFromTheOrigin);
	public AnimationInterpolation getAnimationInterpolation() { return animationInterpolationObjectProperty.getValue(); }
	public void setAnimationInterpolation(AnimationInterpolation value) { animationInterpolationObjectProperty.setValue(value); }
	public CirclePopupMenu withAnimationInterpolation(AnimationInterpolation value) { setAnimationInterpolation(value); return this; } 



	// ==================================================================================================================
	// ACTION
	
	/**
	 * 
	 * @param mouseEvent
	 */
    public void show(MouseEvent mouseEvent) {
    	show(mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }
    
    /**
     * 
     * @param x origin of the circle
     * @param y origin of the circle
     */
    public void show(double x, double y) {

    	// show popup
    	popup.show(node, x - (circularPane.prefWidth(-1) / 2), y - (circularPane.prefHeight(-1) / 2));
    	
    	// animated pane in
		circularPane.animateIn();
		
		// set status
		setShown(true);
	}
    
    public void hide() {
		setShown(false);
		circularPane.animateOut();
		// if no animation, call the event directly
		if (circularPane.getAnimationInterpolation() == null) {
			circularPane.getOnAnimateOutFinished().handle(null);
		}
    }

	// ==================================================================================================================
	// RENDERING
	
	/* 
	 * This class renders a MenuItem in CircularPane
	 */
	private class CirclePopupMenuNode extends Pane {
		CirclePopupMenuNode (MenuItem menuItem) {
			this.menuItem = menuItem;
			setId(this.getClass().getSimpleName() + "#" + menuNodeIdAtomicLong.incrementAndGet());
			
			// show the graphical part
			if (menuItem.getGraphic() == null) {
				throw new NullPointerException("MenuItems in CirclePopupMenu require a graphical part, text is optional");
			}
			getChildren().add(menuItem.getGraphic());

			// show the text as a tooltip
			if (menuItem.getText() != null && menuItem.getText().length() > 0) {
				Tooltip t = new Tooltip(menuItem.getText());
				Tooltip.install(this, t);
			}
			
			// react on a mouse click to perform the menu action
			setOnMouseClicked( (eventHandler) -> {
				hide();
				if (menuItem.getOnAction() != null) {
					menuItem.getOnAction().handle(null);
				}
			});
		}
		final private MenuItem menuItem;
	}
	private final AtomicLong menuNodeIdAtomicLong = new AtomicLong();
}
