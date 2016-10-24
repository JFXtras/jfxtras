package jfxtras.icalendarfx.content;

import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import jfxtras.icalendarfx.utilities.Orderer;

public class SingleLineContent extends ContentLineBase
{
    final private int builderSize;
    final private ObjectProperty<String> name;
    
    public SingleLineContent(
            Orderer orderer,
            ObjectProperty<String> name,
            int builderSize)
    {
        super(orderer);
        this.name = name;
        this.builderSize = builderSize;
    }
    
    @Override
    public String execute()
    {
        StringBuilder builder = new StringBuilder(builderSize);
//        if (name != null)
//        {
            builder.append(name.get());
//        }
        String elements = orderer().sortedContent().stream()
                .collect(Collectors.joining(";"));
        if (! elements.isEmpty())
        {
            builder.append(";" + elements);
        }
        return builder.toString();
    }
}
