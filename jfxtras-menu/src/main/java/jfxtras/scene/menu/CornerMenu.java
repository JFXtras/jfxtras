/**
 * CornerMenu.java
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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import jfxtras.scene.layout.CircularPane;
import jfxtras.scene.layout.CircularPane.AnimationInterpolation;

/**
 * CornerMenu is a menu is intended to be placed in one of the four corners of a pane.
 * It will show the provided menu items in a 90 degree arc with the origin in the corner.
 * It is possible to, and per default will, animate the menu items in and out of view.
 * The showing and hiding of the menu items can be done automatically based on the mouse pointer location.
 * 
 * CornerMenu requires a Pane to attach itself to. 
 *  
 * CornerMenu uses CircularPane and this will leak through in the API. 
 * For example: it is possible to customize the animation, and required interface to implement is the one from CircularPane.
 * 
 * @author Tom Eugelink
 *
 */
public class CornerMenu {
	
	// ==================================================================================================================
	// CONSTRUCTOR

	/**
	 */
	public CornerMenu(Location location, Pane pane, boolean shown)
	{
		locationObjectProperty.set(location);
		construct(pane, shown);
	}

	/*
	 * 
	 */
	private void construct(Pane pane, boolean shown)
	{
    	this.pane = pane;
    	
        // listen to items and modify circular pane's children accordingly
		getItems().addListener( (ListChangeListener.Change<? extends MenuItem> change) -> {
			while (change.next())
			{
				for (MenuItem lMenuItem : change.getRemoved())
				{
					for (javafx.scene.Node lNode : new ArrayList<javafx.scene.Node>(circularPane.getChildren())) {
						if (lNode instanceof CornerMenuNode) {
							CornerMenuNode lCornerMenuNode = (CornerMenuNode)lNode;
							if (lCornerMenuNode.menuItem == lMenuItem) {
								circularPane.remove(lCornerMenuNode);
							}
						}
					}
				}
				for (MenuItem lMenuItem : change.getAddedSubList()) 
				{
					circularPane.add( new CornerMenuNode(lMenuItem) );
				}
			}
	    	circularPane.resize(circularPane.prefWidth(-1), circularPane.prefHeight(-1));
		});	
		
		// auto show and hide
        pane.addEventHandler(MouseEvent.MOUSE_MOVED, (mouseEvent) -> {
			if (isAutoShowAndHide()) {
				autoShowOrHide(mouseEvent);
			}
		});
		
    	// circular pane
    	setupCircularPane();
    	
    	// add to pane
    	pane.getChildren().add(circularPane);
    	circularPane.setManaged(false);
		
		// default status
		circularPane.setVisible(shown);
		setShown(shown);
    }
    private Pane pane = null;
	

	// ==================================================================================================================
	// PROPERTIES
	
	/** Location: TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT */	
    public ReadOnlyObjectProperty<Location> locationProperty() { 
    	return new ReadOnlyObjectWrapper<Location>(this, "location").getReadOnlyProperty();
    }
	final private SimpleObjectProperty<Location> locationObjectProperty = new SimpleObjectProperty<Location>(this, "location", Location.TOP_LEFT);
	public static enum Location {TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT}
	public Location getLocation() { return locationObjectProperty.getValue(); }
	
	/** items */
    private final ObservableList<MenuItem> items = FXCollections.observableArrayList(); 
    public final ObservableList<MenuItem> getItems() {
        return items;
    }
    
	/** AutoShowAndHide: */
	public BooleanProperty autoShowAndHideProperty() { return this.autoShowAndHideObjectProperty; }
	final private SimpleBooleanProperty autoShowAndHideObjectProperty = new SimpleBooleanProperty(this, "autoShowAndHide", true);
	public Boolean isAutoShowAndHide() { return this.autoShowAndHideObjectProperty.getValue(); }
	public void setAutoShowAndHide(Boolean value) { this.autoShowAndHideObjectProperty.setValue(value); }
	public CornerMenu withAutoShowAndHide(Boolean value) { setAutoShowAndHide(value); return this; }

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
	public CornerMenu withAnimationDuration(Duration value) { setAnimationDuration(value); return this; } 

	/** animationInterpolation: calculate the position of a node during the animation (default: move from origin), use node.relocate to position node (or manually apply layoutBounds.minX/Y) */
	public ObjectProperty<AnimationInterpolation> animationInterpolationProperty() { return animationInterpolationObjectProperty; }
	final private ObjectProperty<AnimationInterpolation> animationInterpolationObjectProperty = new SimpleObjectProperty<AnimationInterpolation>(this, "animationInterpolation", CircularPane::animateFromTheOrigin);
	public AnimationInterpolation getAnimationInterpolation() { return animationInterpolationObjectProperty.getValue(); }
	public void setAnimationInterpolation(AnimationInterpolation value) { animationInterpolationObjectProperty.setValue(value); }
	public CornerMenu withAnimationInterpolation(AnimationInterpolation value) { setAnimationInterpolation(value); return this; } 



	// ==================================================================================================================
	// ACTION
	
