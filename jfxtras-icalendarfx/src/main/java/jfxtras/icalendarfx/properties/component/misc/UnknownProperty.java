package jfxtras.icalendarfx.properties.component.misc;

import java.net.URI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.icalendarfx.parameters.AlarmTriggerRelationship;
import jfxtras.icalendarfx.parameters.AlternateText;
import jfxtras.icalendarfx.parameters.Encoding;
import jfxtras.icalendarfx.parameters.FormatType;
import jfxtras.icalendarfx.parameters.FreeBusyType;
import jfxtras.icalendarfx.parameters.ParameterType;
import jfxtras.icalendarfx.parameters.Range;
import jfxtras.icalendarfx.parameters.Relationship;
import jfxtras.icalendarfx.parameters.TimeZoneIdentifierParameter;
import jfxtras.icalendarfx.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;
import jfxtras.icalendarfx.parameters.Encoding.EncodingType;
import jfxtras.icalendarfx.parameters.FreeBusyType.FreeBusyTypeEnum;
import jfxtras.icalendarfx.parameters.Range.RangeType;
import jfxtras.icalendarfx.parameters.Relationship.RelationshipType;
import jfxtras.icalendarfx.properties.PropAlarmTrigger;
import jfxtras.icalendarfx.properties.PropAltText;
import jfxtras.icalendarfx.properties.PropAttachment;
import jfxtras.icalendarfx.properties.PropAttendee;
import jfxtras.icalendarfx.properties.PropDateTime;
import jfxtras.icalendarfx.properties.PropFreeBusy;
import jfxtras.icalendarfx.properties.PropRecurrenceID;
import jfxtras.icalendarfx.properties.PropRelationship;
import jfxtras.icalendarfx.properties.component.relationship.PropertyBaseAttendee;

/**
 * Abstract class for non-standard properties and IANA properties
 * 
 * contains all parameters
 * 
 * @author David Bal
 *
 */
