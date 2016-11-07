package jfxtras.icalendarfx.parameters;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Format Type
 * FMTYPE
 * RFC 5545 iCalendar 3.2.8 page 19
 * 
 * To specify the content type of a referenced object.
 * 
 *  Example:
 *  ATTACH;FMTTYPE=application/msword:ftp://example.com/pub/docs/
 *   agenda.doc
 *   
 *   @see Attachment
 */
public class FormatType extends ParameterBase<FormatType, String>
{
    public String getTypeName() { return typeName.get(); }
    StringProperty typeNameProperty() { return typeName; }
    private StringProperty typeName;
    public void setTypeName(String typeName) { this.typeName.set(typeName); }

    public String getSubtypeName() { return subtypeName.get(); }
    StringProperty subtypeNameProperty() { return subtypeName; }
    private StringProperty subtypeName;
    public void setSubtypeName(String subtypeName) { this.subtypeName.set(subtypeName); }

    // capture type and subtype names
    @Override
    public void setValue(String value)
    {
        if (typeName == null)
        {
            typeName = new SimpleStringProperty(this, ParameterType.FORMAT_TYPE.toString() + "_TYPE_NAME");
        }
        if (subtypeName == null)
        {
            subtypeName = new SimpleStringProperty(this, ParameterType.FORMAT_TYPE.toString() + "_SUBTYPE_NAME");
        }

        int slashIndex = value.indexOf('/');
        if (slashIndex > 0)
        {
            setTypeName(value.substring(0, slashIndex));
            setSubtypeName(value.substring(slashIndex+1));
            super.setValue(value);            
        } else
        {
            throw new IllegalArgumentException(getClass().getSimpleName() + " requires both type-name / subtype-name as defined in RFC4288");
        }
    }

    /*
     * CONSTRUCTORS
     */  
    public FormatType()
    {
        super();
    }

    public FormatType(FormatType source)
    {
        super(source);
    }
    
    public static FormatType parse(String content)
    {
        FormatType parameter = new FormatType();
        parameter.parseContent(content);
        return parameter;
    }
}
