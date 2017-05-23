package jfxtras.icalendarfx;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jfxtras.icalendarfx.calendar.CalendarScaleTest;
import jfxtras.icalendarfx.calendar.CopyCalendarTest;
import jfxtras.icalendarfx.calendar.GeneralCalendarTest;
import jfxtras.icalendarfx.calendar.ParseCalendarTest;
import jfxtras.icalendarfx.calendar.ReadICSFileTest;
import jfxtras.icalendarfx.calendar.RecurrenceIDParentValidTest;
import jfxtras.icalendarfx.calendar.VCalendarRecurrenceIDTest;
import jfxtras.icalendarfx.component.BaseTest;
import jfxtras.icalendarfx.component.ComponentStatusTest;
import jfxtras.icalendarfx.component.CopyComponentTest;
import jfxtras.icalendarfx.component.DateTimeEndComponentTest;
import jfxtras.icalendarfx.component.DaylightSavingsTimeTest;
import jfxtras.icalendarfx.component.DescribableTest;
import jfxtras.icalendarfx.component.DisplayableTest;
import jfxtras.icalendarfx.component.EqualsTest;
import jfxtras.icalendarfx.component.ErrorCatchTest;
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
import jfxtras.icalendarfx.misc.AddAndRemoveChildrenTests;
import jfxtras.icalendarfx.misc.CreateElementsTests;
import jfxtras.icalendarfx.misc.ErrorDetectingTest;
import jfxtras.icalendarfx.misc.FoldingAndUnfoldingTest;
import jfxtras.icalendarfx.misc.MiscICalendarTests;
import jfxtras.icalendarfx.misc.OrdererTest;
import jfxtras.icalendarfx.parameter.AlternateTextRepresentationTest;
import jfxtras.icalendarfx.parameter.CommonNameTest;
import jfxtras.icalendarfx.parameter.DelegateesTest;
import jfxtras.icalendarfx.parameter.DirectoryEntryReferenceTest;
import jfxtras.icalendarfx.parameter.FormatTypeTest;
import jfxtras.icalendarfx.parameter.NonstandardParameterTest;
import jfxtras.icalendarfx.parameter.ParseDateTest;
import jfxtras.icalendarfx.parameter.ParseParameterTest;
import jfxtras.icalendarfx.parameter.ParticipationRoleTest;
import jfxtras.icalendarfx.parameter.RelationshipTypeTest;
import jfxtras.icalendarfx.parameter.rrule.ByDayTest;
import jfxtras.icalendarfx.parameter.rrule.ByHourTest;
import jfxtras.icalendarfx.parameter.rrule.ByMinuteTest;
import jfxtras.icalendarfx.parameter.rrule.ByMonthDayTest;
import jfxtras.icalendarfx.parameter.rrule.ByMonthTest;
import jfxtras.icalendarfx.parameter.rrule.ByRuleTest;
import jfxtras.icalendarfx.parameter.rrule.BySecondTest;
import jfxtras.icalendarfx.parameter.rrule.BySetPositionTest;
import jfxtras.icalendarfx.parameter.rrule.ByWeekNumberTest;
import jfxtras.icalendarfx.parameter.rrule.ByYearDayTest;
import jfxtras.icalendarfx.parameter.rrule.FrequencyTest;
import jfxtras.icalendarfx.parameter.rrule.IntervalTest;
import jfxtras.icalendarfx.parameter.rrule.RRuleErrorTest;
import jfxtras.icalendarfx.parameter.rrule.RecurrenceRuleParseTest;
import jfxtras.icalendarfx.parameter.rrule.RecurrenceRuleStreamTest;
import jfxtras.icalendarfx.property.calendar.MethodTest;
import jfxtras.icalendarfx.property.component.ActionTest;
import jfxtras.icalendarfx.property.component.AttachmentTest;
import jfxtras.icalendarfx.property.component.AttendeeTest;
import jfxtras.icalendarfx.property.component.CategoriesTest;
import jfxtras.icalendarfx.property.component.ClassificationTest;
import jfxtras.icalendarfx.property.component.CommentTest;
import jfxtras.icalendarfx.property.component.ContactTest;
import jfxtras.icalendarfx.property.component.DateTimeCompletedTest;
import jfxtras.icalendarfx.property.component.DateTimeCreatedTest;
import jfxtras.icalendarfx.property.component.DateTimeDueTest;
import jfxtras.icalendarfx.property.component.DateTimeEndTest;
import jfxtras.icalendarfx.property.component.DateTimeStampTest;
import jfxtras.icalendarfx.property.component.DateTimeStartTest;
import jfxtras.icalendarfx.property.component.DescriptionTest;
import jfxtras.icalendarfx.property.component.DurationTest;
import jfxtras.icalendarfx.property.component.ExceptionDatesTest;
import jfxtras.icalendarfx.property.component.FreeBusyTimeTest;
import jfxtras.icalendarfx.property.component.GeneralPropertyTest;
import jfxtras.icalendarfx.property.component.LastModifiedTest;
import jfxtras.icalendarfx.property.component.LocationTest;
import jfxtras.icalendarfx.property.component.NonStandardTest;
import jfxtras.icalendarfx.property.component.OrganizerTest;
import jfxtras.icalendarfx.property.component.PriorityTest;
import jfxtras.icalendarfx.property.component.RecurrenceIdTest;
import jfxtras.icalendarfx.property.component.RecurrenceRuleTest;
import jfxtras.icalendarfx.property.component.RecurrencesTest;
import jfxtras.icalendarfx.property.component.RelatedToTest;
import jfxtras.icalendarfx.property.component.RepeatCountTest;
import jfxtras.icalendarfx.property.component.RequestStatusTest;
import jfxtras.icalendarfx.property.component.ResourcesTest;
import jfxtras.icalendarfx.property.component.SequenceTest;
import jfxtras.icalendarfx.property.component.StatusTest;
import jfxtras.icalendarfx.property.component.SummaryTest;
import jfxtras.icalendarfx.property.component.TimeTransparencyTest;
import jfxtras.icalendarfx.property.component.TimeZoneIdentifierTest;
import jfxtras.icalendarfx.property.component.TimeZoneNameTest;
import jfxtras.icalendarfx.property.component.TimeZoneOffsetTest;
import jfxtras.icalendarfx.property.component.TimeZoneURLTest;
import jfxtras.icalendarfx.property.component.TriggerTest;
import jfxtras.icalendarfx.property.component.URLTest;
import jfxtras.icalendarfx.property.component.UniqueIdentifierTest;

