package jfxtras.scene.control.agenda.test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.internal.scene.control.skin.agenda.AllAppointments;
import jfxtras.scene.control.agenda.Agenda;
import junit.framework.Assert;

import org.junit.Test;

public class AllAppointmentsTest {

	@Test
	public void regularAppointment1() {
		// just an appointment somewhere on a day
		ObservableList<Agenda.Appointment> lAppointments = FXCollections.observableArrayList(new Agenda.AppointmentImplLocal()
			.withStartDisplayedAtLocalDateTime(LocalDateTime.of(2014, 1, 2, 8, 00))
			.withEndDisplayedAtLocalDateTime(LocalDateTime.of(2014, 1, 2, 11, 30))
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
			.withStartDisplayedAtLocalDateTime(LocalDate.of(2014, 1, 2).atStartOfDay())
			.withEndDisplayedAtLocalDateTime(LocalDate.of(2014, 1, 2).plusDays(1).atStartOfDay()) // end is exclusive
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
			.withStartDisplayedAtLocalDateTime(LocalDate.of(2014, 1, 2).atStartOfDay())
			.withEndDisplayedAtLocalDateTime(LocalDate.of(2014, 1, 2).plusDays(1).atStartOfDay().plusNanos(1))
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
			.withStartDisplayedAtLocalDateTime(LocalDateTime.of(2014, 1, 2, 8, 00))
			.withEndDisplayedAtLocalDateTime(LocalDateTime.of(2014, 1, 2, 11, 30))
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
			.withStartDisplayedAtLocalDateTime(LocalDateTime.of(2014, 1, 2, 8, 00))
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
			.withStartDisplayedAtLocalDateTime(LocalDateTime.of(2014, 1, 2, 8, 00))
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
