package jfxtras.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.component.change.DateTimeCreated;
import jfxtras.icalendarfx.properties.component.change.LastModified;
import jfxtras.icalendarfx.properties.component.change.Sequence;
import jfxtras.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.icalendarfx.properties.component.descriptive.Classification;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.descriptive.Classification.ClassificationType;
import jfxtras.icalendarfx.properties.component.descriptive.Status.StatusType;
import jfxtras.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceDates;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.icalendarfx.properties.component.relationship.Contact;
import jfxtras.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;

/**
 * Test following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * 
 * for the following properties:
 * @see Attachment - extended from Describable
 * @see Categories
 * @see Classification
 * @see Contact
 * @see DateTimeCreated
 * @see ExceptionDates
 * @see LastModified - extended from LastModified
 * @see RecurrenceId
 * @see RecurrenceRule - extended from Repeatable
 * @see RecurrenceDates - extended from Repeatable
 * @see RelatedTo
 * @see Sequence
 * @see Status
 * @see Summary - extended from Describable
 * 
 * @author David Bal
 *
 */
public class DisplayableTest
{
    @Test
    public void canBuildDisplayable() throws InstantiationException, IllegalAccessException
    {
        List<VDisplayable<?>> components = Arrays.asList(
                new VEvent()
                    .withAttachments(Attachment.parse("ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com"))
                    .withCategories("group03","group04","group05")
                    .withCategories("group06")
                    .withClassification(ClassificationType.PUBLIC)
                    .withContacts("CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234", "Harry Potter\\, Hogwarts\\, by owl")
                    .withDateTimeCreated("20160420T080000Z")
                    .withExceptionDates("EXDATE:19960301T010000Z,19960304T010000Z,19960307T010000Z")
                    .withDateTimeLastModified("20160306T080000Z")
                    .withRecurrenceDates("RDATE:19960302T010000Z,19960304T010000Z")
                    .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 1, 1, 12, 0), ZoneId.of("Z")))
                    .withRelatedTo("jsmith.part7.19960817T083000.xyzMail@example.com",
                            "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com")
                    .withRecurrenceRule("RRULE:FREQ=DAILY")
                    .withSequence(2)
                    .withStatus(StatusType.NEEDS_ACTION)
                    .withSummary("a test summary"),
                new VTodo()
                    .withAttachments(Attachment.parse("ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com"))
                    .withCategories("group03","group04","group05")
                    .withCategories("group06")
                    .withClassification(ClassificationType.PUBLIC)
                    .withContacts("CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234", "Harry Potter\\, Hogwarts\\, by owl")
                    .withDateTimeCreated("20160420T080000Z")
                    .withExceptionDates("EXDATE:19960301T010000Z,19960304T010000Z,19960307T010000Z")
                    .withDateTimeLastModified("20160306T080000Z")
                    .withRecurrenceDates("RDATE:19960302T010000Z,19960304T010000Z")
                    .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 1, 1, 12, 0), ZoneId.of("Z")))
                    .withRelatedTo("jsmith.part7.19960817T083000.xyzMail@example.com",
                            "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com")
                    .withRecurrenceRule("RRULE:FREQ=DAILY")
                    .withSequence(2)
                    .withStatus(StatusType.NEEDS_ACTION)
                    .withSummary("a test summary"),
                new VJournal()
                    .withAttachments(Attachment.parse("ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com"))
                    .withCategories("group03","group04","group05")
                    .withCategories("group06")
                    .withClassification(ClassificationType.PUBLIC)
                    .withContacts("CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234", "Harry Potter\\, Hogwarts\\, by owl")
                    .withDateTimeCreated("20160420T080000Z")
                    .withExceptionDates("EXDATE:19960301T010000Z,19960304T010000Z,19960307T010000Z")
                    .withDateTimeLastModified("20160306T080000Z")
                    .withRecurrenceDates("RDATE:19960302T010000Z,19960304T010000Z")
                    .withRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 1, 1, 12, 0), ZoneId.of("Z")))
                    .withRelatedTo("jsmith.part7.19960817T083000.xyzMail@example.com",
                            "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com")
                    .withRecurrenceRule("RRULE:FREQ=DAILY")
                    .withSequence(2)
                    .withStatus(StatusType.NEEDS_ACTION)
                    .withSummary("a test summary")
                );
        
        List<ZonedDateTime> expectedDates = Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 2, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 3, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 5, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 9, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 11, 1, 0), ZoneId.of("Z")),
                ZonedDateTime.of(LocalDateTime.of(1996, 3, 13, 1, 0), ZoneId.of("Z"))
                );
        
        for (VDisplayable<?> builtComponent : components)
        {
            String componentName = builtComponent.name();
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com" + System.lineSeparator() +
                    "CATEGORIES:group03,group04,group05" + System.lineSeparator() +
                    "CATEGORIES:group06" + System.lineSeparator() +
                    "CLASS:PUBLIC" + System.lineSeparator() +
                    "CONTACT:Jim Dolittle\\, ABC Industries\\, +1-919-555-1234" + System.lineSeparator() +
                    "CONTACT:Harry Potter\\, Hogwarts\\, by owl" + System.lineSeparator() +
                    "CREATED:20160420T080000Z" + System.lineSeparator() +
                    "EXDATE:19960301T010000Z,19960304T010000Z,19960307T010000Z" + System.lineSeparator() +
                    "LAST-MODIFIED:20160306T080000Z" + System.lineSeparator() +
                    "RDATE:19960302T010000Z,19960304T010000Z" + System.lineSeparator() +
                    "RECURRENCE-ID:20160101T120000Z" + System.lineSeparator() +
                    "RELATED-TO:jsmith.part7.19960817T083000.xyzMail@example.com" + System.lineSeparator() +
                    "RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@example.com" + System.lineSeparator() +
                    "RRULE:FREQ=DAILY" + System.lineSeparator() +
                    "SEQUENCE:2" + System.lineSeparator() +
                    "STATUS:NEEDS-ACTION" + System.lineSeparator() +
                    "SUMMARY:a test summary" + System.lineSeparator() +
                    "END:" + componentName;

            VComponent parsedComponent = builtComponent.getClass().newInstance();
            parsedComponent.parseContent(expectedContent);

            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toContent());     
            
            builtComponent.setRecurrenceRule("RRULE:FREQ=DAILY;INTERVAL=2");
            builtComponent.setDateTimeStart(DateTimeStart.parse(ZonedDateTime.class, "19960301T010000Z"));
            List<Temporal> myDates = builtComponent.streamRecurrences().limit(6).collect(Collectors.toList());
            assertEquals(expectedDates, myDates);
        }
    }
    
    @Test
    public void exceptionTest1()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency("DAILY")
                        .withInterval(3)
                        .withCount(6))
                .withExceptionDates(new ExceptionDates(LocalDateTime.of(2015, 11, 12, 10, 0)
                                     , LocalDateTime.of(2015, 11, 15, 10, 0)));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "EXDATE:20151112T100000,20151115T100000";
        assertEquals(expectedContent, e.getExceptionDates().get(0).toContent());
        String expectedContent2 = "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6";
        assertEquals(expectedContent2, e.getRecurrenceRule().toContent());
    }
    
    @Test // 2 separate EXDATE properties, and out of order too
    public void exceptionTest2()
    {       
        VEvent e = new VEvent()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency("DAILY")
                        .withInterval(3)
                        .withCount(6))
                .withExceptionDates(new ExceptionDates(LocalDateTime.of(2015, 11, 15, 10, 0)))
                .withSequence(2)
                .withExceptionDates(LocalDateTime.of(2015, 11, 12, 10, 0));
        
        List<Temporal> madeDates = e
                .streamRecurrences()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        String expectedContent = "EXDATE:20151115T100000";
        assertEquals(expectedContent, e.getExceptionDates().get(0).toContent());
        String expectedContent2 = "EXDATE:20151112T100000";
        assertEquals(expectedContent2, e.getExceptionDates().get(1).toContent());
        String expectedContent3 = "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6";
        assertEquals(expectedContent3, e.getRecurrenceRule().toContent());
    }
    
    // Google test
    @Test
    public void canStreamGoogleWithExDates()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
                .withExceptionDates(new ExceptionDates(
                            ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 12, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 12, 30), ZoneId.of("America/Los_Angeles"))))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2016, 5, 12, 19, 30, 0), ZoneId.of("Z"))));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 8, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 11, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 13, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 12, 30), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void canChangeGoogleWithExDatesToWholeDay()
    {
        VEvent e = new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
                .withExceptionDates(new ExceptionDates(
                            ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 12, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 12, 30), ZoneId.of("America/Los_Angeles"))))
                .withRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.DAILY)
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2016, 5, 12, 19, 30, 0), ZoneId.of("Z"))));
        e.setExceptionDates(null);
        e.setDateTimeStart(new DateTimeStart(LocalDate.of(2016, 2, 7)));
        e.setExceptionDates(FXCollections.observableArrayList(new ExceptionDates(
                            LocalDate.of(2016, 2, 10)
                          , LocalDate.of(2016, 2, 12)
                          , LocalDate.of(2016, 2, 9)
                          )));
        List<Temporal> madeDates = e
                .streamRecurrences()
                .limit(5)
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2016, 2, 7)
              , LocalDate.of(2016, 2, 8)
              , LocalDate.of(2016, 2, 11)
              , LocalDate.of(2016, 2, 13)
              , LocalDate.of(2016, 2, 14)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void canHandleDTStartTypeChange()
    {
        VEvent component = new VEvent()
            .withDateTimeStart(LocalDate.of(1997, 3, 1))
            .withExceptionDates("EXDATE;VALUE=DATE:19970304,19970504,19970704,19970904");
        component.setDateTimeStart(DateTimeStart.parse(ZonedDateTime.class, "20160302T223316Z")); // invalid
        String expectedError = "DTSTART, EXDATE:";
        boolean isErrorPresent = component.errors().stream()
                .filter(s -> s.substring(0, expectedError.length()).equals(expectedError))
                .findAny()
                .isPresent();
        assertTrue(isErrorPresent);
    }
    
    
    @Test (expected = DateTimeException.class)
    public void canCatchWrongDateType()
    {
        VEvent component = new VEvent()
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
        ObservableList<ExceptionDates> exceptions = FXCollections.observableArrayList();
        exceptions.add(ExceptionDates.parse("20160228T093000"));
        component.setExceptionDates(exceptions); // invalid    
    }
}
