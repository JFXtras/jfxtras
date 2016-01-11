/**
 * SimpleMetroArcGaugeSkin.java
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

package jfxtras.internal.scene.control.gauge.linear.skin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.control.gauge.linear.elements.CompleteSegment;
import jfxtras.scene.control.gauge.linear.elements.Marker;
import jfxtras.scene.control.gauge.linear.elements.Segment;

/**
 * 
 */
public class SimpleMetroArcGaugeSkin extends AbstractLinearGaugeSkin<SimpleMetroArcGaugeSkin, SimpleMetroArcGauge> {

	private static final double SEGMENT_RADIUS_FACTOR = 0.95;
	private static final double NEEDLE_RADIUS_FACTOR = 0.5;
	private static final double NEEDLE_TIP_RADIUS_FACTOR = 0.87;
	private static final double MARKER_RADIUS_FACTOR = SEGMENT_RADIUS_FACTOR;
	private static final double INDICATOR_RADIUS_FACTOR = SEGMENT_RADIUS_FACTOR * 0.15;
	static final private double FULL_ARC_IN_DEGREES = 270.0;

	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public SimpleMetroArcGaugeSkin(SimpleMetroArcGauge control) {
		super(control);
		constructNodes();
	}
	
	// ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void constructNodes()
	{
		// style
		getSkinnable().getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because with multiple skins the CSS should relate to the skin not the control		

		// determine center
		centerX.bind(stackPane.widthProperty().multiply(0.5));
		centerY.bind(stackPane.heightProperty().multiply(0.55));

		// use a stack pane to control the layers
		stackPane.getChildren().addAll(segmentPane, markerPane, indicatorPane, needlePane, valuePane);
		getChildren().add(stackPane);
	}
	final private SimpleDoubleProperty centerX = new SimpleDoubleProperty();
	final private SimpleDoubleProperty centerY = new SimpleDoubleProperty();
	final private SimpleDoubleProperty radius = new SimpleDoubleProperty();
	final private StackPane stackPane = new StackPane();
	final private SegmentPane segmentPane = new SegmentPane();
	final private MarkerPane markerPane = new MarkerPane();
	final private IndicatorPane indicatorPane = new IndicatorPane();
	final private NeedlePane needlePane = new NeedlePane();
	final private ValuePane valuePane = new ValuePane();
	

	// ==================================================================================================================
	// Segments
	
	private class SegmentPane extends Pane {

		final private List<Segment> segments = new ArrayList<>();
		final private Map<Segment, Arc> segmentToArc = new HashMap<>();
		
		/**
		 * 
		 */
		private SegmentPane() {

			// react to changes in the segments
			getSkinnable().segments().addListener( (ListChangeListener.Change<? extends Segment> change) -> {
				createAndAddSegments();
			});
			createAndAddSegments();
		}
		
		/**
		 * 
		 */
		private void createAndAddSegments() {
	 		// determine what segments to draw
			getChildren().clear();
			segments.clear();
			segments.addAll(getSkinnable().segments());
	 		if (segments.size() == 0) {
	 			segments.add(new CompleteSegment(getSkinnable()));
	 		}

	 		// create the nodes representing each segment
	 		segmentToArc.clear();
	 		int segmentCnt = 0;
	 		for (Segment segment : segments) {
	 			
	 			// create an arc for this segment
	 			Arc arc = new Arc();
				getChildren().add(arc);
				segmentToArc.put(segment, arc);
				
				// setup CSS on the path
		        arc.getStyleClass().addAll("segment", "segment" + segmentCnt);
		        if (segment.getId() != null) {
		        	arc.setId(segment.getId());
		        }
	 			segmentCnt++;
	 		}
		}
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();
			
			// preparation
	 		double controlMinValue = getSkinnable().getMinValue();
	 		double controlMaxValue = getSkinnable().getMaxValue();
	 		double controlValueRange = controlMaxValue - controlMinValue;
	 		
