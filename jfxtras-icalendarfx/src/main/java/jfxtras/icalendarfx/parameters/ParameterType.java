package jfxtras.icalendarfx.parameters;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.parameters.AlarmTriggerRelationship.AlarmTriggerRelationshipType;
import jfxtras.icalendarfx.parameters.CalendarUser.CalendarUserType;
import jfxtras.icalendarfx.parameters.Encoding.EncodingType;
import jfxtras.icalendarfx.parameters.FreeBusyType.FreeBusyTypeEnum;
import jfxtras.icalendarfx.parameters.ParticipationRole.ParticipationRoleType;
import jfxtras.icalendarfx.parameters.ParticipationStatus.ParticipationStatusType;
import jfxtras.icalendarfx.parameters.Range.RangeType;
import jfxtras.icalendarfx.parameters.Relationship.RelationshipType;
import jfxtras.icalendarfx.properties.PropAlarmTrigger;
import jfxtras.icalendarfx.properties.PropAltText;
import jfxtras.icalendarfx.properties.PropAttachment;
import jfxtras.icalendarfx.properties.PropAttendee;
import jfxtras.icalendarfx.properties.PropBaseAltText;
import jfxtras.icalendarfx.properties.PropBaseLanguage;
import jfxtras.icalendarfx.properties.PropDateTime;
import jfxtras.icalendarfx.properties.PropFreeBusy;
import jfxtras.icalendarfx.properties.PropRecurrenceID;
import jfxtras.icalendarfx.properties.PropRelationship;
import jfxtras.icalendarfx.properties.Property;
import jfxtras.icalendarfx.properties.PropertyBase;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.relationship.PropertyBaseCalendarUser;
import jfxtras.icalendarfx.utilities.StringConverters;

/**
 * For each VComponent property parameter (RFC 5545, 3.2, page 13) contains the following: <br>
 * <br>
 * Parameter name {@link #toString()} <br>
 * Parameter class {@link #getPropertyClass()}<br>
 * Method to parse parameter string into parent component {@link #parse(Property<?>, String)}<br>
 * Method to get parameter from property {@link #getParameter(Property<?>)}<br>
 * Method to copy parameter into new parent property {@link #copyParameter(Parameter, Property)}<br>
 * 
 * @author David Bal
 *
 */
