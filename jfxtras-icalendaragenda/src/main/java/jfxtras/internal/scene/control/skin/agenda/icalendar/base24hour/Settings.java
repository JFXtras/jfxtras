package jfxtras.internal.scene.control.skin.agenda.icalendar.base24hour;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import jfxtras.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.scene.control.agenda.icalendar.editors.ChangeDialogOption;

/**
 * This class contains text values for controls.  It contains default values and, when the {@link #setup(ResourceBundle)}
 * method is executed, replaces those default values with those from a ResourceBundle
 * 
 * @author David Bal
 *
 */
public final class Settings
{
    private Settings() {}
    
    public static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EEE, MMMM d, yyyy");
    public static DateTimeFormatter DATE_FORMAT_AGENDA_DATEONLY = DateTimeFormatter.ofPattern("MMM d, yyyy");
    public static DateTimeFormatter DATE_FORMAT_AGENDA_EXCEPTION = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");
    public static DateTimeFormatter DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY = DateTimeFormatter.ofPattern("EEE, MMMM d, yyyy");
    public static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("EEE, MMMM d, yyyy h:mm a");
    public static DateTimeFormatter DATE_FORMAT_AGENDA_MONTHDAY = DateTimeFormatter.ofPattern("MMMM d"); // for yearly summary
    public static DateTimeFormatter TIME_FORMAT_END = DateTimeFormatter.ofPattern("hh:mm a");

    public static final Map<FrequencyType, String> REPEAT_FREQUENCIES = defaultRepeatFrequenciesMap();
    private static Map<FrequencyType, String> defaultRepeatFrequenciesMap()
    {
        Map<FrequencyType, String> map = new HashMap<>();
        map.put(FrequencyType.DAILY, "Daily");
        map.put(FrequencyType.WEEKLY, "Weekly");
        map.put(FrequencyType.MONTHLY, "Monthly");
        map.put(FrequencyType.YEARLY, "Yearly");
        return map;
    }
    public static final Map<FrequencyType, String> REPEAT_FREQUENCIES_PLURAL = defaultRepeatFrequenciesPluralMap();
    private static Map<FrequencyType, String> defaultRepeatFrequenciesPluralMap()
    {
        Map<FrequencyType, String> map = new HashMap<>();
        map.put(FrequencyType.DAILY, "days");
        map.put(FrequencyType.WEEKLY, "weeks");
        map.put(FrequencyType.MONTHLY, "months");
        map.put(FrequencyType.YEARLY, "years");
        return map;
    }
    public static final Map<FrequencyType, String> REPEAT_FREQUENCIES_SINGULAR = defaultRepeatFrequenciesSingluarMap();
    private static Map<FrequencyType, String> defaultRepeatFrequenciesSingluarMap()
    {
        Map<FrequencyType, String> map = new HashMap<>();
        map.put(FrequencyType.DAILY, "day");
        map.put(FrequencyType.WEEKLY, "week");
        map.put(FrequencyType.MONTHLY, "month");
        map.put(FrequencyType.YEARLY, "year");
        return map;
    }
    public static final Map<ChangeDialogOption, String> REPEAT_CHANGE_CHOICES = defaultRepeatChangeChoicesMap();
    private static Map<ChangeDialogOption, String> defaultRepeatChangeChoicesMap()
    {
        Map<ChangeDialogOption, String> map = new HashMap<>();
        map.put(ChangeDialogOption.ONE, "This event only");
        map.put(ChangeDialogOption.ALL, "All events");
//        map.put(ChangeDialogOption.SEGMENT, "This segment of events");
        map.put(ChangeDialogOption.THIS_AND_FUTURE, "This and future events");
//        map.put(ChangeDialogOption.THIS_AND_FUTURE_SEGMENT, "This and future events in its segment");
//        map.put(ChangeDialogOption.THIS_AND_FUTURE_ALL, "This and future events in whole series");
        return map;
    }