    public void show() {
		setShown(true);
		circularPane.setVisible(true);
		circularPane.animateIn();
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
	
    final private CircularPane circularPane = new CircularPane();

    /**
     * 
     */
	public void removeFromPane() {
		pane.getChildren().remove(circularPane);
	}
	
    /*
     * 
     */
    private void setupCircularPane() {
    	
    	// bind it up
    	circularPane.animationDurationProperty().bind(this.animationDurationObjectProperty);
    	circularPane.animationInterpolationProperty().bind(this.animationInterpolationObjectProperty);
		// circularPane.setShowDebug(javafx.scene.paint.Color.GREEN);
    	
    	// setup the corner we are in
		if (CornerMenu.Location.TOP_LEFT.equals(getLocation())) {
			circularPane.setStartAngle(90.0);
		}
		else if (CornerMenu.Location.TOP_RIGHT.equals(getLocation())) {
			circularPane.setStartAngle(180.0);
		}
		else if (CornerMenu.Location.BOTTOM_RIGHT.equals(getLocation())) {
			circularPane.setStartAngle(270.0);
		}
		else if (CornerMenu.Location.BOTTOM_LEFT.equals(getLocation())) {
			circularPane.setStartAngle(0.0);
		}		
		circularPane.setArc(90.0);

		// setup the position in the pane 
		if (CornerMenu.Location.TOP_LEFT.equals(getLocation())) {
			circularPane.setLayoutX(0);
			circularPane.setLayoutY(0);
		}
		else if (CornerMenu.Location.TOP_RIGHT.equals(getLocation())) {
			circularPane.layoutXProperty().bind( pane.widthProperty().subtract(circularPane.widthProperty()));
			circularPane.setLayoutY(0);
		}
		else if (CornerMenu.Location.BOTTOM_RIGHT.equals(getLocation())) {
			circularPane.layoutXProperty().bind( pane.widthProperty().subtract(circularPane.widthProperty()));
			circularPane.layoutYProperty().bind( pane.heightProperty().subtract(circularPane.heightProperty()));
		}
		else if (CornerMenu.Location.BOTTOM_LEFT.equals(getLocation())) {
			circularPane.setLayoutX(0);
			circularPane.layoutYProperty().bind( pane.heightProperty().subtract(circularPane.heightProperty()));
		}
		
		// setup the animation
		circularPane.setOnAnimateOutFinished( (actionEvent) -> {
			circularPane.setVisible(false);
		});
    }

	/* 
	 * This class renders a MenuItem in CircularPane
	 */
	private class CornerMenuNode extends Pane {
		CornerMenuNode (MenuItem menuItem) {
			this.menuItem = menuItem;
			setId(this.getClass().getSimpleName() + "#" + menuNodeIdAtomicLong.incrementAndGet());
			
			// show the graphical part
			if (menuItem.getGraphic() == null) {
				throw new NullPointerException("MenuItems in CornerMenu require a graphical part, text is optional");
			}
			getChildren().add(menuItem.getGraphic());

			// show the text as a tooltip
			if (menuItem.getText() != null && menuItem.getText().length() > 0) {
				Tooltip t = new Tooltip(menuItem.getText());
				Tooltip.install(this, t);
			}
			
			// react on a mouse click to perform the menu action
			setOnMouseClicked( (eventHandler) -> {
				if (isAutoShowAndHide()) {
					hide();
				}
				if (menuItem.getOnAction() != null) {
					menuItem.getOnAction().handle(null);
				}
			});
		}
		final private MenuItem menuItem;
	}
	private final AtomicLong menuNodeIdAtomicLong = new AtomicLong();
	
	/*
	 * 
	 */
	private void autoShowOrHide(MouseEvent mouseEvent) {
		
		// determine distance from origin
		double lX = 0;
		double lY = 0;
		if (CornerMenu.Location.TOP_LEFT.equals(getLocation())) {
			lX = mouseEvent.getX();
			lY = mouseEvent.getY();
		}
		else if (CornerMenu.Location.TOP_RIGHT.equals(getLocation())) {
			lX = pane.getWidth() - mouseEvent.getX();
			lY = mouseEvent.getY();
		}
		else if (CornerMenu.Location.BOTTOM_RIGHT.equals(getLocation())) {
			lX = pane.getWidth() - mouseEvent.getX();
			lY = pane.getHeight() - mouseEvent.getY();
		}
		else if (CornerMenu.Location.BOTTOM_LEFT.equals(getLocation())) {
			lX = mouseEvent.getX();
			lY = pane.getHeight() - mouseEvent.getY();
		}
		lX = (lX < 0 ? 0 : lX);
		lY = (lY < 0 ? 0 : lY);
		double lDistanceFromOrigin = Math.sqrt( (lX * lX) + (lY * lY) );

		// show or hide as required
		if (lDistanceFromOrigin < 10 && circularPane.isVisible() == false && circularPane.isAnimatingIn() == false) {
			show();
		}
		if (lDistanceFromOrigin > circularPane.getWidth() && circularPane.isVisible() && circularPane.isAnimatingOut() == false) {
			hide();
		}
	}
}