public enum ParameterType
{
    // in properties COMMENT, CONTACT, DESCRIPTION, LOCATION, RESOURCES
    ALTERNATE_TEXT_REPRESENTATION ("ALTREP", AlternateText.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropBaseAltText<?,?> castProperty = (PropBaseAltText<?, ?>) property;
            castProperty.setAlternateText(AlternateText.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropAltText<?> castProperty = (PropAltText<?>) parent;
            return castProperty.getAlternateText();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriConverterWithQuotes();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropBaseAltText<?,?> castDestination = (PropBaseAltText<?,?>) destination;
            AlternateText parameterCopy = new AlternateText((AlternateText) child);
            castDestination.setAlternateText(parameterCopy);
        }
    },
    // in properties ATTENDEE, ORGANIZER
    COMMON_NAME ("CN", CommonName.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) property;
            castProperty.setCommonName(CommonName.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) parent;
            return castProperty.getCommonName();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyBaseCalendarUser<?,?> castDestination = (PropertyBaseCalendarUser<?,?>) destination;
            CommonName parameterCopy = new CommonName((CommonName) child);
            castDestination.setCommonName(parameterCopy);
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.defaultStringConverterWithQuotes();
        }
    },
    // in property ATTENDEE
    CALENDAR_USER_TYPE ("CUTYPE", CalendarUser.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) property;
            castProperty.setCalendarUser(CalendarUser.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) parent;
            return castProperty.getCalendarUser();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((CalendarUserType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) CalendarUserType.valueOfWithUnknown(string.toUpperCase());
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropAttendee<?> castDestination = (PropAttendee<?>) destination;
            CalendarUser parameterCopy = new CalendarUser((CalendarUser) child);
            castDestination.setCalendarUser(parameterCopy);
        }
    },
    // in property ATTENDEE
    DELEGATORS ("DELEGATED-FROM", Delegators.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) property;
            castProperty.setDelegators(Delegators.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) parent;
            return castProperty.getDelegators();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriListConverter();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropAttendee<?> castDestination = (PropAttendee<?>) destination;
            Delegators parameterCopy = new Delegators((Delegators) child);
            castDestination.setDelegators(parameterCopy);
        }
    },
    // in property ATTENDEE
    DELEGATEES ("DELEGATED-TO", Delegatees.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) property;
            castProperty.setDelegatees(Delegatees.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) parent;
            return castProperty.getDelegatees();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriListConverter();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropAttendee<?> castDestination = (PropAttendee<?>) destination;
            Delegatees parameterCopy = new Delegatees((Delegatees) child);
            castDestination.setDelegatees(parameterCopy);
        }
    },
    // in properties ATTENDEE, ORGANIZER
    DIRECTORY_ENTRY_REFERENCE ("DIR", DirectoryEntry.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) property;
            castProperty.setDirectoryEntryReference(DirectoryEntry.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) parent;
            return castProperty.getDirectoryEntryReference();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriConverterWithQuotes();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropAttendee<?> castDestination = (PropAttendee<?>) destination;
            DirectoryEntry parameterCopy = new DirectoryEntry((DirectoryEntry) child);
            castDestination.setDirectoryEntryReference(parameterCopy);
        }
    },
    // in property ATTACHMENT
    INLINE_ENCODING ("ENCODING", Encoding.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropAttachment<?> castProperty = (PropAttachment<?>) property;
            castProperty.setEncoding(Encoding.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropAttachment<?> castProperty = (PropAttachment<?>) parent;
            return castProperty.getEncoding();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((EncodingType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) EncodingType.enumFromName(string.toUpperCase());
                }
            };
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropAttachment<?> castDestination = (PropAttachment<?>) destination;
            Encoding parameterCopy = new Encoding((Encoding) child);
            castDestination.setEncoding(parameterCopy);
        }
    },
    // in property ATTACHMENT
    FORMAT_TYPE ("FMTTYPE", FormatType.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropAttachment<?> castProperty = (PropAttachment<?>) property;
            castProperty.setFormatType(FormatType.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropAttachment<?> castProperty = (PropAttachment<?>) parent;
            return castProperty.getFormatType();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropAttachment<?> castDestination = (PropAttachment<?>) destination;
            FormatType parameterCopy = new FormatType((FormatType) child);
            castDestination.setFormatType(parameterCopy);
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.defaultStringConverterWithQuotes();
        }
    },
    // in property FREEBUSY
    FREE_BUSY_TIME_TYPE ("FBTYPE", FreeBusyType.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropFreeBusy<?> castProperty = (PropFreeBusy<?>) property;
            castProperty.setFreeBusyType(FreeBusyType.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropFreeBusy<?> castProperty = (PropFreeBusy<?>) parent;
            return castProperty.getFreeBusyType();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((FreeBusyTypeEnum) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) FreeBusyTypeEnum.enumFromName(string.toUpperCase());
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropFreeBusy<?> castDestination = (PropFreeBusy<?>) destination;
            FreeBusyType parameterCopy = new FreeBusyType((FreeBusyType) child);
            castDestination.setFreeBusyType(parameterCopy);
        }
    },