    public static final Map<Integer, String> ORDINALS = defaultOrdinalsMap();
    private static Map<Integer, String> defaultOrdinalsMap()
    {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "first");
        map.put(2, "second");
        map.put(3, "third");
        map.put(4, "fourth");
        map.put(5, "fifth");
        return map;
    }
    public static final Map<DayOfWeek, String> DAYS_OF_WEEK_MAP = defaultDaysOfWeekMap();
    private static Map<DayOfWeek, String> defaultDaysOfWeekMap()
    {
        Map<DayOfWeek, String> map = new HashMap<>();
        map.put(DayOfWeek.MONDAY, "Monday");
        map.put(DayOfWeek.TUESDAY, "Tuesday");
        map.put(DayOfWeek.WEDNESDAY, "Wednesday");
        map.put(DayOfWeek.THURSDAY, "Thursday");
        map.put(DayOfWeek.FRIDAY, "Friday");
        map.put(DayOfWeek.SATURDAY, "Saturday");
        map.put(DayOfWeek.SUNDAY, "Sunday");
        return map;
    }
    
    public static ResourceBundle resources;
   
    public static void setup(ResourceBundle resourcesIn)
    {
        resources = resourcesIn;
        
        DATE_FORMAT = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format"));
        DATE_FORMAT_AGENDA_EXCEPTION = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.exception"));
        DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY = DateTimeFormatter.ofPattern(resources.getString("date.format.agenda.exception.dateonly"));
        DATE_FORMAT_AGENDA_DATEONLY = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.dateonly"));
        DATE_FORMAT_AGENDA_MONTHDAY = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.monthday"));
        DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(resourcesIn.getString("date.time.format"));
        TIME_FORMAT_END = DateTimeFormatter.ofPattern(resourcesIn.getString("time.format"));
        
        REPEAT_FREQUENCIES.put(FrequencyType.DAILY, resourcesIn.getString("daily"));
        REPEAT_FREQUENCIES.put(FrequencyType.WEEKLY, resourcesIn.getString("weekly"));
        REPEAT_FREQUENCIES.put(FrequencyType.MONTHLY, resourcesIn.getString("monthly"));
        REPEAT_FREQUENCIES.put(FrequencyType.YEARLY, resourcesIn.getString("yearly"));
        
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.DAILY, resourcesIn.getString("days"));
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.WEEKLY, resourcesIn.getString("weeks"));
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.MONTHLY, resourcesIn.getString("months"));
        REPEAT_FREQUENCIES_PLURAL.put(FrequencyType.YEARLY, resourcesIn.getString("years"));
        
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.DAILY, resourcesIn.getString("day"));
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.WEEKLY, resourcesIn.getString("week"));
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.MONTHLY, resourcesIn.getString("month"));
        REPEAT_FREQUENCIES_SINGULAR.put(FrequencyType.YEARLY, resourcesIn.getString("year"));

        REPEAT_CHANGE_CHOICES.put(ChangeDialogOption.ONE, resourcesIn.getString("dialog.one"));
        REPEAT_CHANGE_CHOICES.put(ChangeDialogOption.ALL, resourcesIn.getString("dialog.all"));
        REPEAT_CHANGE_CHOICES.put(ChangeDialogOption.THIS_AND_FUTURE, resourcesIn.getString("dialog.future"));
//        REPEAT_CHANGE_CHOICES.put(ChangeDialogOption.SEGMENT, resources.getString("dialog.repeat.change.segment"));
//        REPEAT_CHANGE_CHOICES.put(ChangeDialogOption.THIS_AND_FUTURE_SEGMENT, resources.getString("dialog.repeat.change.future.segment"));
//        REPEAT_CHANGE_CHOICES.put(ChangeDialogOption.THIS_AND_FUTURE_ALL, resources.getString("dialog.repeat.change.future.all"));
        
        ORDINALS.put(1, resourcesIn.getString("first"));
        ORDINALS.put(2, resourcesIn.getString("second"));
        ORDINALS.put(3, resourcesIn.getString("third"));
        ORDINALS.put(4, resourcesIn.getString("fourth"));
        ORDINALS.put(5, resourcesIn.getString("fifth"));

        DAYS_OF_WEEK_MAP.put(DayOfWeek.MONDAY, resourcesIn.getString("monday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.TUESDAY, resourcesIn.getString("tuesday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.WEDNESDAY, resourcesIn.getString("wednesday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.THURSDAY, resourcesIn.getString("thursday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.FRIDAY, resourcesIn.getString("friday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.SATURDAY, resourcesIn.getString("saturday"));
        DAYS_OF_WEEK_MAP.put(DayOfWeek.SUNDAY, resourcesIn.getString("sunday"));
    }

}
