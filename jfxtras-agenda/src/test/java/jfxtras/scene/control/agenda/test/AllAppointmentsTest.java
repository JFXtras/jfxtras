/**
 * AllAppointmentsTest.java
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
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.internal.scene.control.skin.agenda.AllAppointments;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.test.JFXtrasTest;

public class AllAppointmentsTest extends JFXtrasTest {

	@Test
	public void regularAppointment1() {
		// just an appointment somewhere on a day
		ObservableList<Agenda.Appointment> lAppointments = FXCollections.observableArrayList(new Agenda.AppointmentImplLocal()
			.withStartLocalDateTime(LocalDateTime.of(2014, 1, 2, 8, 00))
			.withEndLocalDateTime(LocalDateTime.of(2014, 1, 2, 11, 30))
		);
		AllAppointments lAllAppointments = new AllAppointments(lAppointments);
		Assert.assertEquals(0, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 1)).size());
		Assert.assertEquals(1, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(0, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 3)).size());
		// others
		Assert.assertEquals(0, lAllAppointments.collectWholedayFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(0, lAllAppointments.collectTaskFor(LocalDate.of(2014, 1, 2)).size());
	}

	@Test
	public void regularAppointment2() {
		// an appointment covering a whole day, without being a whole day
		ObservableList<Agenda.Appointment> lAppointments = FXCollections.observableArrayList(new Agenda.AppointmentImplLocal()
			.withStartLocalDateTime(LocalDate.of(2014, 1, 2).atStartOfDay())
			.withEndLocalDateTime(LocalDate.of(2014, 1, 2).plusDays(1).atStartOfDay()) // end is exclusive
		);
		AllAppointments lAllAppointments = new AllAppointments(lAppointments);
		Assert.assertEquals(0, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 1)).size());
		Assert.assertEquals(1, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(0, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 3)).size());
		// others
		Assert.assertEquals(0, lAllAppointments.collectWholedayFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(0, lAllAppointments.collectTaskFor(LocalDate.of(2014, 1, 2)).size());
	}

	@Test
	public void regularAppointment3() {
		// an appointment covering a whole day plus one nano second
		ObservableList<Agenda.Appointment> lAppointments = FXCollections.observableArrayList(new Agenda.AppointmentImplLocal()
			.withStartLocalDateTime(LocalDate.of(2014, 1, 2).atStartOfDay())
			.withEndLocalDateTime(LocalDate.of(2014, 1, 2).plusDays(1).atStartOfDay().plusNanos(1))
		);
		AllAppointments lAllAppointments = new AllAppointments(lAppointments);
		Assert.assertEquals(0, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 1)).size());
		Assert.assertEquals(1, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(1, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 3)).size());
		// others
		Assert.assertEquals(0, lAllAppointments.collectWholedayFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(0, lAllAppointments.collectTaskFor(LocalDate.of(2014, 1, 2)).size());
	}

	@Test
	public void wholedayAppointment1() {
		// even though the appointment is set somewhere on the middle of the day, it simply is a whole day appointment
		ObservableList<Agenda.Appointment> lAppointments = FXCollections.observableArrayList(new Agenda.AppointmentImplLocal()
			.withStartLocalDateTime(LocalDateTime.of(2014, 1, 2, 8, 00))
			.withEndLocalDateTime(LocalDateTime.of(2014, 1, 2, 11, 30))
			.withWholeDay(true)
		);
		AllAppointments lAllAppointments = new AllAppointments(lAppointments);
		Assert.assertEquals(0, lAllAppointments.collectWholedayFor(LocalDate.of(2014, 1, 1)).size());
		Assert.assertEquals(1, lAllAppointments.collectWholedayFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(0, lAllAppointments.collectWholedayFor(LocalDate.of(2014, 1, 3)).size());
		// others
		Assert.assertEquals(0, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(0, lAllAppointments.collectTaskFor(LocalDate.of(2014, 1, 2)).size());
	}

	@Test
	public void wholedayAppointment2() {
		// whole day without end date
		ObservableList<Agenda.Appointment> lAppointments = FXCollections.observableArrayList(new Agenda.AppointmentImplLocal()
			.withStartLocalDateTime(LocalDateTime.of(2014, 1, 2, 8, 00))
			.withWholeDay(true)
		);
		AllAppointments lAllAppointments = new AllAppointments(lAppointments);
		Assert.assertEquals(0, lAllAppointments.collectWholedayFor(LocalDate.of(2014, 1, 1)).size());
		Assert.assertEquals(1, lAllAppointments.collectWholedayFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(0, lAllAppointments.collectWholedayFor(LocalDate.of(2014, 1, 3)).size());
		// others
		Assert.assertEquals(0, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(0, lAllAppointments.collectTaskFor(LocalDate.of(2014, 1, 2)).size());
	}

	@Test
	public void taskAppointment1() {
		// a task has no end date
		ObservableList<Agenda.Appointment> lAppointments = FXCollections.observableArrayList(new Agenda.AppointmentImplLocal()
			.withStartLocalDateTime(LocalDateTime.of(2014, 1, 2, 8, 00))
		);
		AllAppointments lAllAppointments = new AllAppointments(lAppointments);
		Assert.assertEquals(0, lAllAppointments.collectTaskFor(LocalDate.of(2014, 1, 1)).size());
		Assert.assertEquals(1, lAllAppointments.collectTaskFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(0, lAllAppointments.collectTaskFor(LocalDate.of(2014, 1, 3)).size());
		// others
		Assert.assertEquals(0, lAllAppointments.collectRegularFor(LocalDate.of(2014, 1, 2)).size());
		Assert.assertEquals(0, lAllAppointments.collectWholedayFor(LocalDate.of(2014, 1, 2)).size());
	}
}
