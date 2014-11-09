/**
 * AgendaSkinTimeScale24HourAbstract.java
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

package jfxtras.internal.scene.control.skin.agenda;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import jfxtras.animation.Timer;
import jfxtras.internal.scene.control.skin.DateTimeToCalendarHelper;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

/**
 * @author Tom Eugelink
 */
// TBEERNOT: TODO: whole day Task (TaskHeaderPane)
// TBEERNOT: multiple day spanning task
// TBEERNOT: number of call to determineDisplayedLocalDates, can we cache? 
abstract public class AgendaSkinTimeScale24HourAbstract extends SkinBase<Agenda>
implements AgendaSkin
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public AgendaSkinTimeScale24HourAbstract(Agenda control)
	{
		super(control);
		this.control = control;
		construct();
	}
	protected final Agenda control;

	/*
	 * construct the component
	 */
	private void construct()
	{	
		appointments = new AllAppointments(getSkinnable().appointments());
		
		// setup component
		createNodes();

		// react to changes in the locale 
		getSkinnable().localeProperty().addListener( (observable) -> {
			refresh();
		});
		 
		// react to changes in the displayed calendar 
		getSkinnable().displayedDateTime().addListener( (observable) -> {
			assignDateToDayAndHeaderPanes();
			setupAppointments();
		});
		
		// react to changes in the appointments 
		getSkinnable().appointments().addListener( (javafx.collections.ListChangeListener.Change<? extends Appointment> change) -> {
			setupAppointments();
		});
		
		// initial setup
		refresh();
	}
	AllAppointments appointments = null;
	
	/**
	 * Assign a calendar to each day, so it knows what it must draw.
	 */
	private void assignDateToDayAndHeaderPanes()
	{
		// assign it to each day pane
		int i = 0;
		List<LocalDate> lLocalDates = determineDisplayedLocalDates();
		for (LocalDate lLocalDate : lLocalDates)
		{
			// set the calendar
			DayBodyPane lDayPane = weekBodyPane.dayBodyPanes.get(i); 
			lDayPane.localDateObjectProperty.set(lLocalDate);
			DayHeaderPane lDayHeaderPane = weekHeaderPane.dayHeaderPanes.get(i); 
			lDayHeaderPane.localDateObjectProperty.set(lLocalDate);
			i++;
		}		
		
		// place the now line
		nowUpdateRunnable.run(); 
		
		// tell the control what range is displayed, so it can update the appointments
		LocalDate lStartLocalDate = lLocalDates.get(0);
		LocalDate lEndLocalDate = lLocalDates.get(lLocalDates.size() - 1);
		if (getSkinnable().getDateTimeRangeCallback() != null) {
			Agenda.DateTimeRange lRange = new Agenda.DateTimeRange(lStartLocalDate.atStartOfDay(), lEndLocalDate.plusDays(1).atStartOfDay());
			getSkinnable().getDateTimeRangeCallback().call(lRange);
		}
		if (getSkinnable().getCalendarRangeCallback() != null) {
			Agenda.CalendarRange lRange = new Agenda.CalendarRange( DateTimeToCalendarHelper.createCalendarFromLocalDate(lStartLocalDate, Locale.getDefault()), DateTimeToCalendarHelper.createCalendarFromLocalDate(lEndLocalDate, Locale.getDefault()));
			getSkinnable().getCalendarRangeCallback().call(lRange);
		}
	}
	
	/**
	 * 
	 */
	private void refreshLocale()
	{
		// create the formatter to use
		layoutHelp.dayOfWeekDateFormat = new SimpleDateFormat("E", getSkinnable().getLocale());
		layoutHelp.dayOfWeekDateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("E").toFormatter(getSkinnable().getLocale());
		layoutHelp.dateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, getSkinnable().getLocale());
		layoutHelp.dateDateTimeFormatter= new DateTimeFormatterBuilder().appendLocalized(FormatStyle.SHORT, null).toFormatter(getSkinnable().getLocale());

		// assign weekend of weekday class
		for (DayBodyPane lDayBodyPane : weekBodyPane.dayBodyPanes)
		{
			String lWeekendOrWeekday = isWeekend(lDayBodyPane.localDateObjectProperty.get()) ? "weekend" : "weekday";
			lDayBodyPane.getStyleClass().removeAll("weekend", "weekday");
			lDayBodyPane.getStyleClass().add(lWeekendOrWeekday);			
		}
		for (DayHeaderPane lDayHeaderPane : weekHeaderPane.dayHeaderPanes) {
			String lWeekendOrWeekday = isWeekend(lDayHeaderPane.localDateObjectProperty.get()) ? "weekend" : "weekday";
			lDayHeaderPane.getStyleClass().removeAll("weekend", "weekday");
			lDayHeaderPane.getStyleClass().add(lWeekendOrWeekday);			
		}
	}

	/**
	 * Have all days reconstruct the appointments
	 */
	public void setupAppointments() {
		for (DayHeaderPane lDay : weekHeaderPane.dayHeaderPanes) {
			lDay.setupAppointments();
		}
		for (DayBodyPane lDay : weekBodyPane.dayBodyPanes) {
			lDay.setupAppointments();
		}
		calculateSizes(); // must be done after setting up the panes
		nowUpdateRunnable.run(); // set the history
	}

	/**
	 * 
	 */
	public void refresh() {
		assignDateToDayAndHeaderPanes();
		refreshLocale();
		setupAppointments();
		nowUpdateRunnable.run(); 
	}
	
	// ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// we use a borderpane
		borderPane = new BorderPane();
		borderPane.prefWidthProperty().bind(getSkinnable().widthProperty()); // the border pane is the same size as the whole skin
		borderPane.prefHeightProperty().bind(getSkinnable().heightProperty());
		getChildren().add(borderPane);
		
		// borderpane center
		weekBodyPane = new WeekBodyPane();
		weekScrollPane = new ScrollPane();
		weekScrollPane.setContent(weekBodyPane);
		weekScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		weekScrollPane.setFitToWidth(true);
		weekScrollPane.setPannable(false); // panning would conflict with creating a new appointment
		borderPane.setCenter(weekScrollPane);
		// bind to the scrollpane's viewport
		weekScrollPane.viewportBoundsProperty().addListener( (observable) -> {
			calculateSizes();
			nowUpdateRunnable.run();
		});
		
		// borderpane top: header has to be created after the content, because there is a binding
		weekHeaderPane = new WeekHeaderPane(); // must be done after the WeekBodyPane
		weekHeaderPane.setTranslateX(1); // correct for the scrollpane
		borderPane.setTop(weekHeaderPane);
		
		// the borderpane is placed in the drag pane, so DragPane can catch mouse events
		getChildren().remove(borderPane);
		layoutHelp.dragPane.getChildren().add(borderPane);
		getChildren().add(layoutHelp.dragPane);
		
		// style
		getSkinnable().getStyleClass().add(getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control		
	}
	private BorderPane borderPane = null;
	private WeekHeaderPane weekHeaderPane = null;
	private ScrollPane weekScrollPane = null;
	private WeekBodyPane weekBodyPane = null;

	// ==================================================================================================================
	// PANES
	
	abstract protected List<LocalDate> determineDisplayedLocalDates();
	
	/**
	 * Responsible for rendering the day headers within the week
	 */
	class WeekHeaderPane extends Pane
	{
		final List<DayHeaderPane> dayHeaderPanes = new ArrayList<DayHeaderPane>();

		public WeekHeaderPane()
		{
			// one day header pane per day body pane 
			for (DayBodyPane dayBodyPane : weekBodyPane.dayBodyPanes)
			{
				// create pane
				DayHeaderPane lDayHeader = new DayHeaderPane(dayBodyPane.localDateObjectProperty.get(), appointments, layoutHelp); // associate with a day, so we can use its administration. This needs only be done once
				
				// layout in relation to day panes
				lDayHeader.layoutXProperty().bind(dayBodyPane.layoutXProperty()); // same x position as the body			
				lDayHeader.layoutYProperty().set(0);
				lDayHeader.prefWidthProperty().bind(dayBodyPane.prefWidthProperty()); // same width as the body			
				lDayHeader.prefHeightProperty().bind(heightProperty()); // same heigh as the week pane
				getChildren().add(lDayHeader);
				
				// remember
				dayHeaderPanes.add(lDayHeader);
			}
			
			prefWidthProperty().bind(weekBodyPane.widthProperty()); // same width as the weekpane
			prefHeightProperty().bind(layoutHelp.headerHeightProperty);
			
//			// add the skin switcher
//			AgendaSkinSwitcher skinSwitcher = new AgendaSkinSwitcher(control);
//			getChildren().add(skinSwitcher);
		}
	}

	/**
	 * Responsible for rendering the days within the week
	 */
	class WeekBodyPane extends Pane
	{
		final List<DayBodyPane> dayBodyPanes = new ArrayList<DayBodyPane>();

		public WeekBodyPane()
		{
			getStyleClass().add("Week");
			getChildren().add(new TimeScale24Hour(this, layoutHelp));

			int i = 0;
			for (LocalDate localDate : determineDisplayedLocalDates())
			{
				DayBodyPane lDayPane = new DayBodyPane(localDate, appointments, layoutHelp);
				lDayPane.layoutXProperty().bind(layoutHelp.dayWidthProperty.multiply(i).add(layoutHelp.dayFirstColumnXProperty));
				lDayPane.layoutYProperty().set(0.0);
				lDayPane.prefWidthProperty().bind(layoutHelp.dayWidthProperty);
				lDayPane.prefHeightProperty().bind(layoutHelp.dayHeightProperty);
				getChildren().add(lDayPane);
				
				// remember
				dayBodyPanes.add(lDayPane);
				localDate = localDate.plusDays(1);
				i++;
			}
		}
	}
	
	// ==================================================================================================================
	// NOW
	
	final Rectangle nowLine = new Rectangle(0,0,0,0);
	
	/**
	 * This is implemented as a runnable so it can be called from a timer, but also directly
	 */
	Runnable nowUpdateRunnable = new Runnable()
	{
		{
			nowLine.getStyleClass().add("Now");
			nowLine.setHeight(3);
		}
		
		@Override
		public void run()
		{
			//  get now
			LocalDateTime lNow = LocalDateTime.now();
			LocalDate lToday = lNow.toLocalDate();
			
			// see if we are displaying now (this has to do with the fact that now may slide in or out of the view)
			// check all days
			boolean lFound = false;
			for (DayBodyPane lDayPane : weekBodyPane.dayBodyPanes) {
				
				// if the date of the day is the same day as now
				if (lDayPane.localDateObjectProperty.get().isEqual(lToday) == false) {
					
					// not today
					lDayPane.getStyleClass().remove("today");					
				}
				else {
					
					// today
					if (lDayPane.getStyleClass().contains("today") == false) {
						lDayPane.getStyleClass().add("today");
					}
					lFound = true;
					
					// add if not present
					if (weekBodyPane.getChildren().contains(nowLine) == false) {
						weekBodyPane.getChildren().add(nowLine); // this will remove the now line from another day 
						nowLine.xProperty().bind(lDayPane.layoutXProperty());
					}

					// place it
					int lOffsetY = (lNow.getHour() * 60) + lNow.getMinute();
					nowLine.setY(NodeUtil.snapXY(layoutHelp.dayHeightProperty.get() / (24 * 60) * lOffsetY) );
					if (nowLine.widthProperty().isBound() == false) {
						nowLine.widthProperty().bind(layoutHelp.dayWidthProperty);	
					}
				}
			}
			
			// if cannot be placed, remove
			if (lFound == false) {
				weekBodyPane.getChildren().remove(nowLine);
			}
			
			// history 
			for (DayHeaderPane lDayHeaderPane : weekHeaderPane.dayHeaderPanes) {
				for (Node lNode : lDayHeaderPane.getChildren()) {
					if (lNode instanceof AppointmentAbstractPane) {
						((AppointmentAbstractPane)lNode).determineHistoryVisualizer(lNow);
					}
				}
			}
			for (DayBodyPane lDayBodyPane : weekBodyPane.dayBodyPanes) {
				for (Node lNode : lDayBodyPane.getChildren()) {
					if (lNode instanceof AppointmentAbstractPane) {
						((AppointmentAbstractPane)lNode).determineHistoryVisualizer(lNow);
					}
				}
			}
		}
	};
	
	/**
	 * This timer takes care of updating NOW
	 */
	Timer nowTimer = new Timer(nowUpdateRunnable)
		.withCycleDuration(new Duration(60 * 1000)) // every minute
		.withDelay(new Duration( ((60 - LocalDateTime.now().getSecond()) * 1000) - (LocalDateTime.now().getNano() / 1000000)) ) // trigger exactly on each new minute
		.start();  

	
	// ==================================================================================================================
	// SUPPORT

	/**
	 * check if a certain weekday name is a certain day-of-the-week
	 */
	private boolean isWeekend(LocalDate localDate) {
		return (localDate.getDayOfWeek() == DayOfWeek.SATURDAY) || (localDate.getDayOfWeek() == DayOfWeek.SUNDAY);
	}
	

	/**
	 * These values can not be determined by binding them to other values, because their calculation is too complex
	 */
	private void calculateSizes()
	{
		// header
		int lMaxOfWholeDayAppointments = 0;
		for (DayHeaderPane lDayHeaderPane : weekHeaderPane.dayHeaderPanes)
		{
			int lNumberOfWholeDayAppointments = lDayHeaderPane.getNumberOfWholeDayAppointments();
			lMaxOfWholeDayAppointments = Math.max(lMaxOfWholeDayAppointments, lNumberOfWholeDayAppointments);
		}
		layoutHelp.highestNumberOfWholedayAppointmentsProperty.set(lMaxOfWholeDayAppointments);

		// day columns
		if (weekScrollPane.viewportBoundsProperty().get() != null) {
			layoutHelp.dayWidthProperty.set( (weekScrollPane.viewportBoundsProperty().get().getWidth() - layoutHelp.timeWidthProperty.get()) / determineDisplayedLocalDates().size() );
		}
		
		// hour height
		double lScrollbarSize = new ScrollBar().getWidth();
		layoutHelp.hourHeighProperty.set( layoutHelp.textHeightProperty.get() * 2 + 10 ); // 10 is padding
		if (weekScrollPane.viewportBoundsProperty().get() != null && (weekScrollPane.viewportBoundsProperty().get().getHeight() - lScrollbarSize) > layoutHelp.hourHeighProperty.get() * 24) {
			// if there is more room than absolutely required, let the height grow with the available room
			layoutHelp.hourHeighProperty.set( (weekScrollPane.viewportBoundsProperty().get().getHeight() - lScrollbarSize) / 24 );
		}
	}
	private LayoutHelp layoutHelp = new LayoutHelp(getSkinnable(), this);
	
	

	/**
	 * 
	 */
	public LocalDateTime convertClickToDateTime(double x, double y) {
		
		// the click has only value in either the day panes 
		for (DayBodyPane lDayPane : weekBodyPane.dayBodyPanes) {
			LocalDateTime lLocalDateTime = lDayPane.convertClickToDateTime(x, y);
			if (lLocalDateTime != null) {
				return lLocalDateTime;
			}
		}
		// or the day header panes
		for (DayHeaderPane lDayHeaderPane : weekHeaderPane.dayHeaderPanes) {
			LocalDateTime lLocalDateTime = lDayHeaderPane.convertClickToDateTime(x, y);
			if (lLocalDateTime != null) {
				return lLocalDateTime;
			}
		}
		return null;
	}
}