//    @Deprecated
//    IANA_PARAMETER (null,  // name specified in IANAParameter registeredIANAParameters
//            IANAParameter.class) {
//        @Override
//        public void parse(Property<?> property, String content)
//        {
//            final ObservableList<IANAParameter> list;
//            if (property.getIana() == null)
//            {
//                list = FXCollections.observableArrayList();
//                property.setIana(list);
//            } else
//            {
//                list = property.getIana();
//            }
//            list.add(IANAParameter.parse(content));
//        }
//
//        @Override
//        public Object getParameter(Property<?> parent)
//        {
//            return parent.getIana();
//        }
//
//        @Override
//        public void copyParameter(Parameter<?> child, Property<?> destination)
//        {
//            final ObservableList<IANAParameter> list;
//            if (destination.getIana() == null)
//            {
//                list = FXCollections.observableArrayList();
//                destination.setIana(list);
//            } else
//            {
//                list = destination.getIana();
//            }
//            list.add(new IANAParameter((IANAParameter) child));
//        }
//
//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            return (StringConverter<T>) StringConverters.defaultStringConverterWithQuotes();
//        }
//    },
    // in properties CATEGORIES, COMMENT, CONTACT, DESCRIPTION, LOCATION, RESOURCES, TZNAME
    LANGUAGE ("LANGUAGE", Language.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropBaseLanguage<?,?> castProperty = (PropBaseLanguage<?, ?>) property;
            castProperty.setLanguage(Language.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropBaseLanguage<?,?> castProperty = (PropBaseLanguage<?,?>) parent;
            return castProperty.getLanguage();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropBaseLanguage<?,?> castDestination = (PropBaseLanguage<?,?>) destination;
            Language parameterCopy = new Language((Language) child);
            castDestination.setLanguage(parameterCopy);
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.defaultStringConverterWithQuotes();
        }
    },
    GROUP_OR_LIST_MEMBERSHIP ("MEMBER", GroupMembership.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) property;
            castProperty.setGroupMembership(GroupMembership.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) parent;
            return castProperty.getGroupMembership();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriListConverter();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropFreeBusy<?> castDestination = (PropFreeBusy<?>) destination;
            FreeBusyType parameterCopy = new FreeBusyType((FreeBusyType) child);
            castDestination.setFreeBusyType(parameterCopy);
        }
    },
    NON_STANDARD ("X-", // parameter name begins with X- prefix
            NonStandardParameter.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            final ObservableList<NonStandardParameter> list;
            if (property.getNonStandard() == null)
            {
                list = FXCollections.observableArrayList();
                property.setNonStandard(list);
            } else
            {
                list = property.getNonStandard();
            }
            list.add(NonStandardParameter.parse(content));
        }

        @Override
        public Object getParameter(Property<?> parent)
        {
            return parent.getNonStandard();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            final ObservableList<NonStandardParameter> list;
            if (destination.getNonStandard() == null)
            {
                list = FXCollections.observableArrayList();
                destination.setNonStandard(list);
            } else
            {
                list = destination.getNonStandard();
            }
            list.add(new NonStandardParameter((NonStandardParameter) child));
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.defaultStringConverterWithQuotes();
        }
    },
    PARTICIPATION_STATUS ("PARTSTAT", ParticipationStatus.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) property;
            castProperty.setParticipationStatus(ParticipationStatus.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) parent;
            return castProperty.getParticipationStatus();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((ParticipationStatusType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) ParticipationStatusType.enumFromName(string.toUpperCase());
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropAttendee<?> castDestination = (PropAttendee<?>) destination;
            ParticipationStatus parameterCopy = new ParticipationStatus((ParticipationStatus) child);
            castDestination.setParticipationStatus(parameterCopy);
        }
    },
    RECURRENCE_IDENTIFIER_RANGE ("RANGE", Range.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropRecurrenceID<?> castProperty = (PropRecurrenceID<?>) property;
            castProperty.setRange(Range.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropRecurrenceID<?> castProperty = (PropRecurrenceID<?>) parent;
            return castProperty.getRange();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((RangeType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) RangeType.enumFromName(string.toUpperCase());
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropRecurrenceID<?> castDestination = (PropRecurrenceID<?>) destination;
            Range parameterCopy = new Range((Range) child);
            castDestination.setRange(parameterCopy);
        }
    },
    ALARM_TRIGGER_RELATIONSHIP ("RELATED", AlarmTriggerRelationship.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropAlarmTrigger<?> castProperty = (PropAlarmTrigger<?>) property;
            castProperty.setAlarmTrigger(AlarmTriggerRelationship.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropAlarmTrigger<?> castProperty = (PropAlarmTrigger<?>) parent;
            return castProperty.getAlarmTrigger();
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((AlarmTriggerRelationshipType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) AlarmTriggerRelationshipType.valueOf(string.toUpperCase());
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropAlarmTrigger<?> castDestination = (PropAlarmTrigger<?>) destination;
            AlarmTriggerRelationship parameterCopy = new AlarmTriggerRelationship((AlarmTriggerRelationship) child);
            castDestination.setAlarmTrigger(parameterCopy);
        }
    },
    RELATIONSHIP_TYPE ("RELTYPE", Relationship.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropRelationship<?> castProperty = (PropRelationship<?>) property;
            castProperty.setRelationship(Relationship.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropRelationship<?> castProperty = (PropRelationship<?>) parent;
            return castProperty.getRelationship();
        }

        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((RelationshipType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) RelationshipType.valueOfWithUnknown(string.toUpperCase());
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropRelationship<?> castDestination = (PropRelationship<?>) destination;
            Relationship parameterCopy = new Relationship((Relationship) child);
            castDestination.setRelationship(parameterCopy);
        }
    },
    PARTICIPATION_ROLE ("ROLE", ParticipationRole.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) property;
            castProperty.setParticipationRole(ParticipationRole.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) parent;
            return castProperty.getParticipationRole();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((ParticipationRoleType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) ParticipationRoleType.enumFromName(string.toUpperCase());
                }
            };
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropAttendee<?> castDestination = (PropAttendee<?>) destination;
            ParticipationRole parameterCopy = new ParticipationRole((ParticipationRole) child);
            castDestination.setParticipationRole(parameterCopy);
        }
    },
    RSVP_EXPECTATION ("RSVP", RSVP.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) property;
            castProperty.setRSVP(RSVP.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropAttendee<?> castProperty = (PropAttendee<?>) parent;
            return castProperty.getRSVP();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.booleanConverter();
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropAttendee<?> castDestination = (PropAttendee<?>) destination;
            RSVP parameterCopy = new RSVP((RSVP) child);
            castDestination.setRSVP(parameterCopy);
        }
    },
    SENT_BY ("SENT-BY", SentBy.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) property;
            castProperty.setSentBy(SentBy.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBaseCalendarUser<?,?> castProperty = (PropertyBaseCalendarUser<?,?>) parent;
            return castProperty.getSentBy();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return (StringConverter<T>) StringConverters.uriConverterWithQuotes();
        }

        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyBaseCalendarUser<?,?> castDestination = (PropertyBaseCalendarUser<?,?>) destination;
            SentBy parameterCopy = new SentBy((SentBy) child);
            castDestination.setSentBy(parameterCopy);
        }
    },
    TIME_ZONE_IDENTIFIER ("TZID", TimeZoneIdentifierParameter.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            PropDateTime<?> castProperty = (PropDateTime<?>) property;
            castProperty.setTimeZoneIdentifier(TimeZoneIdentifierParameter.parse(content));
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropDateTime<?> castProperty = (PropDateTime<?>) parent;
            TimeZoneIdentifierParameter parameter = castProperty.getTimeZoneIdentifier();
            return ((parameter == null) || (parameter.getValue().equals(ZoneId.of("Z")))) ? null : parameter;
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((ZoneId) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) ZoneId.of(string);
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropDateTime<?> castDestination = (PropDateTime<?>) destination;
            TimeZoneIdentifierParameter parameterCopy = new TimeZoneIdentifierParameter((TimeZoneIdentifierParameter) child);
            castDestination.setTimeZoneIdentifier(parameterCopy);
        }
    },
    VALUE_DATA_TYPES ("VALUE", ValueParameter.class) {
        @Override
        public void parse(Property<?> property, String content)
        {
            int equalsIndex = content.indexOf('=');
            final String valueString;
            if (equalsIndex > 0)
            {
                String name = content.substring(0, equalsIndex);
                boolean isNameValid = name.equals(this.toString());
                valueString = (isNameValid) ? content.substring(equalsIndex+1) : content;    
            } else
            {
                valueString = content;
            }
            ValueType valueType = ValueType.enumFromName(valueString.toUpperCase());
            PropertyBase<?,?> castProperty = (PropertyBase<?,?>) property;
            boolean isValidType = castProperty.propertyType().allowedValueTypes().contains(valueType);
            if (valueType == null || isValidType)
            {
                castProperty.setValueType(ValueParameter.parse(valueString));
            } else
            {
                throw new IllegalArgumentException("Property type " + property.getClass().getSimpleName() + " doesn't allow value type " + valueType);
            }
        }

        @Override
        public Parameter<?> getParameter(Property<?> parent)
        {
            PropertyBase<?,?> castProperty = (PropertyBase<?,?>) parent;
            return castProperty.getValueType();
        }
        
        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return ((ValueType) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) ValueType.enumFromName(string.toUpperCase());
                }
            };
        }
        
        @Override
        public void copyParameter(Parameter<?> child, Property<?> destination)
        {
            PropertyBase<?,?> castDestination = (PropertyBase<?,?>) destination;
            ValueParameter parameterCopy = new ValueParameter((ValueParameter) child);
            castDestination.setValueType(parameterCopy);
        }
    };
    
    // Map to match up name to enum
    private static Map<String, ParameterType> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, ParameterType> makeEnumFromNameMap()
    {
        Map<String, ParameterType> map = new HashMap<>();
        ParameterType[] values = ParameterType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static ParameterType enumFromName(String parameterName)
    {
        final ParameterType prop;
        // minimum property name is 2 characters
        boolean isLongEnough = parameterName.length() > 2;
        boolean isNonStanderd = (isLongEnough) ? parameterName.substring(0, ParameterType.NON_STANDARD.toString().length()).equals(ParameterType.NON_STANDARD.toString()) : false;
        if (isNonStanderd)
        {
            prop = ParameterType.NON_STANDARD;
//        } else if ((IANAProperty.getRegisteredIANAPropertys() != null) && IANAProperty.getRegisteredIANAPropertys().contains(parameterName))
//        {
//            prop = ParameterType.IANA_PARAMETER;            
        } else
        {
            prop = enumFromNameMap.get(parameterName);   
        }
        return prop;
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends Parameter<?>>, ParameterType> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends Parameter<?>>, ParameterType> makeEnumFromClassMap()
    {
        Map<Class<? extends Parameter<?>>, ParameterType> map = new HashMap<>();
        ParameterType[] values = ParameterType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from map */
    public static ParameterType enumFromClass(Class<? extends VElement> myClass)
    {
        ParameterType p = enumFromClassMap.get(myClass);
        if (p == null)
        {
            throw new IllegalArgumentException(ParameterType.class.getSimpleName() + " does not contain an enum to match the class:" + myClass.getSimpleName());
        }
        return p;
    }
    
    private String name;
    private Class<? extends Parameter<?>> myClass;
    @Override  public String toString() { return name; }
    ParameterType(String name, Class<? extends Parameter<?>> myClass)
    {
        this.name = name;
        this.myClass = myClass;
    }

    /*
     * STATIC METHODS
     */

    /**
     * Remove parameter name and equals sign, if present, otherwise returns input string
     * 
     * @param input - parameter content with or without name and equals sign
     * @param name - name of parameter
     * @return - nameless string
     * 
     * example input:
     * ALTREP="CID:part3.msg.970415T083000@example.com"
     * output:
     * "CID:part3.msg.970415T083000@example.com"
     */
    static String extractValue(String content)
    {
        int equalsIndex = content.indexOf('=');
        return (equalsIndex > 0) ? content.substring(equalsIndex+1) : content;
    }

    /*
     * ABSTRACT METHODS
     */
    /** Parses string and sets parameter.  Called by {@link PropertyBase#parseContent()} */
    abstract public void parse(Property<?> property, String content);
    
    /** Returns associated Property<?> or List<Property<?>> */
    // TODO - MAY BE OBSOLETE WITH USE OF ORDERER - ONLY BEING USED TO DOUBLE-CHECK EXISTANCE OF ALL PARAMETERS WHEN COPYING
    abstract public Object getParameter(Property<?> parent);
    
    /** return default String converter associated with property value type */
    abstract public <T> StringConverter<T> getConverter();
    
    abstract public void copyParameter(Parameter<?> child, Property<?> destination);
}
