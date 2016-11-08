package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.AlternateText;

public class AlternateTextRepresentationTest
{
    @Test
    public void canParseAlternateText1()
    {
        String content = "\"cid:part1.0001@example.org\"";
        AlternateText p = AlternateText.parse(content);
        assertEquals(p.name() + "=" + content, p.toContent());
    }
}
