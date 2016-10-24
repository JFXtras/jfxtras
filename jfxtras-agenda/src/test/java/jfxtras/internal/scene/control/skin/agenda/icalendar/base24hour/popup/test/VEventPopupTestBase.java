package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.test;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.Parent;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.Settings;
import jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup.EditVEventTabPane;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import jfxtras.test.JFXtrasGuiTest;

/**
 * Tests the edit popup without an Agenda instance.
 * 
 * @author David Bal
 *
 */
public class VEventPopupTestBase extends JFXtrasGuiTest
{
    public EditVEventTabPane getEditComponentPopup()
    {
        return editComponentPopup;
    }
    public void setEditComponentPopup(EditVEventTabPane editComponentPopup)
    {
        this.editComponentPopup = editComponentPopup;
    }
    private EditVEventTabPane editComponentPopup;
    
    public List<String> categories()
    {
        return IntStream.range(0, 24)
                .mapToObj(i -> new String("group" + (i < 10 ? "0" : "") + i))
                .collect(Collectors.toList());
    }
    
    @Override
    public Parent getRootNode()
    {
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.ICalendarAgenda", Locale.getDefault());
        Settings.setup(resources);
        setEditComponentPopup(new EditVEventTabPane());
        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        getEditComponentPopup().getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
        return getEditComponentPopup();
    }
}
