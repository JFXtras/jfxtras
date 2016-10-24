package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.Settings;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.EditVJournalTabPane;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.scene.control.agenda.icalendar.test.agenda.AgendaTestAbstract;
import jfxtras.test.TestUtil;

public class VJournalMakeiTIPTest extends VEventPopupTestBase
{
    private EditVJournalTabPane editComponentPopup;
    
    @Override
    public Parent getRootNode()
    {
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.ICalendarAgenda", Locale.getDefault());
        Settings.setup(resources);
        editComponentPopup = new EditVJournalTabPane();
        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        editComponentPopup.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
        return editComponentPopup;
    }
    
    @Test
    public void canDisplayPopupWithVTodo()
    {
        VJournal vjournal = new VJournal()
                .withDateTimeStart("20160518T110000")
                .withSummary("test journal")
                .withDateTimeStamp("20160518T232502Z")
                .withUniqueIdentifier("20160518T232502-0@jfxtras.org");
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vjournal,
                    LocalDateTime.of(2016, 5, 18, 11, 0),  // start of edited instance
                    LocalDateTime.of(2016, 5, 18, 12, 0),  // end of edited instance (calculated from duration)
                    AgendaTestAbstract.CATEGORIES);
        });

        TextField summary = find("#summaryTextField");
        assertEquals("test journal", summary.getText());

        LocalDateTimeTextField start = find("#startDateTimeTextField");
        assertEquals(LocalDateTime.of(2016, 5, 18, 11, 0), start.getLocalDateTime());
    }
}
