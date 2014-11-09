package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import jfxtras.internal.scene.control.skin.DateTimeToCalendarHelper;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

/**
 * Responsible for rendering the appointments within a day 
 */
class DayBodyPane extends Pane
{
	// know your header
//	DayHeaderPane dayHeaderPane = null;
	
	public DayBodyPane(LocalDate localDate, AllAppointments allAppointments, LayoutHelp layoutHints) {
		this.localDateObjectProperty.set(localDate);
		this.allAppointments = allAppointments;
		this.layoutHelp = layoutHints;
		construct();
	}
	final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
	final AllAppointments allAppointments;
	final LayoutHelp layoutHelp;
	
	private void construct() {
		
		// for debugging setStyle("-fx-border-color:PINK;-fx-border-width:4px;");		
		getStyleClass().add("Day");
		setId("DayBodyPane" + localDateObjectProperty.get()); // for testing
		
		// react to changes in the appointments
		allAppointments.addOnChangeListener( () -> {
			setupAppointments();
		});
		setupAppointments();
		
		// change the layout related to the size
		widthProperty().addListener( (observable) -> {
			relayout();
		});
		heightProperty().addListener( (observable) -> {
			relayout();
		});
		
		setupMouseDrag();

		// for testing
		localDateObjectProperty.addListener( (observable) -> {
			setId("DayBody" + localDateObjectProperty.get());
		});
		setId("DayBody" + localDateObjectProperty.get());
	}
	
	/**
	 * 
	 */
	private void setupMouseDrag() {
		
		// start new appointment
		setOnMousePressed((mouseEvent) -> {
			// if there is no one to handle the result, don't even bother
			if (layoutHelp.skinnable.createAppointmentCallbackProperty().get() == null && layoutHelp.skinnable.newAppointmentCallbackProperty().get() == null) {
				return;
			}
			
			// show the rectangle
			setCursor(Cursor.V_RESIZE);
			double lY = NodeUtil.snapXY(mouseEvent.getScreenY() - NodeUtil.screenY(DayBodyPane.this));
			resizeRectangle = new Rectangle(0, lY, layoutHelp.dayWidthProperty.get(), 10);
			resizeRectangle.getStyleClass().add("GhostRectangle");
			getChildren().add(resizeRectangle);
			
			// this event should not be processed by the appointment area
			mouseEvent.consume();
			dragged = false;
			layoutHelp.skinnable.selectedAppointments().clear();
		});
		// visualize resize
		setOnMouseDragged((mouseEvent) -> {
			if (resizeRectangle == null) {
				return;
			}
			
			// - calculate the number of pixels from onscreen nodeY (layoutY) to onscreen mouseY					
			double lHeight = mouseEvent.getScreenY() - NodeUtil.screenY(resizeRectangle);
			if (lHeight < 5) {
				lHeight = 5;
			}
			resizeRectangle.setHeight(lHeight);
			
			// no one else
			mouseEvent.consume();
			dragged = true;
		});
		// end resize
		setOnMouseReleased((mouseEvent) -> {
			if (resizeRectangle == null) {
				return;
			}
			
			// no one else
			mouseEvent.consume();
			
			// reset ui
			setCursor(Cursor.HAND);
			getChildren().remove(resizeRectangle);
			
			// must have dragged (otherwise it is considered an "unselect all" action)
			if (dragged == false) {
				return;
			}
			
			// calculate the starttime
			LocalDateTime lStartDateTime = localDateObjectProperty.get().atStartOfDay();
			lStartDateTime = lStartDateTime.plusSeconds( (int)(resizeRectangle.getY() * layoutHelp.durationInMSPerPixelProperty.get() / 1000) );
			lStartDateTime = layoutHelp.roundTimeToNearestMinutes(lStartDateTime, 5);
			
			// calculate the new end date for the appointment (recalculating the duration)
			LocalDateTime lEndDateTime = lStartDateTime.plusSeconds( (int)(resizeRectangle.getHeight() * layoutHelp.durationInMSPerPixelProperty.get() / 1000) );
			lEndDateTime = layoutHelp.roundTimeToNearestMinutes(lEndDateTime, 5);
			
			// clean up
			resizeRectangle = null;					
			
			// ask the control to create a new appointment (null may be returned)
			Agenda.Appointment lAppointment;
			if (layoutHelp.skinnable.newAppointmentCallbackProperty().get() != null) {
				lAppointment = layoutHelp.skinnable.newAppointmentCallbackProperty().get().call(new Agenda.DateTimeRange(lStartDateTime, lEndDateTime));
			}
			else {
				lAppointment = layoutHelp.skinnable.createAppointmentCallbackProperty().get().call(new Agenda.CalendarRange(DateTimeToCalendarHelper.createCalendarFromLocalDateTime(lStartDateTime, Locale.getDefault()), DateTimeToCalendarHelper.createCalendarFromLocalDateTime(lEndDateTime, Locale.getDefault())));
			}
			if (lAppointment != null) {
				layoutHelp.skinnable.appointments().add(lAppointment); // the appointments collection is listened to, so they will automatically be refreshed
			}
		});
	}
	Rectangle resizeRectangle = null;
	boolean dragged = false;

