/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/** BufferedReader that unfolds lines via the {@link #readLine()} method.
 * 
 * Works fine, but not being used now.
 * */
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
