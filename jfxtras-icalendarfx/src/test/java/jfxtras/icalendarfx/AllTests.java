package jfxtras.icalendarfx;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.icalendarfx.calendar.CalendarScaleTest;
import jfxtras.icalendarfx.calendar.CopyCalendarTest;
import jfxtras.icalendarfx.calendar.GeneralCalendarTest;
import jfxtras.icalendarfx.calendar.OrdererTest;
import jfxtras.icalendarfx.calendar.ParseCalendarTest;
import jfxtras.icalendarfx.calendar.ReadICSFileTest;
import jfxtras.icalendarfx.calendar.VCalendarRecurrenceIDTest;
import jfxtras.icalendarfx.component.BaseTest;
import jfxtras.icalendarfx.component.CopyComponentTest;
import jfxtras.icalendarfx.component.DaylightSavingsTimeTest;
import jfxtras.icalendarfx.component.DisplayableTest;
import jfxtras.icalendarfx.component.EqualsTest;
import jfxtras.icalendarfx.component.GeneralComponentTest;
import jfxtras.icalendarfx.component.LocatableTest;
import jfxtras.icalendarfx.component.ParseComponentTest;
import jfxtras.icalendarfx.component.PersonalTest;
import jfxtras.icalendarfx.component.PrimaryTest;
import jfxtras.icalendarfx.component.RepeatableTest;
import jfxtras.icalendarfx.component.ScheduleConflictTest;
import jfxtras.icalendarfx.component.StandardOrDaylightTimeTest;
import jfxtras.icalendarfx.component.VAlarmTest;
import jfxtras.icalendarfx.component.VEventTest;
import jfxtras.icalendarfx.component.VFreeBusyTest;
import jfxtras.icalendarfx.component.VJournalTest;
import jfxtras.icalendarfx.component.VTimeZoneTest;
import jfxtras.icalendarfx.component.VTodoTest;
import jfxtras.icalendarfx.itip.CancelRecurrenceTest;
import jfxtras.icalendarfx.itip.ComboMessageTest;
import jfxtras.icalendarfx.itip.HandleRecurrencesTest;
import jfxtras.icalendarfx.itip.RequestTest;
import jfxtras.icalendarfx.itip.SimpleCancelTest;
import jfxtras.icalendarfx.itip.SimplePublishTest;
import jfxtras.icalendarfx.itip.WholeDayTest;
import jfxtras.icalendarfx.misc.FoldingAndUnfoldingTest;
import jfxtras.icalendarfx.parameter.AlternateTextRepresentationTest;
import jfxtras.icalendarfx.parameter.CommonNameTest;
import jfxtras.icalendarfx.parameter.DelegateesTest;
import jfxtras.icalendarfx.parameter.DirectoryEntryReferenceTest;
import jfxtras.icalendarfx.parameter.NonstandardParameterTest;
import jfxtras.icalendarfx.parameter.ParseDateTest;
import jfxtras.icalendarfx.parameter.ParseParameterTest;
import jfxtras.icalendarfx.parameter.ParticipationRoleTest;
import jfxtras.icalendarfx.parameter.RelationshipTypeTest;
import jfxtras.icalendarfx.property.ActionTest;
import jfxtras.icalendarfx.property.AttachmentTest;
import jfxtras.icalendarfx.property.AttendeeTest;
import jfxtras.icalendarfx.property.CategoriesTest;
import jfxtras.icalendarfx.property.ClassificationTest;
import jfxtras.icalendarfx.property.CommentTest;
import jfxtras.icalendarfx.property.ContactTest;
import jfxtras.icalendarfx.property.DateTimeCompletedTest;
import jfxtras.icalendarfx.property.DateTimeCreatedTest;
import jfxtras.icalendarfx.property.DateTimeDueTest;
import jfxtras.icalendarfx.property.DateTimeEndTest;
import jfxtras.icalendarfx.property.DateTimeStampTest;
import jfxtras.icalendarfx.property.DateTimeStartTest;
import jfxtras.icalendarfx.property.DescriptionTest;
import jfxtras.icalendarfx.property.DurationTest;
import jfxtras.icalendarfx.property.ExceptionDatesTest;
import jfxtras.icalendarfx.property.FreeBusyTimeTest;
import jfxtras.icalendarfx.property.GeneralPropertyTest;
import jfxtras.icalendarfx.property.LocationTest;
import jfxtras.icalendarfx.property.NonStandardTest;
import jfxtras.icalendarfx.property.OrganizerTest;
import jfxtras.icalendarfx.property.PriorityTest;
import jfxtras.icalendarfx.property.RecurrenceIdTest;
import jfxtras.icalendarfx.property.RecurrenceRuleTest;
import jfxtras.icalendarfx.property.RecurrencesTest;
import jfxtras.icalendarfx.property.RelatedToTest;
import jfxtras.icalendarfx.property.RepeatCountTest;
import jfxtras.icalendarfx.property.RequestStatusTest;
import jfxtras.icalendarfx.property.ResourcesTest;
import jfxtras.icalendarfx.property.SequenceTest;
import jfxtras.icalendarfx.property.StatusTest;
import jfxtras.icalendarfx.property.SummaryTest;
import jfxtras.icalendarfx.property.TimeTransparencyTest;
import jfxtras.icalendarfx.property.TimeZoneIdentifierTest;
import jfxtras.icalendarfx.property.TimeZoneNameTest;
import jfxtras.icalendarfx.property.TimeZoneOffsetTest;
import jfxtras.icalendarfx.property.TimeZoneURLTest;
import jfxtras.icalendarfx.property.TriggerTest;
import jfxtras.icalendarfx.property.URLTest;
import jfxtras.icalendarfx.property.UniqueIdentifierTest;
import jfxtras.icalendarfx.property.calendar.MethodTest;
import jfxtras.icalendarfx.property.rrule.ByDayTest;
import jfxtras.icalendarfx.property.rrule.ByMonthTest;
import jfxtras.icalendarfx.property.rrule.ByWeekNumberTest;
import jfxtras.icalendarfx.property.rrule.ByYearDayTest;
import jfxtras.icalendarfx.property.rrule.FrequencyTest;
import jfxtras.icalendarfx.property.rrule.RRuleErrorTest;
import jfxtras.icalendarfx.property.rrule.RecurrenceRuleParseTest;
import jfxtras.icalendarfx.property.rrule.RecurrenceRuleStreamTest;

