/**
 * BasicArcGaugeSkin.java
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import jfxtras.css.CssMetaDataForSkinProperty;
import jfxtras.scene.control.gauge.linear.BasicRoundDailGauge;
import jfxtras.scene.control.gauge.linear.elements.Label;
import jfxtras.scene.control.gauge.linear.elements.Marker;
import jfxtras.scene.control.gauge.linear.elements.Segment;

import com.sun.javafx.css.converters.PaintConverter;

/**
 * 
 */
public class BasicRoundDailGaugeSkin extends AbstractLinearGaugeSkin<BasicRoundDailGaugeSkin, BasicRoundDailGauge> {

	private static final double RING_OUTER_RADIUS_FACTOR = 0.99;
	private static final double RING_INNER_RADIUS_FACTOR = 0.96;
	private static final double RING_WIDTH_FACTOR = 0.04;
	private static final double BACKPLATE_RADIUS_FACTOR = 0.96;
	private static final double TICK_OUTER_RADIUS_FACTOR = 0.93;
	private static final double TICK_INNER_RADIUS_FACTOR = 0.85;
	private static final double TICK_MINOR_RADIUS_FACTOR = 0.82;
	private static final double TICK_MAJOR_RADIUS_FACTOR = 0.80;
	private static final double LABEL_RADIUS_FACTOR = 0.60;
	private static final double SEGMENT_INNER_RADIUS_FACTOR = TICK_MINOR_RADIUS_FACTOR;
	private static final double MARKER_RADIUS_FACTOR = TICK_MAJOR_RADIUS_FACTOR * 0.95;
	private static final double INDICATOR_RADIUS_FACTOR = 0.30;
	static final private double FULL_ARC_IN_DEGREES = 270.0;

	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public BasicRoundDailGaugeSkin(BasicRoundDailGauge control) {
		super(control);
		constructNodes();
	}
	
	// ==================================================================================================================
	// StyleableProperties
	
    /**
     * tickcolor
     */
    public final ObjectProperty<Paint> tickColorProperty() { return tickColorProperty; }
    private ObjectProperty<Paint> tickColorProperty = new SimpleStyleableObjectProperty<Paint>(StyleableProperties.TICKCOLOR_CSSMETADATA, StyleableProperties.TICKCOLOR_CSSMETADATA.getInitialValue(null));
    public final void setTickColor(Paint value) { tickColorProperty().set(value); }
    public final Paint getTickColor() { return tickColorProperty.get(); }
    public final BasicRoundDailGaugeSkin withTickColor(Paint value) { setTickColor(value); return this; }


    // -------------------------
        
    private static class StyleableProperties 
    {
        private static final CssMetaData<BasicRoundDailGauge, Paint> TICKCOLOR_CSSMETADATA = new CssMetaDataForSkinProperty<BasicRoundDailGauge, BasicRoundDailGaugeSkin, Paint>("-fxx-tick-color", PaintConverter.getInstance(), Color.BLACK ) {
        	@Override 
        	protected ObjectProperty<Paint> getProperty(BasicRoundDailGaugeSkin s) {
            	return s.tickColorProperty;
            }
        };
        
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static  {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(SkinBase.getClassCssMetaData());
            styleables.add(TICKCOLOR_CSSMETADATA);
            STYLEABLES = Collections.unmodifiableList(styleables);                
        }
    }
    
    /** 
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */    
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
    	List<CssMetaData<? extends Styleable,?>> classCssMetaData = AbstractLinearGaugeSkin.getClassCssMetaData();
    	classCssMetaData = new ArrayList<CssMetaData<? extends Styleable,?>>(classCssMetaData);
    	classCssMetaData.addAll(StyleableProperties.STYLEABLES);
    	return Collections.unmodifiableList(classCssMetaData);
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
	 * construct the nodes
	 */
	private void constructNodes()
	{
		// style
		getSkinnable().getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because with multiple skins CSS should relate to the skin not the control		

		// determine center
		centerX.bind(stackPane.widthProperty().multiply(0.5));
		centerY.bind(stackPane.heightProperty().multiply(0.5));

		// use a stack pane to control the layers
		stackPane.getChildren().addAll(segmentPane, backPlatePane, markerPane, indicatorPane, valuePane, needlePane, glassPlatePane);
		getChildren().add(stackPane);
		stackPane.setPrefSize(200, 200);
	}
	final private SimpleDoubleProperty centerX = new SimpleDoubleProperty();
	final private SimpleDoubleProperty centerY = new SimpleDoubleProperty();
	final private SimpleDoubleProperty radius = new SimpleDoubleProperty();
	final private StackPane stackPane = new StackPane();
	final private SegmentPane segmentPane = new SegmentPane();
	final private BackPlatePane backPlatePane = new BackPlatePane();
	final private MarkerPane markerPane = new MarkerPane();
	final private IndicatorPane indicatorPane = new IndicatorPane();
	final private NeedlePane needlePane = new NeedlePane();
	final private ValuePane valuePane = new ValuePane();
	final private GlassPlatePane glassPlatePane = new GlassPlatePane();
	

