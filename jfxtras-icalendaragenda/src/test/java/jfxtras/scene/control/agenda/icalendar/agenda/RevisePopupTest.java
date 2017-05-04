package jfxtras.scene.control.agenda.icalendar.agenda;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import jfxtras.scene.control.agenda.icalendar.ICalendarStaticComponents;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.test.TestUtil;
import net.balsoftware.icalendar.components.VEvent;
import net.balsoftware.icalendar.components.VPrimary;
import net.balsoftware.icalendar.properties.component.change.DateTimeStamp;
import net.balsoftware.icalendar.properties.component.recurrence.RecurrenceRule;

public class RevisePopupTest extends AgendaTestAbstract
{
    @Test
    public void canEditOne()
    {
        // create appointment
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().addChild(ICalendarStaticComponents.getDaily1());
            agenda.refresh();
        });
        
        // drag to new location
        moveTo("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);

        // edit property
        TextField summaryTextField = find("#summaryTextField");
        summaryTextField.setText("new summary");
        TestUtil.sleep(1000);
        // save changes to THIS AND FUTURE
        clickOn("#saveComponentButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        System.out.println("button:" + c.getSelectionModel().getSelectedItem().getClass());
//        TestUtil.sleep(1000);
        TestUtil.runThenWaitForPaintPulse( () -> 
        {
        	System.out.println("c.getSelectionModel():" + c.getSelectionModel());
        	c.getSelectionModel().select(ChangeDialogOption.ONE);
//        	c.getSelectionModel().select();
        	System.out.println("changed:" + c.getSelectionModel().getSelectedItem());
//        	c.getSelectionModel().select(0);
        });
//        TestUtil.sleep(3000);
      System.out.println("button2:");
//        Node b = find("#changeDialogOkButton");
        clickOn("#changeDialogOkButton");
       
        // check appointment
        assertEquals(6, agenda.appointments().size());
        List<String> summaries = agenda.appointments().stream()
                .sorted((a1, a2) -> a1.getStartLocalDateTime().compareTo(a2.getStartLocalDateTime()))
                .map(a -> a.getSummary())
                .collect(Collectors.toList());
        List<String> expectedSummaries = Arrays.asList(
                "Daily1 Summary",
                "Daily1 Summary",
                "new summary",
                "Daily1 Summary",
                "Daily1 Summary",
                "Daily1 Summary"
                );
        assertEquals(expectedSummaries, summaries);
        assertEquals(2, agenda.getVCalendar().getVEvents().size());
        agenda.getVCalendar().getVEvents().sort(VPrimary.DTSTART_COMPARATOR);
        VEvent vComponentOriginal = agenda.getVCalendar().getVEvents().get(0);
        System.out.println(vComponentOriginal);
        VEvent vComponentIndividual = agenda.getVCalendar().getVEvents().get(1);

        assertEquals(ICalendarStaticComponents.getDaily1(), vComponentOriginal);
        VEvent expectedVComponentIndividual = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart("20151111T100000")
                .withDateTimeEnd("20151111T110000")
                .withDateTimeStamp(new DateTimeStamp(vComponentIndividual.getDateTimeStamp()))
                .withRecurrenceId("20151111T100000")
                .withRecurrenceRule((RecurrenceRule) null)
                .withSummary("new summary")
                .withSequence(1);
        assertEquals(expectedVComponentIndividual, vComponentIndividual);
    }
}
