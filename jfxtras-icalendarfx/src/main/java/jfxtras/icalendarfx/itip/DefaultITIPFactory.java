package jfxtras.icalendarfx.itip;

import jfxtras.icalendarfx.properties.calendar.Method.MethodType;

/**
 * Default iTIP process method factory that supports one calendar user - just an organizer, no attendees.
 * 
 * The following methods are implemented
 * <ul>
 * <li>PUBLISH
 * <li>REQUEST
 * <li>CANCEL
 * </ul>
 * 
 * 
 * @author David Bal
 *
 */
public class DefaultITIPFactory extends AbstractITIPFactory
{

    @Override
    public Processable getITIPMessageProcess(MethodType methodType)
    {
        switch (methodType)
        {
        case ADD:
            break;
        case CANCEL:
            return new ProcessCancel();
        case COUNTER:
            break;
        case DECLINECOUNTER:
            break;
        case PUBLISH:
            return new ProcessPublish();
        case REFRESH:
            break;
        case REPLY:
            break;
        case REQUEST:
            return new ProcessRequest();
        default:
            break;        
        }
        throw new RuntimeException("not implemented:" + methodType);
    }

}
