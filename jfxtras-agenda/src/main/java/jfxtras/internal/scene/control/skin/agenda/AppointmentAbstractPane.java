package jfxtras.internal.scene.control.skin.agenda;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.util.NodeUtil;

abstract public class AppointmentAbstractPane extends Pane {

	/**
	 * @param calendar
	 * @param appointment
	 */
	public AppointmentAbstractPane(Agenda.Appointment appointment, LayoutHelp layoutHelp, Draggable draggable)
	{
		this.appointment = appointment;
		this.layoutHelp = layoutHelp;
		this.draggable = draggable;

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
		if (draggable == Draggable.YES) {
			setupDragging();
		}
		
		// react to changes in the selected appointments
		layoutHelp.skinnable.selectedAppointments().addListener( (javafx.collections.ListChangeListener.Change<? extends Appointment> change) -> {
			setOrRemoveSelected();
		});
	}
	final protected Agenda.Appointment appointment; 
	final protected LayoutHelp layoutHelp;
	final protected Draggable draggable;
	enum Draggable { YES, NO }
	final protected HistoricalVisualizer historyVisualizer;

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
		historyVisualizer.setVisible(appointment.getStartDateTime().isBefore(now));
	}

	/**
	 * 
	 */
	private void setupDragging() {
		
		// start drag
		setOnMousePressed( (mouseEvent) -> {
			// is this a drag start, action or a select?
			if (mouseEvent.isPrimaryButtonDown() == false) {
				return;
			}

			// we handle this event
			mouseEvent.consume();

			// if this an action
			if (mouseEvent.getClickCount() > 1) {
				handleAction();
				return;
			}

			// remember
			startX = mouseEvent.getScreenX();
			startY = mouseEvent.getScreenY();
			mouseActuallyHasDragged = false;
		});
		
		// visualize dragging
		setOnMouseDragged( (mouseEvent) -> {
			// we handle this event
			mouseEvent.consume();
			
			// show the drag rectangle when we actually drag
			if (dragRectangle == null) {
				setCursor(Cursor.MOVE);
				dragRectangle = new Rectangle(0, 0, NodeUtil.snapWH(0, getWidth()), NodeUtil.snapWH(0, (appointment.isWholeDay() ? layoutHelp.titleDateTimeHeightProperty.get() : getHeight())) );
				dragRectangle.getStyleClass().add("GhostRectangle");
				layoutHelp.dragPane.getChildren().add(dragRectangle);
				// TBEERNOT: show time label in dragged rectangle?
			}
			
			// move the drag rectangle
			double lX = (NodeUtil.screenX(this) - NodeUtil.screenX(layoutHelp.dragPane)) + (mouseEvent.getScreenX() - startX); // top-left of pane + offset of dragrectangle
			double lY = (NodeUtil.screenY(this) - NodeUtil.screenY(layoutHelp.dragPane)) + (mouseEvent.getScreenY() - startY); // top-left of pane + offset of dragrectangle
			dragRectangle.setX(NodeUtil.snapXY(lX));
			dragRectangle.setY(NodeUtil.snapXY(lY));
			mouseActuallyHasDragged = true;
		});
		
		// end drag
		setOnMouseReleased((mouseEvent) -> {
			// we handle this event
			mouseEvent.consume();

			// reset ui
			setCursor(Cursor.HAND);
			if (dragRectangle != null) {
				layoutHelp.dragPane.getChildren().remove(dragRectangle);
				dragRectangle = null;
			}
			
			// if not dragged, then we're selecting
			if (mouseActuallyHasDragged == false) {
				handleSelect(mouseEvent);
				return;
			}
			
			// determine start and end DateTime of the drag
			LocalDateTime dragStartDateTime = layoutHelp.skin.convertClickToDateTime(startX, startY);
			LocalDateTime dragEndDateTime = layoutHelp.skin.convertClickToDateTime(mouseEvent.getScreenX(), mouseEvent.getScreenY());
			if (dragEndDateTime != null) { // not dropped somewhere outside
				handleDrag(dragStartDateTime, dragEndDateTime);					
			}
		});
	}
	private Rectangle dragRectangle = null;
	private double startX = 0;
	private double startY = 0;
	private boolean mouseActuallyHasDragged = false;
	private final int roundToMinutes = 5;

	/**
	 * 
	 */
	private void handleDrag(LocalDateTime dragStartDateTime, LocalDateTime dragEndDateTime) {
		
		// drag start
		boolean dragStartInDayBody = dragInDayBody(dragStartDateTime);
		boolean dragStartInDayHeader = dragInDayHeader(dragStartDateTime);
		dragStartDateTime = layoutHelp.roundTimeToNearestMinutes(dragStartDateTime, roundToMinutes);
		
		// drag end
		boolean dragEndInDayBody = dragInDayBody(dragEndDateTime);
		boolean dragEndInDayHeader = dragInDayHeader(dragEndDateTime);
		dragEndDateTime = layoutHelp.roundTimeToNearestMinutes(dragEndDateTime, roundToMinutes);

		// if dragged from day to day or header to header
		if ( (dragStartInDayBody && dragEndInDayBody) 
		  || (dragStartInDayHeader && dragEndInDayHeader)
		) {				
			// simply add the duration
			Duration duration = Duration.between(dragStartDateTime, dragEndDateTime);
			if (appointment.getStartDateTime() != null) {
				appointment.setStartDateTime( appointment.getStartDateTime().plus(duration) );
			}
			if (appointment.getEndDateTime() != null) {
				appointment.setEndDateTime( appointment.getEndDateTime().plus(duration) );
			}
		}
		
		// if dragged from day to header
		else if ( (dragStartInDayBody && dragEndInDayHeader) ) {
			
			appointment.setWholeDay(true);
			
			// simply add the duration, but without time
			Period period = Period.between(dragStartDateTime.toLocalDate(), dragEndDateTime.toLocalDate());
			if (appointment.getStartDateTime() != null) {
				appointment.setStartDateTime( appointment.getStartDateTime().plus(period) );
			}
			if (appointment.getEndDateTime() != null) {
				appointment.setEndDateTime( appointment.getEndDateTime().plus(period) );
			}
		}
		
		// if dragged from day to header
		else if ( (dragStartInDayHeader && dragEndInDayBody) ) {
			
			appointment.setWholeDay(false);

			// if this is a task
			if (appointment.getStartDateTime() != null && appointment.getEndDateTime() == null) {
				// set the drop time as the task time
				appointment.setStartDateTime(dragEndDateTime );
			}
			else {
				// simply add the duration, but without time
				Period period = Period.between(dragStartDateTime.toLocalDate(), dragEndDateTime.toLocalDate());
				appointment.setStartDateTime( appointment.getStartDateTime().toLocalDate().plus(period).atStartOfDay() );
				appointment.setEndDateTime( appointment.getEndDateTime().toLocalDate().plus(period).plusDays(1).atStartOfDay() );
			}
		}
		
		// redo whole week
		layoutHelp.skin.setupAppointments();
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
		// has the client regsitered an action
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
		return "appointment=" + appointment.getStartDateTime() + "-" + appointment.getEndDateTime()
		     + ";"
			 + "sumary=" + appointment.getSummary()
			 ;
	}
}
