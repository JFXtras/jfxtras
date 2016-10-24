package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.CategorySelectionGridPane;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.test.agenda.AgendaTestAbstract;
import jfxtras.test.TestUtil;

public class PopupReviseOneTest extends VEventPopupTestBase
{
    @Test
    public void canEditOne()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2015, 11, 11, 10, 0), // start selected instance
                    LocalDateTime.of(2015, 11, 11, 11, 0), // end selected instance
                    AgendaTestAbstract.CATEGORIES);
        });

       // edit property
       TextField summaryTextField = find("#summaryTextField");
       summaryTextField.setText("new summary");

       // save changes to THIS AND FUTURE
       click("#saveComponentButton");
       ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
       TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ONE));
       click("#changeDialogOkButton");

       String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
               .map(v -> v.toContent())
               .collect(Collectors.joining(System.lineSeparator()));
       String dtstamp = iTIPMessage.split(System.lineSeparator())[10];
       String expectedDTStamp = new DateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z"))).toContent();
       assertEquals(expectedDTStamp.substring(0, 16), dtstamp.substring(0, 16)); // check date, month and time

       String expectediTIPMessage =
               "BEGIN:VCALENDAR" + System.lineSeparator() +
               "METHOD:PUBLISH" + System.lineSeparator() +
               "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
               "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
               "BEGIN:VEVENT" + System.lineSeparator() +
               "CATEGORIES:group05" + System.lineSeparator() +
               "DTSTART:20151111T100000" + System.lineSeparator() +
               "DTEND:20151111T110000" + System.lineSeparator() +
               "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
               "SUMMARY:new summary" + System.lineSeparator() +
               dtstamp + System.lineSeparator() +
               "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
               "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
               "RECURRENCE-ID:20151111T100000" + System.lineSeparator() +
               "SEQUENCE:1" + System.lineSeparator() +
               "END:VEVENT" + System.lineSeparator() +
               "END:VCALENDAR";
       assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    // edit descriptive properties of a repeating event to make a special recurrence instance
    @Test
    public void canEditDescribableProperties2()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0), // start selected instance
                    LocalDateTime.of(2016, 5, 15, 11, 0), // end selected instance
                    categories());
        });

        // Get properties
        LocalDateTimeTextField startDateTimeTextField = find("#startDateTimeTextField");
        TextField summaryTextField = find("#summaryTextField");
        TextArea descriptionTextArea = find("#descriptionTextArea");
        TextField locationTextField = find("#locationTextField");
        TextField categoryTextField = find("#categoryTextField");
        CategorySelectionGridPane categorySelectionGridPane = find("#categorySelectionGridPane");
        
        // Edit properties
        startDateTimeTextField.setLocalDateTime(LocalDateTime.of(2016, 5, 15, 8, 0));
        summaryTextField.setText("new summary");
        descriptionTextArea.setText("new description");
        locationTextField.setText("new location");        
        TestUtil.runThenWaitForPaintPulse(() -> categorySelectionGridPane.setCategorySelected(11));
        categoryTextField.setText("new group name");  // TODO - FIX THIS - CATEGORY NOT WORKING
        
        // Save changes
        click("#saveComponentButton");        
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ONE));
        click("#changeDialogOkButton");
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        String dtstamp = iTIPMessage.split(System.lineSeparator())[10];
        String expectedDTStamp = new DateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z"))).toContent();
        assertEquals(expectedDTStamp.substring(0, 16), dtstamp.substring(0, 16)); // check date, month and time
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.DEFAULT_PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:new group name" + System.lineSeparator() +
                "DTSTART:20160515T080000" + System.lineSeparator() +
                "DTEND:20160515T090000" + System.lineSeparator() +
                "DESCRIPTION:new description" + System.lineSeparator() +
                "SUMMARY:new summary" + System.lineSeparator() +
                dtstamp + System.lineSeparator() + // need to match time exactly
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "LOCATION:new location" + System.lineSeparator() +
                "RECURRENCE-ID:20160515T100000" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
