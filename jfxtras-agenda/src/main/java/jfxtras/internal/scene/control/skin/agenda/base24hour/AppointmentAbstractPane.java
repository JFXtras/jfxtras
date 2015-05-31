package jfxtras.internal.scene.control.skin.agenda.base24hour;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

abstract public class AppointmentAbstractPane extends Pane {

	/**
	 * @param calendar
	 * @param appointment
	 */
	public AppointmentAbstractPane(Agenda.Appointment appointment, LayoutHelp layoutHelp)
	{
		this.appointment = appointment;
		this.layoutHelp = layoutHelp;
		appointmentMenu = new AppointmentMenu(this, appointment, layoutHelp);
		
		// for debugging setStyle("-fx-border-color:PINK;-fx-border-width:1px;");
		getStyleClass().add("Appointment");
		getStyleClass().add(appointment.getAppointmentGroup().getStyleClass());
		
		// historical visualizer
		historyVisualizer = new HistoricalVisualizer(this);
		getChildren().add(historyVisualizer);

		// tooltip
		if (appointment.getSummary() != null) {
			Tooltip.install(this, new Tooltip(appointment.getSummary()));
		}
		
		// dragging
		setupDragging();
		
		// react to changes in the selected appointments
		layoutHelp.skinnable.selectedAppointments().addListener( (javafx.collections.ListChangeListener.Change<? extends Appointment> change) -> {
			setOrRemoveSelected();
		});
	}
	final protected Agenda.Appointment appointment; 
	final protected LayoutHelp layoutHelp;
	final protected HistoricalVisualizer historyVisualizer;
	final protected AppointmentMenu appointmentMenu;
	
	/**
	 * 
	 */
	private void setOrRemoveSelected() {
		// remove class if not selected
		if ( getStyleClass().contains(SELECTED) == true // visually selected
		  && layoutHelp.skinnable.selectedAppointments().contains(appointment) == false // but no longer in the selected collection
		) {
			getStyleClass().remove(SELECTED);
		}
		
		// add class if selected
		if ( getStyleClass().contains(SELECTED) == false // visually not selected
		  && layoutHelp.skinnable.selectedAppointments().contains(appointment) == true // but still in the selected collection
		) {
			getStyleClass().add(SELECTED); 
		}
	}
	private static final String SELECTED = "Selected";
	
	/**
	 * 
	 * @param now
	 */
	public void determineHistoryVisualizer(LocalDateTime now) {
		historyVisualizer.setVisible(appointment.getStartLocalDateTime().isBefore(now));
	}

	/**
	 * 
	 */
	private void setupDragging() {
		// start drag
		setOnMousePressed( (mouseEvent) -> {
			// action without select: middle button
			if (mouseEvent.getButton().equals(MouseButton.MIDDLE)) {
				handleAction();
				return;
			}
			// popup: right button
			if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
				appointmentMenu.showMenu(mouseEvent);
				return;
			}
			// only on primary
			if (mouseEvent.getButton().equals(MouseButton.PRIMARY) == false) {
				return;
			}

			// we handle this event
			mouseEvent.consume();

			// is dragging allowed
			if (layoutHelp.skinnable.getAllowDragging() == false) {
				handleSelect(mouseEvent);
				return;
			}

			// if this an action
			if (mouseEvent.getClickCount() > 1) {
				handleAction();
				return;
			}

			// remember
			startX = mouseEvent.getX();
			startY = mouseEvent.getY();
			dragPickupDateTime = layoutHelp.skin.convertClickInSceneToDateTime(mouseEvent.getSceneX(), mouseEvent.getSceneY());
			mouseActuallyHasDragged = false;
			dragging = true;
		});
		
