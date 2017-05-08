package jfxtras.icalendarfx.parameters;

import jfxtras.icalendarfx.parameters.ParameterEnumBasedWithUnknown;
import jfxtras.icalendarfx.parameters.Relationship;
import jfxtras.icalendarfx.parameters.Relationship.RelationshipType;
import jfxtras.icalendarfx.utilities.StringConverter;

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
 * 
 * @author David Bal
 *
 */
public class Relationship extends ParameterEnumBasedWithUnknown<Relationship, RelationshipType>
{
	private static final StringConverter<RelationshipType> CONVERTER = new StringConverter<RelationshipType>()
    {
        @Override
        public String toString(RelationshipType object)
        {
            return object.toString();
        }

        @Override
        public RelationshipType fromString(String string)
        {
            return RelationshipType.valueOfWithUnknown(string.toUpperCase());
        }
    };
    
    public Relationship()
    {
        super(RelationshipType.PARENT, CONVERTER); // default value
    }
  
    public Relationship(RelationshipType value)
    {
        super(value, CONVERTER);
    }
    
    public Relationship(Relationship source)
    {
        super(source, CONVERTER);
    }
    
    public enum RelationshipType
    {
        PARENT,
        CHILD,
        SIBLING,
        UNKNOWN;
        
        /** get enum from name */
        public static RelationshipType valueOfWithUnknown(String propertyName)
        {
            RelationshipType match;
            try
            {
                match = valueOf(propertyName);
            } catch (Exception e)
            {
                match = UNKNOWN;
            }
            return match;
        }
    }
    
    public static Relationship parse(String content)
    {
    	return Relationship.parse(new Relationship(), content);
    }
}
