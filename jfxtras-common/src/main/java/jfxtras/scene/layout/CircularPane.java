/**
 * CircularPane.java
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

package jfxtras.scene.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import jfxtras.util.Implements;


/**
 * This pane lays it children out in a circle or part of a circle (arc).
 * 
 * In order to understand how to use this pane, it is important to understand how it places its children.
 * Placing nodes on a circle in essence is not that difficult; a circle is 360 degrees, so each node is spaced 360 / n degrees apart, the real challenge is to determine how large the circle must be.
 * Nodes in JavaFX are rectangles with a width and a height, but for calculating in a circle the rectangular shape is impractical. 
 * So CircularPane treats its child nodes as circles, or for better visualization: as beads on a chain.
 * 
 * The first step is to determine how large a single bead is. This already is an interesting question.
 * A beat should encompass the contents of the node, but CircularPane does not know what exactly is drawn in the node.
 * It could be a simple flat or vertical line, where the encompassing circle's diameter is equal to the width or height (whichever is the largest). 
 * But if the contents is an X or a rectangle, then then encompassing circle's diameter is equal to the diagonal.
 * Since CircularPane does not know, it has to assume the worst and use the diagonal.
 * But the childrenAreCircles property allows the user to inform CircularPane than all the children are circular (or smaller), so it can then use the width or height to calculate the encompassing circle (bead).  
 *
 * CircularPane segments the 360 degrees in equal parts; 360 / number of children. 
 * The largest bead determines the distance from the origin to where it fits in a segment, and this determines the size of the chain.
 * 
 * By setting a debug color, the beads will be drawn and will clarify the layout.
 * 
 * @author Tom Eugelink
 *
 */
public class CircularPane extends Pane {

	// TODO: hiding of children? Reserve space? 
	// TODO: animate between changes in the layout?
	
	private enum MinPrefMax { MIN, PREF, MAX }

	
	// ==========================================================================================================================================================================================================================================
	// PROPERTIES

	/** Id */
	public CircularPane withId(String v) { setId(v); return this; }
	
	/** StartAngle in degrees: the startAngle is used to determine the starting position; default = 0 = north (top) */
	public ObjectProperty<Double> startAngleProperty() { return startAngleObjectProperty; }
	final private ObjectProperty<Double> startAngleObjectProperty = new SimpleObjectProperty<Double>(this, "startAngle", 0.0) {
		{ // anonymous constructor
			addListener( (invalidationEvent) -> {
				requestLayout();
			});
		}
	};
	public Double getStartAngle() { return startAngleObjectProperty.getValue(); }
	public void setStartAngle(Double value) { startAngleObjectProperty.setValue(value); }
	public CircularPane withStartAngle(Double value) { setStartAngle(value); return this; } 
	private Double getStartAngle360() {
		return getStartAngle() % 360;
	}

	/** arc in degrees: the arc is used to determine the end position; default = 360 = north (top) */
	public ObjectProperty<Double> arcProperty() { return arcObjectProperty; }
	final private ObjectProperty<Double> arcObjectProperty = new SimpleObjectProperty<Double>(this, "arc", 360.0) {
		{ // anonymous constructor
			addListener( (invalidationEvent) -> {
				requestLayout();
			});
		}
	};
	public Double getArc() { return arcObjectProperty.getValue(); }
	public void setArc(Double value) { arcObjectProperty.setValue(value); }
	public CircularPane withArc(Double value) { setArc(value); return this; }

	/** gap: space between nodes */
	public ObjectProperty<Double> gapProperty() { return gapObjectProperty; }
	final private ObjectProperty<Double> gapObjectProperty = new SimpleObjectProperty<Double>(this, "gap", 0.0) {
		{ // anonymous constructor
			addListener( (invalidationEvent) -> {
				requestLayout();
			});
		}
	};
	public Double getGap() { return gapObjectProperty.getValue(); }
	public void setGap(Double value) { gapObjectProperty.setValue(value); }
	public CircularPane withGap(Double value) { setGap(value); return this; }

	/** diameter: diameter of the whole layout */
	public ObjectProperty<Double> diameterProperty() { return diameterObjectProperty; }
	final private ObjectProperty<Double> diameterObjectProperty = new SimpleObjectProperty<Double>(this, "diameter", null) {
		{ // anonymous constructor
			addListener( (invalidationEvent) -> {
				requestLayout();
			});
		}
	};
	public Double getDiameter() { return diameterObjectProperty.getValue(); }
	public void setDiameter(Double value) { diameterObjectProperty.setValue(value); }
	public CircularPane withDiameter(Double value) { setDiameter(value); return this; }

	/** childrenAreCircular: if all children are circular, then we can use a different size */
	public ObjectProperty<Boolean> childrenAreCircularProperty() { return childrenAreCircularObjectProperty; }
	final private ObjectProperty<Boolean> childrenAreCircularObjectProperty = new SimpleObjectProperty<Boolean>(this, "childrenAreCircular", false) {
		{ // anonymous constructor
			addListener( (invalidationEvent) -> {
				requestLayout();
			});
		}
	};
	public Boolean getChildrenAreCircular() { return childrenAreCircularObjectProperty.getValue(); }
	public void setChildrenAreCircular(Boolean value) { childrenAreCircularObjectProperty.setValue(value); }
	public CircularPane withChildrenAreCircular(Boolean value) { setChildrenAreCircular(value); return this; }

