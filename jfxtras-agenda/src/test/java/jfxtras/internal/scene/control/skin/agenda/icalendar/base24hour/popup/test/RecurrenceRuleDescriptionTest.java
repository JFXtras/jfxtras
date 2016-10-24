package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.AgendaDateTimeUtilities;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.EditRecurrenceRuleVEventVBox;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;

public class RecurrenceRuleDescriptionTest
{
    // Easy-to-read-summary tests for edit popup RRULE summary
    
    @Test
    public void canMakeRRuleSummaryString1()
    {
        VEvent v = ICalendarStaticComponents.getDaily6();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue()); 
        String expectedString = "Every 2 days, until Dec 1, 2015";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString2()
    {
        VEvent v = ICalendarStaticComponents.getYearly1();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Yearly on November 9";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString3()
    {
        VEvent v = ICalendarStaticComponents.getMonthly1();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Monthly on day 9";
        assertEquals(expectedString, summaryString);
    }

    @Test
    public void canMakeRRuleSummaryString4()
    {
        VEvent v = ICalendarStaticComponents.getMonthly7();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Monthly on the third Monday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString5()
    {
        VEvent v = ICalendarStaticComponents.getWeekly1();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Weekly on Monday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString6()
    {
        VEvent v = ICalendarStaticComponents.getWeekly2();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Every 2 weeks on Monday, Wednesday, Friday";
        assertEquals(expectedString, summaryString);
    }
    
    @Test
    public void canMakeRRuleSummaryString7()
    {
        VEvent v = ICalendarStaticComponents.getWeekly5();
        String summaryString = EditRecurrenceRuleVEventVBox.makeSummary(v.getRecurrenceRule().getValue(), v.getDateTimeStart().getValue());
        String expectedString = "Every 2 weeks on Monday, Wednesday, Friday, 11 times";
        assertEquals(expectedString, summaryString);
    }
    
    /* date-time ranges for edit and delete dialogs */
    @Test
    public void canMakeRangeToString1()
    {
        String dateTimeString = AgendaDateTimeUtilities.formatRange(LocalDateTime.of(2015, 11, 11, 10, 0), LocalDateTime.of(2015, 12, 25, 12, 0));
        assertEquals("Wed, November 11, 2015 10:00 AM - Fri, December 25, 2015 12:00 PM", dateTimeString);
        String dateString = AgendaDateTimeUtilities.formatRange(LocalDate.of(2015, 11, 9), LocalDate.of(2015, 11, 24));
        assertEquals("Mon, November 9, 2015 - Tue, November 24, 2015", dateString);
        String dateForeverString = AgendaDateTimeUtilities.formatRange(LocalDateTime.of(2015, 11, 9, 10, 0), null);
        assertEquals("Mon, November 9, 2015 10:00 AM - forever", dateForeverString);
    }
}
