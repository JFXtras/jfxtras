/**
 * ListSpinnerSkin.java
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

package jfxtras.internal.scene.control.skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;
import javafx.util.Duration;
import jfxtras.animation.Timer;
import jfxtras.css.CssMetaDataForSkinProperty;
import jfxtras.scene.control.ListSpinner;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.layout.VBox;
import jfxtras.util.NodeUtil;

import com.sun.javafx.css.converters.EnumConverter;

/**
 * 
 * @author Tom Eugelink
 * 
 * Possible extension: drop down list or grid for quick selection
 */
public class ListSpinnerSkin<T> extends SkinBase<ListSpinner<T>>
{
	// TODO: vertical centering 
	
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public ListSpinnerSkin(ListSpinner<T> control)
	{
		super(control);
		construct();
	}

	/*
	 * 
	 */
	private void construct()
	{
            // setup component
            createNodes();

            // react to value changes in the model
            getSkinnable().editableProperty().addListener( (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            	replaceValueNode();
            });
            replaceValueNode();

            // react to value changes in the model
            getSkinnable().valueProperty().addListener( (ObservableValue<? extends T> observable, T oldValue, T newValue) -> {
            	refreshValue();
            });
            refreshValue();

            // react to value changes in the model
            setArrowCSS();
            layout();

            // react to value changes in the model
            alignValue();		
	}
	
	/*
	 * 
	 */
	private void refreshValue() 
	{
            // if editable
            if (getSkinnable().isEditable() == true)
            {
                // update textfield
                T lValue = getSkinnable().getValue();
                textField.setText( getSkinnable().getPrefix() + getSkinnable().getStringConverter().toString(lValue) + getSkinnable().getPostfix() );
            }
            else
            {
                // get node for this value
                getSkinnable().getCellFactory().call( getSkinnable() );
            }
	}
	
	// ==================================================================================================================
	// StyleableProperties
	
    /**
     * arrowPosition
     */
    public final ObjectProperty<ArrowPosition> arrowPositionProperty() { return arrowPosition; }
    private ObjectProperty<ArrowPosition> arrowPosition = new SimpleStyleableObjectProperty<ArrowPosition>(StyleableProperties.ARROW_POSITION, this, "arrowPosition", StyleableProperties.ARROW_POSITION.getInitialValue(null)) {
    	{ // anonymous constructor
			addListener( (invalidationEvent) -> {
                setArrowCSS();
                layout();
			});
		}
    };
    public final void setArrowPosition(ArrowPosition value) { arrowPositionProperty().set(value); }
    public final ArrowPosition getArrowPosition() { return arrowPosition.get(); }
    public final ListSpinnerSkin<T> withArrowPosition(ArrowPosition value) { setArrowPosition(value); return this; }
    public enum ArrowPosition {LEADING, TRAILING, SPLIT}
    
    /**
     * arrowDirection
     */
    public final ObjectProperty<ArrowDirection> arrowDirectionProperty() { return arrowDirection; }
    private ObjectProperty<ArrowDirection> arrowDirection = new SimpleStyleableObjectProperty<ArrowDirection>(StyleableProperties.ARROW_DIRECTION, this, "arrowDirection", StyleableProperties.ARROW_DIRECTION.getInitialValue(null)) {
    	{ // anonymous constructor
			addListener( (invalidationEvent) -> {
                setArrowCSS();
                layout();
			});
		}
    };
    public final void setArrowDirection(ArrowDirection value) { arrowDirectionProperty().set(value); }
    public final ArrowDirection getArrowDirection() { return arrowDirection.get(); }
    public final ListSpinnerSkin<T> withArrowDirection(ArrowDirection value) { setArrowDirection(value); return this; }
    public enum ArrowDirection {VERTICAL, HORIZONTAL}
    
    /**
     * valueAlignment
     */
    public final ObjectProperty<Pos> valueAlignmentProperty() { return valueAlignment; }
    private ObjectProperty<Pos> valueAlignment = new SimpleStyleableObjectProperty<Pos>(StyleableProperties.VALUE_ALIGNMENT, this, "valueAlignment", StyleableProperties.VALUE_ALIGNMENT.getInitialValue(null)) {
    	{ // anonymous constructor
			addListener( (invalidationEvent) -> {
                alignValue();
			});
		}
    };
    public final void setValueAlignment(Pos value) { valueAlignmentProperty().set(value); }
    public final Pos getValueAlignment() { return valueAlignment.get(); }
    public final ListSpinnerSkin<T> withValueAlignment(Pos value) { setValueAlignment(value); return this; }