	/** clipAwayExcessWhitespace: cut away excess whitespace on the outside */
	public ObjectProperty<Boolean> clipAwayExcessWhitespaceProperty() { return clipAwayExcessWhitespaceObjectProperty; }
	final private ObjectProperty<Boolean> clipAwayExcessWhitespaceObjectProperty = new SimpleObjectProperty<Boolean>(this, "clipAwayExcessWhitespace", true) {
		{ // anonymous constructor
			addListener( (invalidationEvent) -> {
				requestLayout();
			});
		}
	};
	public Boolean getClipAwayExcessWhitespace() { return clipAwayExcessWhitespaceObjectProperty.getValue(); }
	public void setClipAwayExcessWhitespace(Boolean value) { clipAwayExcessWhitespaceObjectProperty.setValue(value); }
	public CircularPane withClipAwayExcessWhitespace(Boolean value) { setClipAwayExcessWhitespace(value); return this; } 

	/** animationDuration */
	public ObjectProperty<Duration> animationDurationProperty() { return animationDurationObjectProperty; }
	final private ObjectProperty<Duration> animationDurationObjectProperty = new SimpleObjectProperty<Duration>(this, "animationDuration", Duration.millis(500));
	public Duration getAnimationDuration() { return animationDurationObjectProperty.getValue(); }
	public void setAnimationDuration(Duration value) { animationDurationObjectProperty.setValue(value); }
	public CircularPane withAnimationDuration(Duration value) { setAnimationDuration(value); return this; } 

	/** animationInterpolation: calculate the position of a node during the animation (default: move from origin), use node.relocate to position node (or manually apply layoutBounds.minX/Y) */
	public ObjectProperty<AnimationInterpolation> animationInterpolationProperty() { return animationInterpolationObjectProperty; }
	final private ObjectProperty<AnimationInterpolation> animationInterpolationObjectProperty = new SimpleObjectProperty<AnimationInterpolation>(this, "animationInterpolation", null);
	public AnimationInterpolation getAnimationInterpolation() { return animationInterpolationObjectProperty.getValue(); }
	public void setAnimationInterpolation(AnimationInterpolation value) { animationInterpolationObjectProperty.setValue(value); }
	public CircularPane withAnimationInterpolation(AnimationInterpolation value) { setAnimationInterpolation(value); return this; } 

	/** animating */
	public final ReadOnlyBooleanProperty animatingProperty() { return animating.getReadOnlyProperty(); }
    private void setAnimating(boolean value) { animating.set(value); }
    public final boolean isAnimating() { return animatingProperty().get(); }
    private ReadOnlyBooleanWrapper animating = new ReadOnlyBooleanWrapper(this, "animating");
	
	/** animatingIn */
	public final ReadOnlyBooleanProperty animatingInProperty() { return animatingIn.getReadOnlyProperty(); }
    private void setAnimatingIn(boolean value) { animatingIn.set(value); }
    public final boolean isAnimatingIn() { return animatingInProperty().get(); }
    private ReadOnlyBooleanWrapper animatingIn = new ReadOnlyBooleanWrapper(this, "animatingIn");
	
	/** animatingOut */
	public final ReadOnlyBooleanProperty animatingOutProperty() { return animatingOut.getReadOnlyProperty(); }
    private void setAnimatingOut(boolean value) { animatingOut.set(value); }
    public final boolean isAnimatingOut() { return animatingOutProperty().get(); }
    private ReadOnlyBooleanWrapper animatingOut = new ReadOnlyBooleanWrapper(this, "animatingOut");
	
	/** animateInFinished */
	public ObjectProperty<EventHandler<ActionEvent>> animateInFinishedProperty() { return animateInFinishedObjectProperty; }
	final private ObjectProperty<EventHandler<ActionEvent>> animateInFinishedObjectProperty = new SimpleObjectProperty<EventHandler<ActionEvent>>(this, "animateInFinished", null);
	public EventHandler<ActionEvent> getOnAnimateInFinished() { return animateInFinishedObjectProperty.getValue(); }
	public void setOnAnimateInFinished(EventHandler<ActionEvent> value) { animateInFinishedObjectProperty.setValue(value); }
	public CircularPane witOnhAnimateInFinished(EventHandler<ActionEvent> value) { setOnAnimateInFinished(value); return this; } 

	/** animateOutFinished */
	public ObjectProperty<EventHandler<ActionEvent>> animateOutFinishedProperty() { return animateOutFinishedObjectProperty; }
	final private ObjectProperty<EventHandler<ActionEvent>> animateOutFinishedObjectProperty = new SimpleObjectProperty<EventHandler<ActionEvent>>(this, "animateOutFinished", null);
	public EventHandler<ActionEvent> getOnAnimateOutFinished() { return animateOutFinishedObjectProperty.getValue(); }
	public void setOnAnimateOutFinished(EventHandler<ActionEvent> value) { animateOutFinishedObjectProperty.setValue(value); }
	public CircularPane withOnAnimateOutFinished(EventHandler<ActionEvent> value) { setOnAnimateOutFinished(value); return this; } 

	/** debug: show debug hints */
	public ObjectProperty<Paint> showDebugProperty() { return showDebugObjectProperty; }
	final private ObjectProperty<Paint> showDebugObjectProperty = new SimpleObjectProperty<Paint>(this, "showDebug", null) {
		{ // anonymous constructor
			addListener( (invalidationEvent) -> {
				requestLayout();
			});
		}
	};
	public Paint getShowDebug() { return showDebugObjectProperty.getValue(); }
	public void setShowDebug(Paint value) { showDebugObjectProperty.setValue(value); }
	public CircularPane withShowDebug(Paint value) { setShowDebug(value); return this; } 


