package jfxtras.icalendarfx.components;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.properties.PropertyType;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;

/**
 * <p>{@link VComponent} with the following properties
 * <ul>
 * <li>{@link NonStandardProperty X-PROP}
 * </ul>
 * </p>
 * 
 * @author David Bal
 *
 * @param <T> concrete subclass
 */
public abstract class VCommon<T> extends VComponentBase
{
    /**
     * Provides a framework for defining non-standard properties.
     * 
     * <p>Example:
     * <ul>
     * <l1>X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.
        org/mysubj.au
     *  </ul>
     */
    public ObjectProperty<ObservableList<NonStandardProperty>> nonStandardProperty()
    {
        if (nonStandardProps == null)
        {
            nonStandardProps = new SimpleObjectProperty<>(this, PropertyType.NON_STANDARD.toString());
        }
        return nonStandardProps;
    }
    private ObjectProperty<ObservableList<NonStandardProperty>> nonStandardProps;
    public ObservableList<NonStandardProperty> getNonStandard()
    {
        return (nonStandardProps == null) ? null : nonStandardProps.get();
    }
    public void setNonStandard(ObservableList<NonStandardProperty> nonStandardProps)
    {
        if (nonStandardProps != null)
        {
            if ((this.nonStandardProps != null) && (this.nonStandardProps.get() != null))
            {
                // replace sort order in new list
                orderer().replaceList(nonStandardProperty().get(), nonStandardProps);
            }
            orderer().registerSortOrderProperty(nonStandardProps);
        } else
        {
            orderer().unregisterSortOrderProperty(nonStandardProperty().get());
        }
        nonStandardProperty().set(nonStandardProps);
    }
    /**
     * Sets the value of the {@link #nonStandardProperty()} by parsing a vararg of
     * iCalendar content text representing individual {@link NonStandardProperty} objects.
     * 
     * @return - this class for chaining
     */
    public T withNonStandard(String...nonStandardProps)
    {
        List<NonStandardProperty> a = Arrays.stream(nonStandardProps)
                .map(c -> NonStandardProperty.parse(c))
                .collect(Collectors.toList());
        setNonStandard(FXCollections.observableArrayList(a));
        return (T) this;
    }
    /**
     * Sets the value of the {@link #nonStandardProperty()}
     * 
     * @return - this class for chaining
     */
    public T withNonStandard(ObservableList<NonStandardProperty> nonStandardProps)
    {
        setNonStandard(nonStandardProps);
        return (T) this;
    }
    /**
     * Sets the value of the {@link #nonStandardProperty()} from a vararg of {@link NonStandardProperty} objects.
     * 
     * @return - this class for chaining
     */    
    public T withNonStandard(NonStandardProperty...nonStandardProps)
    {
        if (getNonStandard() == null)
        {
            setNonStandard(FXCollections.observableArrayList(nonStandardProps));
        } else
        {
            getNonStandard().addAll(nonStandardProps);
        }
        return (T) this;
    }

    /*
     * CONSTRUCTORS
     */
    VCommon()
    {
        super();
    }
    
    VCommon(VCommon<T> source)
    {
        super(source);
    }
}
