package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.scene.control.TextField;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.test.agenda.AgendaTestAbstract;
import jfxtras.test.TestUtil;

public class MiscPopupTest extends VEventPopupTestBase
{
    @Test // simple press save
    public void canSaveWithNoEdit()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),  // start of edited instance
                    LocalDateTime.of(2016, 5, 15, 11, 0),  // end of edited instance
                    AgendaTestAbstract.CATEGORIES);
        });
        
        // click save button (no changes so no dialog)
        click("#saveComponentButton");
        
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals("", iTIPMessage);
    }
    
    @Test
    public void canCancelEdit()
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

        // edit properties
        TextField summaryTextField = find("#summaryTextField");
        summaryTextField.setText("new summary");
        LocalDateTimeTextField startDateTimeTextField = find("#startDateTimeTextField");
        startDateTimeTextField.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 10, 30));

        // cancel changes
        click("#saveComponentButton");
        click("#changeDialogCancelButton");
        click("#cancelComponentButton");
        
        String iTIPMessage = getEditComponentPopup().iTIPMessagesProperty().get().stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals("", iTIPMessage);
    }
}