    // -------------------------
        
    private static class StyleableProperties 
    {
        private static final CssMetaData<ListSpinner<?>, ArrowPosition> ARROW_POSITION = new CssMetaDataForSkinProperty<ListSpinner<?>, ListSpinnerSkin<?>, ArrowPosition>("-fxx-arrow-position", new EnumConverter<ArrowPosition>(ArrowPosition.class), ArrowPosition.TRAILING ) {
        	@Override 
        	protected ObjectProperty<ArrowPosition> getProperty(ListSpinnerSkin<?> s) {
            	return s.arrowPositionProperty();
            }
        };
        
        private static final CssMetaData<ListSpinner<?>, ArrowDirection> ARROW_DIRECTION = new CssMetaDataForSkinProperty<ListSpinner<?>, ListSpinnerSkin<?>, ArrowDirection>("-fxx-arrow-direction", new EnumConverter<ArrowDirection>(ArrowDirection.class), ArrowDirection.HORIZONTAL ) {
        	@Override 
        	protected ObjectProperty<ArrowDirection> getProperty(ListSpinnerSkin<?> s) {
            	return s.arrowDirectionProperty();
            }
        };
        
        private static final CssMetaData<ListSpinner<?>, Pos> VALUE_ALIGNMENT = new CssMetaDataForSkinProperty<ListSpinner<?>, ListSpinnerSkin<?>, Pos>("-fxx-value-alignment", new EnumConverter<Pos>(Pos.class), Pos.CENTER_LEFT ) {
        	@Override 
        	protected ObjectProperty<Pos> getProperty(ListSpinnerSkin<?> s) {
            	return s.valueAlignmentProperty();
            }
        };
        
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static  {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(SkinBase.getClassCssMetaData());
            styleables.add(ARROW_POSITION);
            styleables.add(ARROW_DIRECTION);
            styleables.add(VALUE_ALIGNMENT);
            STYLEABLES = Collections.unmodifiableList(styleables);                
        }
    }
    