@RunWith(Suite.class)
@SuiteClasses({ 
        
        // misc tests
        FoldingAndUnfoldingTest.class,
        OrdererTest.class,
        
        // iTIP tests
        CancelRecurrenceTest.class,
        ComboMessageTest.class,
        HandleRecurrencesTest.class,
        RequestTest.class,
        SimpleCancelTest.class,
        SimplePublishTest.class,
        WholeDayTest.class,
    
        // calendar tests
        CalendarScaleTest.class,
        CopyCalendarTest.class,
        GeneralCalendarTest.class,
        MethodTest.class,
        ParseCalendarTest.class,
        ReadICSFileTest.class,
        VCalendarRecurrenceIDTest.class,
                
        //component tests
        BaseTest.class,
        CopyComponentTest.class,
        DateTimeEndTest.class,
        DaylightSavingsTimeTest.class,
        DisplayableTest.class,
        EqualsTest.class,
        GeneralComponentTest.class,
        LocatableTest.class,
        ParseComponentTest.class,
        PrimaryTest.class,
        PersonalTest.class,
        RepeatableTest.class,
        ScheduleConflictTest.class,
        StandardOrDaylightTimeTest.class,
        VAlarmTest.class,
        VEventTest.class,
        VFreeBusyTest.class,
        VJournalTest.class,
        VTimeZoneTest.class,
        VTodoTest.class,
       
       // property tests
        ActionTest.class,
        AttachmentTest.class,
        AttendeeTest.class,
        CategoriesTest.class,
        ClassificationTest.class,
        CommentTest.class,
        ContactTest.class,
        DateTimeCompletedTest.class,
        DateTimeCreatedTest.class,
        DateTimeDueTest.class,
        DateTimeEndTest.class,
        DateTimeStampTest.class,
        DateTimeStartTest.class,
        DescriptionTest.class,
        DurationTest.class,
        ExceptionDatesTest.class,
        FreeBusyTimeTest.class,
        GeneralPropertyTest.class,
        LocationTest.class,
        NonStandardTest.class,
        OrganizerTest.class,
        PriorityTest.class,
        RecurrenceIdTest.class,
        RecurrenceRuleTest.class,
        RecurrenceRuleParseTest.class,
        RecurrencesTest.class,
        RelatedToTest.class,
        RepeatCountTest.class,
        RequestStatusTest.class,
        ResourcesTest.class,
        SequenceTest.class,
        StatusTest.class,
        SummaryTest.class,
        TimeTransparencyTest.class,
        TimeZoneIdentifierTest.class,
        TimeZoneNameTest.class,
        TimeZoneOffsetTest.class,
        TimeZoneURLTest.class,
        TriggerTest.class,
        UniqueIdentifierTest.class,
        URLTest.class,
        
        // parameter tests
        AlternateTextRepresentationTest.class,
        CommonNameTest.class,
        DelegateesTest.class,
        DirectoryEntryReferenceTest.class,
        NonstandardParameterTest.class,
        ParseDateTest.class,
        ParseParameterTest.class,
        ParticipationRoleTest.class,
        RelationshipTypeTest.class,
        
        // Recurrence Rule tests
        RecurrenceRuleParseTest.class,
        RecurrenceRuleStreamTest.class,
        RRuleErrorTest.class,
        FrequencyTest.class,
        ByDayTest.class,
        ByMonthTest.class,
        ByWeekNumberTest.class,
        ByYearDayTest.class
              
              })

public class AllTests {

}