	// ==================================================================================================================
	// Segment
	
	private class SegmentPane extends Pane {
		final private Map<Segment, Arc> segmentToArc = new HashMap<>();

		/**
		 * 
		 */
		private SegmentPane() {
			this.getStyleClass().add("SegmentPane");
			
			// backpane
			backpaneCircle.getStyleClass().addAll("backplate");
			backpaneCircle.centerXProperty().bind(centerX);
			backpaneCircle.centerYProperty().bind(centerY);
            
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
			getChildren().addAll(backpaneCircle);

	 		// create the nodes representing each segment
	 		segmentToArc.clear();
	 		int segmentCnt = 0;
	 		for (Segment segment : getSkinnable().segments()) {
	 			
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
		final private Circle backpaneCircle = new Circle();
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// prep
			double radius = calculateRadius(); // radius must be calculated and cannot use bind

			// size the circle
			double plateRadius = radius * BACKPLATE_RADIUS_FACTOR;
			backpaneCircle.setRadius(plateRadius);
			
			// preparation
	 		double controlMinValue = getSkinnable().getMinValue();
	 		double controlMaxValue = getSkinnable().getMaxValue();
	 		double controlValueRange = controlMaxValue - controlMinValue;
	 		
			// layout the segments
	 		double segmentRadius = calculateRadius() * BACKPLATE_RADIUS_FACTOR;
	 		for (Segment segment : getSkinnable().segments()) {
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
	// BackPlate
	
	private class BackPlatePane extends Pane {

		/**
		 * 
		 */
		private BackPlatePane() {
			this.getStyleClass().add("BackPlatePane");
			
			// backpane
			backpaneCircle.getStyleClass().addAll("backplate");
			backpaneCircle.setStyle("-fx-fill: -fxx-backplate-color;");
			backpaneCircle.centerXProperty().bind(centerX);
			backpaneCircle.centerYProperty().bind(centerY);
			
			// ticks
            ticksCanvas.setCache(true);
            ticksCanvas.setCacheHint(CacheHint.QUALITY);
            ticksCanvas.getStyleClass().addAll("tick");
			ticksCanvas.layoutXProperty().set(0.0);
			ticksCanvas.layoutYProperty().set(0.0);
			ticksCanvas.widthProperty().bind(stackPane.widthProperty());
			ticksCanvas.heightProperty().bind(stackPane.heightProperty());
			backpaneCircle.fillProperty().addListener( (observable) -> {
				layoutChildren();
			});
            
            // add them
			getChildren().addAll(backpaneCircle, ticksCanvas);
			
			// react to changes in the segments
			getSkinnable().segments().addListener( (ListChangeListener.Change<? extends Segment> change) -> {
				requestLayout();
			});
		}
		final private Circle backpaneCircle = new Circle();
		final private Canvas ticksCanvas = new Canvas();
		final private Text tickText = new Text();
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// prep
			double radius = calculateRadius(); // radius must be calculated and cannot use bind

			// size the circle
			double plateRadius = radius * (getSkinnable().segments().size() == 0 ? 0.99 : SEGMENT_INNER_RADIUS_FACTOR);
			backpaneCircle.setRadius(plateRadius);
			
			// paint the ticks
			double size = radius * 2.0;
			GraphicsContext graphicsContext = ticksCanvas.getGraphicsContext2D();
			graphicsContext.clearRect(0.0, 0.0, ticksCanvas.getWidth(), ticksCanvas.getHeight());
			graphicsContext.setStroke(getTickColor());
			double tickInnerRadius = radius * TICK_INNER_RADIUS_FACTOR;
			double tickOuterRadius = radius * TICK_OUTER_RADIUS_FACTOR;
			double tickMajorRadius = radius * TICK_MAJOR_RADIUS_FACTOR;
			double tickMinorRadius = radius * TICK_MINOR_RADIUS_FACTOR;
            for (int i = 0; i <= 100; i++) { 
            	double angle = FULL_ARC_IN_DEGREES / 100.0 * (double)i; 
            	Point2D outerPoint2D = calculatePointOnCircle(tickOuterRadius, angle);
            	Point2D innerPoint2D = null;
            	
            	// major
            	if (i % 10 == 0) {
                	innerPoint2D = calculatePointOnCircle(tickMajorRadius, angle);
	            	graphicsContext.setLineWidth(size * 0.0055);
            	}
            	// medium
            	else if (i % 5 == 0) {
                	innerPoint2D = calculatePointOnCircle(tickMinorRadius, angle);
	            	graphicsContext.setLineWidth(size * 0.0035);
            	}
            	// minor
            	else {
                	innerPoint2D = calculatePointOnCircle(tickInnerRadius, angle);
	            	graphicsContext.setLineWidth(size * 0.00225);
            	}
            	graphicsContext.strokeLine(innerPoint2D.getX(), innerPoint2D.getY(), outerPoint2D.getX(), outerPoint2D.getY());
            }
            for (Label lLabel : getSkinnable().labels()) {
            	double angle = FULL_ARC_IN_DEGREES / 100.0 * lLabel.getValue();            	
    			tickText.setFont(Font.font("Verdana", FontWeight.NORMAL, 0.045 * size));
        		 
                // Draw text
            	Point2D textPoint2D = calculatePointOnCircle(radius * LABEL_RADIUS_FACTOR, angle);
                graphicsContext.save();
                graphicsContext.translate(textPoint2D.getX(), textPoint2D.getY());
                graphicsContext.setFont(Font.font("Verdana", FontWeight.NORMAL, 0.045 * size));
                graphicsContext.setTextAlign(TextAlignment.CENTER);
                graphicsContext.setTextBaseline(VPos.CENTER);
                graphicsContext.setFill(getTickColor());
                graphicsContext.fillText(lLabel.getText(), 0, 0); // TBEERNOT print value and format
                graphicsContext.restore();
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
			rotate.setAngle(angle + 45.0); // the angle also determines the rotation	 			
 			scale.setX(2 * radius / 300.0); // SVG shape was created against a sample gauge with 300x300 pixels  
 			scale.setY(scale.getX()); 
		}
	}
	
	
	// ==================================================================================================================
	// Indicators
	
	protected class IndicatorPane extends AbstractIndicatorPane {
		
		@Override
		protected double calculateScaleFactor() {
			// SVG is setup on a virtual 100x100 canvas, it is scaled to fit the size of the gauge. For a width of 300 (radius 150) this is 30 pixels
			return 30.0/100.0 * BasicRoundDailGaugeSkin.this.calculateRadius()/150.0;
		}
		
		@Override
		protected Point2D calculateLocation(int idx) {

			// prepare
	 		double radius = calculateRadius();
			double indicatorRadius = radius * INDICATOR_RADIUS_FACTOR;
	 			
			// six positions
			if (idx < 6) {
				return calculatePointOnCircle(indicatorRadius, idx * FULL_ARC_IN_DEGREES / 5);
 			}
			System.err.println("The " + getSkinnable().getClass().getSimpleName() + " gauge supports indicators [0,4], not " + idx);
			return null;
		}
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
			valueTextPane.layoutYProperty().bind(centerY
					.add( radius.multiply(0.65) )
					.subtract( valueTextPane.heightProperty().multiply(0.5).multiply(valueScale.yProperty() )) 
					);
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
	 		double radius = calculateRadius() * 0.70;
			
			// use the two extreme's to determine the scaling factor
			double minScale = calculateValueTextScaleFactor(radius, getSkinnable().getMinValue());
			double maxScale = calculateValueTextScaleFactor(radius, getSkinnable().getMaxValue());
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
			// Width and height construct a right angled triangle, where the hypotenuse should be equal to the available room
			// So apply some Pythagoras...
			//System.out.println(Math.sqrt((stackPane.getWidth()*stackPane.getWidth()) + (stackPane.getHeight()*stackPane.getHeight())));
			//System.out.println(Math.sqrt((width*width) + (height*height)));
			double scaleFactor = radius / Math.sqrt((width*width) + (height*height));
			return scaleFactor;
		}
	}

	// ==================================================================================================================
	// Needle
	
	private class NeedlePane extends Pane {

		/**
		 * 
		 */
		private NeedlePane() {
			this.getStyleClass().add("NeedlePane");
			
			// needle
			needleRegion.setPickOnBounds(false);
			needleRegion.getStyleClass().setAll("needle", "needle-standard");
			needleRegion.setPrefSize(6.0, 75.0);
			needleRegion.layoutXProperty().bind(centerX.add( needleRegion.widthProperty().multiply(-0.5) ));
			needleRegion.layoutYProperty().bind(centerY); //.add( needleRegion.heightProperty().multiply(-1.0) ));
			needleRotate.pivotXProperty().bind(needleRegion.widthProperty().multiply(0.5));
			needleRegion.getTransforms().add(needleRotate);
			needleRegion.getTransforms().add(needleScale);
			needleScale.yProperty().bind(needleScale.xProperty());

			// knob
			knobRegion.setPickOnBounds(false);
			knobRegion.getStyleClass().setAll("knob");
			knobRegion.layoutXProperty().bind(centerX);
			knobRegion.layoutYProperty().bind(centerY);
			knobRegion.getTransforms().add(new Scale(1.0, 1.0));

            // add them
			getChildren().addAll(needleRegion, knobRegion);
			
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
			rotateNeedle(false);
		}
		final private Region needleRegion = new Region();
		final private Rotate needleRotate = new Rotate();
		final private Scale needleScale = new Scale();
		final private Region knobRegion = new Region();
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// prep
			double radius = calculateRadius();
			
			// needle
			{
				needleScale.setX(radius / 100.0);
			}
			
			// knob
			{
				Scale scale = (Scale)knobRegion.getTransforms().get(0);
				scale.setX(radius / 200.0 * 0.3);
				scale.setY(scale.getX());
			}
		}
		
		/**
		 * @param allowAnimation AllowAnimation is needed only in the first pass during skin construction: the Animated property has not been set at that time, so we do not need if animation is wanted. So the initial rotation is always done unanimated.  
		 */
		private void rotateNeedle(boolean allowAnimation) {
			if (!validateValueAndHandleInvalid()) {
				return;
			}

			// preparation
	 		double controlMinValue = getSkinnable().getMinValue();
	 		double controlMaxValue = getSkinnable().getMaxValue();
	 		double controlValueRange = controlMaxValue - controlMinValue;
	 		double value = getSkinnable().getValue();
	 		double angle = (value - controlMinValue) / controlValueRange * FULL_ARC_IN_DEGREES;
	 		angle += 45;
	 		
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
	
	protected boolean validateValueAndHandleInvalid() {
		String validationMessage = validateValue();
		if (validationMessage != null) {
			new Throwable(validationMessage).printStackTrace();
			if (needlePane != null && valuePane != null) {
				valuePane.valueText.setText("");
				needlePane.needleRotate.setAngle(0.0);
			}
			return false;
		};
		return true;
	}


	// ==================================================================================================================
	// GlassPlate
	
	private class GlassPlatePane extends Pane {

		/**
		 * 
		 */
		private GlassPlatePane() {
			this.getStyleClass().add("GlassPlatePane");
			
			// backpane
			outerringCircle.getStyleClass().addAll("outerring");
			outerringCircle.centerXProperty().bind(centerX);
			outerringCircle.centerYProperty().bind(centerY);
			
			// backpane
			innerringCircle.getStyleClass().addAll("innerring");
			innerringCircle.centerXProperty().bind(centerX);
			innerringCircle.centerYProperty().bind(centerY);
			
            // add them
			getChildren().addAll(outerringCircle, innerringCircle);

			// clip the dropshadow
			clipCircle.centerXProperty().bind(centerX);
			clipCircle.centerYProperty().bind(centerY);
			clipCircle.setRadius(100.0); // just a dummy initial value
		    setClip(clipCircle);
		}
		final private Circle outerringCircle = new Circle();
		final private Circle innerringCircle = new Circle();
		final private Circle clipCircle = new Circle();
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			// prep
			double radius = calculateRadius();
			
			// size the circle
			outerringCircle.setRadius(radius * RING_OUTER_RADIUS_FACTOR);
			outerringCircle.setStyle("-fx-stroke-width: " + (radius * RING_WIDTH_FACTOR) + ";");
			innerringCircle.setRadius(radius * RING_INNER_RADIUS_FACTOR);
			innerringCircle.setStyle("-fx-stroke-width: " + (radius * RING_WIDTH_FACTOR) + ";");
			if (outerringCircle.getRadius() > 1.0) {
				clipCircle.setRadius(this.getWidth() / 2);
			}
		}
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