	// ==========================================================================================================================================================================================================================================
	// PANE
	
    @Override 
    protected double computeMinWidth(double height) {
    	return calculateLayout(MinPrefMax.MIN).clippedWidth;
    }

    @Override 
    protected double computeMinHeight(double width) {
    	return calculateLayout(MinPrefMax.MIN).clippedHeight;
    }

    @Override 
    protected double computePrefWidth(double height) {
    	return calculateLayout(MinPrefMax.PREF).clippedWidth;
    }

    @Override 
    protected double computePrefHeight(double width) {
    	return calculateLayout(MinPrefMax.PREF).clippedHeight;
    }

    @Override 
    protected double computeMaxWidth(double height) {
    	// In 'normal' layout logic these computed sizes would denote an ability.
    	// - Min would indicate the minimal size the node or layout is able to render itself, 
    	// - Pref the preferred size a node would like to have, 
    	// - Max the maximum size a node is ABLE to render itself.
    	//
    	// If a node were given more space to render itself, without any further instructions from the user (through layout constraints), 
    	// it should still stick to its preferred size, because that after all is its preferred size.
    	//
    	// However, in JavaFX Max does not denote an ability, but is seen as an intent.
    	// If a node indicates it has a max of, say, Double.MAX_VALUE (as CircularPane should do, because it is able to render itself that size),
    	// the Pane implementations in JavaFX will actually make CircularPane always grow into any additional space, instead of keeping its preferred size.
    	// In my opinion this is wrong, but unfortunately this is the way things are in JavaFX.
    	// Therefore the Max size is the preferred size.
    	return computePrefWidth(height);
    }

    @Override 
    protected double computeMaxHeight(double width) {
    	// For an explanation, see computerMaxWidth
    	return computePrefHeight(width);
    }

    @Override 
    protected void layoutChildren() {
    	
    	if (layingoutChildren.get() > 0) {
    		return;
    	}    	
    	layingoutChildren.incrementAndGet();
    	try {
    		//System.out.println("=============== layoutChildren ");

			// remove all beads
			getChildren().removeAll(nodeToBeadMap.values());
			nodeToBeadMap.clear();
			getChildren().removeAll(nodeToConnectorMap.values());
			nodeToConnectorMap.clear();
			animationLayoutInfos.clear();
			
			// calculate the layout
			LayoutInfo lLayoutInfo = calculateLayout(null); // null: use the available size instead of a calculated one

	    	// position the nodes
	    	List<Node> nodes = getManagedChildren();
	    	int idx = 0;
	    	for (Node lNode : nodes) {
	    		
	    		/// get layout
	    		NodeLayoutInfo lNodeLayoutInfo = lLayoutInfo.layoutInfoMap.get(lNode);

	    		// position node
	    		lNode.resize(lNodeLayoutInfo.w, lNodeLayoutInfo.h);
	    		
    			// create the administration for the animation
    			AnimationLayoutInfo lAnimationLayoutInfo = new AnimationLayoutInfo();
        		lAnimationLayoutInfo.layoutInfo = lLayoutInfo;
    			lAnimationLayoutInfo.node = lNode;
    			lAnimationLayoutInfo.idx = idx++;
    			lAnimationLayoutInfo.nodeStartX = lNode.getLayoutX() + lNode.getLayoutBounds().getMinX(); // undo what relocate() adds to the X (and thus is included in the value of getLayoutX())
    			lAnimationLayoutInfo.nodeStartY = lNode.getLayoutY() + lNode.getLayoutBounds().getMinY(); // undo what relocate() adds to the Y (and thus is included in the value of getLayoutY())
    			lAnimationLayoutInfo.nodeLayoutInfo = lNodeLayoutInfo;
    			lAnimationLayoutInfo.originX = (lLayoutInfo.chainDiameter / 2) + (lLayoutInfo.beadDiameter/2) - lLayoutInfo.clipLeft;
    			lAnimationLayoutInfo.originY = (lLayoutInfo.chainDiameter / 2) + (lLayoutInfo.beadDiameter/2) - lLayoutInfo.clipTop;
    			lAnimationLayoutInfo.initial = initial;
    			animationLayoutInfos.put(lNode, lAnimationLayoutInfo);

	    		// add a bead to show where this node boundary is
	    		if (getShowDebug() != null) {
	    			// bead
		    		Bead lBead = new Bead(lNodeLayoutInfo);
		    		nodeToBeadMap.put(lNode, lBead);
		    		getChildren().add(lBead);
		    		// connector
		    		Connector lConnector = new Connector(lAnimationLayoutInfo);
		    		nodeToConnectorMap.put(lNode, lConnector);
		    		getChildren().add(lConnector);
	    		}

	    		// are we animating?
    			if (initial && getAnimationInterpolation() != null) {
	    			// reposition to the initial position for the animation
					getAnimationInterpolation().interpolate(0.0, lAnimationLayoutInfo);
	    		}
    			else {
    				lNode.relocate(lNodeLayoutInfo.x, lNodeLayoutInfo.y);
    			}
	    	}    	
	    	
	    	// are we animating?
			if (initial && getAnimationInterpolation() != null) {
				animateIn();
			}
			
	    	// no longer the initial layout
			if (initial) {
	    		initial = false;
			}
		}
		finally {
	    	layingoutChildren.decrementAndGet();
		}
    }
    private final AtomicInteger layingoutChildren = new AtomicInteger(0);
    private boolean initial = true;
	private final Map<Node, Bead> nodeToBeadMap = new WeakHashMap<>();
	private final Map<Node, Connector> nodeToConnectorMap = new WeakHashMap<>();
    
