package jfxtras.icalendarfx.content;

import jfxtras.icalendarfx.VElement;

/** Interface for delegated content line generators */
public interface ContentLineStrategy
{
    /** Produce output for {@link VElement#toContent()} */
    String execute();
}
