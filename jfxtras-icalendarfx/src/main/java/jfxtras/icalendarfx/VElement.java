package jfxtras.icalendarfx;

import java.util.List;

/**
 * <p>Interface for all calendar elements.</p>
 * 
 * @author David Bal
 *
 */
public interface VElement
{
    /**
     * <p>Returns the name of the component as it would appear in the iCalendar content line.</p>
     * <p>Examples:
     * <ul>
     * <li>VEVENT
     * <li>SUMMARY
     * <li>LANGUAGE
     * </ul>
     * </p>
     * @return - the component name
     */
    String name();
    /**
     * Returns iCalendar content text.
     * 
     * @return the content string
     */
    String toContent(); // This method is invoked recursively to include content of all children.
    
    /** Parse content line into calendar element.
     * If element contains children {@link #parseContent(String)} is invoked recursively to parse child elements also
     * 
     * @param content  calendar content string to parse
     * @return  log of information and error messages
     * @throws IllegalArgumentException  if calendar content is not valid, such as null
     */
    List<String> parseContent(String content) throws IllegalArgumentException;
    
    /**
     * Checks element to determine if necessary properties are set.
     * {@link #isValid()} is invoked recursively to test child elements if element is a parent
     * 
     * @return - true if component is valid, false otherwise
     */
    default boolean isValid() { return errors().isEmpty(); }
    
    /**
     * Produces a list of error messages indicating problems with calendar element
     * {@link #errors()} is invoked recursively to return errors of child elements in addition to errors in parent
     * 
     * @return - list of error messages
     */
    List<String> errors();
}
