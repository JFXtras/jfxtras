package jfxtras.icalendarfx.utilities;

/**
 * Converts an object to a string and back to the object.
 * 
 * @author David Bal
 *
 * @param <T>
 */
public interface StringConverter<T>
{
    /**
    * Converts the object provided into its string form.
    * Format of the returned string is defined by the specific converter.
    * @return a string representation of the object passed in.
    */
    String toString(T object);

    /**
    * Converts the string provided into an object defined by the specific converter.
    * Format of the string and type of the resulting object is defined by the specific converter.
    * @return an object representation of the string passed in.
    */
    T fromString(String string);
}
