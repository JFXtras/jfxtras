package jfxtras.icalendarfx.properties.component.descriptive;

import jfxtras.icalendarfx.components.DaylightSavingTime;
import jfxtras.icalendarfx.components.StandardTime;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropBaseAltText;

/**
 * COMMENT
 * RFC 5545 iCalendar 3.8.1.4. page 83
 * 
 * This property specifies non-processing information intended
 * to provide a comment to the calendar user
 * 
 * Example:
 * COMMENT:The meeting really needs to include both ourselves
 *  and the customer. We can't hold this meeting without them.
 *  As a matter of fact\, the venue for the meeting ought to be at
 *  their site. - - John
 *  
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see StandardTime
 * @see DaylightSavingTime
 */
public class Comment extends PropBaseAltText<String, Comment>
{   
    public Comment(Comment source)
    {
        super(source);
    }

    public Comment()
    {
        super();
    }
    
    public static Comment parse(String propertyContent)
    {
        Comment property = new Comment();
        property.parseContent(propertyContent);
        return property;
    }
}