	@Override 
	public void requestLayout() {
		// When to clear the calculation cache; will this be enough?
		calculateLayoutCache.clear();
		super.requestLayout();
	}
	

	// ==========================================================================================================================================================================================================================================
	// LAYOUT

    /**
     * 
     */
    protected LayoutInfo calculateLayout(MinPrefMax size) {

		// layout info, it may be cached
		LayoutInfo lLayoutInfo = calculateLayoutCache.get(size);
		if (lLayoutInfo != null) {
			return lLayoutInfo;
		}
		lLayoutInfo = new LayoutInfo();
		
    	// get the nodes we need to render
    	List<Node> nodes = getManagedChildren();
    	nodes.removeAll(nodeToBeadMap.values());
    	nodes.removeAll(nodeToConnectorMap.values());
    	if (nodes.size() == 0) {
    		// nothing to do
    		return lLayoutInfo;
    	}
    	lLayoutInfo.numberOfNodes = nodes.size();
    	
    	// First we're going to render like we have a full chain (circle) available
    	// Then we're going to clip away excess white space

		// determine the bead and chain size we layout with
    	// If we have a size, then this is a forced calculation
    	// If we do not have a size, we project our (assigned) size to the calculated preferred size to determine how if scaling is needed 
    	double lPrefToMinScaleFactor = 1.0;
    	if (size != null) {
    		// calculation
    		lLayoutInfo.beadDiameter = determineBeadDiameter(size);
        	lLayoutInfo.chainDiameter = computeChainDiameter(lLayoutInfo.beadDiameter);
    	}
    	else {
    		// project preferred to the assigned size
    		LayoutInfo lMinLayoutInfo = calculateLayout(MinPrefMax.MIN);
    		LayoutInfo lPrefLayoutInfo = calculateLayout(MinPrefMax.PREF);
    		double lWidth = Math.max( getWidth(), lMinLayoutInfo.clippedWidth);
    		double lHeight = Math.max( getHeight(), lMinLayoutInfo.clippedHeight);
    		lPrefToMinScaleFactor = Math.min( lWidth / lPrefLayoutInfo.clippedWidth,lHeight/ lPrefLayoutInfo.clippedHeight);
        	//System.out.println(getId() + ": layout lPrefScaleFactor=" + lPrefToMinScaleFactor);	    	
        	if (lPrefToMinScaleFactor > 1.0) {
            	// TODO: should we allow scaling up? (Trail5)
        		lPrefToMinScaleFactor = 1.0;
        	}
    		lLayoutInfo.beadDiameter = lPrefLayoutInfo.beadDiameter * lPrefToMinScaleFactor;
        	lLayoutInfo.chainDiameter = lPrefLayoutInfo.chainDiameter * lPrefToMinScaleFactor;
    	}
		//System.out.println("----------------- " + size);
    	//System.out.println(getId() + ": layout lPrefToMinScaleFactor=" + lPrefToMinScaleFactor);	    	
    	//System.out.println(getId() + ": layout beadDiameter=" + lLayoutInfo.beadDiameter);	    	
    	//System.out.println(getId() + ": layout chainDiameter=" + lLayoutInfo.chainDiameter);	    	

		// prepare the layout loop
    	// the chain diameter runs through the center of the beads
    	lLayoutInfo.minX = lLayoutInfo.chainDiameter + lLayoutInfo.beadDiameter;
    	lLayoutInfo.minY = lLayoutInfo.chainDiameter + lLayoutInfo.beadDiameter;
    	lLayoutInfo.maxX = 0;
    	lLayoutInfo.maxY = 0;
    	lLayoutInfo.angleStep = getArc() / lLayoutInfo.numberOfNodes;
    	double lAngle = getStartAngle360() + (lLayoutInfo.angleStep / 2);
    	lLayoutInfo.startAngle = lAngle;
    	//System.out.println(getId() + ": layout startAngle=" + lAngle);	    	
    	//int cnt = 0;
    	for (Node lNode : nodes) {
    		
    		// bead layout
    		NodeLayoutInfo lNodeLayoutInfo = new NodeLayoutInfo();
    		lNodeLayoutInfo.layoutInfo = lLayoutInfo;
    		lNodeLayoutInfo.angle = lAngle;
    		lLayoutInfo.layoutInfoMap.put(lNode, lNodeLayoutInfo);
    		
    		// size the node 
    		// if we are rendered smaller than the preferred, scale down to min gracefully
    		lNodeLayoutInfo.w = calculateNodeWidth(lNode, MinPrefMax.PREF) * lPrefToMinScaleFactor;
    		lNodeLayoutInfo.h = calculateNodeHeight(lNode, MinPrefMax.PREF) * lPrefToMinScaleFactor;
    		
    		// calculate the origin X,Y position on the chain where the bead should be placed
    		//System.out.println(cnt + " layout startAngle=" + lAngle + " " + lNode);
    		lNodeLayoutInfo.beadX = calculateX(lLayoutInfo.chainDiameter, lAngle) + (lLayoutInfo.beadDiameter / 2);
    		lNodeLayoutInfo.beadY = calculateY(lLayoutInfo.chainDiameter, lAngle) + (lLayoutInfo.beadDiameter / 2);
    		//System.out.println(getId() + ": " + cnt + " layout beadCenter=" + lAngle + " (" + lNodeLayoutInfo.beadX + "," + lNodeLayoutInfo.beadY + ")");
    		
    		// place on the right spot
    		lNodeLayoutInfo.x = lNodeLayoutInfo.beadX
    			 	          - (lNodeLayoutInfo.w / 2) // add the difference between the bead's size and the node's, so it ends up in the center
    				          ; 
    		lNodeLayoutInfo.y = lNodeLayoutInfo.beadY 
    				          - (lNodeLayoutInfo.h / 2)  // add the difference between the bead's size and the node's, so it ends up in the center
    				          ; 
    		//System.out.println(getId() + ": " + cnt + " layout startAngle=" + lAngle + " (" + lNodeLayoutInfo.x + "," + lNodeLayoutInfo.y + ") " + lNodeLayoutInfo.w + "x" + lNodeLayoutInfo.h + " " + lNode);	    		

    		// remember the min and max XY
    		lLayoutInfo.minX = Math.min(lLayoutInfo.minX, lNodeLayoutInfo.beadX - (lLayoutInfo.beadDiameter / 2));
    		lLayoutInfo.minY = Math.min(lLayoutInfo.minY, lNodeLayoutInfo.beadY - (lLayoutInfo.beadDiameter / 2));
    		lLayoutInfo.maxX = Math.max(lLayoutInfo.maxX, lNodeLayoutInfo.beadX - (lLayoutInfo.beadDiameter / 2));
    		lLayoutInfo.maxY = Math.max(lLayoutInfo.maxY, lNodeLayoutInfo.beadY - (lLayoutInfo.beadDiameter / 2));
    		
			// next
        	lAngle += lLayoutInfo.angleStep;
        	//cnt++;
    	}
    	
    	// calculate the clip sizes
    	lLayoutInfo.clipTop = (getClipAwayExcessWhitespace() ? lLayoutInfo.minY : 0);
    	lLayoutInfo.clipRight = (getClipAwayExcessWhitespace() ? lLayoutInfo.chainDiameter - lLayoutInfo.maxX : 0);
    	lLayoutInfo.clipBottom = (getClipAwayExcessWhitespace() ? lLayoutInfo.chainDiameter - lLayoutInfo.maxY : 0);
    	lLayoutInfo.clipLeft = (getClipAwayExcessWhitespace() ? lLayoutInfo.minX : 0);
    	for (Node lNode : nodes) {
    		NodeLayoutInfo lNodeLayoutInfo = lLayoutInfo.layoutInfoMap.get(lNode);
    		lNodeLayoutInfo.x -= lLayoutInfo.clipLeft;
    		lNodeLayoutInfo.y -= lLayoutInfo.clipTop;
    		lNodeLayoutInfo.beadX -= lLayoutInfo.clipLeft;
    		lNodeLayoutInfo.beadY -= lLayoutInfo.clipTop;
    	}    	
    	
    	// calculate size
    	lLayoutInfo.clippedWidth = lLayoutInfo.chainDiameter + lLayoutInfo.beadDiameter // the chain runs through the center of the beads, so we need to add 2x 1/2 a bead to get to the encompassing diameter
       		                     - lLayoutInfo.clipLeft - lLayoutInfo.clipRight;
    	lLayoutInfo.clippedHeight = lLayoutInfo.chainDiameter + lLayoutInfo.beadDiameter // the chain runs through the center of the beads, so we need to add 2x 1/2 a bead to get to the encompassing diameter
                                  - lLayoutInfo.clipTop - lLayoutInfo.clipBottom;
    	
    	// done, cache it
    	if (size != null) {
    		calculateLayoutCache.put(size,  lLayoutInfo);
    	}
    	return lLayoutInfo;
    }
    private final Map<MinPrefMax, LayoutInfo> calculateLayoutCache = new HashMap<>();
	
