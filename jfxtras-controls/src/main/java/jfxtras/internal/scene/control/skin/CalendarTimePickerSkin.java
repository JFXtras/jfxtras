/**
 * CalendarTimePickerSkin.java
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

package jfxtras.internal.scene.control.skin;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jfxtras.css.converters.SimpleDateFormatConverter;
import jfxtras.scene.control.CalendarTimePicker;
import jfxtras.test.TestUtil;

import com.sun.javafx.css.converters.EnumConverter;

/**
 * @author Tom Eugelink
 *
 */
public class CalendarTimePickerSkin extends SkinBase<CalendarTimePicker>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarTimePickerSkin(CalendarTimePicker control)
	{
		super(control);//, new CalendarTimePickerBehavior(control));
		construct();
	}

	/*
	 * construct the component
	 */
	private void construct()
	{	
		// setup component
		createNodes();
		
		// react to changes in the calendar 
		getSkinnable().calendarProperty().addListener( (observable) -> {
			refresh();
		});

		// react to changes in the minuteStep 
		getSkinnable().minuteStepProperty().addListener( (observable) -> {
			minuteScrollSlider.setBlockIncrement(getSkinnable().getMinuteStep().doubleValue());
		});
		minuteScrollSlider.setBlockIncrement(getSkinnable().getMinuteStep().doubleValue());
		
		// react to changes in the minuteStep 
		getSkinnable().secondStepProperty().addListener( (observable) -> {
			secondScrollSlider.setBlockIncrement(getSkinnable().getSecondStep().doubleValue());
		});
		secondScrollSlider.setBlockIncrement(getSkinnable().getSecondStep().doubleValue());
		
		// react to changes in the calendar 
		getSkinnable().localeProperty().addListener( (observable) -> {
			refresh();
		});
		
		// initial refresh
		refresh();
	}
	
	// ==================================================================================================================
	// PROPERTIES
	

	// ==================================================================================================================
	// StyleableProperties
	
	/** ShowTickLabels: */
    public final ObjectProperty<ShowTickLabels> showTickLabelsProperty()
    {
        if (showTickLabels == null)
        {
            showTickLabels = new StyleableObjectProperty<ShowTickLabels>(SHOW_TICKLABELS_DEFAULT)
            {
                @Override public void invalidated()
                {
                	refreshLayout();
                }

                @Override public CssMetaData<CalendarTimePicker,ShowTickLabels> getCssMetaData() { return StyleableProperties.SHOW_TICKLABELS; }
                @Override public Object getBean() { return CalendarTimePickerSkin.this; }
                @Override public String getName() { return "showTickLabels"; }
            };
        }
        return showTickLabels;
    }
    private ObjectProperty<ShowTickLabels> showTickLabels = null;
    public final void setShowTickLabels(ShowTickLabels value) { showTickLabelsProperty().set(value); }
    public final ShowTickLabels getShowTickLabels() { return showTickLabels == null ? SHOW_TICKLABELS_DEFAULT : showTickLabels.get(); }
    public final CalendarTimePickerSkin withShowTickLabels(ShowTickLabels value) { setShowTickLabels(value); return this; }
    public enum ShowTickLabels {YES, NO}
    static private final ShowTickLabels SHOW_TICKLABELS_DEFAULT = ShowTickLabels.NO;
    
	/** LabelDateFormat: */
    public final ObjectProperty<DateFormat> labelFormatProperty()
    {
        if (labelFormat == null)
        {
            labelFormat = new StyleableObjectProperty<DateFormat>(getLABEL_DATEFORMAT_DEFAULT())
            {
                @Override public void invalidated()
                {
                	refreshLayout();
                	refresh();
                }

                @Override public CssMetaData<CalendarTimePicker,DateFormat> getCssMetaData() { return StyleableProperties.LABEL_DATEFORMAT; }
                @Override public Object getBean() { return CalendarTimePickerSkin.this; }
                @Override public String getName() { return "labelFormat"; }
            };
        }
        return labelFormat;
    }
    private ObjectProperty<DateFormat> labelFormat = null;
    public final void setLabelDateFormat(DateFormat value) { labelFormatProperty().set(value); }
    public final DateFormat getLabelDateFormat() { return labelFormat == null ? getLABEL_DATEFORMAT_DEFAULT() : labelFormat.get(); }
    public final CalendarTimePickerSkin withLabelDateFormat(DateFormat value) { setLabelDateFormat(value); return this; }
    private DateFormat getLABEL_DATEFORMAT_DEFAULT() {
    	return DateFormat.getTimeInstance(DateFormat.SHORT, getSkinnable().getLocale());
    }

    // ----------------------------
    // communicate the styleables

    private static class StyleableProperties
    {
        private static final CssMetaData<CalendarTimePicker, ShowTickLabels> SHOW_TICKLABELS = new CssMetaData<CalendarTimePicker, ShowTickLabels>("-fxx-show-ticklabels", new EnumConverter<ShowTickLabels>(ShowTickLabels.class), SHOW_TICKLABELS_DEFAULT )
        {
            @Override public boolean isSettable(CalendarTimePicker n) { return !((CalendarTimePickerSkin)n.getSkin()).showTickLabelsProperty().isBound(); }
            @Override public StyleableProperty<ShowTickLabels> getStyleableProperty(CalendarTimePicker n) { return (StyleableProperty<ShowTickLabels>)((CalendarTimePickerSkin)n.getSkin()).showTickLabelsProperty(); }
        };

        private static final CssMetaData<CalendarTimePicker, DateFormat> LABEL_DATEFORMAT = new CssMetaData<CalendarTimePicker, DateFormat>("-fxx-label-dateformat", new SimpleDateFormatConverter(), null )
        {
            @Override public boolean isSettable(CalendarTimePicker n) { return !((CalendarTimePickerSkin)n.getSkin()).showTickLabelsProperty().isBound(); }
            @Override public StyleableProperty<DateFormat> getStyleableProperty(CalendarTimePicker n) { return (StyleableProperty<DateFormat>)((CalendarTimePickerSkin)n.getSkin()).labelFormatProperty(); }
        };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static
        {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(SkinBase.getClassCssMetaData());
            styleables.add(SHOW_TICKLABELS);
            styleables.add(LABEL_DATEFORMAT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData()
    {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * This method should delegate to {@link javafx.scene.Node#getClassCssMetaData()} so that
     * a Node's CssMetaData can be accessed without the need for reflection.
     * @return The CssMetaData associated with this node, which may include the
     * CssMetaData of its super classes.
     */
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData()
    {
        return getClassCssMetaData();
    }


	// ==================================================================================================================
	// DRAW
	
    /**
	 * construct the nodes
	 * TODO: snap to tick when released
	 */
	private void createNodes()
	{
		// two sliders
		hourScrollSlider.setId("hourSlider");
		hourScrollSlider.minProperty().set(00);
		hourScrollSlider.maxProperty().set(23);
		hourScrollSlider.setMajorTickUnit(12);
		hourScrollSlider.setMinorTickCount(3);
		hourScrollSlider.valueProperty().addListener( (observable, oldValue, newValue) ->  {
			if (refreshingAtomicInteger.get() > 0) {
				return;
			}
			
			Calendar lCalendar = getChangingCalendar();
			lCalendar = (lCalendar == null ? Calendar.getInstance() : (Calendar)lCalendar.clone());
			lCalendar.set(Calendar.HOUR_OF_DAY, newValue.intValue());
			setChangingCalendar(lCalendar);
			if (hourScrollSlider.valueChangingProperty().get() == false) {
				acceptChangingCalendar();
			}
		});
		hourScrollSlider.valueChangingProperty().addListener( (observable, oldValue, newValue) ->  {
			if (refreshingAtomicInteger.get() > 0) {
				return;
			}
			acceptChangingCalendar();
		});
		minuteScrollSlider.setId("minuteSlider");
		minuteScrollSlider.minProperty().set(00);
		minuteScrollSlider.maxProperty().set(59);
		minuteScrollSlider.setMajorTickUnit(10);
		minuteScrollSlider.valueProperty().addListener( (observable, oldValue, newValue) ->  {
			if (refreshingAtomicInteger.get() > 0) {
				return;
			}
			
			Calendar lCalendar = getChangingCalendar();
			lCalendar = (lCalendar == null ? Calendar.getInstance() : (Calendar)lCalendar.clone());
			
			// in order not to first set a non stepsize calendar, we step the minutes here 
			int lMinutes = newValue.intValue();
			int lMinuteStep = getSkinnable().getMinuteStep();
			if (lMinuteStep > 1)
			{
				lMinutes += getSkinnable().getMinuteStep() / 2; // add half a step, so the scroller jumps to the next tick when the mouse is half way
				if (lMinutes > 59) lMinutes -= lMinuteStep;
			}
			lCalendar.set(Calendar.MINUTE, lMinutes);
			lCalendar = blockMinutesToStep(lCalendar, getSkinnable().getMinuteStep());
			setChangingCalendar(lCalendar);
			if (minuteScrollSlider.valueChangingProperty().get() == false) {
				acceptChangingCalendar();
			}
		});
		minuteScrollSlider.valueChangingProperty().addListener( (observable, oldValue, newValue) ->  {
			if (refreshingAtomicInteger.get() > 0) {
				return;
			}
			acceptChangingCalendar();
		});
		secondScrollSlider.setId("secondSlider");
		secondScrollSlider.minProperty().set(00);
		secondScrollSlider.maxProperty().set(59);
		secondScrollSlider.setMajorTickUnit(10);
		secondScrollSlider.valueProperty().addListener( (observable, oldValue, newValue) ->  {
			if (refreshingAtomicInteger.get() > 0) {
				return;
			}
			
			Calendar lCalendar = getChangingCalendar();
			lCalendar = (lCalendar == null ? Calendar.getInstance() : (Calendar)lCalendar.clone());
			
			// in order not to first set a non stepsize calendar, we step the minutes here 
			int lSeconds = newValue.intValue();
			int lSecondStep = getSkinnable().getSecondStep();
			if (lSecondStep > 1)
			{
				lSeconds += getSkinnable().getSecondStep() / 2; // add half a step, so the scroller jumps to the next tick when the mouse is half way
				if (lSeconds > 59) lSeconds -= lSecondStep;
			}
			lCalendar.set(Calendar.SECOND, lSeconds);
			lCalendar = blockSecondsToStep(lCalendar, getSkinnable().getMinuteStep());
			setChangingCalendar(lCalendar);
			if (secondScrollSlider.valueChangingProperty().get() == false) {
				acceptChangingCalendar();
			}
		});
		secondScrollSlider.valueChangingProperty().addListener( (observable, oldValue, newValue) ->  {
			if (refreshingAtomicInteger.get() > 0) {
				return;
			}
			acceptChangingCalendar();
		});
		
		// add label
		timeText.setDisable(true);
		timeText.getStyleClass().add("timeLabel");

		// layout
		refreshLayout();
		
		// add self as CSS style
		getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control		
	}

	final private Slider hourScrollSlider = new Slider();
	final private Slider minuteScrollSlider = new Slider();
	final private Slider secondScrollSlider = new Slider();
	final private Text timeText = new Text("XX:XX:XX");
	final Pane hourLabelsPane = new Pane()
	{
		{
			prefWidthProperty().bind(hourScrollSlider.prefWidthProperty());
			layoutChildren();
			//setStyle("-fx-border-color: red; -fx-border-width:1px;");
		}
		
		protected void layoutChildren()
		{
			getChildren().clear();
			
			// get some basic numbers
			double lLabelWidth = new Text("88").prefWidth(0);
			double lWhitespace = lLabelWidth / 2;
			double lLabelWidthPlusWhitespace = lLabelWidth + lWhitespace;
			double lScrollSliderOuterPadding = 5;

			// add a dummy rectangle to make sure the are has enough height
			{
				Text lText = new Text("0");
				Rectangle lRectangle = new Rectangle(0,0, minuteScrollSlider.getWidth(), lText.prefHeight(0));
				lRectangle.setFill(Color.TRANSPARENT);
				getChildren().add(lRectangle);
			}

			// now we're going to play with some numbers
			// given the available width, how many labels cold we place (rounded down)
			int lNumberOfLabels = (int)(this.getWidth() / lLabelWidthPlusWhitespace) + 2;
			int lStep = 24;
			if (lNumberOfLabels >= 24/1) lStep = 1; 
			else if (lNumberOfLabels >= 24/2) lStep = 2;
			else if (lNumberOfLabels >= 24/3) lStep = 3;
			else if (lNumberOfLabels >= 24/4) lStep = 4;
			else if (lNumberOfLabels > 24/6) lStep = 6;			
			else if (lNumberOfLabels > 24/12) lStep = 12;			
			for (int i = 0; i < 24; i += lStep)
			{
				Text lText = new Text("" + i);
				lText.setY(lText.prefHeight(0));
				double lX = (lScrollSliderOuterPadding + ((minuteScrollSlider.getWidth() - (2*lScrollSliderOuterPadding)) / 23 * i)) - (lText.prefWidth(0) 
						  / (i == 23 ? 1 : 2) // center, except the most right 
						  * (i == 0  ? 0 : 1)); // and the most left
				lText.setX(lX);
				getChildren().add(lText);
			}
			for (int i = 0; i < 24; i += 1)
			{
				Text lText = new Text("0");
				double lX = (lScrollSliderOuterPadding + ((minuteScrollSlider.getWidth() - (2*lScrollSliderOuterPadding)) / 23 * i));
				getChildren().add(new Line(lX, lText.prefHeight(0) + 3, lX, lText.prefHeight(0) + 3 + 3));
			}
		}
	};
	final Pane minuteLabelsPane = new Pane()
	{
		{
			layoutChildren();
			//setStyle("-fx-border-color: red; -fx-border-width:1px;");
		}
		
		protected void layoutChildren()
		{
			getChildren().clear();
			
			// get some basic numbers
			double lLabelWidth = new Text("88").prefWidth(0);
			double lWhitespace = lLabelWidth / 2;
			double lLabelWidthPlusWhitespace = lLabelWidth + lWhitespace;
			double lScrollSliderOuterPadding = 5;

			// add a dummy rectangle to make sure the are has enough height
			if (getShowTickLabels() == ShowTickLabels.YES)  {
				Text lText = new Text("0");
				Rectangle lRectangle = new Rectangle(0,0, minuteScrollSlider.getWidth(), lText.prefHeight(0));
				lRectangle.setFill(Color.TRANSPARENT);
				getChildren().add(lRectangle);
			}

			// now we're going to play with some numbers
			// given the available width, how many labels cold we place (rounded down)
			int lNumberOfLabels = (int)(this.getWidth()  / lLabelWidthPlusWhitespace) + 2;
			int lStep = 60;
			if (lNumberOfLabels >= 60/1) lStep = 1; 
			else if (lNumberOfLabels >= 60/2) lStep = 2;
			else if (lNumberOfLabels >= 60/3) lStep = 3;
			else if (lNumberOfLabels >= 60/4) lStep = 4;
			else if (lNumberOfLabels >= 60/5) lStep = 5;
			else if (lNumberOfLabels >= 60/10) lStep = 10;
			else if (lNumberOfLabels >= 60/15) lStep = 15;			
			else if (lNumberOfLabels >= 60/30) lStep = 30;
			if (lStep < getSkinnable().getMinuteStep()) {
				lStep = getSkinnable().getMinuteStep();
			}
			for (int i = 0; i <= 59; i += lStep)
			{
				Text lText = new Text("" + i);
				lText.setY(lText.prefHeight(0));
				double lX = (lScrollSliderOuterPadding + ((minuteScrollSlider.getWidth() - (2*lScrollSliderOuterPadding)) / 59 * i)) - (lText.prefWidth(0) 
						  / (i == 59 ? 1 : 2) // center, except the most right 
						  * (i == 0  ? 0 : 1)); // and the most left
				lText.setX(lX);
				getChildren().add(lText);
			}
			for (int i = 0; i <= 59; i += 1)
			{
				double lX = (lScrollSliderOuterPadding + ((minuteScrollSlider.getWidth() - (2*lScrollSliderOuterPadding)) / 59 * i)); 
				getChildren().add(new Line(lX, 0, lX, 3));
			}
		}
	};
	
	/**
	 * hide or show nodes (VBox reserves room for not visible nodes)
	 */
	private void refreshLayout()
	{
		getChildren().clear();
		StackPane lStackPane = new StackPane();
		VBox lVBox = new VBox(0);
		lVBox.alignmentProperty().set(Pos.CENTER);
		if ( getLabelDateFormat().format(DATE).contains("2") ) {
			if (getShowTickLabels() == ShowTickLabels.YES) {
				lVBox.getChildren().add(hourLabelsPane);
			}
			lVBox.getChildren().add(hourScrollSlider);
		}
		if ( getLabelDateFormat().format(DATE).contains("3") ) {
			lVBox.getChildren().add(minuteScrollSlider);
			if (getShowTickLabels() == ShowTickLabels.YES) {
				lVBox.getChildren().add(minuteLabelsPane);
			}
		}
		if ( getLabelDateFormat().format(DATE).contains("4") ) {
			lVBox.getChildren().add(secondScrollSlider);
		}
		lStackPane.getChildren().add(lVBox);
		lStackPane.getChildren().add(timeText);
		StackPane.setAlignment(timeText, getShowTickLabels() == ShowTickLabels.YES ? Pos.CENTER : Pos.TOP_CENTER);
		getChildren().add(lStackPane);
	}
	final static Date DATE = new Date(9999-1900, 0, 1, 2, 3, 4);
	
	/**
	 * 
	 */
	private void refresh()
	{
		try {
			refreshingAtomicInteger.addAndGet(1);

			Calendar lCalendar = getChangingCalendar();
			int lHour = lCalendar == null ? 0 : lCalendar.get(Calendar.HOUR_OF_DAY);
			int lMinute = lCalendar == null ? 0 : lCalendar.get(Calendar.MINUTE);
			int lSecond = lCalendar == null ? 0 : lCalendar.get(Calendar.SECOND);
			hourScrollSlider.valueProperty().set(lHour);
			minuteScrollSlider.valueProperty().set(lMinute);
			secondScrollSlider.valueProperty().set(lSecond);
			timeText.setText( lCalendar == null ? "" : getLabelDateFormat().format(lCalendar.getTime()) );
		}
		finally {
			refreshingAtomicInteger.addAndGet(-1);
		}
	}
	final private AtomicInteger refreshingAtomicInteger = new AtomicInteger(0);
	
	/**
	 * minutes fit in the minute steps
	 */
	static public Calendar blockMinutesToStep(Calendar calendar, Integer stepSize)
	{
		if (stepSize == null || calendar == null) return calendar;
			
		// set the minutes to match the step size
		int lValue = calendar.get(Calendar.MINUTE);
		if (stepSize == 1) return calendar;
		lValue = lValue / stepSize; // trunk
		lValue *= stepSize;
		if (calendar.get(Calendar.MINUTE) != lValue)
		{
			Calendar lCalendar = (Calendar)calendar.clone();
			lCalendar.set(Calendar.MINUTE, lValue);
			calendar = lCalendar;
		}
		return calendar;
	}

	/**
	 * seconds fit in the second steps
	 */
	static public Calendar blockSecondsToStep(Calendar calendar, Integer stepSize)
	{
		if (stepSize == null || calendar == null) return calendar;
			
		// set the minutes to match the step size
		int lValue = calendar.get(Calendar.SECOND);
		if (stepSize == 1) return calendar;
		lValue = lValue / stepSize; // trunk
		lValue *= stepSize;
		if (calendar.get(Calendar.SECOND) != lValue)
		{
			Calendar lCalendar = (Calendar)calendar.clone();
			lCalendar.set(Calendar.SECOND, lValue);
			calendar = lCalendar;
		}
		return calendar;
	}
	
    /** changingCalendar holds the displayed value while the sliders send events with value-is-changing is true */
    private Calendar changingCalendar = null;
    private Calendar getChangingCalendar() {
    	if (this.changingCalendar != null) {
    		return this.changingCalendar;
    	}
    	return getSkinnable().getCalendar();
    }
	public void setChangingCalendar(Calendar lCalendar) {
		if (lCalendar.equals(getChangingCalendar()) == false) {
			this.changingCalendar = lCalendar;
			refresh();
		}
	}    
	public void acceptChangingCalendar() {
		if (this.changingCalendar != null) {
			if (callValueValidationCallback((Calendar)this.changingCalendar.clone())) {
				getSkinnable().setCalendar(this.changingCalendar);
			}
			this.changingCalendar = null;
			refresh();
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private boolean callValueValidationCallback(Calendar value) {
		Callback<Calendar, Boolean> lCallback = getSkinnable().getValueValidationCallback();
		if (lCallback == null) {
			return true;
		}
		return lCallback.call(value);
	}
}