	/**
	 * 
	 */
	private void relayout()
	{
		// first add all the whole day appointments
		int lWholedayCnt = 0;
		for (AppointmentWholedayBodyPane lAppointmentPane : wholedayAppointmentBodyPanes) {
			lAppointmentPane.setLayoutX(NodeUtil.snapXY(lWholedayCnt * layoutHelp.wholedayAppointmentFlagpoleWidthProperty.get()));
			lAppointmentPane.setLayoutY(0);
			lAppointmentPane.setPrefSize(NodeUtil.snapWH(lAppointmentPane.getLayoutX(), layoutHelp.wholedayAppointmentFlagpoleWidthProperty.get()), NodeUtil.snapWH(lAppointmentPane.getLayoutY(), layoutHelp.dayHeightProperty.get()));
			lWholedayCnt++;
		}
		
		// calculate how much room is remaining for the regular appointments
		double lRemainingWidthForRegularAppointments = layoutHelp.dayContentWidthProperty.get() - (lWholedayCnt * layoutHelp.wholedayAppointmentFlagpoleWidthProperty.get());			
		
		// then add all tracked appointments (regular & task) to the day
		for (AppointmentAbstractTrackedPane lAppointmentAbstractTrackedPane : trackedAppointmentBodyPanes) {
			
			// the X is determine by offsetting the wholeday appointments and then calculate the X of the track the appointment is placed in (available width / number of tracks) 
			lAppointmentAbstractTrackedPane.setLayoutX( NodeUtil.snapXY((lWholedayCnt * layoutHelp.wholedayAppointmentFlagpoleWidthProperty.get()) + (lRemainingWidthForRegularAppointments / lAppointmentAbstractTrackedPane.clusterOwner.clusterTracks.size() * lAppointmentAbstractTrackedPane.clusterTrackIdx)));
			
			// the Y is determined by the start time in minutes projected onto the total day height (being 24 hours)
			int lTimeFactor = (lAppointmentAbstractTrackedPane.startDateTime.getHour() * 60) + lAppointmentAbstractTrackedPane.startDateTime.getMinute();
			lAppointmentAbstractTrackedPane.setLayoutY( NodeUtil.snapXY(layoutHelp.dayHeightProperty.get() / (24 * 60) * lTimeFactor) );
			
			// the width is the remaining width (subtracting the wholeday appointments) divided by the number of tracks in the cluster
			double lW = (layoutHelp.dayContentWidthProperty.get() - (wholedayAppointmentBodyPanes.size() * layoutHelp.wholedayAppointmentFlagpoleWidthProperty.get())) * (1.0 / (((double)lAppointmentAbstractTrackedPane.clusterOwner.clusterTracks.size())));
			// all but the most right appointment get 50% extra width, so they underlap the next track 
			if (lAppointmentAbstractTrackedPane.clusterTrackIdx < lAppointmentAbstractTrackedPane.clusterOwner.clusterTracks.size() - 1) {
				lW *= 1.75;
			}
			lAppointmentAbstractTrackedPane.setPrefWidth( NodeUtil.snapWH(lAppointmentAbstractTrackedPane.getLayoutX(), lW) );
			// TBEERNOT: task may scale to full remaining width (including the columns to the right)
			
			// the height is determing by the duration projected against the total dayHeight (being 24 hours)
			double lH;
			if (lAppointmentAbstractTrackedPane instanceof AppointmentTaskBodyPane) {
				lH = 5; // TBEERNOT: task height via layoutHelper?
			}
			else {
				lH = (layoutHelp.dayHeightProperty.get() / (24 * 60) * (lAppointmentAbstractTrackedPane.durationInMS / 1000 / 60) );

				// the height has a minimum size, in order to be able to render sensibly
				if (lH < 2 * layoutHelp.paddingProperty.get()) {
					lH = 2 * layoutHelp.paddingProperty.get(); 
				}
			}
			lAppointmentAbstractTrackedPane.setPrefHeight( NodeUtil.snapWH(lAppointmentAbstractTrackedPane.getLayoutY(), lH) );
		}
	}			