	/**
	 * This class holds layout information at pane level
	 */
    public class LayoutInfo {
    	public int numberOfNodes;
    	public double startAngle;
    	public double angleStep;
    	public double chainDiameter = 0;
    	public double beadDiameter = 0;
    	public double minX = 0;
    	public double minY = 0;
    	public double maxX = 0;
    	public double maxY = 0;
    	public double clipTop = 0;
    	public double clipRight = 0;
    	public double clipBottom = 0;
    	public double clipLeft = 0;
    	public double clippedWidth = 0;
    	public double clippedHeight = 0;
    	final public Map<Node, NodeLayoutInfo> layoutInfoMap = new WeakHashMap<>();
    }
    
	/**
	 * This class holds layout information at node level
	 */
    public class NodeLayoutInfo {
    	public LayoutInfo layoutInfo;
    	public double angle;
    	public double beadX;
    	public double beadY;
    	public double x;
    	public double y;
    	public double w;
    	public double h;
    }
    

	// ==========================================================================================================================================================================================================================================
	// ANIMATION
    
    /**
     * 
     */
	public void animateIn() {
		animate(1);
	}

    /**
     * 
     */
	public void animateOut() {
		animate(-1);
	}

    /**
     * 
     */
	private void animate(double rate) {
		// no animation configured
		if (getAnimationInterpolation() == null) {
			return;
		}
		
		if (transition == null) {
			transition = new Transition() {
				// anonymous constructor
				{
					setCycleDuration(getAnimationDuration());
					setAutoReverse(false);
					setCycleCount(1);
					setOnFinished( (event) -> {
						setAnimating(false);
						setAnimatingIn(false);
						setAnimatingOut(false);
				    	layingoutChildren.decrementAndGet();
				    	if (transition.getRate() > 0 && getOnAnimateInFinished() != null) {
					    	transition = null;
				    		getOnAnimateInFinished().handle(event);
				    	}
				    	else if (transition.getRate() < 0 && getOnAnimateOutFinished() != null) {
					    	transition = null;
				    		getOnAnimateOutFinished().handle(event);
				    	}
					});
				}
				
				@Override
				protected void interpolate(double progress) {
					for (AnimationLayoutInfo lAnimationLayoutInfo : animationLayoutInfos.values()) {
						getAnimationInterpolation().interpolate( progress, lAnimationLayoutInfo);
					}
				}
			};
		}
		
		// set direction (this is done here instead of in the constructor because it may be modified mid-animation)
		transition.setRate(rate);
		
		// if animation not started
		if (transition.getStatus() != Animation.Status.RUNNING) {
			
			// while the animation is running, don't touch the children
			layingoutChildren.incrementAndGet();
			setAnimating(true);
			setAnimatingIn(rate > 0);
			setAnimatingOut(rate < 0);
			
			// play forward or backwards
			transition.playFrom(transition.getRate() > 0 ? Duration.ZERO : transition.getCycleDuration());
		}
		
	}
	Transition transition = null; 
	
