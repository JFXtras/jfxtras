/**
 * LinearGaugeSkin.java
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.SimpleStyleableStringProperty;
import javafx.css.Styleable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import jfxtras.css.CssMetaDataForSkinProperty;
import jfxtras.scene.control.gauge.linear.AbstractLinearGauge;
import jfxtras.scene.control.gauge.linear.elements.Indicator;
import jfxtras.scene.control.gauge.linear.elements.Marker;
import jfxtras.scene.control.gauge.linear.elements.Segment;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.StringConverter;

/**
 * 
 */
public class AbstractLinearGaugeSkin<T, C extends AbstractLinearGauge<?>> extends SkinBase<C> {

	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public AbstractLinearGaugeSkin(C control) {
		super(control);
	}

	// ==================================================================================================================
	// StyleableProperties
	
    /**
     * animated
     */
    public final ObjectProperty<Animated> animatedProperty() { return animatedProperty; }
    private ObjectProperty<Animated> animatedProperty = new SimpleStyleableObjectProperty<Animated>(StyleableProperties.ANIMATED_CSSMETADATA, StyleableProperties.ANIMATED_CSSMETADATA.getInitialValue(null));
    public final void setAnimated(Animated value) { animatedProperty().set(value); }
    public final Animated getAnimated() { return animatedProperty.get(); }
    public final T withAnimated(Animated value) { setAnimated(value); return (T)this; }
    public enum Animated {YES, NO}

    /**
     * valueFormat
     */
    public final SimpleStyleableStringProperty valueFormatProperty() { return valueFormatProperty; }
    private SimpleStyleableStringProperty valueFormatProperty = new SimpleStyleableStringProperty(StyleableProperties.VALUE_FORMAT_CSSMETADATA, StyleableProperties.VALUE_FORMAT_CSSMETADATA.getInitialValue(null)) {
//		{ // anonymous constructor
//			addListener( (invalidationEvent) -> {
//				System.out.println("ValueFormat changed " + valueFormatProperty.get());
//				System.out.println("Style " + getSkinnable().getStyle());
//				new Throwable().printStackTrace();
//			});
//		}
	};
    public final void setValueFormat(String value) { valueFormatProperty.set(value); }
    public final String getValueFormat() { return valueFormatProperty.get(); }
    public final T withValueFormat(String value) { setValueFormat(value); return (T)this; }
    protected String valueFormat(double value) {
    	// TBEERNOT do not create a decimal format every time
    	return new DecimalFormat(getValueFormat()).format(value);
    }
    

    // -------------------------
        
    private static class StyleableProperties 
    {
        private static final CssMetaData<AbstractLinearGauge<?>, Animated> ANIMATED_CSSMETADATA = new CssMetaDataForSkinProperty<AbstractLinearGauge<?>, AbstractLinearGaugeSkin<?,?>, Animated>("-fxx-animated", new EnumConverter<Animated>(Animated.class), Animated.YES ) {
        	@Override 
        	protected ObjectProperty<Animated> getProperty(AbstractLinearGaugeSkin<?,?> s) {
            	return s.animatedProperty;
            }
        };
        