		// visualize dragging
		setOnMouseDragged( (mouseEvent) -> {
			if (dragging == false) {
				return;
			}

			// we handle this event
			mouseEvent.consume();
			
			// show the drag rectangle when we actually drag
			if (dragRectangle == null) {
				setCursor(Cursor.MOVE);
				// TODO: when dropping an appointment overlapping the day edge, the appointment is correctly(?) split in two. When dragging up such a splitted appointment the visualization does not match the actual time 
				dragRectangle = new Rectangle(0, 0, NodeUtil.snapWH(0, getWidth()), NodeUtil.snapWH(0, (appointment.isWholeDay() ? layoutHelp.titleDateTimeHeightProperty.get() : getHeight())) );
				dragRectangle.getStyleClass().add("GhostRectangle");
				layoutHelp.dragPane.getChildren().add(dragRectangle);
				
				// place a text node at the bottom of the resize rectangle
				startTimeText = new Text("...");
				startTimeText.getStyleClass().add("GhostRectangleText");
				if (showStartTimeText()) {
					layoutHelp.dragPane.getChildren().add(startTimeText);
				}
				endTimeText = new Text("...");
				endTimeText.getStyleClass().add("GhostRectangleText");
				if (showEndTimeText()) {
					layoutHelp.dragPane.getChildren().add(endTimeText);
				}
				// we use a clone for calculating the current time during the drag
				appointmentForDrag = new Agenda.AppointmentImplLocal();
			}
			
			// move the drag rectangle
			double lX = NodeUtil.xInParent(this, layoutHelp.dragPane) + (mouseEvent.getX() - startX); // top-left of the original appointment pane + offset of drag 
			double lY = NodeUtil.yInParent(this, layoutHelp.dragPane) + (mouseEvent.getY() - startY); // top-left of the original appointment pane + offset of drag 
			dragRectangle.setX(NodeUtil.snapXY(lX));
			dragRectangle.setY(NodeUtil.snapXY(lY));
			startTimeText.layoutXProperty().set(dragRectangle.getX()); 
			startTimeText.layoutYProperty().set(dragRectangle.getY()); 
			endTimeText.layoutXProperty().set(dragRectangle.getX()); 
			endTimeText.layoutYProperty().set(dragRectangle.getY() + dragRectangle.getHeight() + endTimeText.getBoundsInParent().getHeight()); 
			mouseActuallyHasDragged = true;
			
			// update the start time
			appointmentForDrag.setStartLocalDateTime(appointment.getStartLocalDateTime());
			appointmentForDrag.setEndLocalDateTime(appointment.getEndLocalDateTime());
			appointmentForDrag.setWholeDay(appointment.isWholeDay());
			// determine start and end DateTime of the drag
			LocalDateTime dragCurrentDateTime = layoutHelp.skin.convertClickInSceneToDateTime(mouseEvent.getSceneX(), mouseEvent.getSceneY());
			if (dragCurrentDateTime != null) { // not dropped somewhere outside
				handleDrag(appointmentForDrag, dragPickupDateTime, dragCurrentDateTime);					
				startTimeText.setText(appointmentForDrag.isWholeDay() ? "" : layoutHelp.timeDateTimeFormatter.format(appointmentForDrag.getStartLocalDateTime()));
				endTimeText.setText(appointmentForDrag.isWholeDay() || appointmentForDrag.getEndLocalDateTime() == null ? "" : layoutHelp.timeDateTimeFormatter.format(appointmentForDrag.getEndLocalDateTime()));
			}
			
		});
		
