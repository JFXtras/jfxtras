package jfxtras.icalendarfx.content;

import java.util.stream.Collectors;

import jfxtras.icalendarfx.utilities.Callback;

public class SingleLineContent extends ContentLineBase
{
    final private int builderSize;
    final private Callback<Void,String> nameCallback;
    
    public SingleLineContent(
            Orderer orderer,
            Callback<Void,String> nameCallback,
            int builderSize)
    {
        super(orderer);
        this.nameCallback = nameCallback;
        this.builderSize = builderSize;
    }
    
    @Override
    public String execute()
    {
        StringBuilder builder = new StringBuilder(builderSize);
        builder.append(nameCallback.call(null));
        String elements = orderer.childrenUnmodifiable().stream()
                .map(c -> c.toString())
                .collect(Collectors.joining(";"));
        if (! elements.isEmpty())
        {
            builder.append(";" + elements);
        }
        return builder.toString();
    }
}
