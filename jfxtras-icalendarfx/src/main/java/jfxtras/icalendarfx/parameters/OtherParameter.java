package jfxtras.icalendarfx.parameters;

/**
 * Parameter with custom name and value
 * 
 * @author David Bal
 *
 */
@Deprecated
public class OtherParameter extends ParameterBase<OtherParameter, String>
{
    final String name;
    @Override
    public String name() { return name; }
    
    public OtherParameter(String content)
    {
        super();
        int equalsIndex = content.indexOf('=');
        name = (equalsIndex >= 0) ? content.substring(0, equalsIndex) : content;
        String value = (equalsIndex >= 0) ? content.substring(equalsIndex+1) : null;
        setValue(value);
    }

    public OtherParameter(OtherParameter source)
    {
        super(source);
        this.name = source.name;
    }

    public static OtherParameter parse(String content)
    {
        return new OtherParameter(content);
    }
    
    @Override
    public String toContent()
    {
        return (getValue() != null) ? name() + "=" + getValue() : null;
    }
}