        private static final CssMetaData<AbstractLinearGauge<?>, String> VALUE_FORMAT_CSSMETADATA = new CssMetaDataForSkinProperty<AbstractLinearGauge<?>, AbstractLinearGaugeSkin<?,?>, String>("-fxx-value-format", StringConverter.getInstance(), "0" ) {
        	@Override 
        	protected SimpleStyleableStringProperty getProperty(AbstractLinearGaugeSkin<?,?> s) {
            	return s.valueFormatProperty;
            }
        };
        
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static  {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(SkinBase.getClassCssMetaData());
            styleables.add(ANIMATED_CSSMETADATA);
            styleables.add(VALUE_FORMAT_CSSMETADATA);
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
	// VALIDATE
	
    /**
     * 
     * @return
     */
	protected String validateValue() {
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();
 		double controlValue = getSkinnable().getValue();
		if (controlMinValue > controlMaxValue) {
			return String.format("Min-value (%f) cannot be greater than max-value (%f)", controlMinValue, controlMaxValue);
		}
		if (controlMinValue > controlValue) {
			return String.format("Min-value (%f) cannot be greater than the value (%f)", controlMinValue, controlValue);
		}
		if (controlValue > controlMaxValue) {
			return String.format("Value (%f) cannot be greater than max-value (%f)", controlValue, controlMaxValue);
		}
		return null;
	}
	
	/**
	 * 
	 * @param segment
	 * @return
	 */
	protected String validateSegment(Segment segment) {
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();

 		double segmentMinValue = segment.getMinValue();
 		double segmentMaxValue = segment.getMaxValue();
		if (segmentMinValue < controlMinValue) {
			return String.format("Segments min-value (%f) cannot be less than the controls min-value (%f)", segmentMinValue, controlMinValue);
		}
		if (segmentMaxValue > controlMaxValue) {
			return String.format("Segments max-value (%f) cannot be greater than the controls max-value (%f)", segmentMaxValue, controlMaxValue);
		}
 		return null;
	}

	/**
	 * 
	 * @param marker
	 * @return
	 */
	protected String validateMarker(Marker marker) {
 		double controlMinValue = getSkinnable().getMinValue();
 		double controlMaxValue = getSkinnable().getMaxValue();

 		double markerValue = marker.getValue();
		if (markerValue < controlMinValue) {
			return String.format("Marker value (%f) cannot be less than the controls min-value (%f)", markerValue, controlMinValue);
		}
		if (markerValue > controlMaxValue) {
			return String.format("Marker max-value (%f) cannot be greater than the controls max-value (%f)", markerValue, controlMaxValue);
		}
 		return null;
	}

	
	// ==================================================================================================================
	// Value

	abstract protected class AbstractValuePane extends Pane {
		final protected Text valueText = new Text("");
		final protected Pane valueTextPane = new StackPane(valueText);
		final protected Scale valueScale = new Scale(1.0, 1.0);
		final protected Text hiddenText = new Text("");

		/**
		 * 
		 */
		protected AbstractValuePane() {
	        
	        // value text
	        getChildren().add(valueTextPane);
			valueText.getStyleClass().add("value");
			valueTextPane.getTransforms().setAll(valueScale);
			
			// react to changes
			getSkinnable().valueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				setValueText();
			});
			
	        // min and max value text need to be added to the scene in order to have the CSS applied
			getSkinnable().minValueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				scaleValueText();
			});
			getSkinnable().maxValueProperty().addListener( (observable) -> {
				if (!validateValueAndHandleInvalid()) {
					return;
				}
				scaleValueText();
			});

			// a hidden text to determine how large the min and max value would be
			hiddenText.getStyleClass().add("value");
	        hiddenText.setVisible(false);
	        getChildren().add(hiddenText);

