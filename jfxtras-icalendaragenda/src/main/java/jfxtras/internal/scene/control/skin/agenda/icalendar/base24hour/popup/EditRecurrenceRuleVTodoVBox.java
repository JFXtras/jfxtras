package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour.popup;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.Temporal;

import net.balsoftware.icalendar.components.VTodo;
import net.balsoftware.icalendar.properties.component.recurrence.RecurrenceRule;

/**
 * VBox containing controls to edit the {@link RecurrenceRule} in a {@link VTodo}.
 * 
 * @author David Bal
 */
public class EditRecurrenceRuleVTodoVBox extends EditRecurrenceRuleVBox<VTodo>
{
    @Override
    void synchStartDatePickerAndComponent(LocalDate oldValue, LocalDate newValue)
    {
        super.synchStartDatePickerAndComponent(oldValue, newValue);
        Period shift = Period.between(oldValue, newValue);
        Temporal newEnd = vComponent.getDateTimeDue().getValue().plus(shift);
        vComponent.setDateTimeDue(newEnd);
    }
}