			// layout the segments
	 		double segmentRadius = calculateRadius() * SEGMENT_RADIUS_FACTOR;
	 		for (Segment segment : segments) {
	 			String message = validateSegment(segment);
	 			if (message != null) {
	 				new Throwable(message).printStackTrace();
	 				continue;
	 			}
	 			
	 			// layout the arc for this segment
	 	 		double segmentMinValue = segment.getMinValue();
	 	 		double segmentMaxValue = segment.getMaxValue();
	 			double startAngle = (segmentMinValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
	 			double endAngle = (segmentMaxValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES; 
	 			Arc arc = segmentToArc.get(segment);
	 			if (arc != null) {
		 			arc.setCenterX(centerX.get());
		 			arc.setCenterY(centerY.get());
		 			arc.setRadiusX(segmentRadius);
		 			arc.setRadiusY(segmentRadius);
		 			// 0 degrees is on the right side of the circle (3 o'clock), the gauge starts in the bottom left (about 7 o'clock), so add 90 + 45 degrees to offset to that.
		 			// The arc draws counter clockwise, so we need to negate to make it clock wise.
		 			arc.setStartAngle(-1 * (startAngle + 135.0));
		 			arc.setLength(-1 * (endAngle - startAngle));
		 			arc.setType(ArcType.ROUND);
	 			}
	 		}
		}
	}
	
	
	// ==================================================================================================================
	// Marker
	
	private class MarkerPane extends AbstractMarkerPane {

		@Override
		protected void positionAndScaleMarker(Marker marker, Rotate rotate, Scale scale) {
			
			// preparation
	 		double controlMinValue = getSkinnable().getMinValue();
	 		double controlMaxValue = getSkinnable().getMaxValue();
	 		double controlValueRange = controlMaxValue - controlMinValue;
	 		double radius = calculateRadius();
			double markerRadius = radius * MARKER_RADIUS_FACTOR;
	 		
 			// layout the svg shape 
 	 		double markerValue = marker.getValue();
 			double angle = (markerValue - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES;
 			Region region = markerToRegion.get(marker);
 			Point2D markerPoint2D = calculatePointOnCircle(markerRadius, angle);
 			region.setLayoutX(markerPoint2D.getX());
 			region.setLayoutY(markerPoint2D.getY());
			rotate.setAngle(angle - 135.0); // the angle also determines the rotation	 			
 			scale.setX(2 * radius / 300.0); // SVG shape was created against a sample gauge with 300x300 pixels  
 			scale.setY(scale.getX()); 
		}
	}
	
	
	// ==================================================================================================================
	// Indicators
	
	protected class IndicatorPane extends AbstractIndicatorPane {
		
		@Override
		protected double calculateScaleFactor() {
			// SVG is setup on a virtual 100x100 canvas, it is scaled to fit the size of the gauge. For a width of 300 (radius 150) this is 40 pixels
			return 40.0/100.0 * SimpleMetroArcGaugeSkin.this.calculateRadius()/150.0;
		}
		
		@Override
		protected Point2D calculateLocation(int idx) {

			// prepare
	 		double radius = calculateRadius();
			double segmentRadius = radius * SEGMENT_RADIUS_FACTOR;
			double indicatorRadius = radius * INDICATOR_RADIUS_FACTOR;
	 		double indicatorDiameter = 2 * indicatorRadius;
	 			
			// two positions
			if (idx == 0 ) {
		 		return new Point2D(centerX.get() - indicatorDiameter, centerY.get() + segmentRadius - indicatorDiameter);
 			}
 			if (idx == 1) {
 				return new Point2D(centerX.get() + indicatorDiameter, centerY.get() + segmentRadius - indicatorDiameter);
 			}
			System.err.println("The " + getSkinnable().getClass().getSimpleName() + " gauge supports indicators [0,1], not " + idx);
			return null;
		}
	}
	
	
	// ==================================================================================================================
	// NEEDLE
	// OPTION: Move needle path out into CSS and allow for other needles, like https://www.youtube.com/watch?v=oAdwQTy4jms
	
	private class NeedlePane extends Pane {
		
		final private Path needlePath = new Path();
		final private Rotate needleRotate = new Rotate(0.0);

