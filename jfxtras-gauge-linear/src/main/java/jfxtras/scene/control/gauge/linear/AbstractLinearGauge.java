/**
 * LinearGauge.java
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

package jfxtras.scene.control.gauge.linear;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import jfxtras.internal.scene.control.gauge.linear.skin.AbstractLinearGaugeSkin;
import jfxtras.scene.control.gauge.linear.elements.Indicator;
import jfxtras.scene.control.gauge.linear.elements.Label;
import jfxtras.scene.control.gauge.linear.elements.Marker;
import jfxtras.scene.control.gauge.linear.elements.Segment;

/**
 * This class contains commonalities for all linear gauges
 */
abstract public class AbstractLinearGauge<T> extends Control {
	
	// ==================================================================================================================
	// CONSTRUCTOR

	/**
	 */
	public AbstractLinearGauge() {
		construct();
	}

	/*
	 * 
	 */
	private void construct() {
		
		// setup the CSS
		this.getStyleClass().add(this.getClass().getSimpleName());
	}
	

	// ==================================================================================================================
	// ABSTRACT

	@Override abstract public String getUserAgentStylesheet();

	@Override abstract public Skin<?> createDefaultSkin();

	
	// ==================================================================================================================
	// PROPERTIES

	/** value: the currently rendered value */
	public DoubleProperty valueProperty() { return valueProperty; }
	final private DoubleProperty valueProperty = new SimpleDoubleProperty(this, "value", 0.0);
	public double getValue() { return valueProperty.getValue(); }
	public void setValue(double value) { valueProperty.setValue(value); }
	@SuppressWarnings("unchecked")
	public T withValue(double value) { setValue(value); return (T)this; } 

	/** minValue: the lowest value of the gauge */
	public DoubleProperty minValueProperty() { return minValueProperty; }
	final private DoubleProperty minValueProperty = new SimpleDoubleProperty(this, "minValue", 0.0);
	public double getMinValue() { return minValueProperty.getValue(); }
	public void setMinValue(double value) { minValueProperty.setValue(value); }
	@SuppressWarnings("unchecked")
	public T withMinValue(double value) { setMinValue(value); return (T)this; } 

	/** maxValue: the highest value of the gauge */
	public DoubleProperty maxValueProperty() { return maxValueProperty; }
	final private DoubleProperty maxValueProperty = new SimpleDoubleProperty(this, "maxValue", 100.0);
	public double getMaxValue() { return maxValueProperty.getValue(); }
	public void setMaxValue(double value) { maxValueProperty.setValue(value); }
	@SuppressWarnings("unchecked")
	public T withMaxValue(double value) { setMaxValue(value); return (T)this; } 

	/** labels */
	public ObservableList<Label> labels() { return labels; }
	final private ObservableList<Label> labels =  javafx.collections.FXCollections.observableArrayList();

	/** segments */
	public ObservableList<Segment> segments() { return segments; }
	final private ObservableList<Segment> segments =  javafx.collections.FXCollections.observableArrayList();

	/** markers */
	public ObservableList<Marker> markers() { return markers; }
	final private ObservableList<Marker> markers =  javafx.collections.FXCollections.observableArrayList();

	/** indicators */
	public ObservableList<Indicator> indicators() { return indicators; }
	final private ObservableList<Indicator> indicators =  javafx.collections.FXCollections.observableArrayList();
	public static String segmentColorschemeCSSPath() {
		return AbstractLinearGaugeSkin.class.getResource("/jfxtras/internal/scene/control/gauge/linear/_segment.css").toExternalForm();
	}
}
