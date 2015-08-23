/**
 * AllAppointments.java
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

package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Capture the logic to extract the different types of appointments in one place.
 * 
 * @author Tom Eugelink
 */
public class AllAppointments {

	/**
	 * 
	 */
	public AllAppointments(ObservableList<Agenda.Appointment> appointments) {
		this.appointments = appointments;
		
		appointments.addListener( new WeakListChangeListener<>(listChangeListener) );

	}
	final private ObservableList<Agenda.Appointment> appointments;
	final private ListChangeListener<Appointment> listChangeListener = new ListChangeListener<Appointment>() {
		@Override
		public void onChanged(javafx.collections.ListChangeListener.Change<? extends Appointment> changes) {
			fireOnChangeListener();
		}
	};
	
	/**
	 * fires when something changes in the appointments 
	 */
	public void addOnChangeListener(Runnable runnable) {
		this.runnables.add(runnable);
	}
	public void removeOnChangeListener(Runnable runnable) {
		this.runnables.remove(runnable);
	}
	private List<Runnable> runnables = new ArrayList<>();

	private void fireOnChangeListener() {
		for (Runnable runnable : runnables) {
			runnable.run();
		}
	}
	
	/**
	 * 
	 */
	public List<Appointment> collectWholedayFor(LocalDate localDate) {
		List<Appointment> collectedAppointments = new ArrayList<>();
		
		// scan all appointments and filter the ones for this day
		for (Agenda.Appointment lAppointment : appointments) {
			if (lAppointment.isWholeDay()) {
				
				LocalDate startLocalDate = lAppointment.getStartLocalDateTime().toLocalDate();
				LocalDate endLocalDate = (lAppointment.getEndLocalDateTime() == null ? startLocalDate : lAppointment.getEndLocalDateTime().minusNanos(1).toLocalDate());  // end is exclusive, so subtract one nano
				if ( (startLocalDate.isEqual(localDate) || startLocalDate.isBefore(localDate))
				  && (endLocalDate.isEqual(localDate) || endLocalDate.isAfter(localDate)) 
				) {
					collectedAppointments.add(lAppointment);
				}
			}
		}
		return collectedAppointments;
	}
	
	/**
	 * 
	 */
	public List<Appointment> collectTaskFor(LocalDate localDate) {
		List<Appointment> collectedAppointments = new ArrayList<>();
		
		// scan all appointments and filter the ones for this day
		for (Agenda.Appointment lAppointment : appointments) {
			// an not-wholeday appointment WITHOUT an end is a task
			if (!lAppointment.isWholeDay() && lAppointment.getEndLocalDateTime() == null) {
				
				if (lAppointment.getStartLocalDateTime().toLocalDate().isEqual(localDate)) {
					collectedAppointments.add(lAppointment);
				}
			}
		}
		return collectedAppointments;
	}
	
	/**
	 * 
	 */
	public List<Appointment> collectRegularFor(LocalDate localDate) {
		List<Appointment> collectedAppointments = new ArrayList<>();
		
		// scan all appointments and filter the ones for this day
		for (Agenda.Appointment lAppointment : appointments) {
			// an not-wholeday appointment WITH a set enddate is a regular appointment
			if (!lAppointment.isWholeDay() && lAppointment.getEndLocalDateTime() != null) {
				
				LocalDate startLocalDate = lAppointment.getStartLocalDateTime().toLocalDate();
				LocalDate endLocalDate = lAppointment.getEndLocalDateTime().minusNanos(1).toLocalDate();  // end is exclusive, so subtract one nano
				if ( (startLocalDate.isEqual(localDate) || startLocalDate.isBefore(localDate))
				  && (endLocalDate.isEqual(localDate) || endLocalDate.isAfter(localDate)) 
				) {
					collectedAppointments.add(lAppointment);
				}
			}
		}
		return collectedAppointments;
	}
}
