package jfxtras.icalendarfx.components;

import jfxtras.icalendarfx.components.VDateTimeEnd;
import jfxtras.icalendarfx.components.VDescribable2;
import jfxtras.icalendarfx.components.VLocatable;
import jfxtras.icalendarfx.components.VRepeatable;

public abstract class NonStandardComponent<T> extends VLocatable<T> implements VDateTimeEnd<T>,
VDescribable2<T>, VRepeatable<T>
{

    NonStandardComponent()
    {
        throw new RuntimeException("not implemented");
        // TODO - FINISH THIS
    }
}
