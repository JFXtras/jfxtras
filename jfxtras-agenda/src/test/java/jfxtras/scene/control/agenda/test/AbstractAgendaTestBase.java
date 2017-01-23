/**
 * AbstractAgendaTestBase.java
 *
 * Copyright (c) 2011-2016, JFXtras
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

package jfxtras.scene.control.agenda.test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.JFXtrasGuiTest;

public class AbstractAgendaTestBase extends JFXtrasGuiTest {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		
		vbox = new VBox();

		agenda = new Agenda();
		agenda.setDisplayedLocalDateTime(LocalDate.of(2014, 1, 1).atStartOfDay());
		agenda.setPrefSize(1000, 800);
		
        // setup appointment groups
        for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {
        	appointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
        }

        // accept new appointments
        agenda.newAppointmentCallbackProperty().set(new Callback<Agenda.LocalDateTimeRange, Agenda.Appointment>()
        {
            @Override
            public Agenda.Appointment call(Agenda.LocalDateTimeRange calendarRange)
            {
                return new Agenda.AppointmentImplLocal()
                        .withStartLocalDateTime(calendarRange.getStartLocalDateTime())
                        .withEndLocalDateTime(calendarRange.getEndLocalDateTime())
                        .withSummary("new")
                        .withDescription("new")
                        .withAppointmentGroup(appointmentGroupMap.get("group01"));
            }
        });
        
        // tick callbacks
        agenda.appointmentChangedCallbackProperty().set( appointment -> {
        	if (!appointmentChangedCallbackList.contains(appointment)) {
        		appointmentChangedCallbackList.add(appointment);
        	}
        	int identityHashCode = System.identityHashCode(appointment);
        	if (!appointmentChangedCallbackMap.containsKey(identityHashCode)) {
        		appointmentChangedCallbackMap.put(identityHashCode, 0);
        	}
        	appointmentChangedCallbackMap.put(identityHashCode, appointmentChangedCallbackMap.get(identityHashCode) + 1);
			return null;
		});
        
		vbox.getChildren().add(agenda);
		return vbox;
	}
	protected VBox vbox = null; // cannot make this final and assign upon construction
    final protected Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
    protected Agenda agenda = null; // cannot make this final and assign upon construction
    final protected List<Appointment> appointmentChangedCallbackList = new ArrayList<>();
    final protected Map<Integer, Integer> appointmentChangedCallbackMap = new LinkedHashMap<>();
}
