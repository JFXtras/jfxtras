package jfxtras.icalendarfx.utilities;

import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VChild;
import jfxtras.icalendarfx.VElement;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.properties.Property;
import jfxtras.icalendarfx.properties.component.recurrence.RecurrenceRule;

/** 
 * Provides a framework to maintain the sort order of {@link VChild} elements
 * 
 * @see VCalendar
 * @see VComponent
 * @see Property
 * @see RecurrenceRule
 *  */ 
public interface Orderer
{
    /** 
     * <p>Sort order map for {@link VChild} elements of a {@link VParent}.</p>
     * 
     * <p>The map key is a {@link VChild}.  The map value is its sort order.</p>
     * 
     * <p>Under normal conditions, this map shouldn't be modified.  Only modify it when you want to force
     * a specific sort order.</p>
     * 
     * @return  the sort order map
     */
    Map<VChild, Integer> elementSortOrderMap();
    
    /**
     * <p>The list of sorted content text for {@link VChild} elements.
     * 
     * @return - unmodifiable list of sorted content lines
     * @see VElement#toContent()
     */
    List<String> sortedContent();
    
    /**
     * <p>Register an ObservableList with the {@link Orderer}.</p>
     * <p>Enables maintaining automatic sort order.</p>
     * 
     * @param list  ObservableList from a property containing an ObservableList
     */
    void registerSortOrderProperty(ObservableList<? extends VChild> list);
    /** Unregister an ObservableList with the {@link Orderer} */
    void unregisterSortOrderProperty(ObservableList<? extends VChild> list);
    
    /**
     * <p>Register an ObjectProperty with the {@link Orderer}.</p>
     * <p>Enables maintaining automatic sort order.</p>
     * 
     * @param list  the property to be registered
     */
    void registerSortOrderProperty(ObjectProperty<? extends VChild> property);
    /** Unregister an ObjectProperty with the {@link Orderer} */
    void unregisterSortOrderProperty(ObjectProperty<? extends VChild> property);

    /**
     * <p>Replace the elements in the oldList with those in newList.  Copies sort order values from the
     * elements of oldList when newList items are added to the elementSortOrderMap.</p>
     * 
     * <p>If the number of elements in oldList is less than or equal to the number of elements in the newList then all
     * the elements in the newList get the sort order of the elements in the oldList, in the same order they are found in the map.</p>
     * 
     * <p>If the number of elements in the oldList is greater than the number of elements in the newList then only the
     * number of elements in the oldList have their sort order copied to the newList elements.  Remaining newList elements
     * will get new sort order values when they are added.</p>
     * @param <T> class of VChild in the lists
     * 
     * @param oldList
     * @param newList
     */
    <T extends VChild> void replaceList(ObservableList<T> oldList, ObservableList<T> newList);
}