	void setupAppointments() {
		setupWholedayAppointments();
		setupTaskAppointments();
		setupRegularAppointments();
		
		// place appointments in tracks
		trackedAppointmentBodyPanes.clear();
		trackedAppointmentBodyPanes.addAll(regularAppointmentBodyPanes);
		trackedAppointmentBodyPanes.addAll(taskAppointmentBodyPanes);
		List<? extends AppointmentAbstractTrackedPane> determineTracks = AppointmentRegularBodyPane.determineTracks(trackedAppointmentBodyPanes);
		// add the appointments to the pane in the correct order, so they overlap nicely
		//getChildren().removeAll(determineTracks);
		getChildren().addAll(determineTracks);
		
		relayout();
	}
	final List<AppointmentAbstractTrackedPane> trackedAppointmentBodyPanes = new ArrayList<>();
	
	
	/**
	 * 
	 */
	private void setupWholedayAppointments() {
		wholedayAppointments.clear();
		wholedayAppointments.addAll( allAppointments.collectWholedayFor(localDateObjectProperty.get()) );
		
		// remove all appointments
		getChildren().removeAll(wholedayAppointmentBodyPanes);
		wholedayAppointmentBodyPanes.clear();
		
		// for all wholeday appointments on this date, create a header appointment pane
		int lCnt = 0;
		for (Appointment lAppointment : wholedayAppointments) {
			AppointmentWholedayBodyPane lAppointmentPane = new AppointmentWholedayBodyPane(localDateObjectProperty.get(), lAppointment, layoutHelp);
			wholedayAppointmentBodyPanes.add(lAppointmentPane);
			lAppointmentPane.setId(lAppointmentPane.getClass().getSimpleName() + (++lCnt)); // for testing
		}
		getChildren().addAll(wholedayAppointmentBodyPanes);				
	}
	final private List<Appointment> wholedayAppointments = new ArrayList<>();
	final private List<AppointmentWholedayBodyPane> wholedayAppointmentBodyPanes = new ArrayList<>();
	
	/**
	 * 
	 */
	private void setupTaskAppointments() {
		taskAppointments.clear();
		taskAppointments.addAll( allAppointments.collectTaskFor(localDateObjectProperty.get()) );
		
		// remove all appointments
		getChildren().removeAll(taskAppointmentBodyPanes);
		taskAppointmentBodyPanes.clear();
		
		// for all task appointments on this date, create a header appointment pane
		int lCnt = 0;
		for (Appointment lAppointment : taskAppointments) {
			AppointmentTaskBodyPane lAppointmentPane = new AppointmentTaskBodyPane(lAppointment, layoutHelp);
			taskAppointmentBodyPanes.add(lAppointmentPane);
			lAppointmentPane.setId(lAppointmentPane.getClass().getSimpleName() + (++lCnt)); // for testing
		}
		//getChildren().addAll(taskAppointmentBodyPanes);				
	}
	final private List<Appointment> taskAppointments = new ArrayList<>();
	final private List<AppointmentTaskBodyPane> taskAppointmentBodyPanes = new ArrayList<>();
	
	/**
	 * 
	 */
	private void setupRegularAppointments() {
		regularAppointments.clear();
		regularAppointments.addAll( allAppointments.collectRegularFor(localDateObjectProperty.get()) );
		
		// remove all appointments
		getChildren().removeAll(regularAppointmentBodyPanes);
		regularAppointmentBodyPanes.clear();
		
		// for all regular appointments on this date, create a header appointment pane
		int lCnt = 0;
		for (Appointment lAppointment : regularAppointments) {
			AppointmentRegularBodyPane lAppointmentPane = new AppointmentRegularBodyPane(localDateObjectProperty.get(), lAppointment, layoutHelp);
			regularAppointmentBodyPanes.add(lAppointmentPane);
			lAppointmentPane.setId(lAppointmentPane.getClass().getSimpleName() + (++lCnt)); // for testing
		}
		//getChildren().addAll(regularAppointmentBodyPanes);				
	}
	final private List<Appointment> regularAppointments = new ArrayList<>();
	final private List<AppointmentRegularBodyPane> regularAppointmentBodyPanes = new ArrayList<>();
	
	
	/**
	 * 
	 * @param x screen coordinate
	 * @param y screen coordinate
	 * @return a localDateTime where nano seconds == 1
	 */
	LocalDateTime convertClickToDateTime(double x, double y) {
		Rectangle r = new Rectangle(NodeUtil.screenX(this), NodeUtil.screenY(this), this.getWidth(), this.getHeight());
		if (r.contains(x, y)) {
			LocalDate localDate = localDateObjectProperty.get();
			double lHeightOffset = (y -  r.getY());
			int ms = (int)(lHeightOffset * layoutHelp.durationInMSPerPixelProperty.get());
			LocalDateTime localDateTime = localDate.atStartOfDay().plusSeconds(ms / 1000);
			localDateTime = localDateTime.withNano(AppointmentAbstractPane.DRAG_DAY); // we abuse the nano second to deviate body panes from header panes
			return localDateTime;
		}
		return null;
	}
}

