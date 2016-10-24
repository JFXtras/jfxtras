package jfxtras.scene.control.agenda.icalendar.editors;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.Temporal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javafx.util.Pair;
import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.properties.PropertyType;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.scene.control.agenda.icalendar.editors.deleters.Deleter;
import jfxtras.scene.control.agenda.icalendar.editors.revisors.Reviser;

/**
 * Options available when editing or deleting a repeatable appointment.
 * 
 */
public enum ChangeDialogOption
{
    ONE                                     // individual instance
  , ALL                                     // entire series
  , ALL_IGNORE_RECURRENCES                  // entire series with special recurrences (components with RECURRENCEID) ignored
  , THIS_AND_FUTURE                         // selected instance and all in the future
  , THIS_AND_FUTURE_IGNORE_RECURRENCES      // selected instance and all in the future, with special recurrences (components with RECURRENCEID) ignored
  , CANCEL;                                 // do nothing
    
    /**
     * Produce the map of change dialog options and the date range the option affects
     * 
     * @param vComponentOriginal  clone of unedited {@link VDisplayable}
     * @param vComponentEdited  edited {@link VDisplayable} (only descriptive properties are edited, date/time properties are not edited)
     * @param startRecurrence  start date/time of selected recurrence  
     * @param changedProperties  list of PropertyType that are changed between vComponentOriginal and vComponentEdited
     * @return  Map with key as available {@link ChangeDialogOption} and value start, end date/time pair to be affected by change
     * 
     * @see Reviser
     */
   public static <U extends VDisplayable<U>> Map<ChangeDialogOption, Pair<Temporal,Temporal>> makeDialogChoices(
            U vComponentOriginal,
            U vComponentEdited,
            Temporal startRecurrence,
            List<PropertyType> changedProperties)
   {
       Map<ChangeDialogOption, Pair<Temporal,Temporal>> choices = new LinkedHashMap<>();
       if (! changedProperties.contains(PropertyType.RECURRENCE_RULE))
       {
           choices.put(ChangeDialogOption.ONE, new Pair<Temporal,Temporal>(startRecurrence, startRecurrence));
       }
        
       Temporal lastRecurrence = vComponentEdited.lastRecurrence();
       // TODO - Gradle requires variable s.  I can't inline it or I get a compile error.
       Stream<Temporal> s = vComponentEdited.streamRecurrences();
       Temporal firstRecurrence = s.findFirst().get();
       boolean isLastRecurrence = (lastRecurrence == null) ? false : startRecurrence.equals(lastRecurrence);
       boolean isAfterLastRecurrence = (lastRecurrence == null) ? false : DateTimeUtilities.isAfter(startRecurrence, lastRecurrence);
       boolean isFirstRecurrence = startRecurrence.equals(firstRecurrence);
       boolean isDTStartChanged = ! vComponentEdited.getDateTimeStart().equals(vComponentOriginal.getDateTimeStart());
       boolean isFirstOrLastChanged = ! (isLastRecurrence || isFirstRecurrence);
       if (! isAfterLastRecurrence)
       {
           if (isFirstOrLastChanged || isDTStartChanged)
           {
               Temporal start = (startRecurrence == null) ? vComponentEdited.getDateTimeStart().getValue() : startRecurrence; // set initial start
               Period dateTimeStartShift = Period.between(LocalDate.from(vComponentEdited.getDateTimeStart().getValue()),
                       LocalDate.from(vComponentOriginal.getDateTimeStart().getValue()));
               start = start.plus(dateTimeStartShift);
               choices.put(ChangeDialogOption.THIS_AND_FUTURE, new Pair<Temporal,Temporal>(start, lastRecurrence));
           }
       }
       choices.put(ChangeDialogOption.ALL, new Pair<Temporal,Temporal>(vComponentEdited.getDateTimeStart().getValue(), lastRecurrence));
       return choices;
    }

   /**
    * Produce the map of change dialog options and the date range the option affects
    * 
    * @param vComponent   {@link VDisplayable} to be deleted
    * @param startOriginalRecurrence  start date/time of selected recurrence
    * @return  Map with key as available {@link ChangeDialogOption} and value start, end date/time pair to be affected by change
    * 
    * @see Deleter
    */
    public static <U extends VDisplayable<U>> Map<ChangeDialogOption, Pair<Temporal, Temporal>> makeDialogChoices(
            VDisplayable<?> vComponent,
            Temporal startOriginalRecurrence)
    {
        Map<ChangeDialogOption, Pair<Temporal,Temporal>> choices = new LinkedHashMap<>();
        final Temporal lastRecurrence;
        
        final long rdates;
        if (vComponent.getRecurrenceDates() != null)
        {
            rdates = vComponent.getRecurrenceDates().stream().flatMap(r -> r.getValue().stream()).count();
        } else
        {
            rdates = 0;
        }
        if (vComponent.getRecurrenceRule() != null || rdates > 1)
        {
            choices.put(ChangeDialogOption.ONE, new Pair<Temporal,Temporal>(startOriginalRecurrence, startOriginalRecurrence));
            lastRecurrence = vComponent.lastRecurrence();
            boolean isLastRecurrence = (lastRecurrence == null) ? false : startOriginalRecurrence.equals(lastRecurrence);
            if (! isLastRecurrence)
            {
                Temporal start = (startOriginalRecurrence == null) ? vComponent.getDateTimeStart().getValue() : startOriginalRecurrence; // set initial start
                Period dateTimeStartShift = Period.between(LocalDate.from(vComponent.getDateTimeStart().getValue()),
                        LocalDate.from(vComponent.getDateTimeStart().getValue()));
                start = start.plus(dateTimeStartShift);
                choices.put(ChangeDialogOption.THIS_AND_FUTURE, new Pair<Temporal,Temporal>(start, lastRecurrence));            
            }
        } else
        {
            lastRecurrence = vComponent.getDateTimeStart().getValue();
        }
        choices.put(ChangeDialogOption.ALL, new Pair<Temporal,Temporal>(vComponent.getDateTimeStart().getValue(), lastRecurrence));
        return choices;
    }        
}
