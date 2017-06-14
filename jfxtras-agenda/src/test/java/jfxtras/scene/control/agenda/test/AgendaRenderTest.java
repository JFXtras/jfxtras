/**
 * AgendaRenderTest.java
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

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.test.OSInfo.OS;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;
import junit.framework.Assert;

/**
 * 
 */
public class AgendaRenderTest extends AbstractAgendaTestBase {

	/**
	 * 
	 */
	@Override
	public Parent getRootNode()
	{
		return super.getRootNode();
	}

	/**
	 * 
	 */
	@Test
	public void renderRegularAppointment()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
				
		Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
		//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
		new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void renderTwoConsequetiveRegularAppointment()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().addAll( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
	        ,  new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T13:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group02"))
	        );
		});
				
		Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
		//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
		new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
		n = find("#AppointmentRegularBodyPane2014-01-01/1");
		//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
		new AssertNode(n).assertXYWH(0.5, 503.5, 125.0, 42.0, 0.01);
//		TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void displayedDateTime()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
				
		assertFind("#AppointmentRegularBodyPane2014-01-01/0");
		
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.setDisplayedLocalDateTime(LocalDate.of(2015, 1, 1).atStartOfDay());
		});
		
		assertNotFind("#AppointmentRegularBodyPane2014-01-01/0");
	}


	/**
	 * 
	 */
	@Test
	public void renderRegularAppointmentCalendar()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImpl()
	            .withStartTime(TestUtil.quickParseCalendarFromDateTime("2014-01-01T10:00:00.000"))
	            .withEndTime(TestUtil.quickParseCalendarFromDateTime("2014-01-01T12:00:00.000"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
				
		Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
		//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
		new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
    @Test
    public void renderRegularAppointmentZoned()
    {
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
                .withStartTemporal(ZonedDateTime.of(LocalDateTime.of(2014, 1, 1, 10, 0), ZoneId.systemDefault()))
                .withEndTemporal(ZonedDateTime.of(LocalDateTime.of(2014, 1, 1, 12, 0), ZoneId.systemDefault()))
                .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
        });

        Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
        //TestUtil.sleep(3000);
    }
    
    @Test
    public void renderWholeDayAppointmentLocalDate()
    {
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
                .withStartTemporal(LocalDate.of(2014, 1, 1))
                .withEndTemporal(LocalDate.of(2014, 1, 2))
                .withWholeDay(true)
                .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
        });

        Node n = find("#AppointmentWholedayBodyPane2014-01-01/0");
        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.5, 0.0, 5.0, 1006.125, 0.01);
        //TestUtil.sleep(3000);
    }
    
    @Test
    public void renderWholeDayAppointmentHijrahDate()
    {
        TestUtil.runThenWaitForPaintPulse( () -> {
            Temporal h = HijrahDate.from(LocalDate.of(2014, 1, 1));
            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
                .withStartTemporal(h)
                .withEndTemporal(h.plus(1, ChronoUnit.DAYS))
                .withWholeDay(true)
                .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
        });

        Node n = find("#AppointmentWholedayBodyPane2014-01-01/0");
        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.5, 0.0, 5.0, 1006.125, 0.01);
        //TestUtil.sleep(3000);
    }
    
    @Test
    public void renderRegularAppointmentOffset()
    {
        TestUtil.runThenWaitForPaintPulse( () -> {
            ZoneOffset offset = ZonedDateTime.now().getOffset();
            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
                .withStartTemporal(OffsetDateTime.of(LocalDateTime.of(2014, 1, 1, 10, 0), offset))
                .withEndTemporal(OffsetDateTime.of(LocalDateTime.of(2014, 1, 1, 12, 0), offset))
                .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
        });

        Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
        //TestUtil.sleep(3000);
    }
    
    @Test
    public void renderRegularAppointmentInstant()
    {
        TestUtil.runThenWaitForPaintPulse( () -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
            String dateInString = "2014-1-1 10:00:00";
            Date date = null;
            try {
                date = sdf.parse(dateInString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Instant instant = date.toInstant();
            agenda.appointments().add( new Agenda.AppointmentImplTemporal()
                .withStartTemporal(instant)
                .withEndTemporal(instant.plus(2, ChronoUnit.HOURS))
                .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
        });

        Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
        //TestUtil.sleep(3000);
    }

	/**
	 * Since day skin uses 99.999% of the week skin's code, we just test if it renders a single appointment as expected.
	 */
	@Test
	public void renderRegularAppointmentDaySkin()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.setSkin(new AgendaDaySkin(agenda));
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});
				
		Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
		//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
		new AssertNode(n).assertXYWH(0.5, 419.5, 937.0, 84.0, 0.01);
		//TestUtil.sleep(3000);
	}


	/**
	 * 
	 */
	@Test
	public void renderRegularSpanningAppointment()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-02T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
		});

		double y = (OSInfo.MY_OS == OS.UNIX) ? 402.5 : 419.5;
		double w = (OSInfo.MY_OS == OS.UNIX) ? 124.0 : 125.0;
		double h = (OSInfo.MY_OS == OS.UNIX) ? 564.0 : 587.0;
		double h2 = (OSInfo.MY_OS == OS.UNIX) ? 483.0 : 503.0;
		{
			Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
			//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
			new AssertNode(n).assertXYWH(0.5, y, w, h, 0.01);
		}
		{
			Node n = find("#AppointmentRegularBodyPane2014-01-02/0");
			//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
			new AssertNode(n).assertXYWH(0.5, 0.5, w, h2, 0.01);
		}
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void renderWholeDayAppointment()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
	            .withWholeDay(true)
            );
		});
			
		{
			Node n = find("#AppointmentWholedayBodyPane2014-01-01/0");
			//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
			double h = (OSInfo.MY_OS == OS.UNIX) ? 966.375 : 1006.125;
			new AssertNode(n).assertXYWH(0.5, 0.0, 5.0, h, 0.01);
		}
		{
			Node n = find("#AppointmentWholedayHeaderPane2014-01-01/0");
			//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
			double y = (OSInfo.MY_OS == OS.UNIX) ? 22.8671875 : 24.0390625;
			double w = (OSInfo.MY_OS == OS.UNIX) ? 133.50516183035714 : 135.21763392857142;
			double h = (OSInfo.MY_OS == OS.UNIX) ? 20.1328125 : 20.9609375;
			new AssertNode(n).assertXYWH(0.0, y, w, h, 0.01);
//			new AssertNode(n).assertXYWH(0.0, 24.0390625, 135.21763392857142, 20.9609375, 0.01);
		}
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void renderOverlappingAppointments()
	{
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
            );
			agenda.appointments().add( new Agenda.AppointmentImplLocal()
	            .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T11:00"))
	            .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T13:00"))
	            .withAppointmentGroup(appointmentGroupMap.get("group01"))
	        );
		});
				
		{
			Node n = find("#AppointmentRegularBodyPane2014-01-01/0");
			//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.A.XYWH);
			new AssertNode(n).assertXYWH(0.5, 419.5, 110.0, 84.0, 0.01);
		}
		{
			Node n = find("#AppointmentRegularBodyPane2014-01-01/1");
			//AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.A.XYWH);
			new AssertNode(n).assertXYWH(62.5, 461.5, 63.0, 84.0, 0.01);
		}
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void firstDayOfWeekDefault()
	{
		// assert Sunday is the first day of the week
		assertFind("#DayHeader2013-12-29");
		assertFind("#DayHeader2014-01-04");
		Assert.assertTrue(find("#DayHeader2013-12-29").getStyleClass().contains("weekend"));
		Assert.assertTrue(find("#DayHeader2014-01-04").getStyleClass().contains("weekend"));
		//TestUtil.sleep(3000);
	}
	
	/**
	 * 
	 */
	@Test
	public void firstDayOfWeekGermany()
	{
		// given
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.setLocale(Locale.GERMANY);
		});
		
		// assert Monday is the first day of the week
		assertFind("#DayHeader2013-12-30");
		assertFind("#DayHeader2014-01-05");
		Assert.assertTrue(find("#DayHeader2014-01-04").getStyleClass().contains("weekend"));
		Assert.assertTrue(find("#DayHeader2014-01-05").getStyleClass().contains("weekend"));
		//TestUtil.sleep(3000);
	}
	
	/**
	 * 
	 */
	@Test
	public void scrollToDisplayedTime()
	{
		// find the scrollpane
		ScrollPane lScrollPane = (ScrollPane)find(".scroll-pane");
		
		// given that the scrollpane is all the way at the top
		TestUtil.runThenWaitForPaintPulse( () -> {
			lScrollPane.setVvalue(0.0);
		});
		Assert.assertEquals(0.0, lScrollPane.getVvalue());
		
		// when the show a time all the way at the bottow
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.setDisplayedLocalDateTime(LocalDateTime.now().withHour(23).withMinute(59));
		});
		
		// then the scrollpane must have scrolled down
		Assert.assertTrue(lScrollPane.getVvalue() > 0.95); // we do not want to pinpoint it too hard, but somewhere way high is close enough to assert the behavior
		//TestUtil.sleep(3000);
	}
}
