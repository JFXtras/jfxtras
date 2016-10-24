package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;
import jfxtras.scene.control.agenda.icalendar.test.ICalendarStaticComponents;
import jfxtras.test.TestUtil;

public class ChangeDialogOptionsTest extends VEventPopupTestBase
{
    @Test
    public void describleChangeProducesThreeOptions()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),
                    LocalDateTime.of(2016, 5, 15, 11, 0),
                    categories());
        });

        // Get properties
        TextField summaryTextField = find("#summaryTextField");
        
        // Make changes
        summaryTextField.setText("new summary");
        click("#saveComponentButton");
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        List<ChangeDialogOption> expectedItems = Arrays.asList(ChangeDialogOption.ONE, ChangeDialogOption.THIS_AND_FUTURE, ChangeDialogOption.ALL);
        assertEquals(expectedItems , comboBox.getItems());
        click("#changeDialogCancelButton");
        click("#cancelComponentButton");
    }
    
    @Test
    public void repeatChangeProducesTwoOptions()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),
                    LocalDateTime.of(2016, 5, 15, 11, 0),
                    categories());
        });

        click("#recurrenceRuleTab");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.WEEKLY));
                
        // Make changes
        click("#saveRepeatButton");
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        List<ChangeDialogOption> expectedItems = Arrays.asList(ChangeDialogOption.THIS_AND_FUTURE, ChangeDialogOption.ALL);
        assertEquals(expectedItems , comboBox.getItems());
        click("#changeDialogCancelButton");
        click("#cancelRepeatButton");
    }
}