			// init
			setValueText();
			scaleValueText();
		}
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

	        // layout value
			setValueText();
			scaleValueText();
		}

		/**
		 * 
		 */
		private void setValueText() {
			if (!validateValueAndHandleInvalid()) {
				return;
			}
			valueText.setText(valueFormat(getSkinnable().getValue()));
		}

		/**
		 * The value should automatically fill the needle as much as possible.
		 * But it should not constantly switch font size, so it cannot be based on the current content of value's Text node.
		 * So to determine how much the Text node must be scaled, the calculation is based on value's extremes: min and max value.
		 * The smallest scale factor is the one to use (using the larger would make the other extreme go out of the circle).   
		 */
		protected void scaleValueText() {
		}
	}
	
	protected boolean validateValueAndHandleInvalid() {
		return true;
	}

	// ==================================================================================================================
	// Segments

	/**
	 * Make segments active
	 */
	void activateSegments(Map<Segment, ? extends Node> segmentToNode) {
 		// make those segments active that fall under the needle
		double lValue = getSkinnable().getValue();
 		int cnt = 0;
 		for (Segment segment : segmentToNode.keySet()) {
 			
 			// layout the arc for this segment
 	 		double segmentMinValue = segment.getMinValue();
 	 		double segmentMaxValue = segment.getMaxValue();
 	 		String lSegmentActiveId = "segment" + cnt + "-active";
 	 		String lSegmentIdActiveId = "segment-" + segment.getId() + "-active";
 	 		// remove classes
 	 		getSkinnable().getStyleClass().remove(lSegmentActiveId);
 	 		segmentToNode.get(segment).getStyleClass().remove("segment-active");
 	 		if (segment.getId() != null) {
 	 			getSkinnable().getStyleClass().remove(lSegmentIdActiveId);
 	 		}
 	 		// add classes if active
 	 		if (segmentMinValue <= lValue && lValue <= segmentMaxValue) {
 	 			getSkinnable().getStyleClass().add(lSegmentActiveId);
 	 			segmentToNode.get(segment).getStyleClass().add("segment-active");
	 	 		if (segment.getId() != null) {
	 	 			getSkinnable().getStyleClass().add(lSegmentIdActiveId);
	 	 		}
 	 		}
 			cnt++;
 		}
	}

	// ==================================================================================================================
	// Marker
	
	abstract protected class AbstractMarkerPane extends Pane {

		final protected Map<Marker, Region> markerToRegion = new HashMap<>();
		
		/**
		 * 
		 */
		protected AbstractMarkerPane() {

			// react to changes in the markers
			getSkinnable().markers().addListener( (ListChangeListener.Change<? extends Marker> change) -> {
				createAndAddMarkers();
			});
			createAndAddMarkers();
		}
		
		/**
		 * 
		 */
		private void createAndAddMarkers() {
	 		// create the nodes representing each marker
			getChildren().clear();
	 		markerToRegion.clear();
	 		int markerCnt = 0;
	 		for (Marker marker : getSkinnable().markers()) {

	 			// create an svg path for this marker
	 			Region region = new Region();
				getChildren().add(region);
				markerToRegion.put(marker, region);
				
				// setup rotation
				Rotate rotate = new Rotate(0.0);
				rotate.setPivotX(0.0);
				rotate.setPivotY(0.0);
				region.getTransforms().add(rotate);
				
				// setup scaling
				Scale scale = new Scale();
				region.getTransforms().add(scale);
				
				// setup CSS on the path
				region.getStyleClass().addAll("marker", "marker" + markerCnt);
		        if (marker.getId() != null) {
		        	region.setId(marker.getId());
		        }
	 			markerCnt++;
	 		}
		}
		
		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();
			
			// layout the markers
	 		for (Marker marker : getSkinnable().markers()) {
	 			String message = validateMarker(marker);
	 			if (message != null) {
	 				new Throwable(message).printStackTrace();
	 				continue;
	 			}

	 			// layout the svg shape 
	 			Region region = markerToRegion.get(marker);
	 			Rotate rotate = (Rotate)region.getTransforms().get(0);
	 			Scale scale = (Scale)region.getTransforms().get(1);
	 			positionAndScaleMarker(marker, rotate, scale);
	 		}
		}

		abstract protected void positionAndScaleMarker(Marker marker, Rotate rotate, Scale scale);
	}
		
	// ==================================================================================================================
	// Indicators
	
	abstract protected class AbstractIndicatorPane extends Pane {
		final private Map<Indicator, Region> indicatorToRegion = new HashMap<>();

		/**
		 * 
		 */
		protected AbstractIndicatorPane() {

			// react to changes in the markers
			getSkinnable().indicators().addListener( (ListChangeListener.Change<? extends Indicator> change) -> {
				createAndAddIndicators();
			});
			createAndAddIndicators();
		}
		
		/**
		 * 
		 */
		private void createAndAddIndicators() {
	 		// create the nodes representing each marker
			getChildren().clear();
			indicatorToRegion.clear();
	 		for (Indicator indicator : getSkinnable().indicators()) {
	 			
	 			// create an svg path for this marker
	 			Region region = new Region();
				getChildren().add(region);
				indicatorToRegion.put(indicator, region);
				
				// setup scaling
				Scale scale = new Scale();
				region.getTransforms().add(scale);
				
				// setup CSS on the path
				region.getStyleClass().addAll("indicator", indicator.getId() + "-indicator");
	        	region.setId(indicator.getId());
	 		}
		}

		/**
		 * 
		 */
		@Override
		protected void layoutChildren() {
			super.layoutChildren();

	 		// size & position the indicators
	 		double scaleFactor = calculateScaleFactor();
	 		for (Indicator indicator : getSkinnable().indicators()) {
	 			
	 			int idx = indicator.getIdx();
	 			Region region = indicatorToRegion.get(indicator);
	 			if (region != null) {
	 				Point2D point2D = calculateLocation(idx);
					if (point2D != null) {
				 		region.layoutXProperty().set(point2D.getX());
				 		region.layoutYProperty().set(point2D.getY());
		 			}
	 				Scale scale = (Scale)region.getTransforms().get(0);
		 			scale.setX(scaleFactor); 
		 			scale.setY(scale.getX()); 
	 			}
	 		}
		}
		
		abstract protected double calculateScaleFactor();
		abstract protected Point2D calculateLocation(int idx);
	}
}
