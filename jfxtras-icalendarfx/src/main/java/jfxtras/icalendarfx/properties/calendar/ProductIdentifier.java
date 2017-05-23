package jfxtras.icalendarfx.properties.calendar;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.properties.VPropertyBase;
import jfxtras.icalendarfx.properties.calendar.ProductIdentifier;

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
public class ProductIdentifier extends VPropertyBase<String, ProductIdentifier> implements VElement
{
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
    }
    
    public static ProductIdentifier parse(String content)
    {
    	return ProductIdentifier.parse(new ProductIdentifier(), content);
    }
}