@RunWith(Suite.class)
@SuiteClasses({ 
        
        // misc tests
		AddAndRemoveChildrenTests.class,
        CreateElementsTests.class,
		ErrorDetectingTest.class,
        FoldingAndUnfoldingTest.class,
        MiscICalendarTests.class,
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
        OrdererTest.class,
        ParseCalendarTest.class,
        ReadICSFileTest.class,
        RecurrenceIDParentValidTest.class,
        VCalendarRecurrenceIDTest.class,
                
        //component tests
        BaseTest.class,
        ComponentStatusTest.class,
        CopyComponentTest.class,
        DateTimeEndComponentTest.class,
        DaylightSavingsTimeTest.class,
        DescribableTest.class,
        DisplayableTest.class,
        EqualsTest.class,
        ErrorCatchTest.class,
        GeneralComponentTest.class,
        LastModifiedTest.class,
        LocatableTest.class,
        ParseComponentTest.class,
        PersonalTest.class,
        PrimaryTest.class,
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
	    LastModifiedTest.class,
	    LocationTest.class,
	    MethodTest.class,
	    NonStandardTest.class,
	    OrganizerTest.class,
	    PriorityTest.class,
	    RecurrenceIdTest.class,
	    RecurrenceRuleTest.class,
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
        FormatTypeTest.class,
        NonstandardParameterTest.class,
        ParseDateTest.class,
        ParseParameterTest.class,
        ParticipationRoleTest.class,
        RelationshipTypeTest.class,
        
        // Recurrence Rule tests
        RRuleErrorTest.class,
        ByDayTest.class,
        ByHourTest.class,
        ByMinuteTest.class,
        ByMonthDayTest.class,
        ByMonthTest.class,
        BySecondTest.class,
        BySetPositionTest.class,
        ByWeekNumberTest.class,
        ByYearDayTest.class,
        FrequencyTest.class,
        IntervalTest.class,
        RecurrenceRuleParseTest.class,
        RecurrenceRuleStreamTest.class,
        ByRuleTest.class
              })

public class AllTests {

}
