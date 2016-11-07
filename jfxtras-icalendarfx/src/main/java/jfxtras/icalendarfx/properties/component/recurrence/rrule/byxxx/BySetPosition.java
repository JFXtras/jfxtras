package jfxtras.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BySetPosition extends ByRuleIntegerAbstract<BySetPosition>
{
    public BySetPosition()
    {
        super();
    }
    
    public BySetPosition(Integer... setPositions)
    {
        super(setPositions);
    }
    
    public BySetPosition(BySetPosition source)
    {
        super(source);
    }
    
    @Override
    Predicate<Integer> isValidValue()
    {
        return (value) -> (value >= -366) && (value <= 366) && (value != 0);
    }
    
    @Override
    public Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal startTemporal)
    {
        List<Temporal> inList = inStream.collect(Collectors.toList()); // can't be an infinite stream or will hang
        List<Temporal> outList = new ArrayList<>();
        for (int setPosition : getValue())
        {
            if (setPosition > 0)
            {
                outList.add(inList.get(setPosition-1));                
            } else if (setPosition < 0)
            {
                outList.add(inList.get(inList.size() + setPosition));                
            }
        }
        return outList.stream();
    }

    public static BySetPosition parse(String content)
    {
        BySetPosition element = new BySetPosition();
        element.parseContent(content);
        return element;
    }
}