		// end drag
		setOnMouseReleased((mouseEvent) -> {
			if (dragging == false) {
				return;
			}
			
			// we handle this event
			mouseEvent.consume();
			dragging = false;

			// reset ui
			setCursor(Cursor.HAND);
			if (dragRectangle != null) {
				layoutHelp.dragPane.getChildren().remove(dragRectangle);
				layoutHelp.dragPane.getChildren().remove(startTimeText);
				layoutHelp.dragPane.getChildren().remove(endTimeText);
				dragRectangle = null;
				startTimeText = null;
				endTimeText = null;
				appointmentForDrag = null;
			}
			
			// if not dragged, then we're selecting
			if (mouseActuallyHasDragged == false) {
				handleSelect(mouseEvent);
				return;
			}
			
			// determine start and end DateTime of the drag
			LocalDateTime dragDropDateTime = layoutHelp.skin.convertClickInSceneToDateTime(mouseEvent.getSceneX(), mouseEvent.getSceneY());
			if (dragDropDateTime != null) { // not dropped somewhere outside
				handleDrag(appointment, dragPickupDateTime, dragDropDateTime);					
				
				// relayout whole week
				layoutHelp.skin.setupAppointments();
			}
		});
	}
	private boolean dragging = false;
	private Rectangle dragRectangle = null;
	private double startX = 0;
	private double startY = 0;
	private LocalDateTime dragPickupDateTime;
	private boolean mouseActuallyHasDragged = false;
	private final int roundToMinutes = 5;
	private Text startTimeText = null;
	private Text endTimeText = null;
	private Agenda.Appointment appointmentForDrag = null;

	protected  boolean showStartTimeText() {
		return true;
	}

	protected  boolean showEndTimeText() {
		return true;
	}

	/**
	 * 
	 */
	private void handleDrag(Agenda.Appointment appointment, LocalDateTime dragPickupDateTime, LocalDateTime dragDropDateTime) {
		
		// drag start
		boolean dragPickupInDayBody = dragInDayBody(dragPickupDateTime);
		boolean dragPickupInDayHeader = dragInDayHeader(dragPickupDateTime);
		dragPickupDateTime = layoutHelp.roundTimeToNearestMinutes(dragPickupDateTime, roundToMinutes);
		
		// drag end
		boolean dragDropInDayBody = dragInDayBody(dragDropDateTime);
		boolean dragDropInDayHeader = dragInDayHeader(dragDropDateTime);
		dragDropDateTime = layoutHelp.roundTimeToNearestMinutes(dragDropDateTime, roundToMinutes);

		// if dragged from day to day or header to header
		if ( (dragPickupInDayBody && dragDropInDayBody) 
		  || (dragPickupInDayHeader && dragDropInDayHeader)
		) {				
			// simply add the duration
			Duration duration = Duration.between(dragPickupDateTime, dragDropDateTime);
			if (appointment.getStartLocalDateTime() != null) {
				appointment.setStartLocalDateTime( appointment.getStartLocalDateTime().plus(duration) );
			}
			if (appointment.getEndLocalDateTime() != null) {
				appointment.setEndLocalDateTime( appointment.getEndLocalDateTime().plus(duration) );
			}
		}
		
		// if dragged from day to header
		else if ( (dragPickupInDayBody && dragDropInDayHeader) ) {
			
			appointment.setWholeDay(true);
			
			// simply add the duration, but without time
			Period period = Period.between(dragPickupDateTime.toLocalDate(), dragDropDateTime.toLocalDate());
			if (appointment.getStartLocalDateTime() != null) {
				appointment.setStartLocalDateTime( appointment.getStartLocalDateTime().plus(period) );
			}
			if (appointment.getEndLocalDateTime() != null) {
				appointment.setEndLocalDateTime( appointment.getEndLocalDateTime().plus(period) );
			}
		}
		
		// if dragged from day to header
		else if ( (dragPickupInDayHeader && dragDropInDayBody) ) {
			
			appointment.setWholeDay(false);

			// if this is a task
			if (appointment.getStartLocalDateTime() != null && appointment.getEndLocalDateTime() == null) {
				// set the drop time as the task time
				appointment.setStartLocalDateTime(dragDropDateTime );
			}
			else {
				// simply add the duration, but without time
				Period period = Period.between(dragPickupDateTime.toLocalDate(), dragDropDateTime.toLocalDate());
				appointment.setStartLocalDateTime( appointment.getStartLocalDateTime().toLocalDate().plus(period).atStartOfDay() );
				appointment.setEndLocalDateTime( appointment.getEndLocalDateTime().toLocalDate().plus(period).plusDays(1).atStartOfDay() );
			}
		}
	}

	/**
	 * 
	 */
	private void handleSelect(MouseEvent mouseEvent) {
		// if not shift pressed, clear the selection
		if (mouseEvent.isShiftDown() == false && mouseEvent.isControlDown() == false) {
			layoutHelp.skinnable.selectedAppointments().clear();
		}
		
		// add to selection if not already added
		if (layoutHelp.skinnable.selectedAppointments().contains(appointment) == false) {
			layoutHelp.skinnable.selectedAppointments().add(appointment);
		}
		// pressing control allows to toggle
		else if (mouseEvent.isControlDown()) {
			layoutHelp.skinnable.selectedAppointments().remove(appointment);
		}
	}
	
	/**
	 * 
	 */
	private void handleAction() {
		// has the client registered an action
		Callback<Appointment, Void> lCallback = layoutHelp.skinnable.getActionCallback();
		if (lCallback != null) {
			lCallback.call(appointment);
			return;
		}
	}

	/**
	 * 
	 * @param localDateTime
	 * @return
	 */
	private boolean dragInDayBody(LocalDateTime localDateTime) {
		return localDateTime.getNano() == DRAG_DAY;
	}
	
	/**
	 * 
	 * @param localDateTime
	 * @return
	 */
	private boolean dragInDayHeader(LocalDateTime localDateTime) {
		return localDateTime.getNano() == DRAG_DAYHEADER;
	}
	static public final int DRAG_DAY = 1;
	static public final int DRAG_DAYHEADER = 0;
	
	/**
	 * 
	 */
	public String toString()
	{
		return "appointment=" + appointment.getStartLocalDateTime() + "-" + appointment.getEndLocalDateTime()
		     + ";"
			 + "sumary=" + appointment.getSummary()
			 ;
	}
}
