/**
 * AppointmentAbstractPane.java
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

package jfxtras.internal.scene.control.skin.agenda.base24hour;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
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

abstract class AppointmentAbstractPane extends Pane {

	/**
	 * @param calendar
	 * @param appointment
	 */
	AppointmentAbstractPane(Agenda.Appointment appointment, LayoutHelp layoutHelp)
	{
		this.appointment = appointment;
		this.layoutHelp = layoutHelp;
		appointmentMenu = new AppointmentMenu(this, appointment, layoutHelp);
		
		// for debugging setStyle("-fx-border-color:PINK;-fx-border-width:1px;");
		getStyleClass().add("Appointment");
		getStyleClass().add(appointment.getAppointmentGroup() != null ? appointment.getAppointmentGroup().getStyleClass() : "group0");
		
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
		layoutHelp.skinnable.selectedAppointments().addListener( new WeakListChangeListener<>(listChangeListener) );
	}
	final protected Agenda.Appointment appointment; 
	final protected LayoutHelp layoutHelp;
	final protected HistoricalVisualizer historyVisualizer;
	final protected AppointmentMenu appointmentMenu;
	final private ListChangeListener<Appointment> listChangeListener = new ListChangeListener<Appointment>() {
		@Override
		public void onChanged(javafx.collections.ListChangeListener.Change<? extends Appointment> changes) {
			setOrRemoveSelected();
		}
	};

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
	void determineHistoryVisualizer(LocalDateTime now) {
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

			// if this an action
			if (mouseEvent.getClickCount() > 1) {
				handleAction();
				return;
			}

			// is dragging allowed
			if (layoutHelp.skinnable.getAllowDragging() == false) {
				handleSelect(mouseEvent);
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
				appointmentForDrag = new AppointmentForDrag();
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

	public static class AppointmentForDrag extends Agenda.AppointmentImplLocal {
		
	}
	
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
            boolean changed = false;
			Duration duration = Duration.between(dragPickupDateTime, dragDropDateTime);
			if (appointment.getStartLocalDateTime() != null) {
				appointment.setStartLocalDateTime( appointment.getStartLocalDateTime().plus(duration) );
                changed = true;
			}
			if (appointment.getEndLocalDateTime() != null) {
				appointment.setEndLocalDateTime( appointment.getEndLocalDateTime().plus(duration) );
				changed = true;
			}
			if (changed) {
				layoutHelp.callAppointmentChangedCallback(appointment);
			}
		}
		
		// if dragged from day to header
		else if ( (dragPickupInDayBody && dragDropInDayHeader) ) {
			
			appointment.setWholeDay(true);
			
			// simply add the duration, but without time
            boolean changed = false;
			Period period = Period.between(dragPickupDateTime.toLocalDate(), dragDropDateTime.toLocalDate());
			if (appointment.getStartLocalDateTime() != null) {
				appointment.setStartLocalDateTime( appointment.getStartLocalDateTime().plus(period) );
                changed = true;
			}
			if (appointment.getEndLocalDateTime() != null) {
				appointment.setEndLocalDateTime( appointment.getEndLocalDateTime().plus(period) );
                changed = true;
			}
            if (changed) {
            	layoutHelp.callAppointmentChangedCallback(appointment);
            }
		}
		
		// if dragged from header to day
		else if ( (dragPickupInDayHeader && dragDropInDayBody) ) {
			
			appointment.setWholeDay(false);

			// if this is a task
            boolean changed = false;
			if (appointment.getStartLocalDateTime() != null && appointment.getEndLocalDateTime() == null) {
				// set the drop time as the task time
				appointment.setStartLocalDateTime(dragDropDateTime);
                changed = true;
			}
			else {
				// simply add the duration - default to 1 hour duration
				appointment.setStartLocalDateTime(dragDropDateTime);
				appointment.setEndLocalDateTime(dragDropDateTime.plusHours(1));
                changed = true;
			}
            if (changed) {
            	layoutHelp.callAppointmentChangedCallback(appointment);
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
	static final int DRAG_DAY = 1;
	static final int DRAG_DAYHEADER = 0;
	
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
