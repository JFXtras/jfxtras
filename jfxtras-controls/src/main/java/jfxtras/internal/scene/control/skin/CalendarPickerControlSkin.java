/**
 * CalendarPickerControlSkin.java
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import jfxtras.css.CssMetaDataForSkinProperty;
import jfxtras.css.converters.SimpleDateFormatConverter;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.CalendarTimePicker;
import jfxtras.scene.control.ListSpinner;
import jfxtras.scene.control.ListSpinner.CycleEvent;
import jfxtras.scene.control.ListSpinnerIntegerList;
import jfxtras.scene.layout.GridPane;

import com.sun.javafx.css.converters.EnumConverter;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 * This skin uses regular JavaFX controls
 * @author Tom Eugelink
 *
 */
public class CalendarPickerControlSkin extends CalendarPickerMonthlySkinAbstract<CalendarPickerControlSkin>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarPickerControlSkin(CalendarPicker control)
	{
		super(control);
		construct();
	}

	/*
	 * construct the component
	 */
	private void construct()
	{
		// setup component
		createNodes();
		layoutNodes();
		
		// start listening to changes
		// if the calendar changes, the display calendar will jump to show that
		getSkinnable().calendarProperty().addListener( (InvalidationListener) observable -> {
			if (getSkinnable().getCalendar() != null) {
				getSkinnable().setDisplayedCalendar(getSkinnable().getCalendar());
			}
		});
		if (getSkinnable().getCalendar() != null) {
			getSkinnable().setDisplayedCalendar(getSkinnable().getCalendar());
		}
		
		// if the calendars change, the selection must be refreshed
		getSkinnable().calendars().addListener( (InvalidationListener) observable -> {
			refreshDayButtonToggleState();
		});
		
        // react to changes in the locale
        getSkinnable().localeProperty().addListener( (InvalidationListener) observable -> {
			monthListSpinner.setItems(FXCollections.observableArrayList(getMonthLabels()));

			// force change the locale in the displayed calendar
			getSkinnable().displayedCalendar().set( (Calendar)getSkinnable().getDisplayedCalendar().clone() );
			refresh();
        });

        // react to changes in the locale
        getSkinnable().showTimeProperty().addListener( (InvalidationListener) observable -> {
            layoutNodes();
        });

        // react to changes in the disabled calendars
        getSkinnable().disabledCalendars().addListener( new ListChangeListener<Calendar>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Calendar> change) {
				refreshDayButtonsVisibilityAndLabel();
			}
	    });
        
        // react to changes in the highlighted calendars
        getSkinnable().highlightedCalendars().addListener(new ListChangeListener<Calendar>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Calendar> arg0) {
				refreshDayButtonsVisibilityAndLabel();
			}
   	     });

		// react to changes in the displayed calendar
		getSkinnable().displayedCalendar().addListener( (InvalidationListener) observable -> {
			refresh();
		});

		// update the data
		refresh();
	}
	
	// ==================================================================================================================
	// PROPERTIES

	/**
	 * This skin has the displayed date always pointing to the first of the month
	 * @param displayedCalendar
	 * @return
	 */
	protected Calendar deriveDisplayedCalendar(Calendar displayedCalendar)
	{
		// always the 1st of the month, without time
		Calendar lCalendar = Calendar.getInstance(getSkinnable().getLocale());
		lCalendar.set(Calendar.DATE, 1);
		lCalendar.set(Calendar.MONTH, displayedCalendar.get(Calendar.MONTH));
		lCalendar.set(Calendar.YEAR, displayedCalendar.get(Calendar.YEAR));
		lCalendar.set(Calendar.HOUR_OF_DAY, 0);
		lCalendar.set(Calendar.MINUTE, 0);
		lCalendar.set(Calendar.SECOND, 0);
		lCalendar.set(Calendar.MILLISECOND, 0);
		return lCalendar;
	}

	// ==================================================================================================================
	// StyleableProperties
	
	/** ShowWeeknumbers: */
    public final ObjectProperty<ShowWeeknumbers> showWeeknumbersProperty() { return showWeeknumbers; }
    private ObjectProperty<ShowWeeknumbers> showWeeknumbers = new SimpleStyleableObjectProperty<ShowWeeknumbers>(StyleableProperties.SHOW_WEEKNUMBERS, this, "showWeeknumbers", StyleableProperties.SHOW_WEEKNUMBERS.getInitialValue(null)) {
    	{ // anonymous constructor
			addListener( (invalidationEvent) -> {
                layoutNodes();
			});
		}
    };
    public final void setShowWeeknumbers(ShowWeeknumbers value) { showWeeknumbersProperty().set(value); }
    public final ShowWeeknumbers getShowWeeknumbers() { return showWeeknumbers.get(); }
    public final CalendarPickerControlSkin withShowWeeknumbers(ShowWeeknumbers value) { setShowWeeknumbers(value); return this; }
    public enum ShowWeeknumbers {YES, NO}
    
	/** LabelDateFormat: */
    public final ObjectProperty<DateFormat> labelDateFormatProperty() { return labelDateFormat; }
    private ObjectProperty<DateFormat> labelDateFormat = new SimpleStyleableObjectProperty<DateFormat>(StyleableProperties.LABEL_DATEFORMAT, this, "labelDateFormat", StyleableProperties.LABEL_DATEFORMAT.getInitialValue(null)) {
    	{ // anonymous constructor
			addListener( (invalidationEvent) -> {
                refreshDayButtonsVisibilityAndLabel();
			});
		}
    };
    public final void setLabelDateFormat(DateFormat value) { labelDateFormatProperty().set(value); }
    public final DateFormat getLabelDateFormat() { return labelDateFormat.get(); }
    public final CalendarPickerControlSkin withLabelDateFormat(DateFormat value) { setLabelDateFormat(value); return this; }
    static private final SimpleDateFormat ID_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    // ----------------------------
    // communicate the styleables

    private static class StyleableProperties {
    	
        private static final CssMetaData<CalendarPicker, ShowWeeknumbers> SHOW_WEEKNUMBERS = new CssMetaDataForSkinProperty<CalendarPicker, CalendarPickerControlSkin, ShowWeeknumbers>("-fxx-show-weeknumbers", new EnumConverter<ShowWeeknumbers>(ShowWeeknumbers.class), ShowWeeknumbers.YES ) {
        	@Override 
        	protected ObjectProperty<ShowWeeknumbers> getProperty(CalendarPickerControlSkin s) {
            	return s.showWeeknumbersProperty();
            }
        };

        private static final CssMetaData<CalendarPicker, DateFormat> LABEL_DATEFORMAT = new CssMetaDataForSkinProperty<CalendarPicker, CalendarPickerControlSkin, DateFormat>("-fxx-label-dateformat", new SimpleDateFormatConverter(), new SimpleDateFormat("d") ) {
        	@Override 
        	protected ObjectProperty<DateFormat> getProperty(CalendarPickerControlSkin s) {
            	return s.labelDateFormatProperty();
            }
        };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<CssMetaData<? extends Styleable, ?>>(SkinBase.getClassCssMetaData());
            styleables.add(SHOW_WEEKNUMBERS);
            styleables.add(LABEL_DATEFORMAT);
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
     * This method should delegate to {@link javafx.scene.Node#getClassCssMetaData()} so that
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
	private void createNodes()
	{
		// setup the grid so all weekday togglebuttons will grow, but the weeknumbers do not
		ColumnConstraints lColumnConstraintsAlwaysGrow = new ColumnConstraints();
		lColumnConstraintsAlwaysGrow.setHgrow(Priority.ALWAYS);
		ColumnConstraints lColumnConstraintsNeverGrow = new ColumnConstraints();
		lColumnConstraintsNeverGrow.setHgrow(Priority.NEVER);

		// month spinner
		List<String> lMonthLabels = getMonthLabels();
		monthListSpinner = new ListSpinner<String>(lMonthLabels).withIndex(Calendar.getInstance().get(Calendar.MONTH)).withCyclic(Boolean.TRUE);
		monthListSpinner.setId("monthListSpinner");
		// on cycle overflow to year
		monthListSpinner.setOnCycle(new EventHandler<ListSpinner.CycleEvent>()
		{
			@Override
			public void handle(CycleEvent evt)
			{
				// if we've cycled down
				if (evt.cycledDown()) 
				{
					yearListSpinner.increment();
				}
				else 
				{
					yearListSpinner.decrement();				
				}
					 
			}
		});
		// if the value changed, update the displayed calendar
		monthListSpinner.valueProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue arg0, String arg1, String arg2)
			{
				setDisplayedCalendarFromSpinners();
			}
		});
		
		// year spinner
		yearListSpinner = new ListSpinner<Integer>(new ListSpinnerIntegerList()).withValue(Calendar.getInstance().get(Calendar.YEAR));
		yearListSpinner.setId("yearListSpinner");
		// if the value changed, update the displayed calendar
		yearListSpinner.valueProperty().addListener(new ChangeListener<Integer>()
		{
			@Override
			public void changed(ObservableValue observableValue, Integer oldValue, Integer newValue)
			{
				setDisplayedCalendarFromSpinners();
			}
		});
		
		// double click here to show today
		todayButton = new Button("   ");
        todayButton.getStyleClass().add("today-button");
        todayButton.setMinSize(16, 16);
		todayButton.setOnAction((ActionEvent event) -> {
            setDisplayedCalendarToToday();
        });
		
		// weekday labels
		for (int i = 0; i < 7; i++)
		{
			// create buttons
			Label lLabel = new Label("" + i);
			// style class is set together with the label
			lLabel.getStyleClass().add("weekday-label"); 
			lLabel.setMaxWidth(Integer.MAX_VALUE); // this is one of those times; why the @#$@#$@#$ do I need to specify this in order to make the damn label centered?
			
			// remember the column it is associated with
			lLabel.setUserData(Integer.valueOf(i));
			lLabel.onMouseClickedProperty().set(weekdayLabelMouseClickedPropertyEventHandler);

			// remember it
			weekdayLabels.add(lLabel);
		}
		
		// weeknumber labels
		for (int i = 0; i < 6; i++)
		{
			// create buttons
			Label lLabel = new Label("" + i);
			lLabel.getStyleClass().add("weeknumber");
			lLabel.setAlignment(Pos.BASELINE_RIGHT);
			
			// remember it
			weeknumberLabels.add(lLabel);
			
			// remember the row it is associated with
			lLabel.setUserData(Integer.valueOf(i));
			lLabel.onMouseClickedProperty().set(weeknumerLabelMouseClickedPropertyEventHandler);
		}
		
		// setup: 6 rows of 7 days per week (which is the maximum number of buttons required in the worst case layout)
		for (int i = 0; i < 6 * 7; i++)
		{
			// create buttons
			ToggleButton lToggleButton = new ToggleButton("" + i);
			lToggleButton.setId("day" + i);
			lToggleButton.getStyleClass().add("day-button");
			lToggleButton.selectedProperty().addListener(toggleButtonSelectedPropertyChangeListener); // for minimal memory usage, use a single listener
			lToggleButton.onMouseReleasedProperty().set(toggleButtonMouseReleasedPropertyEventHandler); // for minimal memory usage, use a single listener
			lToggleButton.onKeyReleasedProperty().set(toggleButtonKeyReleasedPropertyEventHandler); // for minimal memory usage, use a single listener
			
			// remember which button belongs to this property
			booleanPropertyToDayToggleButtonMap.put(lToggleButton.selectedProperty(), lToggleButton);
			
			// add it
			lToggleButton.setMaxWidth(Double.MAX_VALUE); // make the button grow to fill a GridPane's cell
			lToggleButton.setAlignment(Pos.BASELINE_CENTER);
			
			// remember it
			dayButtons.add(lToggleButton);
		}

		// add timepicker
		Bindings.bindBidirectional(timePicker.calendarProperty(), getSkinnable().calendarProperty());
		Bindings.bindBidirectional(timePicker.valueValidationCallbackProperty(), getSkinnable().valueValidationCallbackProperty());
		Bindings.bindBidirectional(timePicker.localeProperty(), getSkinnable().localeProperty());

		// add to self
        getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
	}
	// the result
	private ListSpinner<String> monthListSpinner = null;
	private ListSpinner<Integer> yearListSpinner = null;
	private Button todayButton = new Button("   ");
	final private List<Label> weekdayLabels = new ArrayList<Label>();
	final private List<Label> weeknumberLabels = new ArrayList<Label>();
	final private List<ToggleButton> dayButtons = new ArrayList<ToggleButton>();
	final private CalendarTimePicker timePicker = new CalendarTimePicker();
	final private Map<BooleanProperty, ToggleButton> booleanPropertyToDayToggleButtonMap = new WeakHashMap<BooleanProperty, ToggleButton>();
	final private ChangeListener<Boolean> toggleButtonSelectedPropertyChangeListener = new ChangeListener<Boolean>()
	{
		@Override
		public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue)
		{
			refreshDayButtonToggleState();
		}
	};
	final private EventHandler<MouseEvent> toggleButtonMouseReleasedPropertyEventHandler = new EventHandler<MouseEvent>()
	{
		@Override
		public void handle(MouseEvent event)
		{
			ToggleButton lToggleButton = (ToggleButton)event.getSource();					
			toggle(lToggleButton, event.isShiftDown());
		}
	};
	final private EventHandler<KeyEvent> toggleButtonKeyReleasedPropertyEventHandler = new EventHandler<KeyEvent>()
	{
		@Override
		public void handle(KeyEvent event)
		{
			ToggleButton lToggleButton = (ToggleButton)event.getSource();
			if (" ".equals(event.getText()))
			{
				toggle(lToggleButton, event.isShiftDown());
			}
		}
	};
	final private EventHandler<MouseEvent> weekdayLabelMouseClickedPropertyEventHandler = new EventHandler<MouseEvent>()
	{
		@Override
		public void handle(MouseEvent event)
		{
			// in single or range mode this does not do anything
			if (getSkinnable().getMode() == CalendarPicker.Mode.SINGLE) return;
			if (getSkinnable().getMode() == CalendarPicker.Mode.RANGE) return;
			
			// process the calendars
			int lColIdx = ((Integer)((Label)event.getSource()).getUserData()).intValue();
			for (int lRowIdx = 0; lRowIdx < 6; lRowIdx++)
			{			
				int lIdx = (lRowIdx * 7) + lColIdx;
				ToggleButton lToggleButton = dayButtons.get(lIdx);	
				if (lToggleButton.isVisible() == true) toggle(lToggleButton, false);
			}
		}
	};
	final private EventHandler<MouseEvent> weeknumerLabelMouseClickedPropertyEventHandler = new EventHandler<MouseEvent>()
	{
		@Override
		public void handle(MouseEvent event)
		{
			// in single mode this does not do anything
			if (getSkinnable().getMode() == CalendarPicker.Mode.SINGLE) return;
			
			// in range mode we clear the existing selection
			if (getSkinnable().getMode() == CalendarPicker.Mode.RANGE)
			{
				getSkinnable().calendars().clear();
			}
			
			// process the calendars
			int lRowIdx = ((Integer)((Label)event.getSource()).getUserData()).intValue();
			for (int i = lRowIdx * 7; i < (lRowIdx * 7) + 7; i++)
			{			
				ToggleButton lToggleButton = dayButtons.get(i);	
				if (getSkinnable().getMode() == CalendarPicker.Mode.RANGE) 
				{
					Calendar lCalendar = calendarForToggleButton(lToggleButton);
					if (callValueValidationCallback(lCalendar)) {
						getSkinnable().calendars().add(lCalendar);
					}
				}
				else
				{
					toggle(lToggleButton, false);
				}
			}
		}
	};
		
	/**
	 * construct the nodes
	 */
	private void layoutNodes()
	{
		getChildren().clear();
		
		// the result
		GridPane gridPane = new GridPane();
		gridPane = new GridPane();
		gridPane.setVgap(2.0);
		gridPane.setHgap(2.0);
		//gridPane.setPadding(new javafx.geometry.Insets(0,0,0,0));
		//gridPane.gridLinesVisibleProperty().set(true);
		getChildren().add(gridPane);
		
		// show weeknumbers
		boolean lShowWeeknumbers = ShowWeeknumbers.YES.equals( getShowWeeknumbers() );
		int lWeeknumbersCols = (lShowWeeknumbers ? 1 : 0);
		 
		// setup the grid so all weekday togglebuttons will grow, but the weeknumbers do not
		ColumnConstraints lColumnConstraintsAlwaysGrow = new ColumnConstraints();
		lColumnConstraintsAlwaysGrow.setHgrow(Priority.ALWAYS);
		ColumnConstraints lColumnConstraintsNeverGrow = new ColumnConstraints();
		lColumnConstraintsNeverGrow.setHgrow(Priority.NEVER);
		if (lShowWeeknumbers) 
		{
			gridPane.getColumnConstraints().addAll(lColumnConstraintsNeverGrow);
		}
		gridPane.getColumnConstraints().addAll(lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow);

		// month spinner
		gridPane.add(monthListSpinner, new GridPane.C().col(lWeeknumbersCols).row(0).colSpan(4).rowSpan(1));
		
		// year spinner
		gridPane.add(yearListSpinner, new GridPane.C().col(lWeeknumbersCols + 4).row(0).colSpan(3).rowSpan(1));
		
		// double click here to show today
		if (lShowWeeknumbers) {
			gridPane.add(todayButton, new GridPane.C().col(0).row(1));
		}		
		
		// weekday labels
		for (int i = 0; i < 7; i++)
		{
			gridPane.add(weekdayLabels.get(i), new GridPane.C().col(lWeeknumbersCols + i).row(1));
		}
		
		// weeknumber labels
		if (lShowWeeknumbers) 
		{
			for (int i = 0; i < 6; i++)
			{
				gridPane.add(weeknumberLabels.get(i), new GridPane.C().col(0).row(i + 2).margin(new javafx.geometry.Insets(0,0,0,0)));
			}
		}
		
		// setup: 6 rows of 7 days per week (which is the maximum number of buttons required in the worst case layout)
		for (int i = 0; i < 6 * 7; i++)
		{
			gridPane.add(dayButtons.get(i), new GridPane.C().col(lWeeknumbersCols + (i % 7)).row((i / 7) + 2));
		}

		// add timepicker
		// TODO: this is done upon construction, we need to make this dynamic based on Mode and showTime
		if (getSkinnable().getMode().equals(CalendarPicker.Mode.SINGLE) && getSkinnable().showTimeProperty().get() == true)
		{
			gridPane.add(timePicker, new GridPane.C().col(lWeeknumbersCols).row(8).colSpan(7).rowSpan(1));
		}
	}

	/**
	 * 
	 * @param toggleButton
	 * @return
	 */
	private Calendar calendarForToggleButton(ToggleButton toggleButton)
	{
		// base reference
		int lDayToggleButtonIdx = dayButtons.indexOf(toggleButton);
		
		// calculate the day-of-month
		int lFirstOfMonthIdx = determineFirstOfMonthDayOfWeek();
		int lDayOfMonth = lDayToggleButtonIdx - lFirstOfMonthIdx + 1;

		// create calendar representing the date that was toggled
		Calendar lToggledCalendar = (Calendar)getSkinnable().getDisplayedCalendar().clone();
		lToggledCalendar.set(Calendar.YEAR, getSkinnable().getDisplayedCalendar().get(Calendar.YEAR));
		lToggledCalendar.set(Calendar.MONTH, getSkinnable().getDisplayedCalendar().get(Calendar.MONTH));
		lToggledCalendar.set(Calendar.DATE, lDayOfMonth);
		
		// include time
		if (timePicker.isVisible() && timePicker.getCalendar() != null)
		{
			lToggledCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getCalendar().get(Calendar.HOUR_OF_DAY));
			lToggledCalendar.set(Calendar.MINUTE, timePicker.getCalendar().get(Calendar.MINUTE));
			lToggledCalendar.set(Calendar.SECOND, timePicker.getCalendar().get(Calendar.SECOND));
		}
		
		// return
		return lToggledCalendar;
	}
	
	/**
	 * 
	 * @param toggleButton
	 * @param shiftIsPressed
	 */
	private void toggle(ToggleButton toggleButton, boolean shiftIsPressed)		
	{
		// create calendar representing the date that was toggled
		Calendar lToggledCalendar = calendarForToggleButton(toggleButton);

		// select or deselect
		List<Calendar> lCalendars = getSkinnable().calendars();
		Calendar lFoundCalendar = find(lCalendars, lToggledCalendar); // find solely on YMD not HMS 
		if (lFoundCalendar == null) // if not found 
		{
			// make sure it adheres to the mode
			// SINGLE: clear all but the last selected
			// RANGE: if shift is not pressed, behave like single
			if ( getSkinnable().getMode() == CalendarPicker.Mode.SINGLE 
			  || (getSkinnable().getMode() == CalendarPicker.Mode.RANGE && shiftIsPressed == false)
			   ) {
				// Normally one would removed all the old ones and add one new,
				// but this would result in a moment where the value would be null and that can be unwanted.
				// So we first add the new value and then clear away the old ones until 1 remains.
				if (callValueValidationCallback(lToggledCalendar)) {
					lCalendars.add(lToggledCalendar);
					while (lCalendars.size() > 1) {
						lCalendars.remove(0);
					}
				}
			}
			if ( getSkinnable().getMode() == CalendarPicker.Mode.MULTIPLE  && shiftIsPressed == false ) {
				if (callValueValidationCallback(lToggledCalendar)) {
					lCalendars.add(lToggledCalendar);
				}
			}
			if ( (getSkinnable().getMode() == CalendarPicker.Mode.MULTIPLE || getSkinnable().getMode() == CalendarPicker.Mode.RANGE) 
			  && shiftIsPressed == true
			   ) {
				// we must have a last selected
				if (iLastSelected != null) 
				{
					// get the other calendar and make sure other <= toggle
					Calendar lOtherCalendar = iLastSelected;
					int lDirection = (lOtherCalendar.after(lToggledCalendar) ? -1 : 1);
					
					// walk towards the target (toggle) date and add all in between
					Calendar lWalker = (Calendar)lOtherCalendar.clone(); 
					lWalker.add(Calendar.DATE, lDirection);
					Calendar lTarget = (Calendar)lToggledCalendar.clone(); 
					lTarget.add(Calendar.DATE, lDirection);
					while (lWalker.equals(lTarget) == false)
					{
						// if valid do add to range
						if (callValueValidationCallback(lWalker)) {
							lCalendars.add((Calendar)lWalker.clone());
						}
						else {
							// if a calendar was not valid in range mode (in which ranges must always be uninterrupted), exit the loop
							if (getSkinnable().getMode() == CalendarPicker.Mode.RANGE) {
								break; 
							}
						}
						
						// next
						lWalker.add(Calendar.DATE, lDirection);
					}
				}
			}
			
			// remember
			iLastSelected = (Calendar)lToggledCalendar.clone();
		}
        else 
        {
            // special behavior for CalendarTextField: reselect in case of not allow null, by inserting the same calendar again, so the current can be removed (the popup will be closed then)
            if (lCalendars.size() == 1 && getSkinnable().getAllowNull() == false)
            {
// for some reason this causes a UnsupportedOperationException:
//            	lCalendars.add( (Calendar)lFoundCalendar.clone() );
            	Calendar lCalendar = (Calendar)lFoundCalendar.clone();
            	if (callValueValidationCallback(lCalendar)) {
            		getSkinnable().setCalendar(lCalendar);
            	}
            }
            
            if (lCalendars.size() > 1 || getSkinnable().getAllowNull()) {
	            // remove
	            lCalendars.remove(lFoundCalendar);
	            iLastSelected = null;
	        }
        }

		// make sure the buttons are toggled correctly
		refreshDayButtonToggleState();
	}
	private Calendar iLastSelected = null;

	/*
	 * 
	 */
	private void setDisplayedCalendarFromSpinners()
	{
		// get spinner values
		int lYear = yearListSpinner.getValue().intValue();
		int lMonth = monthListSpinner.getIndex();
		
		// get new calendar to display
		Calendar lCalendar = (Calendar)getSkinnable().getDisplayedCalendar().clone();
		lCalendar.set(Calendar.YEAR, lYear);
		lCalendar.set(Calendar.MONTH, lMonth);
		
		// set it
		getSkinnable().setDisplayedCalendar(lCalendar);
	}
	

	/*
	 * 
	 */
	private void setDisplayedCalendarToToday()
	{
		// get spinner values
		Calendar lTodayCalendar = Calendar.getInstance();
		
		// get new calendar to display
		Calendar lCalendar = (Calendar)getSkinnable().getDisplayedCalendar().clone();
		lCalendar.set(Calendar.YEAR, lTodayCalendar.get(Calendar.YEAR));
		lCalendar.set(Calendar.MONTH, lTodayCalendar.get(Calendar.MONTH));
		
		// set it
		getSkinnable().setDisplayedCalendar(lCalendar);
	}
	
	/**
	 * refresh all
	 */
	protected void refresh()
	{
		calendarRangeCallback();
		refreshSpinner();
		refreshWeekdayLabels();
		refreshWeeknumberLabels();
		refreshDayButtonsVisibilityAndLabel();
		refreshDayButtonToggleState();
	}
	
	/*
	 * 
	 */
	private void refreshSpinner()
	{
		// no updating while in callback, because we will update all in one go afterwards
		if (calendarRangeCallbackAtomicInteger.get() !=0) return;
		
		// get calendar
		Calendar lCalendar = (Calendar)getSkinnable().getDisplayedCalendar();

		// get the value for the corresponding index and set that
		List<String> lMonthLabels = getMonthLabels();
		String lMonthLabel = lMonthLabels.get( lCalendar.get(Calendar.MONTH) ); 
		monthListSpinner.setValue( lMonthLabel );
		
		// set value
		yearListSpinner.setValue(lCalendar.get(Calendar.YEAR));
		
	}
	
	/*
	 * 
	 */
	private void refreshWeekdayLabels()
	{
		// no updating while in callback, because we will update all in one go afterwards
		if (calendarRangeCallbackAtomicInteger.get() !=0) return;
		
		// get labels
		List<String> lWeekdayLabels = getWeekdayLabels();
		
		// set them
		for (int i = 0; i < weekdayLabels.size(); i++)
		{
			Label lLabel = weekdayLabels.get(i);
			lLabel.setText( lWeekdayLabels.get(i) );
			lLabel.getStyleClass().removeAll("weekend", "weekday");
			lLabel.getStyleClass().add(isWeekdayWeekend(i) ? "weekend" : "weekday");
		}
	}
	
	/*
	 * 
	 */
	private void refreshWeeknumberLabels()
	{
		// no updating while in callback, because we will update all in one go afterwards
		if (calendarRangeCallbackAtomicInteger.get() !=0) return;
		
		// get labels
		List<Integer> lWeeknumbers = getWeeknumbers();
		
		// set them
		for (int i = 0; i < lWeeknumbers.size(); i++)
		{
			Label lLabel = weeknumberLabels.get(i);
			lLabel.setText( (lWeeknumbers.get(i).intValue() < 10 ? "0" : "") + lWeeknumbers.get(i).toString() );
		}
	}
	
	/*
	 * 
	 */
	private void refreshDayButtonsVisibilityAndLabel()
	{
		// no updating while in callback, because we will update all in one go afterwards
		if (calendarRangeCallbackAtomicInteger.get() !=0) return;
		
		// prep
		List<Calendar> highlightedCalendars = getSkinnable().highlightedCalendars();
		List<Calendar> disabledCalendars = getSkinnable().disabledCalendars();
		
		// setup the buttons [0..(6*7)-1]
		// displayed calendar always points to the 1st of the month
		int lFirstOfMonthIdx = determineFirstOfMonthDayOfWeek();

		// hide the preceeding buttons
		for (int i = 0; i < lFirstOfMonthIdx; i++)
		{
			ToggleButton lToggleButton = dayButtons.get(i); 
			lToggleButton.setVisible(false);
			lToggleButton.setId("day" + i);
		}
		// make all weeklabels invisible
		for (int i = 1; i < 6; i++)
		{
			weeknumberLabels.get(i).setVisible(false);
		}
		
		// set the month buttons
		int lDaysInMonth = determineDaysInMonth();
		Calendar lCalendar = (Calendar)getSkinnable().getDisplayedCalendar().clone();
		for (int i = 1; i <= lDaysInMonth; i++)
		{
			// set the date
			lCalendar.set(java.util.Calendar.DATE, i);

			// determine the index in the buttons
			int lIdx = lFirstOfMonthIdx + i - 1;

			// update the button
			ToggleButton lToggleButton = dayButtons.get(lIdx); 
			lToggleButton.setId(ID_DATEFORMAT.format(lCalendar.getTime()));
			lToggleButton.setVisible(true);
			lToggleButton.setText(getLabelDateFormat().format(lCalendar.getTime()));
			lToggleButton.setAlignment(Pos.BASELINE_CENTER);
			lToggleButton.getStyleClass().removeAll("weekend", "weekday");
			lToggleButton.getStyleClass().add(isWeekdayWeekend(lIdx % 7) ? "weekend" : "weekday"); 
			lToggleButton.setDisable( disabledCalendars != null && find(disabledCalendars, lCalendar) != null );
			
			// make the corresponding weeklabel visible
			weeknumberLabels.get(lIdx / 7).setVisible(true);

			// highlight today
			lToggleButton.getStyleClass().remove("today");
			if (isToday(lCalendar))
			{
				lToggleButton.getStyleClass().add("today");
			}	

			// highlight
			lToggleButton.getStyleClass().remove("highlight");
			if (highlightedCalendars != null && find(highlightedCalendars, lCalendar) != null)
			{
				lToggleButton.getStyleClass().add("highlight");
			}
		}

		// hide the trailing buttons
		for (int i = lFirstOfMonthIdx + lDaysInMonth; i < 6*7; i++)
		{
			ToggleButton lToggleButton = dayButtons.get(i); 
			lToggleButton.setVisible(false);
			lToggleButton.setId("day" + i);
		}
	}

	/*
	 * 
	 */
	private void refreshDayButtonToggleState()
	{
		// no updating while in callback, because we will update all in one go afterwards
		if (calendarRangeCallbackAtomicInteger.get() !=0) return;
		
		iRefreshingSelection.addAndGet(1);
		try
		{
			// setup the buttons [0..(6*7)-1]
			// displayed calendar always points to the 1st of the month
			int lFirstOfMonthIdx = determineFirstOfMonthDayOfWeek();
			List<Calendar> lCalendars = getSkinnable().calendars();
			
			// set the month buttons
			int lDaysInMonth = determineDaysInMonth();
			Calendar lCalendar = (Calendar)getSkinnable().getDisplayedCalendar().clone();
			for (int i = 1; i <= lDaysInMonth; i++)
			{
				// set the date
				lCalendar.set(java.util.Calendar.DATE, i);
	
				// determine the index in the buttons
				int lIdx = lFirstOfMonthIdx + i - 1;
	
				// is selected
				boolean lSelected = (find(lCalendars, lCalendar) != null);  
				dayButtons.get(lIdx).setSelected( lSelected );
			}
		}
		finally
		{
			iRefreshingSelection.addAndGet(-1);
		}
	}
	final private AtomicInteger iRefreshingSelection = new AtomicInteger(0);
	
	/**
	 * contains only check YMD
	 * @param calendars
	 * @param calendar
	 * @return
	 */
	private Calendar find(List<Calendar> calendars, Calendar calendar)
	{
		for (Calendar c : calendars)
		{
			if ( c.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
			  && c.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
			  && c.get(Calendar.DATE) == calendar.get(Calendar.DATE)
			   )
			{
				return c;
			}
		}
		return null;
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
		return lCallback.call(value == null ? null : (Calendar)value.clone());
	}
}
