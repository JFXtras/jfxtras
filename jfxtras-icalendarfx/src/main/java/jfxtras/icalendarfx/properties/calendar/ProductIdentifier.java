package jfxtras.icalendarfx.properties.calendar;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.properties.PropertyBase;

/**
 * PRODID
 * Product Identifier
 * RFC 5545, 3.7.3, page 78
 * 
 * This property specifies the identifier for the product that created the iCalendar object.
 * 
 * The vendor of the implementation SHOULD assure that
 * this is a globally unique identifier; using some technique such as
 * an FPI value, as defined in [ISO.9070.1991]
 * 
 * Example:
 * PRODID:-//ABC Corporation//NONSGML My Product//EN
 * 
 * @author David Bal
 * @see VCalendar
 */
public class ProductIdentifier extends PropertyBase<String, ProductIdentifier> implements VElement
{
//    public static final String DEFAULT_PRODUCT_IDENTIFIER = ("-//JFxtras//iCalendarFx " + VCalendar.myVersion + "//EN");

    public ProductIdentifier(ProductIdentifier source)
    {
        super(source);
    }
    
    public ProductIdentifier(String productIdentifier)
    {
        super(productIdentifier);
    }

    public ProductIdentifier()
    {
        super();
//        setValue(DEFAULT_PRODUCT_IDENTIFIER);
    }
    
    public static ProductIdentifier parse(String string)
    {
        ProductIdentifier property = new ProductIdentifier();
        property.parseContent(string);
        return property;
    }
}
