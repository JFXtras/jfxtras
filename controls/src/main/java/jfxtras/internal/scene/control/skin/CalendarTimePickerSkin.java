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

import java.util.Calendar;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import jfxtras.scene.control.CalendarTimePicker;

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
		getSkinnable().calendarProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				// paint
				refresh();
			} 
		});
		refresh();

		// react to changes in the minuteStep 
		getSkinnable().minuteStepProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				minuteScrollSlider.setBlockIncrement(getSkinnable().getMinuteStep().doubleValue());
			} 
		});
		minuteScrollSlider.setBlockIncrement(getSkinnable().getMinuteStep().doubleValue());
		
		// react to changes in showLabels 
		getSkinnable().showLabelsProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				// paint
				refreshLayout();
			} 
		});
	}
	
	// ==================================================================================================================
	// PROPERTIES
	

	// ==================================================================================================================
	// DRAW
	
    /**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// two sliders
		hourScrollSlider.setId("hourSlider");
		hourScrollSlider.minProperty().set(00);
		hourScrollSlider.maxProperty().set(23);
//		hourScrollSlider.setShowTickLabels(true);
//		hourScrollSlider.setShowTickMarks(true);
		hourScrollSlider.setMajorTickUnit(12);
		hourScrollSlider.setMinorTickCount(3);
		minuteScrollSlider.setId("minuteSlider");
		minuteScrollSlider.minProperty().set(00);
		minuteScrollSlider.maxProperty().set(59);
//		minuteScrollSlider.setShowTickLabels(true);
//		minuteScrollSlider.setShowTickMarks(true);
		minuteScrollSlider.setMajorTickUnit(10);
		hourScrollSlider.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				Calendar lCalendar = (Calendar)getSkinnable().getCalendar();
				lCalendar = (lCalendar == null ? Calendar.getInstance() : (Calendar)lCalendar.clone());
				lCalendar.set(Calendar.HOUR_OF_DAY, newValue.intValue());
				if (lCalendar.equals(getSkinnable().getCalendar()) == false) {
					getSkinnable().setCalendar(lCalendar);
				}
			}
		});
		minuteScrollSlider.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				Calendar lCalendar = (Calendar)getSkinnable().getCalendar();
				lCalendar = (lCalendar == null ? Calendar.getInstance() : (Calendar)lCalendar.clone());
				
				// in order no to first set a non stepsize calendar, we step the minutes here 
				int lMinutes = newValue.intValue();
				int lMinuteStep = getSkinnable().getMinuteStep();
				if (lMinuteStep > 1)
				{
					lMinutes += getSkinnable().getMinuteStep() / 2; // add half a step, so the scroller jumps to the next tick when the mouse is half way
					if (lMinutes > 59) lMinutes -= lMinuteStep;
				}
				lCalendar.set(Calendar.MINUTE, lMinutes);
				lCalendar = blockMinutesToStep(lCalendar, getSkinnable().getMinuteStep());
				if (lCalendar.equals(getSkinnable().getCalendar()) == false) {
					getSkinnable().setCalendar(lCalendar);
				}
			}
		});
		
		// add label
		timeText.setDisable(true);
		timeText.getStyleClass().add("timeLabel");

		// refresh layout
		refreshLayout();
		
		// add self as CSS style
		getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control		
	}
	final private Slider hourScrollSlider = new Slider();
	final private Slider minuteScrollSlider = new Slider();
	final private Text timeText = new Text("XX:XX");
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
						  * ( i == 0 ? 0 : 1)); // and the most left
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
			if (getSkinnable().showLabelsProperty().get())
			{
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
			if (lStep < getSkinnable().getMinuteStep()) lStep = getSkinnable().getMinuteStep();
			for (int i = 0; i <= 59; i += lStep)
			{
				Text lText = new Text("" + i);
				lText.setY(lText.prefHeight(0));
				double lX = (lScrollSliderOuterPadding + ((minuteScrollSlider.getWidth() - (2*lScrollSliderOuterPadding)) / 59 * i)) - (lText.prefWidth(0) 
						  / (i == 59? 1 : 2) // center, except the most right 
						  * ( i == 0 ? 0 : 1)); // and the most left
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
	 * 
	 */
	private void refreshLayout()
	{
		// layout
		getChildren().clear();
		VBox lVBox = new VBox(0);
		lVBox.alignmentProperty().set(Pos.CENTER);
		if (getSkinnable().getShowLabels()) lVBox.getChildren().add(hourLabelsPane);
		lVBox.getChildren().add(hourScrollSlider);
		lVBox.getChildren().add(minuteScrollSlider);
		if (getSkinnable().getShowLabels()) lVBox.getChildren().add(minuteLabelsPane);
		getChildren().add(lVBox);
		getChildren().add(timeText);
	}
	
	/**
	 * 
	 */
	private void refresh()
	{
		Calendar lCalendar = getSkinnable().getCalendar();
		int lHour = lCalendar == null ? 0 : lCalendar.get(Calendar.HOUR_OF_DAY);
		int lMinute = lCalendar == null ? 0 : lCalendar.get(Calendar.MINUTE);
		hourScrollSlider.valueProperty().set(lHour);
		minuteScrollSlider.valueProperty().set(lMinute);
		timeText.setText( calendarTimeToText(lCalendar));
	}
	
	/**
	 * 
	 * @param calendar
	 * @return
	 */
	static public String calendarTimeToText(Calendar calendar)
	{
		if (calendar == null) return "";
		int lHour = calendar.get(Calendar.HOUR_OF_DAY);
		int lMinute = calendar.get(Calendar.MINUTE);
		String lText = (lHour < 10 ? "0" : "") + lHour + ":" + (lMinute < 10 ? "0" : "") + lMinute;
		return lText;
	}
	
	/**
	 * minutes fit in the minute steps
	 */
	static public Calendar blockMinutesToStep(Calendar calendar, Integer stepSize)
	{
		if (stepSize == null || calendar == null) return calendar;
			
		// set the minutes to match the step size
		int lMinutes = calendar.get(Calendar.MINUTE);
		if (stepSize == 1) return calendar;
		lMinutes = lMinutes / stepSize; // trunk
		lMinutes *= stepSize;
		if (calendar.get(Calendar.MINUTE) != lMinutes)
		{
			Calendar lCalendar = (Calendar)calendar.clone();
			lCalendar.set(Calendar.MINUTE, lMinutes);
			calendar = lCalendar;
		}
		return calendar;
	}


}
