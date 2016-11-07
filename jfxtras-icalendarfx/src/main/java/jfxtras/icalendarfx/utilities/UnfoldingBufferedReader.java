package jfxtras.icalendarfx.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/** BufferedReader that unfolds lines via the {@link #readLine()} method. */
@Deprecated
public class UnfoldingBufferedReader extends BufferedReader
{
    String lastLine;
    
    public UnfoldingBufferedReader(Reader in)
    {
        super(in);
    }
    
    public UnfoldingBufferedReader(Reader in, int sz)
    {
        super(in, sz);
    }

    /** Unfolds lines based on RFC 5545 iCalendar specification */
    @Override
    public String readLine() throws IOException
    {
        StringBuilder builder = new StringBuilder(200);
        if (lastLine != null)
        {
            builder.append(lastLine);
            lastLine = null;
        } else
        {
            builder.append(super.readLine());
        }
        boolean isContinuationLine = false;
        String currentLine = super.readLine();
        while (currentLine != null)
        {
            isContinuationLine = (currentLine.charAt(0) == ' ') || (currentLine.charAt(0) == '\t');
            if (isContinuationLine)
            {
                builder.append(currentLine.substring(1, currentLine.length()));
                currentLine = super.readLine();
            } else
            {
                break;
            }
        }
        pushBackLine(currentLine);
        return builder.toString();
    }
    
    private void pushBackLine(String s)
    {
        lastLine = s;
    }
}