	/**
	 * This class holds additional layout information for animation.
	 */
    public class AnimationLayoutInfo {
    	public Node node;
    	public int idx;
    	public LayoutInfo layoutInfo;
    	public NodeLayoutInfo nodeLayoutInfo;
    	public double originX;
    	public double originY;
    	public double nodeStartX;
    	public double nodeStartY;
    	public boolean initial;
		
    	public double calculateX(double angle) {
			return CircularPane.calculateX(layoutInfo.chainDiameter, angle);
		}
		
    	public double calculateY(double angle) {
			return CircularPane.calculateY(layoutInfo.chainDiameter, angle);
		}
    }
    final Map<Node, AnimationLayoutInfo> animationLayoutInfos = new HashMap<>();
    
    /**
     * 
     * @author user
     *
     */
    @FunctionalInterface
    public interface AnimationInterpolation {
		public void interpolate(double progress, AnimationLayoutInfo animationLayoutInfo);    	
    }

    
	// ==========================================================================================================================================================================================================================================
	// ANIMATION IMPLEMENTATIONS
    
    /**
     * 
     * @param progress
     * @param animationLayoutInfo
     */
    @Implements(interfaces=AnimationInterpolation.class)
    static public void animateFromTheOrigin(double progress, AnimationLayoutInfo animationLayoutInfo) {
    	double lOX = animationLayoutInfo.originX - (animationLayoutInfo.layoutInfo.beadDiameter / 2);
    	double lOY = animationLayoutInfo.originY - (animationLayoutInfo.layoutInfo.beadDiameter / 2);
		double lX = lOX + (progress * (animationLayoutInfo.nodeLayoutInfo.x - lOX));
		double lY = lOY + (progress * (animationLayoutInfo.nodeLayoutInfo.y - lOY));
		animationLayoutInfo.node.relocate(lX, lY);    	
    }
	
    /**
     * 
     * @param progress
     * @param animationLayoutInfo
     */
    @Implements(interfaces=AnimationInterpolation.class)
    static public void animateSpiralOut(double progress, AnimationLayoutInfo animationLayoutInfo) {
    	double spreadWindow = 0.4;
    	double animationWindow = 1.0 - spreadWindow;
    	double offset = animationLayoutInfo.idx / ((double)animationLayoutInfo.layoutInfo.numberOfNodes) * spreadWindow;
		
		double scaledProgress;
    	if (progress < offset) {
    		scaledProgress = 0.0;
    	}
    	else if (progress >= (offset + animationWindow)) {
    		scaledProgress = 1.0;
    	}
    	else {
    		scaledProgress = ((progress - offset) / animationWindow);
    	}
    	animateFromTheOrigin(scaledProgress, animationLayoutInfo);
    }
	
	/**
	 * 
	 * @param progress
	 * @param animationLayoutInfo
	 */
    @Implements(interfaces=CircularPane.AnimationInterpolation.class)
    static public void animateFromTheOriginWithFadeRotate(double progress, AnimationLayoutInfo animationLayoutInfo) {
    	// do the calculation
    	CircularPane.animateFromTheOrigin(progress, animationLayoutInfo);
    	
    	// add a fade
    	animationLayoutInfo.node.setOpacity(progress);
    	animationLayoutInfo.node.setRotate(2 * 360 * progress);
    }
    
    /**
     * 
     * @param progress
     * @param animationLayoutInfo
     */
    @Implements(interfaces=AnimationInterpolation.class)
    static public void animateOverTheArc(double progress, AnimationLayoutInfo animationLayoutInfo) {
		double lAngle = animationLayoutInfo.layoutInfo.startAngle + (progress * (animationLayoutInfo.nodeLayoutInfo.angle - animationLayoutInfo.layoutInfo.startAngle));
		double lX = calculateX(animationLayoutInfo.layoutInfo.chainDiameter, lAngle) + (animationLayoutInfo.layoutInfo.beadDiameter / 2)
			 	  - (animationLayoutInfo.nodeLayoutInfo.w / 2) // add the difference between the bead's size and the node's, so it ends up in the center
			 	  - animationLayoutInfo.layoutInfo.clipLeft
				  ; 
		double lY = calculateY(animationLayoutInfo.layoutInfo.chainDiameter, lAngle) + (animationLayoutInfo.layoutInfo.beadDiameter / 2)
			 	  - (animationLayoutInfo.nodeLayoutInfo.w / 2) // add the difference between the bead's size and the node's, so it ends up in the center
			 	  - animationLayoutInfo.layoutInfo.clipTop
				  ; 
		animationLayoutInfo.node.relocate(lX, lY);
    }
	
