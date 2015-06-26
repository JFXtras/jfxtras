package jfxtras.internal.scene.control.skin.agenda.base24hour;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import jfxtras.internal.scene.control.skin.DateTimeToCalendarHelper;
import jfxtras.internal.scene.control.skin.agenda.AllAppointments;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

/**
 * Responsible for rendering the appointments within a day 
 */
class DayBodyPane extends Pane
{
	/**
	 * 
	 */
	public DayBodyPane(LocalDate localDate, AllAppointments allAppointments, LayoutHelp layoutHints) {
		this.localDateObjectProperty.set(localDate);
		this.allAppointments = allAppointments;
		this.layoutHelp = layoutHints;
		construct();
	}
	final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
	final AllAppointments allAppointments;
	final LayoutHelp layoutHelp;
	
	/**
	 * 
	 */
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
			// only on primary
			if (mouseEvent.getButton().equals(MouseButton.PRIMARY) == false) {
				return;
			}
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
			Agenda.Appointment lAppointment = null;
			if (layoutHelp.skinnable.newAppointmentCallbackProperty().get() != null) {
				lAppointment = layoutHelp.skinnable.newAppointmentCallbackProperty().get().call(new Agenda.LocalDateTimeRange(lStartDateTime, lEndDateTime));
			}
			if (layoutHelp.skinnable.createAppointmentCallbackProperty().get() != null) {
				lAppointment = layoutHelp.skinnable.createAppointmentCallbackProperty().get().call(new Agenda.CalendarRange(DateTimeToCalendarHelper.createCalendarFromLocalDateTime(lStartDateTime, TimeZone.getDefault(), Locale.getDefault()), DateTimeToCalendarHelper.createCalendarFromLocalDateTime(lEndDateTime, TimeZone.getDefault(), Locale.getDefault())));
			}
			if (lAppointment != null) {
				layoutHelp.skinnable.appointments().add(lAppointment); // the appointments collection is listened to, so they will automatically be refreshed
			}
		});
	}
	private Rectangle resizeRectangle = null;
	private boolean dragged = false;

	/**
	 * The tracked panes are too complex to do via binding (unlike the wholeday flagpoles)
	 */
	private void relayout()
	{
		// prepare
		int lWholedayCnt = wholedayAppointmentBodyPanes.size();
		double lAllFlagpolesWidth = layoutHelp.wholedayAppointmentFlagpoleWidthProperty.get() * lWholedayCnt;
		double lDayWidth = layoutHelp.dayContentWidthProperty.get();
		double lRemainingWidthForAppointments = lDayWidth - lAllFlagpolesWidth;
		double lNumberOfPixelsPerMinute = layoutHelp.dayHeightProperty.get() / (24 * 60);
		
		// then add all tracked appointments (regular & task) to the day
		for (AppointmentAbstractTrackedPane lAppointmentAbstractTrackedPane : trackedAppointmentBodyPanes) {
			
			// for this pane specifically
			double lNumberOfTracks = (double)lAppointmentAbstractTrackedPane.clusterOwner.clusterTracks.size();
			double lTrackWidth = lRemainingWidthForAppointments / lNumberOfTracks;
			double lTrackIdx = (double)lAppointmentAbstractTrackedPane.clusterTrackIdx;
			
			// the X is determined by offsetting the wholeday appointments and then calculate the X of the track the appointment is placed in (available width / number of tracks) 
			double lX = lAllFlagpolesWidth + (lTrackWidth * lTrackIdx);
			lAppointmentAbstractTrackedPane.setLayoutX( NodeUtil.snapXY(lX));
			
			// the Y is determined by the start time in minutes projected onto the total day height (being 24 hours)
			int lStartOffsetInMinutes = (lAppointmentAbstractTrackedPane.startDateTime.getHour() * 60) + lAppointmentAbstractTrackedPane.startDateTime.getMinute();
			double lY = lNumberOfPixelsPerMinute * lStartOffsetInMinutes;
			lAppointmentAbstractTrackedPane.setLayoutY( NodeUtil.snapXY(lY) );
			
			// the width is the remaining width (subtracting the wholeday appointments) divided by the number of tracks in the cluster
			double lW = lTrackWidth;
			// all but the most right appointment get 50% extra width, so they underlap the next track 
			if (lTrackIdx < lNumberOfTracks - 1) {
				lW *= 1.75;
			}
			lAppointmentAbstractTrackedPane.setPrefWidth( NodeUtil.snapWH(lAppointmentAbstractTrackedPane.getLayoutX(), lW) );
			
			// the height is determined by the duration projected against the total dayHeight (being 24 hours)
			double lH;
			if (lAppointmentAbstractTrackedPane instanceof AppointmentTaskBodyPane) {
				lH = 5; // task height
			}
			else {
				long lHeightInMinutes = lAppointmentAbstractTrackedPane.durationInMS / 1000 / 60;
				lH = lNumberOfPixelsPerMinute * lHeightInMinutes;

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
			// create pane
			AppointmentWholedayBodyPane lAppointmentPane = new AppointmentWholedayBodyPane(localDateObjectProperty.get(), lAppointment, layoutHelp);
			wholedayAppointmentBodyPanes.add(lAppointmentPane);
			lAppointmentPane.setId(lAppointmentPane.getClass().getSimpleName() + localDateObjectProperty.get() + "/" + lCnt); // for testing
			
			// position by binding
			lAppointmentPane.layoutXProperty().bind(NodeUtil.snapXY( layoutHelp.wholedayAppointmentFlagpoleWidthProperty.multiply(lCnt) ));
			lAppointmentPane.setLayoutY(0);
			lAppointmentPane.prefWidthProperty().bind(layoutHelp.wholedayAppointmentFlagpoleWidthProperty);
			lAppointmentPane.prefHeightProperty().bind(layoutHelp.dayHeightProperty);
			
			lCnt++;
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
			lAppointmentPane.setId(lAppointmentPane.getClass().getSimpleName() + localDateObjectProperty.get() + "/" + lCnt); // for testing
			
			lCnt++;
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
			lAppointmentPane.setId(lAppointmentPane.getClass().getSimpleName() + localDateObjectProperty.get() + "/" + lCnt); // for testing
			
			lCnt++;
		}
		//getChildren().addAll(regularAppointmentBodyPanes);				
	}
	final private List<Appointment> regularAppointments = new ArrayList<>();
	final private List<AppointmentRegularBodyPane> regularAppointmentBodyPanes = new ArrayList<>();
	
	
	/**
	 * 
	 * @param x scene coordinate
	 * @param y scene coordinate
	 * @return a localDateTime where nano seconds == 1
	 */
	LocalDateTime convertClickInSceneToDateTime(double x, double y) {
		Rectangle r = new Rectangle(NodeUtil.sceneX(this), NodeUtil.sceneY(this), this.getWidth(), this.getHeight());
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

