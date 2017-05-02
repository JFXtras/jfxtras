package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import javafx.scene.Scene;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import net.balsoftware.icalendar.components.VDisplayable;
import net.balsoftware.icalendar.properties.component.recurrence.RecurrenceRule;

/** 
 * Base Scene for editing descriptive properties and a {@link RecurrenceRule} in a {@link VDisplayable}.
 * A {@link EditDisplayableTabPane} passed to the constructor is the root node of the scene graph<br>
 * 
 * @author David Bal
 */
public abstract class EditDisplayableScene extends Scene
{
    public EditDisplayableScene(EditDisplayableTabPane<?,?> parent)
    {
        super(parent);
        ICalendarAgenda.class.getResource(ICalendarAgenda.class.getSimpleName() + ".css").toExternalForm();
        getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET);
        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
    }

    public EditDisplayableTabPane<?,?> getEditDisplayableTabPane()
    {
        return (EditDisplayableTabPane<?, ?>) getRoot();
    }
}
