package jfxtras.icalendarfx.utilities;

import java.util.Iterator;

/**
 * A simple {@code Iterator<String>} decorator that unfolds line according to the 
 * RFC 5545 iCalendar standard.  Lines are unfolded automatically by calling the next() method.
 * Non-folded lines are preserved by using a push-back mechanism. 
 * 
 * @author David Bal
 *
 */
public class UnfoldingStringIterator implements Iterator<String>
{
    /** The iterator being decorated. */
    private final Iterator<String> iterator;
    
    private String lastLine;
    
    /**
     * Constructor.
     *
     * @param iterator  the iterator to decorate
     */
    public UnfoldingStringIterator(final Iterator<String> iterator)
    {
        super();
        this.iterator = iterator;
    }
    
    @Override
    public boolean hasNext()
    {
        return (lastLine != null) ? true : iterator.hasNext();
    }

    @Override
    public String next()
    {
        StringBuilder builder = new StringBuilder(200);
        if (lastLine != null)
        {
            builder.append(lastLine);
            lastLine = null;
        } else
        {
            builder.append(iterator.next());
        }
        boolean isContinuationLine = false;
        String currentLine = (iterator.hasNext()) ? iterator.next() : null;
        while (currentLine != null)
        {
            if (! currentLine.isEmpty())
            {
                isContinuationLine = (currentLine.charAt(0) == ' ') || (currentLine.charAt(0) == '\t');
                if (isContinuationLine)
                {
                    builder.append(currentLine.substring(1, currentLine.length()));
                } else
                {
                    break;
                }
            }
            currentLine = iterator.next();
        }
        pushBackLine(currentLine);
        return builder.toString();
    }
    
    private void pushBackLine(String s)
    {
        lastLine = s;
    }

}
