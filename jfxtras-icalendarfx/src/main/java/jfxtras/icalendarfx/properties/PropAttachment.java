package jfxtras.icalendarfx.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.icalendarfx.parameters.Encoding;
import jfxtras.icalendarfx.parameters.FormatType;

public interface PropAttachment<T> extends Property<T>
{
    /**
     * FMTTYPE: Format type parameter
     * RFC 5545, 3.2.8, page 19
     * specify the content type of a referenced object.
     */
    FormatType getFormatType();
    ObjectProperty<FormatType> formatTypeProperty();
    void setFormatType(FormatType formatType);
    
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
    Encoding getEncoding();
    ObjectProperty<Encoding> encodingProperty();
    void setEncoding(Encoding encoding);
}