    /**
     * 
     * @param progress
     * @param animationLayoutInfo
     */
    @Implements(interfaces=CircularPane.AnimationInterpolation.class)
    static public void animateOverTheArcWithFade(double progress, AnimationLayoutInfo animationLayoutInfo) {
    	// do the calculation
    	CircularPane.animateOverTheArc(progress, animationLayoutInfo);
    	
    	// add a fade
    	animationLayoutInfo.node.setOpacity(progress);
    }

    /**
     * 
     * @param progress
     * @param animationLayoutInfo
     */
    @Implements(interfaces=AnimationInterpolation.class)
    static public void animateAppear(double progress, AnimationLayoutInfo animationLayoutInfo) {
    	animationLayoutInfo.node.setOpacity(progress);
		animationLayoutInfo.node.relocate(animationLayoutInfo.nodeLayoutInfo.x, animationLayoutInfo.nodeLayoutInfo.y);    	
    }

    
    // ==========================================================================================================================================================================================================================================
	// CONVENIENCE
    
    public CircularPane add(Node node) {
    	getChildren().add(node);
    	return this;
    }

    public CircularPane remove(Node node) {
    	getChildren().remove(node);
    	return this;
    }

    
	// ==========================================================================================================================================================================================================================================
	// SUPPORT

	private List<Node> getManagedChildrenWithoutBeads() {
    	List<Node> nodes = new ArrayList<>(getManagedChildren());
    	nodes.removeAll(nodeToBeadMap.values());
    	nodes.removeAll(nodeToConnectorMap.values());
    	return nodes;
	}
	
	static private double calculateX(double chainDiameter, double angle) {
		angle = angle % 360;
		double lX = (chainDiameter / 2) // from the center
	              + ( (chainDiameter / 2) * -1 * Math.sin(degreesToRadials(angle + 180))) // determine the position on the chain (-1 = clockwise, 180 = start north)
	              ;
		//System.out.println(getId() + ": calculateX chainDiameter=" + chainDiameter + " angle=" + angle + " -> " + lX);
		return lX;
	}
	
	static private double calculateY(double chainDiameter, double angle) {
		angle = angle % 360;
		double lY = (chainDiameter / 2) // from the center 
	              + ( (chainDiameter / 2) * Math.cos(degreesToRadials(angle + 180))) // determine the position on the chain (180 = start north)
	              ;
		//System.out.println(getId() + ": calculateY chainDiameter=" + chainDiameter + " angle=" + angle + " -> " + lY);
		return lY;
	}
	
    private class Bead extends Circle {
    	public Bead(NodeLayoutInfo nodeLayoutInfo) {
    		super();
    		setRadius( (nodeLayoutInfo.layoutInfo.beadDiameter - getStrokeWidth()) / 2); // make the line fall within the allotted room
    		setFill(null);
    		setStroke(getShowDebug());
    		setLayoutX(nodeLayoutInfo.beadX - (nodeLayoutInfo.layoutInfo.beadDiameter / 2)); 
    		setLayoutY(nodeLayoutInfo.beadY - (nodeLayoutInfo.layoutInfo.beadDiameter / 2));
    		// because a JavaFX circle has its origin in the top-left corner, we need to offset half a bead
    		setTranslateX(nodeLayoutInfo.layoutInfo.beadDiameter / 2);  
    		setTranslateY(nodeLayoutInfo.layoutInfo.beadDiameter / 2);
    	}
    }
    private class Connector extends Line {
    	public Connector(AnimationLayoutInfo animationLayoutInfo) {
    		super();
    		setFill(null);
    		setStroke(getShowDebug());
    		setStartX(animationLayoutInfo.originX); 
    		setStartY(animationLayoutInfo.originY);
    		setEndX(animationLayoutInfo.nodeLayoutInfo.beadX); 
    		setEndY(animationLayoutInfo.nodeLayoutInfo.beadY);
    	}
    }
    
