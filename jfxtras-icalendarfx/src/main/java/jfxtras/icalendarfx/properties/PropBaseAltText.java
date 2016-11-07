package jfxtras.icalendarfx.properties;

import java.net.URI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import jfxtras.icalendarfx.parameters.AlternateText;
import jfxtras.icalendarfx.parameters.ParameterType;
import jfxtras.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.icalendarfx.properties.component.descriptive.Description;
import jfxtras.icalendarfx.properties.component.descriptive.Location;
import jfxtras.icalendarfx.properties.component.descriptive.Resources;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.icalendarfx.properties.component.relationship.Contact;

/**
 * Property with language, alternate text display, and a text-based value
 *  
 * @param <U>
 * 
 * concrete subclasses
 * @see Comment
 * @see Contact
 * @see Description
 * @see Location
 * @see Resources
 * @see Summary
 */
public abstract class PropBaseAltText<T,U> extends PropBaseLanguage<T,U> implements PropAltText<T>
{
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
    
    /*
     * CONSTRUCTORS
     */    
    // copy constructor
    public PropBaseAltText(PropBaseAltText<T,U> property)
    {
        super(property);
    }

    public PropBaseAltText(T value, StringConverter<T> converter)
    {
        super(value);
    }
    
    protected PropBaseAltText()
    {
        super();
    }
}