		/**
		 * 
		 */
		private NeedlePane() {
	        
	        // add the needle
	        getChildren().add(needlePath);
	        needlePath.getStyleClass().add("needle");
	        needleRotate.pivotXProperty().bind(centerX);
	        needleRotate.pivotYProperty().bind(centerY);
	        
			// for debugging valueTextPane.setStyle("-fx-border-color: #000000;");
			getSkinnable().valueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true);
			});
			
	        // min and max value text need to be added to the scene in order to have the CSS applied
			getSkinnable().minValueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true); 
			});
			getSkinnable().maxValueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				rotateNeedle(true);
			});
			
			// init
			rotateNeedle(false);
		}
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// we only need to layout if the size changes
			if (previousWidth != getWidth() || previousHeight != getHeight()) {
				
				// preparation
		 		double radius = calculateRadius();
				double tipRadius = radius * NEEDLE_TIP_RADIUS_FACTOR;
				double arcRadius = radius * NEEDLE_RADIUS_FACTOR;
				
				// calculate the important points of the needle
				Point2D arcStartPoint2D = calculatePointOnCircle(arcRadius, 0.0 - 15.0);
				Point2D arcEndPoint2D = calculatePointOnCircle(arcRadius, 0.0 + 15.0);
				Point2D tipPoint2D = calculatePointOnCircle(tipRadius, 0.0);
				
				// we use a path to draw the needle
				needlePath.getElements().clear();
		        needlePath.setFillRule(FillRule.EVEN_ODD);        
				needlePath.getStyleClass().add("needle");
				needlePath.setStrokeLineJoin(StrokeLineJoin.ROUND);
				
		        // begin of arc
		        needlePath.getElements().add( new MoveTo(arcStartPoint2D.getX(), arcStartPoint2D.getY()) );
		        
		        // arc to the end point
		        ArcTo arcTo = new ArcTo();
		        arcTo.setX(arcEndPoint2D.getX());
		        arcTo.setY(arcEndPoint2D.getY());
		        arcTo.setRadiusX(arcRadius);
		        arcTo.setRadiusY(arcRadius);
		        arcTo.setLargeArcFlag(true);
		        arcTo.setSweepFlag(false);
		        needlePath.getElements().add(arcTo);
		        
		        // two lines to the tip
		        needlePath.getElements().add(new LineTo(tipPoint2D.getX(), tipPoint2D.getY()));
		        needlePath.getElements().add(new LineTo(arcStartPoint2D.getX(), arcStartPoint2D.getY()));
		        
		        // set the line around the needle; this is relative to the size of the gauge, so it is not set in CSS
		        needlePath.setStrokeWidth(arcRadius * 0.10);
		        
		        // set to rotate around the center of the gauge
		        needlePath.getTransforms().setAll(needleRotate);
		        
				// remember
				previousWidth = getWidth();
				previousHeight = getHeight();
			}
		}
		private double previousWidth = -1.0;
		private double previousHeight = -1.0;

		/**
		 * @param allowAnimation AllowAnimation is needed only in the first pass during skin construction: the Animated property has not been set at that time, so we do not need if animation is wanted. So the initial rotation is always done unanimated.  
		 */
		private void rotateNeedle(boolean allowAnimation) {
			
			// preparation
	 		double controlMinValue = getSkinnable().getMinValue();
	 		double controlMaxValue = getSkinnable().getMaxValue();
	 		double controlValueRange = controlMaxValue - controlMinValue;
	 		double value = getSkinnable().getValue();
	 		double angle = (value - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES;
	 		
	 		// We cannot use node.setRotate(angle), because this rotates always around the center of the node and the needle's rotation center is not the same as the node's center.
	 		// So we need to use the Rotate transformation, which allows to specify the center of rotation.
	 		// This however also means that we cannot use RotateTransition, because that manipulates the rotate property of a node (and -as explain above- we couldn't use that).
	 		// The only way to animate a Rotate transformation is to use a timeline and keyframes.
	 		if (allowAnimation == false || Animated.NO.equals(getAnimated())) {
	 	 		needleRotate.setAngle(angle);
	 		}
	 		else {
	 			timeline.stop();
		        final KeyValue KEY_VALUE = new KeyValue(needleRotate.angleProperty(), angle, Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
		        final KeyFrame KEY_FRAME = new KeyFrame(Duration.millis(1000), KEY_VALUE);
		        timeline.getKeyFrames().setAll(KEY_FRAME);
		        timeline.play();
	 		}
	 		
	 		// make certain segments active because the needle moved
	 		activateSegments(segmentPane.segmentToArc);
		}
		final private Timeline timeline = new Timeline();
	}

	// ==================================================================================================================
	// VALUE

	private class ValuePane extends AbstractValuePane {

		/**
		 * 
		 */
		private ValuePane() {

			// position valueTextPane
			valueTextPane.layoutXProperty().bind(centerX.subtract( valueTextPane.widthProperty().multiply(0.5).multiply(valueScale.xProperty()) )); 
			valueTextPane.layoutYProperty().bind(centerY.subtract( valueTextPane.heightProperty().multiply(0.5).multiply(valueScale.yProperty()) ));
		}

		/**
		 * The value should automatically fill the needle as much as possible.
		 * But it should not constantly switch font size, so it cannot be based on the current content of value's Text node.
		 * So to determine how much the Text node must be scaled, the calculation is based on value's extremes: min and max value.
		 * The smallest scale factor is the one to use (using the larger would make the other extreme go out of the circle).   
		 */
		@Override
		protected void scaleValueText() {
			
			// preparation
	 		double radius = calculateRadius();
			double arcRadius = radius * NEEDLE_RADIUS_FACTOR;
			
			// use the two extreme's to determine the scaling factor
			double minScale = calculateValueTextScaleFactor(arcRadius, getSkinnable().getMinValue());
			double maxScale = calculateValueTextScaleFactor(arcRadius, getSkinnable().getMaxValue());
			double scale = Math.min(minScale, maxScale);
			valueScale.setX(scale);
			valueScale.setY(scale);
		}
		
		/**
		 * Determine how much to scale the Text node containing the value to fill up the needle's circle
		 * @param radius The radius of the needle
		 * @param value The value to be rendered
		 * @return
		 */
		protected double calculateValueTextScaleFactor(double radius, double value) {
			hiddenText.setText(valueFormat(value));
			double width = hiddenText.getBoundsInParent().getWidth();
			double height = hiddenText.getBoundsInParent().getHeight();
			double diameter = radius * 2.0;
			// Width and height construct a right angled triangle, where the hypotenuse should be equal to the diameter of the needle's circle.
			// So apply some Pythagoras...
			double scaleFactor = diameter / Math.sqrt((width*width) + (height*height));
			return scaleFactor;
		}
	}

	@Override
	protected boolean validateValueAndHandleInvalid() {
		String validationMessage = validateValue();
		if (validationMessage != null) {
			new Throwable(validationMessage).printStackTrace();
			if (needlePane != null && valuePane != null) {
				valuePane.valueText.setText("");
				needlePane.needleRotate.setAngle(-45.0);
			}
			return false;
		};
		return true;
	}
	
	// ==================================================================================================================
	// SUPPORT
	
	/**
	 * http://www.mathopenref.com/coordparamcircle.html
	 * @param center
	 * @param radius
	 * @param angleInDegrees
	 * @return
	 */
	private Point2D calculatePointOnCircle(double radius, double angleInDegrees) {
		// Java's math uses radians
		// 0 degrees is on the right side of the circle (3 o'clock), the gauge starts in the bottom left (about 7 o'clock), so add 90 + 45 degrees to offset to that. 
		double angleInRadians = Math.toRadians(angleInDegrees + 135.0);
		
		// calculate point on circle
		double x = centerX.get() + (radius * Math.cos(angleInRadians));
		double y = centerY.get() + (radius * Math.sin(angleInRadians));
		return new Point2D(x, y);
	}
	
	/**
	 * 
	 * @return
	 */
	private double calculateRadius() {
		radius.set( Math.min(centerX.get(), centerY.get()) );
		return radius.get();
	}
}