    /**
     * The chain is the circle that runs through the bead's centers
     * @param size
     * @param width
     * @return
     */
    private double computeChainDiameter(double beadDiameter) {

    	// force set?
    	if (getDiameter() != null) {
    		// The specified diameter denotes the layout's outer circle, the chain however runs through the bead's centers, 
    		// so subtract a bead's radius from either side of the outer circle, to get to the one going through the bead centers.
    		// 2x radius = diameter.
    		return getDiameter() - beadDiameter; 
    	}
    	
    	// Determining the size of the circle where the center of the bead would be placed on is not that complex, once it is explained.
    	// If anyone ever wants to understand it, the logic behind this formula is the following:
    	// - The distance between the centers of two touching circles, not matter how they are positioned in relation to each other, is the sum of their radiuses.
    	// - Two adjacent beads can be seen as two equally sized touching circles, so the distance between their centers is the sum of their radiuses (and since these are equal; the diameter of a bead). 
    	// - Suppose N beads must be rendered on a chain.
    	// - If you draw lines from the origin of the chain to the center of each bead, the lines would have an angle of 360/N degrees.
    	// - The length of the lines to the center of the beads is the radius of the chain, this is the value we are looking for.
    	// - Now it is possible to draw a rectangle:
    	//   * from the chain's origin, 
    	//   * to the center of a bead, 
    	//   * to the center of an adjacent bead, 
    	//   * back to the origin.
    	// - The next step is to construct a right angled triangle, by drawing a perpendicular line from the touch point of two adjacent beads to the origin of the chain.
    	// - This line is perpendicular, because the beads are equal in size and thus the distance from the touch point to the centers is the radius of the bead.
    	// - Trigonometry defines that the sin of the angle (a) between the adjacent side (A) and the hypothenuse (H), equals the length of the opposing side (O) divided by the length of the hypotenuse. 
    	//   * http://en.wikipedia.org/wiki/Right_triangle#Trigonometric_ratios
    	//   * sin(a) = O/H
    	// - It just so happens that the hypotenuse is what we are looking for, it is the radius of the chain (Rc), so H = Rc.
    	// - We know that the angle "a" is half that of the total angle available to the segment, so a = 1/2 * 360/N = 360/2N = 180/N
    	// - And we know that the opposing side "O" is the radius of the bead (Rb), so O = Rb.
    	// - Thus we can write: sin(a) = O / H
    	//                  <=> sin(180/N) = Rb / Rc
    	//                  <=> Rc * sin(180/N) = Rb  
    	//                  <=> Rc = Rb / sin(180/N)
    	// - The hypotenuse equals the radius of the chain, in order to calculate the chain's diameter (Dc) it must be multiplied by 2. 
    	// Hence the resulting formula: Rc = Rb / sin(180/N)  
    	//                         <=> 2Rc = 2Rb / sin(180/N)  
    	//                         <=> Dc = Db / sin(180/N)
    	// - Or: chain-diameter = bead-diameter / sin( 180 / number-of-nodes-on-the-chain )
    	//
    	// This formula works great except for a few special situations, namely 0 and 1 nodes on a chain: this would cause division by zero errors and must be treated separately.

    	// prepare
    	List<Node> nodes = getManagedChildrenWithoutBeads();
    	int numberOfNodes = nodes.size();
    	int numberOfNodesOnFullChain = (int)Math.ceil(numberOfNodes * (360 / getArc())); // these are the number of bead as would be placed on the chain if it were used 360
    	//System.out.println(getId() + ": computeChainDiameter numberOfNodes=" + numberOfNodes + ", for calculation=" + numberOfNodesOnFullChain);
    	
    	// special situations
    	if (numberOfNodes == 0) {
    		return 0;
    	}
    	if (numberOfNodes == 1) {
    		return 0; // the chain runs through the center of the beads, with only one bead, there is no chain, so this would cause a divide by zero in the formula below
    	}
    	// Since the layout allows for gaps, this is simply added to the bead diameter.  
    	double lDiameter = (beadDiameter + getGap()) / Math.sin(degreesToRadials(180 / ((double)numberOfNodesOnFullChain))); // See description above: Dc = Db / sin(180/N)
    	return lDiameter;
    }

	private double determineBeadDiameter(MinPrefMax size) {
		double lBeadDiameter = 0.0;
		if (getChildrenAreCircular()) {
			lBeadDiameter = determineBeadDiameterUsingWidthOrHeight(size);
		}
		else {
			lBeadDiameter = determineBeadDiameterUsingTheDiagonal(size);
		}
		return lBeadDiameter;
	}

	private double determineBeadDiameterUsingWidthOrHeight(MinPrefMax size) {
		List<Node> nodes = getManagedChildrenWithoutBeads();
		double lMaximumSize = 0; 
		for (Node lNode : nodes) {
			if (lNode instanceof Bead) {
				continue;
			}
			double lWidth = calculateNodeWidth(lNode, size);
			if (lWidth > lMaximumSize) {
				lMaximumSize = lWidth;
			}
			double lHeight = calculateNodeHeight(lNode, size);
			if (lHeight > lMaximumSize) {
				lMaximumSize = lHeight;
			}
		}
		return lMaximumSize;
	}
    
	private double determineBeadDiameterUsingTheDiagonal(MinPrefMax size) {
		List<Node> nodes = getManagedChildrenWithoutBeads();
		double lMaximumSize = 0; 
		for (Node lNode : nodes) {
			if (lNode instanceof Bead) {
				continue;
			}
			double lWidth = calculateNodeWidth(lNode, size);
			double lHeight = calculateNodeHeight(lNode, size);
			double lSize = Math.sqrt( (lWidth * lWidth) + (lHeight * lHeight) );
			if (lSize > lMaximumSize) {
				lMaximumSize = lSize;
			}
		}
		return lMaximumSize;
	}
    
    private double calculateNodeWidth(Node n, MinPrefMax size) {
    	if (size == MinPrefMax.MIN) {
    		return n.minWidth(-1);
    	}
    	if (size == MinPrefMax.MAX) {
    		return n.maxWidth(-1);
    	}
    	return n.prefWidth(-1);
    }
    
    private double calculateNodeHeight(Node n, MinPrefMax size) {
    	if (size == MinPrefMax.MIN) {
    		return n.minHeight(-1);
    	}
    	if (size == MinPrefMax.MAX) {
    		return n.maxHeight(-1);
    	}
    	return n.prefHeight(-1);
    }
    
    static private double degreesToRadials(double d) {
    	double r = (d % 360.0) / 360.0 * 2.0 * Math.PI;
    	return r;
    }
}