    /** 
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */    
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * This method should delegate to {@link Node#getClassCssMetaData()} so that
     * a Node's CssMetaData can be accessed without the need for reflection.
     * @return The CssMetaData associated with this node, which may include the
     * CssMetaData of its super classes.
     */
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
        
	// ==================================================================================================================
	// DRAW
	
	/**
	 * Construct the nodes. 
	 * Spinner uses a GridPane where the arrows and the node for the value are laid out according to the arrows direction and location.
	 * A place holder in inserted into the GridPane to hold the value node, so the spinner can alternate between editable or readonly mode, without having to recreate the GridPane.  
	 */
	private void createNodes()
	{
		// left arrow
		decrementArrow = new Region();
		decrementArrow.getStyleClass().add("idle");

		// place holder for showing the value
		valueHolderNode = new BorderPane();
		valueHolderNode.getStyleClass().add("valuePane");
		//valueHolderNode.setStyle("-fx-border-color: white;");
		
		// right arrow
		incrementArrow = new Region();
		incrementArrow.getStyleClass().add("idle");

		// construct a placeholder node
		skinNode = new BorderPane();
		skinNode.setCenter(valueHolderNode);

		// we're not catching the mouse events on the individual children, but let it bubble up to the parent and handle it there, this makes our life much more simple
		// process mouse clicks
		skinNode.setOnMouseClicked( (mouseEvent) -> {
			
			// if click was the in the greater vicinity of the decrement arrow
			if (mouseEventOverArrow(mouseEvent, decrementArrow))
			{
				// left
				NodeUtil.addStyleClass(decrementArrow, "clicked");
				NodeUtil.removeStyleClass(incrementArrow, "clicked");
				getSkinnable().decrement();
				unclickTimer.restart();
				return;
			}

			// if click was the in the greater vicinity of the increment arrow
			if (mouseEventOverArrow(mouseEvent, incrementArrow))
			{
				// right
				NodeUtil.removeStyleClass(decrementArrow, "clicked");
				NodeUtil.addStyleClass(incrementArrow, "clicked");
				getSkinnable().increment();
				unclickTimer.restart();
				return;
			}
		});
		// process mouse holds
		skinNode.setOnMousePressed( evt -> {
			
			// if click was the in the greater vicinity of the decrement arrow
			if (mouseEventOverArrow(evt, decrementArrow))
			{
				// left
				decrementArrow.getStyleClass().add("clicked");
				repeatDecrementClickTimer.restart();
			}

			// if click was the in the greater vicinity of the increment arrow
			else if (mouseEventOverArrow(evt, incrementArrow))
			{
				// right
				incrementArrow.getStyleClass().add("clicked");
				repeatIncrementClickTimer.restart();
				return;
			}
			
			// if a control does not have the focus, request focus
			ListSpinner<T> lControl = getSkinnable();
			if (!lControl.isFocused() && lControl.isFocusTraversable()) {
				lControl.requestFocus();
			}
		});
		skinNode.setOnMouseReleased( evt -> {
			unclickArrows();
			repeatDecrementClickTimer.stop();
			repeatIncrementClickTimer.stop();
		});
		skinNode.setOnMouseExited( evt -> {
			unclickArrows();
			repeatDecrementClickTimer.stop();
			repeatIncrementClickTimer.stop();
		});
		// mouse wheel
		skinNode.setOnScroll( evt -> {
			// if click was the in the greater vicinity of the decrement arrow
			if (evt.getDeltaY() < 0 || evt.getDeltaX() < 0)
			{
				// left
				NodeUtil.addStyleClass(decrementArrow, "clicked");
				NodeUtil.removeStyleClass(incrementArrow, "clicked");
				getSkinnable().decrement();
				unclickTimer.restart();
				return;
			}

			// if click was the in the greater vicinity of the increment arrow
			if (evt.getDeltaY() > 0 || evt.getDeltaX() > 0)
			{
				// right
				NodeUtil.removeStyleClass(decrementArrow, "clicked");
				NodeUtil.addStyleClass(incrementArrow, "clicked");
				getSkinnable().increment();
				unclickTimer.restart();
				return;
			}
		});
		
		// key events
		getSkinnable().onKeyTypedProperty().set( keyEvent -> {
			KeyCode lKeyCode = keyEvent.getCode();
			if ( KeyCode.MINUS.equals(lKeyCode)
			  || KeyCode.SUBTRACT.equals(lKeyCode)
			  || KeyCode.DOWN.equals(lKeyCode)
			  || KeyCode.LEFT.equals(lKeyCode)
 			   ) {
				getSkinnable().decrement();
			}
			if ( KeyCode.PLUS.equals(lKeyCode)
			  || KeyCode.ADD.equals(lKeyCode)
			  || KeyCode.UP.equals(lKeyCode)
			  || KeyCode.RIGHT.equals(lKeyCode)
 			   ) {
				getSkinnable().increment();
			}
		});
		
		// add to self
		getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
		getChildren().add(skinNode);
	}
	private Region decrementArrow = null;
	private Region incrementArrow = null;
	private BorderPane skinNode = null;
	private BorderPane valueHolderNode;
	
	// timer to remove the click styling on the arrows after a certain delay
	final private Timer unclickTimer = new Timer( () -> {
		unclickArrows();
	}).withDelay(Duration.millis(100)).withRepeats(false);

	// timer to handle the holding of the decrement button
	final private Timer repeatDecrementClickTimer = new Timer( () -> {
		getSkinnable().decrement();
	}).withDelay(Duration.millis(500)).withCycleDuration(Duration.millis(50));
	
	// timer to handle the holding of the increment button
	final private Timer repeatIncrementClickTimer = new Timer( () -> {
		getSkinnable().increment();
	}).withDelay(Duration.millis(500)).withCycleDuration(Duration.millis(50));

	/**
	 * Check if the mouse event is considered to have happened over the arrow
	 * @param evt
	 * @param region
	 * @return
	 */
	private boolean mouseEventOverArrow(MouseEvent evt, Region region)
	{
		// if click was the in the greater vicinity of the arrow
		Point2D lClickInRelationToArrow = region.sceneToLocal(evt.getSceneX(), evt.getSceneY());
		if ( lClickInRelationToArrow.getX() >= 0.0 && lClickInRelationToArrow.getX() <= region.getWidth()
		  && lClickInRelationToArrow.getY() >= 0.0 && lClickInRelationToArrow.getY() <= region.getHeight()
		   )
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Remove clicked CSS styling from the arrows
	 */
	private void unclickArrows()
	{
		decrementArrow.getStyleClass().remove("clicked");
		incrementArrow.getStyleClass().remove("clicked");
	}
	
	/**
	 * Put the correct node for the value's place holder: 
	 * - either the TextField when in editable mode, 
	 * - or a node generated by the cell factory when in readonly mode.  
	 */
	private void replaceValueNode()
	{
		// clear
		valueHolderNode.getChildren().clear();
		
		// if not editable
		if (getSkinnable().isEditable() == false)
		{
			// use the cell factory
			Node lNode = getSkinnable().getCellFactory().call(getSkinnable());
			//lNode.setStyle("-fx-border-color: blue;");
			valueHolderNode.setCenter(lNode);
			if (lNode.getStyleClass().contains("value") == false) lNode.getStyleClass().add("value");
			if (lNode.getStyleClass().contains("readonly") == false) lNode.getStyleClass().add("readonly");
		}
		else
		{
			// use the textfield
			if (textField == null) 
			{
				textField = new TextField();
				textField.getStyleClass().add("value");
				textField.getStyleClass().add("editable");
				
				// process text entry
				textField.focusedProperty().addListener(new InvalidationListener()
				{			
					@Override
					public void invalidated(Observable arg0)
					{
						if (textField.isFocused() == false) 
						{
							parse(textField);
						}
					}
				});
				textField.setOnAction( (actionEvent) -> {
					parse(textField);
				});
				textField.setOnKeyPressed( (keyEvent) -> { 
	                if (keyEvent.getCode() == KeyCode.ESCAPE) 
	                {
	    				// refresh
	    				refreshValue();
	                }
		        });
				
				// alignment
				textField.alignmentProperty().bind(valueAlignmentProperty());
			}
			valueHolderNode.setCenter(textField);
			//textField.setStyle("-fx-border-color: blue;");
		}
		
		// align
		alignValue();
	}
	private TextField textField = null;

	/**
	 * align the value inside the plave holder
	 */
	private void alignValue()
	{
		// valueHolderNode always only holds one child (the value)
		BorderPane.setAlignment(valueHolderNode.getChildren().get(0), valueAlignmentProperty().getValue());
	}
	
	// ==================================================================================================================
	// EDITABLE
	
	/**
	 * Parse the contents of the textfield
	 * @param textField
	 */
	protected void parse(TextField textField)
	{
		// get the text to parse
		String lText = textField.getText();

		// process it
		parse(lText);
		
		// refresh
		refreshValue();
		return;
	}
	
	/**
	 * Lays out the spinner, depending on the location and direction of the arrows.
	 */
	private void layout()
	{
		// get the things we decide on
		ArrowDirection lArrowDirection = getArrowDirection();
		ArrowPosition lArrowPosition = getArrowPosition();
		
		// get helper values
		ColumnConstraints lColumnValue = new ColumnConstraints(valueHolderNode.getMinWidth(), valueHolderNode.getPrefWidth(), Double.MAX_VALUE);
		lColumnValue.setHgrow(Priority.ALWAYS);
		
		// get helper values
		RowConstraints lRowValue = new RowConstraints(valueHolderNode.getMinHeight(), valueHolderNode.getPrefHeight(), Double.MAX_VALUE);
		lRowValue.setVgrow(Priority.ALWAYS);

		// create the grid
		skinNode.getChildren().clear();
		skinNode.setCenter(valueHolderNode);

		if (lArrowDirection == ArrowDirection.HORIZONTAL)
		{
			if (lArrowPosition == ArrowPosition.LEADING)
			{
				HBox lHBox = new HBox(0);
				lHBox.add(decrementArrow, new HBox.C().hgrow(Priority.ALWAYS));
				lHBox.add(incrementArrow, new HBox.C().hgrow(Priority.ALWAYS));
				skinNode.setLeft(lHBox);
				BorderPane.setAlignment(lHBox, Pos.CENTER_LEFT);
				//lHBox.setStyle("-fx-border-color: blue;");
			}
			if (lArrowPosition == ArrowPosition.TRAILING)
			{
				HBox lHBox = new HBox(0);
				lHBox.add(decrementArrow, new HBox.C().hgrow(Priority.ALWAYS));
				lHBox.add(incrementArrow, new HBox.C().hgrow(Priority.ALWAYS));
				skinNode.setRight(lHBox);
				BorderPane.setAlignment(lHBox, Pos.CENTER_RIGHT);
				//lHBox.setStyle("-fx-border-color: blue;");
			}
			if (lArrowPosition == ArrowPosition.SPLIT)
			{
				skinNode.setLeft(decrementArrow);
				skinNode.setRight(incrementArrow);
				BorderPane.setAlignment(decrementArrow, Pos.CENTER_LEFT);
				BorderPane.setAlignment(incrementArrow, Pos.CENTER_RIGHT);
			}
		}
		if (lArrowDirection == ArrowDirection.VERTICAL)
		{
			if (lArrowPosition == ArrowPosition.LEADING)
			{
				VBox lVBox = new VBox(0);
				lVBox.add(incrementArrow, new VBox.C().vgrow(Priority.ALWAYS));
				lVBox.add(decrementArrow, new VBox.C().vgrow(Priority.ALWAYS));
				skinNode.setLeft(lVBox);
				BorderPane.setAlignment(lVBox, Pos.CENTER_LEFT);
				//lVBox.setStyle("-fx-border-color: blue;");
			}
			if (lArrowPosition == ArrowPosition.TRAILING)
			{
				VBox lVBox = new VBox(0);
				lVBox.add(incrementArrow, new VBox.C().vgrow(Priority.ALWAYS));
				lVBox.add(decrementArrow, new VBox.C().vgrow(Priority.ALWAYS));
				skinNode.setRight(lVBox);
				BorderPane.setAlignment(lVBox, Pos.CENTER_RIGHT);
				//lVBox.setStyle("-fx-border-color: blue;");
			}
			if (lArrowPosition == ArrowPosition.SPLIT)
			{
				skinNode.setTop(incrementArrow);
				skinNode.setBottom(decrementArrow);
				BorderPane.setAlignment(incrementArrow, Pos.TOP_CENTER);
				BorderPane.setAlignment(decrementArrow, Pos.BOTTOM_CENTER);
			}
		}
	}
	
	/**
	 * Set the CSS according to the direction of the arrows, so the correct arrows are shown
	 */
	private void setArrowCSS()
	{
		decrementArrow.getStyleClass().remove("down-arrow");
		decrementArrow.getStyleClass().remove("left-arrow");
		incrementArrow.getStyleClass().remove("up-arrow");
		incrementArrow.getStyleClass().remove("right-arrow");
		if (getArrowDirection().equals(ArrowDirection.HORIZONTAL))
		{
			decrementArrow.getStyleClass().add("left-arrow");
			incrementArrow.getStyleClass().add("right-arrow");
		}
		else
		{
			decrementArrow.getStyleClass().add("down-arrow");
			incrementArrow.getStyleClass().add("up-arrow");
		}
	}
	
	// ==================================================================================================================
	// EDITABLE
	
	/**
	 * Parse the value (which usually comes from the TextField in the skin).
	 * If the value exists in the current items, select it.
	 * If not and a callback is defined, call the callback to have it handle it.
	 * Otherwise do nothing (leave it to the skin).
	 */
	public void parse(String text)
	{
		// strip
		String lText = text;
		String lPostfix = getSkinnable().getPostfix();
		if (lPostfix.length() > 0 && lText.endsWith(lPostfix)) {
			lText = lText.substring(0, lText.length() - lPostfix.length());
		}
		String lPrefix = getSkinnable().getPrefix();
		if (lPrefix.length() > 0 && lText.startsWith(lPrefix)) {
			lText = lText.substring(lPrefix.length());
		}
		
		// convert from string to value
		T lValue = getSkinnable().getStringConverter().fromString(lText);
		
		// if the value does exists in the domain
		int lItemIndex = getSkinnable().getItems().indexOf(lValue);
		if (lItemIndex >= 0)
		{
			// accept value and bail out
			getSkinnable().setValue(lValue);
			return;
		}
		
		// check to see if we have a addCallback
		Callback<T, Integer> lAddCallback = getSkinnable().getAddCallback();
		if (lAddCallback != null)
		{
			// call the callback
			Integer lIndex = lAddCallback.call(lValue);
			
			// if the callback reports that it has processed the value by returning the index where it has added the item. (Or at least the index it wants to show now.)
			if (lIndex != null)
			{
				// accept value and bail out
				getSkinnable().setIndex(lIndex);
				return;
			}
		}
	}
}
