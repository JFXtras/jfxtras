package jfxtras.icalendarfx.itip;

import java.util.List;

import jfxtras.icalendarfx.VCalendar;

/**
 * Interface for a iTIP process method including the following:
 * <ul>
 * <li>PUBLISH
 * <li>REQUEST
 * <li>REPLY
 * <li>ADD
 * <li>CANCEL
 * <li>REFRESH
 * <li>COUNTER
 * <li>DECLINECOUNTER
 * </ul>
 * 
 * Returns a list of strings containing a log of errors, warnings or other comments regarding the
 * result of the message process
 * 
 * @author David Bal
 *
 */
public interface Processable
{
    List<String> process(VCalendar mainVCalendar, VCalendar iTIPMessage);
}
