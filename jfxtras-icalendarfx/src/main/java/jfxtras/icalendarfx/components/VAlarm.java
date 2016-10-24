package jfxtras.icalendarfx.components;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.properties.PropertyType;
import jfxtras.icalendarfx.properties.component.alarm.Action;
import jfxtras.icalendarfx.properties.component.alarm.RepeatCount;
import jfxtras.icalendarfx.properties.component.alarm.Trigger;
import jfxtras.icalendarfx.properties.component.alarm.Action.ActionType;
import jfxtras.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.icalendarfx.properties.component.time.DurationProp;

/** 
 * <p><h2>VALARM</h2>
 * Alarm Component<br>
 * RFC 5545 iCalendar 3.6.6. page 71</p>
 * 
 * <p>The body of the iCalendar object is defined by the following
 * notation:
 *<ul>
 *<li>alarmc
 *  <ul>
 *  <li>"BEGIN" ":" "VALARM" CRLF
 *  <li>(audioprop / dispprop / emailprop)
 *  <li> "END" ":" "VALARM" CRLF
 *  </ul>
 *<li>audioprop
 *  <ul>
 *  <li>The following are REQUIRED, but MUST NOT occur more than once.
 *    <ul>
 *    <li>{@link Action ACTION}
 *    <li>{@link Trigger TRIGGER}
 *    </ul>
 *  <li>The following are OPTIONAL, but MUST NOT occur more than once; but if one occurs, so MUST the other.
 *    <ul>
 *    <li>{@link DurationProp DURATION}
 *    <li>{@link RepeatCount REPEAT}
 *    </ul>
 *  <li>The following are OPTIONAL, but MUST NOT occur more than once.
 *    <ul>
 *    <li>{@link Attachment ATTACH}
 *    </ul>
 *  <li>The following are OPTIONAL, and MAY occur more than once.
 *    <ul>
 *    <li>{@link IANAProperty IANA-PROP}
 *    <li>{@link NonStandardProperty X-PROP}
 *    </ul>
 *  </ul>
 *<li>dispprop
 *  <ul>
 *  <li>The following are REQUIRED, but MUST NOT occur more than once.
 *    <ul>
 *    <li>{@link Action ACTION}
 *    <li>{@link Description DESCRIPTION}
 *    <li>{@link Trigger TRIGGER}
 *    </ul>
 *  <li>The following are OPTIONAL, but MUST NOT occur more than once; but if one occurs, so MUST the other.
 *    <ul>
 *    <li>{@link DurationProp DURATION}
 *    <li>{@link RepeatCount REPEAT}
 *    </ul>
 *  <li>The following are OPTIONAL, and MAY occur more than once.
 *    <ul>
 *    <li>{@link IANAProperty IANA-PROP}
 *    <li>{@link NonStandardProperty X-PROP}
 *    </ul>
 *  </ul>
 *<li>emailprop
 *  <ul>
 *  <li>The following are REQUIRED, but MUST NOT occur more than once.
 *    <ul>
 *    <li>{@link Action ACTION}
 *    <li>{@link Description DESCRIPTION}
 *    <li>{@link Summary SUMMARY}
 *    <li>{@link Trigger TRIGGER}
 *    </ul>
 *  <li>The following are REQUIRED, but MAY occur more than once.
 *    <ul>
 *    <li>{@link Attendee ATTENDEE}
 *    </ul>
 *  <li>The following are OPTIONAL, but MUST NOT occur more than once; but if one occurs, so MUST the other.
 *    <ul>
 *    <li>{@link DurationProp DURATION}
 *    <li>{@link RepeatCount REPEAT}
 *    </ul>
 *  <li>The following are OPTIONAL, and MAY occur more than once.
 *    <ul>
 *    <li>{@link Attachment ATTACH}
 *    <li>{@link NonStandardProperty X-PROP}
 *    </ul>
 *  </ul>
 *</ul>
 * 
 * <p>Provide a grouping of component properties that define an alarm.</p>
 * 
 * <p>Description:  A {@link VAlarm VALARM} calendar component is a grouping of
 *    component properties that is a reminder or alarm for an event or a
 *    to-do.  For example, it may be used to define a reminder for a
 *    pending event or an overdue to-do.</p>
 *
 *    <p>The {@link VAlarm VALARM} calendar component MUST include the {@link Action ACTION} and
 *    {@link Trigger TRIGGER} properties.  The {@link Action ACTION} property further constrains
 *    the {@link VAlarm VALARM} calendar component in the following ways:</p>
 *
 *    <p>When the action is "AUDIO", the alarm can also include one and
 *    only one {@link Attachment ATTACH} property, which MUST point to a sound resource,
 *    which is rendered when the alarm is triggered.</p>
 *
 *    <p>When the action is "DISPLAY", the alarm MUST also include a
 *    {@link Description DESCRIPTION} property, which contains the text to be displayed
 *    when the alarm is triggered.</p>
 *
 *    <p>When the action is "EMAIL", the alarm MUST include a {@link Description DESCRIPTION}
 *    property, which contains the text to be used as the message body,
 *    a {@link Summary SUMMARY} property, which contains the text to be used as the
 *    message subject, and one or more {@link Attendee ATTENDEE} properties, which
 *    contain the email address of attendees to receive the message.  It
 *    can also include one or more {@link Attachment ATTACH} properties, which are
 *    intended to be sent as message attachments.  When the alarm is
 *    triggered, the email message is sent.</p>
 *
 *    <p>The {@link VAlarm VALARM} calendar component MUST only appear within either a
 *    {@link VEvent VEVENT} or {@link VTodo VTODO} calendar component.  {@link VAlarm VALARM} calendar
 *    components cannot be nested.  Multiple mutually independent</p>
 *
 *    <p>{@link VAlarm VALARM} calendar components can be specified for a single
 *    {@link VEvent VEVENT} or {@link VTodo VTODO} calendar component.</p>
 *
 *    <p>The {@link Trigger TRIGGER} property specifies when the alarm will be triggered.
 *    The {@link Trigger TRIGGER} property specifies a duration prior to the start of
 *    an event or a to-do.  The {@link Trigger TRIGGER} edge may be explicitly set to
 *    be relative to the "START" or "END" of the event or to-do with the
 *    "RELATED" parameter of the {@link Trigger TRIGGER} property.  The {@link Trigger TRIGGER}
 *    property value type can alternatively be set to an absolute
 *    calendar date with UTC time.</p>
 *
 *    <p>In an alarm set to trigger on the "START" of an event or to-do,
 *    the {@link DateTimeStart DTSTART} property MUST be present in the associated event or
 *    to-do.  In an alarm in a {@link VEvent VEVENT} calendar component set to
 *    trigger on the "END" of the event, either the {@link DateTimeEnd DTEND} property
 *    MUST be present, or the {@link DateTimeStart DTSTART} and {@link Duration DURATION} properties MUST
 *    both be present.  In an alarm in a {@link VTodo VTODO} calendar component set
 *    to trigger on the "END" of the to-do, either the "DUE" property
 *    MUST be present, or the {@link DateTimeStart DTSTART} and {@link Duration DURATION} properties MUST
 *    both be present.</p>
 *
 *    <p>The alarm can be defined such that it triggers repeatedly.  A
 *    definition of an alarm with a repeating trigger MUST include both
 *    the {@link Duration DURATION} and {@link RepeatCount REPEAT} properties.  The {@link Duration DURATION} property
 *    specifies the delay period, after which the alarm will repeat.
 *    The {@link RepeatCount REPEAT} property specifies the number of additional
 *    repetitions that the alarm will be triggered.  This repetition
 *    count is in addition to the initial triggering of the alarm.  Both
 *    of these properties MUST be present in order to specify a
 *    repeating alarm.  If one of these two properties is absent, then
 *    the alarm will not repeat beyond the initial trigger.</p>
 *
 *    <p>The {@link Action ACTION} property is used within the {@link VAlarm VALARM} calendar
 *    component to specify the type of action invoked when the alarm is
 *    triggered.  The {@link VAlarm VALARM} properties provide enough information for
 *    a specific action to be invoked.  It is typically the
 *    responsibility of a "Calendar User Agent" (CUA) to deliver the
 *    alarm in the specified fashion.  An {@link Action ACTION} property value of
 *    AUDIO specifies an alarm that causes a sound to be played to alert
 *    the user; DISPLAY specifies an alarm that causes a text message to
 *    be displayed to the user; and EMAIL specifies an alarm that causes
 *    an electronic email message to be delivered to one or more email
 *    addresses.</p>
 *
 *    <p>In an AUDIO alarm, if the optional {@link Attachment ATTACH} property is included,
 *    it MUST specify an audio sound resource.  The intention is that
 *    the sound will be played as the alarm effect.  If an {@link Attachment ATTACH}
 *    property is specified that does not refer to a sound resource, or
 *    if the specified sound resource cannot be rendered (because its
 *    format is unsupported, or because it cannot be retrieved), then
 *    the CUA or other entity responsible for playing the sound may
 *    choose a fallback action, such as playing a built-in default
 *    sound, or playing no sound at all.</p>
 *
 *    <p>In a DISPLAY alarm, the intended alarm effect is for the text
 *    value of the {@link Description DESCRIPTION} property to be displayed to the user.</p>
 *
 *    <p>In an EMAIL alarm, the intended alarm effect is for an email
 *    message to be composed and delivered to all the addresses
 *    specified by the {@link Attendee ATTENDEE} properties in the {@link VAlarm VALARM} calendar
 *    component.  The {@link Description DESCRIPTION} property of the {@link VAlarm VALARM} calendar
 *    component MUST be used as the body text of the message, and the
 *    {@link Summary SUMMARY} property MUST be used as the subject text.  Any {@link Attachment ATTACH}
 *    properties in the {@link VAlarm VALARM} calendar component SHOULD be sent as
 *    attachments to the message.</p>
 *
 *       <p>Note: Implementations should carefully consider whether they
 *       accept alarm components from untrusted sources, e.g., when
 *       importing calendar objects from external sources.  One
 *       reasonable policy is to always ignore alarm components that the
 *       calendar user has not set herself, or at least ask for
 *       confirmation in such a case.</p>
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 */
// TODO - add to isValid tests to verify audioprop / dispprop / emailprop
public class VAlarm extends VDescribableBase<VAlarm> implements VDescribable2<VAlarm>,
        VAttendee<VAlarm>, VDuration<VAlarm>
{
    /**
     * <p>Defines the action to be invoked when an alarm is triggered.<br>
     * RFC 5545 iCalendar 3.8.6.1 page 132</p>
     * <p>actionvalue = "AUDIO" / "DISPLAY" / "EMAIL" / iana-token / x-name</p>
     * 
     * <p>Example:
     * <ul>
     * <li>ACTION:DISPLAY
     * </ul>
     * </p>
     */
    public ObjectProperty<Action> actionProperty()
    {
        if (action == null)
        {
            action = new SimpleObjectProperty<>(this, PropertyType.ACTION.toString());
            orderer().registerSortOrderProperty(action);
        }
        return action;
    }
    private ObjectProperty<Action> action;
    public Action getAction() { return actionProperty().get(); }
    public void setAction(String action) { setAction(Action.parse(action)); }
    public void setAction(Action action) { actionProperty().set(action); }
    public void setAction(ActionType action) { setAction(new Action(action)); }
    /**
     * Sets the value of the {@link #actionProperty()}
     * 
     * @return  this class for chaining
     */
    public VAlarm withAction(Action action)
    {
        setAction(action);
        return this;
    }
    /**
     * Sets the value of the {@link #actionProperty()} by creating a new {@link Action} from the {@link ActionType} parameter
     * 
     * @return  this class for chaining
     */    
    public VAlarm withAction(ActionType actionType)
    {
        setAction(actionType);
        return this;
    }
    /** Sets the value of the {@link #actionProperty()} by parsing iCalendar content text
     * @return  this class for chaining */
    public VAlarm withAction(String action)
    {
        setAction(Action.parse(action));
        return this;
    }
    
    /*
     * ATTENDEE: Attendee
     * RFC 5545 iCalendar 3.8.4.1 page 107
     * This property defines an {@link Attendee ATTENDEE} within a calendar component.
     * 
     * Examples:
     * ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com":
     *  mailto:joecool@example.com
     * ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=ACCEPTED;CN=Jane Doe
     *  :mailto:jdoe@example.com
     */
    @Override
    public ObjectProperty<ObservableList<Attendee>> attendeesProperty()
    {
        if (attendees == null)
        {
            attendees = new SimpleObjectProperty<>(this, PropertyType.ATTENDEE.toString());
        }
        return attendees;
    }
    private ObjectProperty<ObservableList<Attendee>> attendees;
    @Override
    public ObservableList<Attendee> getAttendees() { return (attendees == null) ? null : attendees.get(); }
    @Override
    public void setAttendees(ObservableList<Attendee> attendees)
    {
        if (attendees != null)
        {
            orderer().registerSortOrderProperty(attendees);
        } else
        {
            orderer().unregisterSortOrderProperty(this.attendees.get());
        }
        attendeesProperty().set(attendees);
    }
    
    /*
     * DESCRIPTION
     * RFC 5545 iCalendar 3.8.1.5. page 84
     * 
     * This property provides a more complete description of the
     * calendar component than that provided by the {@link Summary SUMMARY} property.
     * 
     * Example:
     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
     *  design.\nHappy Face Conference Room. Phoenix design team
     *  MUST attend this meeting.\nRSVP to team leader.
     *
     * Note: Only VJournal allows multiple instances of DESCRIPTION
     */
    @Override
    public ObjectProperty<Description> descriptionProperty()
    {
        if (description == null)
        {
            description = new SimpleObjectProperty<>(this, PropertyType.DESCRIPTION.toString());
            orderer().registerSortOrderProperty(description);
        }
        return description;
    }
    @Override
    public Description getDescription() { return (description == null) ? null : description.get(); }
    private ObjectProperty<Description> description;
    
    /*
     * DURATION
     * RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Can't be used if DTEND is used.  Must be one or the other.
     * 
     * Example:
     * DURATION:PT15M
     * */
    @Override public ObjectProperty<DurationProp> durationProperty()
    {
        if (duration == null)
        {
            duration = new SimpleObjectProperty<>(this, PropertyType.DURATION.toString());
            orderer().registerSortOrderProperty(duration);
        }
        return duration;
    }
    private ObjectProperty<DurationProp> duration;
    
    /**
     * <p>This property defines the number of times the alarm should
     * be repeated, after the initial trigger.<br>
     * RFC 5545 iCalendar 3.8.6.2 page 133,</p>
     * 
     * <p>If the alarm triggers more than once, then this property MUST be specified
     * along with the {@link Duration DURATION} property.</p>
     * 
     * <p>Example:  The following is an example of this property for an alarm
     * that repeats 4 additional times with a 5-minute delay after the
     * initial triggering of the alarm:<br>
     * 
     * REPEAT:4<br>
     * DURATION:PT5M
     * </p>
     */
    public ObjectProperty<RepeatCount> repeatCountProperty()
    {
        if (repeatCount == null)
        {
            repeatCount = new SimpleObjectProperty<>(this, PropertyType.ACTION.toString());
            orderer().registerSortOrderProperty(repeatCount);
        }
        return repeatCount;
    }
    private ObjectProperty<RepeatCount> repeatCount;
    public RepeatCount getRepeatCount() { return (repeatCount == null) ? null : repeatCount.get(); }
    public void setRepeatCount(RepeatCount repeatCount) { repeatCountProperty().set(repeatCount); }
    public void setRepeatCount(int repeatCount) { setRepeatCount(new RepeatCount(repeatCount)); }
    public void setRepeatCount(String repeatCount) { setRepeatCount(RepeatCount.parse(repeatCount)); }
    /** Sets the value of the {@link #repeatCountProperty()}
     * @return  this class for chaining */
    public VAlarm withRepeatCount(RepeatCount repeatCount) { setRepeatCount(repeatCount); return this; }
    /** Sets the value of the {@link #repeatCountProperty()} by creating new {@link RepeatCount} from int parameter
     * @return  this class for chaining */
    public VAlarm withRepeatCount(int repeatCount) { setRepeatCount(repeatCount); return this; }
    /** Sets the value of the {@link #repeatCountProperty()} by parsing iCalendar content text
     * @return  this class for chaining */
    public VAlarm withRepeatCount(String repeatCount) { setRepeatCount(repeatCount); return this; }
    
    /**
     * <p>This property specifies when an alarm will trigger.<br>
     * RFC 5545 iCalendar 3.8.6.3 page 133</p>
     * 
     * <p>Examples:
     * <ul>
     * A trigger set 15 minutes prior to the start of the event or to-do.
     * <li>TRIGGER:-PT15M<br>
     *  
     * A trigger set five minutes after the end of an event or the due
     * date of a to-do.
     * <li>TRIGGER;RELATED=END:PT5M<br>
     * 
     * A trigger set to an absolute DATE-TIME.
     * <li>TRIGGER;VALUE=DATE-TIME:19980101T050000Z
     * </ul>
     * </p>
     */
    public ObjectProperty<Trigger<?>> triggerProperty()
    {
        if (trigger == null)
        {
            trigger = new SimpleObjectProperty<>(this, PropertyType.ACTION.toString());
            orderer().registerSortOrderProperty(trigger);
        }
        return trigger;
    }
    private ObjectProperty<Trigger<?>> trigger;
    public Trigger<?> getTrigger() { return triggerProperty().get(); }
    public void setTrigger(String trigger) { PropertyType.TRIGGER.parse(this, trigger); }
    public void setTrigger(Trigger<?> trigger) { triggerProperty().set(trigger); }
    public void setTrigger(Duration trigger) { setTrigger(new Trigger<Duration>(trigger)); }
    public void setTrigger(ZonedDateTime trigger) { setTrigger(new Trigger<ZonedDateTime>(trigger)); }
    /** Sets the value of the {@link #triggerProperty()}
     * @return  this class for chaining */
    public VAlarm withTrigger(Trigger<?> trigger) { setTrigger(trigger); return this; }
    /** Sets the value of the {@link #triggerProperty()} by creating new {@link Trigger} from Duration parameter
     * @return  this class for chaining */
    public VAlarm withTrigger(Duration trigger) { setTrigger(trigger); return this; }
    /** Sets the value of the {@link #triggerProperty()} by creating new {@link Trigger} from ZonedDateTime parameter
     * @return  this class for chaining */
    public VAlarm withTrigger(ZonedDateTime trigger) { setTrigger(trigger); return this; }
    /** Sets the value of the {@link #triggerProperty()} by parsing iCalendar content text
     * @return  this class for chaining */
    public VAlarm withTrigger(String trigger) { setTrigger(trigger); return this; }

    
    /*
     * CONSTRUCTORS
     */
    /**
     * Creates a default VAlarm calendar component with no properties
     */
    public VAlarm()
    {
        super();
    }
    
    /**
     * Creates a deep copy of a VAlarm calendar component
     */
    public VAlarm(VAlarm source)
    {
        super(source);
    }

    @Override
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
        if (getAction() == null)
        {
            errors.add("ACTION is not present.  ACTION is REQUIRED and MUST NOT occur more than once");
        }
        if (getTrigger() == null)
        {
            errors.add("TRIGGER is not present.  TRIGGER is REQUIRED and MUST NOT occur more than once");
        }
        boolean isDurationNull = getDuration() == null;
        boolean isRepeatNull = getRepeatCount() == null;
        
        if (isDurationNull && ! isRepeatNull)
        {
            errors.add("DURATION is present but REPEAT is not present.  DURATION and REPEAT are both OPTIONAL, and MUST NOT occur more than once each, but if one occurs, so MUST the other.");
        }
        if (! isDurationNull && isRepeatNull)
        {
            errors.add("REPEAT is present but DURATION is not present.  DURATION and REPEAT are both OPTIONAL, and MUST NOT occur more than once each, but if one occurs, so MUST the other.");
        }
        return Collections.unmodifiableList(errors);
    }
    
    @Override
    public boolean isValid()
    {
        boolean isActionPresent = getAction() != null;
        boolean isTriggerPresent = getTrigger() != null;
        boolean isDurationNull = getDuration() == null;
        boolean isRepeatNull = getRepeatCount() == null;
        boolean isBothNull = isDurationNull && isRepeatNull;
        boolean isNeitherNull = ! isDurationNull && ! isRepeatNull;
        boolean isDurationAndRepeatOK = isBothNull || isNeitherNull;
        return isActionPresent && isTriggerPresent && isDurationAndRepeatOK;
    }

    /**
     *  Creates a new VAlarm calendar component by parsing a String of iCalendar content lines
     *
     * @param contentLines  the text to parse, not null
     * @return  the parsed VAlarm
     */
    public static VAlarm parse(String contentLines)
    {
        VAlarm component = new VAlarm();
        component.parseContent(contentLines);
        return component;
    }
}