public abstract class UnknownProperty<T,U> extends PropertyBaseAttendee<T,U> implements PropAttendee<T>, PropAltText<T>,
    PropAttachment<T>, PropFreeBusy<T>, PropRecurrenceID<T>, PropDateTime<T>, PropAlarmTrigger<T>, PropRelationship<T>
{
    private String propertyName;
    // TODO - MOVE PROPERTY NAME HERE
    // Override getter and add setter.  Other properties have constant property name.
    
    /**
     * ALTREP : Alternate Text Representation
     * To specify an alternate text representation for the property value.
     * 
     * Example:
     * DESCRIPTION;ALTREP="CID:part3.msg.970415T083000@example.com":
     *  Project XYZ Review Meeting will include the following agenda
     *   items: (a) Market Overview\, (b) Finances\, (c) Project Man
     *  agement
     *
     *The "ALTREP" property parameter value might point to a "text/html"
     *content portion.
     *
     * Content-Type:text/html
     * Content-Id:<part3.msg.970415T083000@example.com>
     *
     * <html>
     *   <head>
     *    <title></title>
     *   </head>
     *   <body>
     *     <p>
     *       <b>Project XYZ Review Meeting</b> will include
     *       the following agenda items:
     *       <ol>
     *         <li>Market Overview</li>
     *         <li>Finances</li>
     *         <li>Project Management</li>
     *       </ol>
     *     </p>
     *   </body>
     * </html>
     */
    @Override
    public AlternateText getAlternateText() { return (alternateText == null) ? null : alternateText.get(); }
    @Override
    public ObjectProperty<AlternateText> alternateTextProperty()
    {
        if (alternateText == null)
        {
            alternateText = new SimpleObjectProperty<>(this, ParameterType.ALTERNATE_TEXT_REPRESENTATION.toString());
            orderer().registerSortOrderProperty(alternateText);
        }
        return alternateText;
    }
    private ObjectProperty<AlternateText> alternateText;
    @Override
    public void setAlternateText(AlternateText alternateText) { alternateTextProperty().set(alternateText); }
    public void setAlternateText(String value) { setAlternateText(AlternateText.parse(value)); }
    public U withAlternateText(AlternateText altrep) { setAlternateText(altrep); return (U) this; }
    public U withAlternateText(URI value) { setAlternateText(new AlternateText(value)); return (U) this; }
    public U withAlternateText(String content) { setAlternateText(content); return (U) this; }
    
    /**
     * ENCODING: Incline Encoding
     * RFC 5545, 3.2.7, page 18
     * 
     * Specify an alternate inline encoding for the property value.
     * Values can be "8BIT" text encoding defined in [RFC2045]
     *               "BASE64" binary encoding format defined in [RFC4648]
     *
     * If the value type parameter is ";VALUE=BINARY", then the inline
     * encoding parameter MUST be specified with the value" ;ENCODING=BASE64".
     */
    @Override
    public Encoding getEncoding() { return (encoding == null) ? null : encoding.get(); }
    @Override
    public ObjectProperty<Encoding> encodingProperty()
    {
        if (encoding == null)
        {
            encoding = new SimpleObjectProperty<>(this, ParameterType.INLINE_ENCODING.toString());
            orderer().registerSortOrderProperty(encoding);
        }
        return encoding;
    }
    private ObjectProperty<Encoding> encoding;
    @Override
    public void setEncoding(Encoding encoding)
    {
        if (encoding.getValue() != EncodingType.BASE64)
        {
            throw new IllegalArgumentException("Attachment property only allows ENCODING to be set to" + EncodingType.BASE64);
        }
        encodingProperty().set(encoding);
    }
    public U withEncoding(Encoding encoding) { setEncoding(encoding); return (U) this; }
    public U withEncoding(EncodingType encoding) { setEncoding(new Encoding(encoding)); return (U) this; }

    /**
     * FBTYPE: Incline Free/Busy Time Type
     * RFC 5545, 3.2.9, page 20
     * 
     * To specify the free or busy time type.
     * 
     * Values can be = "FBTYPE" "=" ("FREE" / "BUSY" / "BUSY-UNAVAILABLE" / "BUSY-TENTATIVE"
     */
    @Override
    public FreeBusyType getFreeBusyType() { return (freeBusyType == null) ? null : freeBusyType.get(); }
    @Override
    public ObjectProperty<FreeBusyType> freeBusyTypeProperty()
    {
        if (freeBusyType == null)
        {
            freeBusyType = new SimpleObjectProperty<>(this, ParameterType.INLINE_ENCODING.toString());
            orderer().registerSortOrderProperty(freeBusyType);
        }
        return freeBusyType;
    }
    private ObjectProperty<FreeBusyType> freeBusyType;
    @Override
    public void setFreeBusyType(FreeBusyType freeBusyType) { freeBusyTypeProperty().set(freeBusyType); }
    public void setFreeBusyType(FreeBusyTypeEnum type) { setFreeBusyType(new FreeBusyType(type)); }
    public U withFreeBusyType(FreeBusyType freeBusyType) { setFreeBusyType(freeBusyType); return (U)this; }
    public U withFreeBusyType(FreeBusyTypeEnum type) { setFreeBusyType(type); return (U) this; }
    public U withFreeBusyType(String freeBusyType) { setFreeBusyType(FreeBusyType.parse(freeBusyType)); return (U) this; }

    /**
     * FMTTYPE: Format type parameter
     * RFC 5545, 3.2.8, page 19
     * specify the content type of a referenced object.
     */
    @Override
    public FormatType getFormatType() { return (formatType == null) ? null : formatType.get(); }
    @Override
    public ObjectProperty<FormatType> formatTypeProperty()
    {
        if (formatType == null)
        {
            formatType = new SimpleObjectProperty<>(this, ParameterType.FORMAT_TYPE.toString());
            orderer().registerSortOrderProperty(formatType);
        }
        return formatType;
    }
    private ObjectProperty<FormatType> formatType;
    @Override
    public void setFormatType(FormatType formatType) { formatTypeProperty().set(formatType); }
    public U withFormatType(FormatType format) { setFormatType(format); return (U) this; }
    public U withFormatType(String format) { setFormatType(FormatType.parse(format)); return (U) this; }

    /**
     * RANGE
     * Recurrence Identifier Range
     * RFC 5545, 3.2.13, page 23
     * 
     * To specify the effective range of recurrence instances from
     *  the instance specified by the recurrence identifier specified by
     *  the property.
     * 
     * Example:
     * RECURRENCE-ID;RANGE=THISANDFUTURE:19980401T133000Z
     * 
     * @author David Bal
     *
     */
    @Override
    public Range getRange() { return (range == null) ? null : range.get(); }
    @Override
    public ObjectProperty<Range> rangeProperty()
    {
        if (range == null)
        {
            range = new SimpleObjectProperty<>(this, ParameterType.RECURRENCE_IDENTIFIER_RANGE.toString());
            orderer().registerSortOrderProperty(range);
        }
        return range;
    }
    private ObjectProperty<Range> range;
    @Override
    public void setRange(Range range) { rangeProperty().set(range); }
    public void setRange(String value) { setRange(new Range(value)); }
    public U withRange(Range altrep) { setRange(altrep); return (U) this; }
    public U withRange(RangeType value) { setRange(new Range(value)); return (U) this; }
    public U withRange(String content) { setRange(content); return (U) this; }

    /**
     * RELATED: Alarm Trigger Relationship
     * RFC 5545, 3.2.14, page 24
     * To specify the relationship of the alarm trigger with
     * respect to the start or end of the calendar component.
     */
    @Override
    public AlarmTriggerRelationship getAlarmTrigger() { return (alarmTrigger == null) ? null : alarmTrigger.get(); }
    @Override
    public ObjectProperty<AlarmTriggerRelationship> AlarmTriggerProperty()
    {
        if (alarmTrigger == null)
        {
            alarmTrigger = new SimpleObjectProperty<>(this, ParameterType.FORMAT_TYPE.toString());
            orderer().registerSortOrderProperty(alarmTrigger);
      }
        return alarmTrigger;
    }
    private ObjectProperty<AlarmTriggerRelationship> alarmTrigger;
    @Override
    public void setAlarmTrigger(AlarmTriggerRelationship AlarmTrigger) { AlarmTriggerProperty().set(AlarmTrigger); }
    public void setAlarmTrigger(AlarmTriggerRelationshipType type) { setAlarmTrigger(new AlarmTriggerRelationship(type)); } 
    public U withAlarmTrigger(AlarmTriggerRelationship format) { setAlarmTrigger(format); return (U) this; }
    public U withAlarmTrigger(AlarmTriggerRelationshipType type) { setAlarmTrigger(type); return (U) this; }
    public U withAlarmTrigger(String format) { setAlarmTrigger(AlarmTriggerRelationship.parse(format)); return (U) this; }

    /**
     * RELTYPE
     * Relationship Type
     * RFC 5545, 3.2.15, page 25
     * 
     * To specify the type of hierarchical relationship associated
     *  with the calendar component specified by the property.
     * 
     * Example:
     * RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@
     *  example.com
     */
    @Override
    public Relationship getRelationship() { return (relationship == null) ? null : relationship.get(); }
    @Override
    public ObjectProperty<Relationship> relationshipProperty()
    {
        if (relationship == null)
        {
            relationship = new SimpleObjectProperty<>(this, ParameterType.RELATIONSHIP_TYPE.toString());
            orderer().registerSortOrderProperty(relationship);
        }
        return relationship;
    }
    private ObjectProperty<Relationship> relationship;
    @Override
    public void setRelationship(Relationship relationship) { relationshipProperty().set(relationship); }
    public void setRelationship(String value) { setRelationship(Relationship.parse(value)); }
    public U withRelationship(Relationship altrep) { setRelationship(altrep); return (U) this; }
    public U withRelationship(RelationshipType value) { setRelationship(new Relationship(value)); return (U) this; }
    public U withRelationship(String content) { setRelationship(content); return (U) this; }

    /**
     * TZID
     * Time Zone Identifier
     * To specify the identifier for the time zone definition for
     * a time component in the property value.
     * 
     * Examples:
     * DTSTART;TZID=America/New_York:19980119T020000
     */
    @Override
    public TimeZoneIdentifierParameter getTimeZoneIdentifier() { return (timeZoneIdentifier == null) ? null : timeZoneIdentifier.get(); }
    @Override
    public ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifierProperty()
    {
        if (timeZoneIdentifier == null)
        {
            timeZoneIdentifier = new SimpleObjectProperty<>(this, ParameterType.TIME_ZONE_IDENTIFIER.toString());
            orderer().registerSortOrderProperty(timeZoneIdentifier);
        }
        return timeZoneIdentifier;
    }
    private ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifier;
    @Override
    public void setTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier) { timeZoneIdentifierProperty().set(timeZoneIdentifier); }
    public void setTimeZoneIdentifier(String value) { setTimeZoneIdentifier(TimeZoneIdentifierParameter.parse(value)); }
    public U withTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier) { setTimeZoneIdentifier(timeZoneIdentifier); return (U) this; }
    public U withTimeZoneIdentifier(String content) { ParameterType.TIME_ZONE_IDENTIFIER.parse(this, content); return (U) this; }        

    /*
     * CONSTRUCTORS
     */
    
    public UnknownProperty(T value)
    {
        super(value);
    }
    
    public UnknownProperty(UnknownProperty<T,U> source)
    {
        super(source);
        setPropertyName(source.name());
    }
    
    UnknownProperty()
    {
        super();
    }
}
