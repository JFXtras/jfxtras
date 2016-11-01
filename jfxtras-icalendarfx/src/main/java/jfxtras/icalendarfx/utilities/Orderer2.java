package jfxtras.icalendarfx.utilities;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VParent;
import jfxtras.icalendarfx.components.VComponentBase;
import jfxtras.icalendarfx.properties.PropertyBase;
import jfxtras.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleValue;

/** Generic version of orderer - for blog 
 * Maintains a sort order of {@link C} elements of a {@link VParent}
 * 
 * @see VParent
 * @see VCalendar
 * @see VComponentBase
 * @see PropertyBase
 * @see RecurrenceRuleValue
 *  */ 
public class Orderer2<P, C>
{
    final private VParent parent;
    
    /** 
     * <p>Sort order map for {@link C} elements of a {@link VParent}.</p>
     * 
     * <p>The map key is a {@link C}.  The map value is its sort order.</p>
     * 
     * <p>Under normal conditions, this map shouldn't be modified.  Only modify it when you want to force
     * a specific sort order.</p>
     * 
     * @return  the sort order map
     */
    public Map<C, Integer> elementSortOrderMap() { return elementSortOrderMap; }
    final private Map<C, Integer> elementSortOrderMap = new HashMap<>();
    private volatile Integer sortOrderCounter = 0;

    /*
     * CONSTRUCTOR
     */
    /** Create an {@link OrdererBase} for the {@link VParent} parameter */
    public Orderer2(VParent aParent)
    {
        this.parent = aParent;
        sortOrderListChangeListener = (ListChangeListener.Change<? extends C> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList().forEach(C ->  
                    {
                        if (C != null)
                        {
                            elementSortOrderMap.put(C, sortOrderCounter);
//                            C.setParent(parent);
                            sortOrderCounter += 100;
                        }
                    });
                } else
                {
                    if (change.wasRemoved())
                    {
                        change.getRemoved().forEach(vComponent -> 
                        {
                            elementSortOrderMap.remove(vComponent);
                        });
                    }                
                }
            }
        };
        
        sortOrderChangeListener = (obs, oldValue, newValue) ->
        {
            final int sortOrder;
            if ((oldValue != null) && (elementSortOrderMap.get(oldValue) != null))
            {
                sortOrder = elementSortOrderMap.get(oldValue);
                elementSortOrderMap.remove(oldValue);
//                System.out.println("remove from map:" + oldValue.toContent() + " " + elementSortOrderMap.size() + " " + System.identityHashCode(parent));
            } else
            {
                sortOrder = sortOrderCounter;
                sortOrderCounter += 100;
            }
            if (newValue != null)
            {
                elementSortOrderMap.put(newValue, sortOrder);
//                System.out.println("add to map:" + newValue.toContent() + " " + elementSortOrderMap.size()+ " " + System.identityHashCode(parent));
//                newValue.setParent(parent);
            }
        };
    }
    
    /**
     * Sort order listener for properties containing an ObservableList to 
     * maintains the {@link #elementSortOrderMap}
     */
    final private ListChangeListener<C> sortOrderListChangeListener;

    /**
     * <p>Register an ObservableList with the {@link Orderer2}.</p>
     * <p>Enables maintaining automatic sort order.</p>
     * 
     * @param list  ObservableList from a property containing an ObservableList
     */
    public void registerSortOrderProperty(ObservableList<? extends C> list)
    {
        list.addListener(sortOrderListChangeListener);
        if (! list.isEmpty())
        { // add existing elements to sort order
            list.forEach(C ->  
            {
                if (elementSortOrderMap.get(C) == null)
                {
                    elementSortOrderMap.put(C, sortOrderCounter);
//                    C.setParent(parent);
                    sortOrderCounter += 100;
                }
            });
        }
    }
    /** Unregister an ObservableList with the {@link Orderer2} */
    public void unregisterSortOrderProperty(ObservableList<? extends C> list)
    {
        if ((list != null) && ! list.isEmpty())
        { // remove existing elements to sort order
            list.forEach(C -> elementSortOrderMap.remove(C));
        }
    }
    
    /** Sort order listener for object properties to 
     * maintains the {@link #elementSortOrderMap}
     */
    final private ChangeListener<? super C> sortOrderChangeListener;

    /**
     * <p>Register an ObjectProperty with the {@link Orderer2}.</p>
     * <p>Enables maintaining automatic sort order.</p>
     * 
     * @param list  the property to be registered
     */
    public void registerSortOrderProperty(ObjectProperty<? extends C> property)
    {
        property.addListener(sortOrderChangeListener);
        if (property.get() != null)
        { // add existing element to sort order
            elementSortOrderMap.put(property.get(), sortOrderCounter);
//            property.get().setParent(parent);
            sortOrderCounter += 100;
        }
    }
    /** Unregister an ObjectProperty with the {@link Orderer2} */
    public void unregisterSortOrderProperty(ObjectProperty<? extends C> property)
    {
        elementSortOrderMap.remove(property);
    }
    
    public List<C> childrenUnmodifiable()
    {
        return Collections.unmodifiableList(
                elementSortOrderMap().entrySet().stream()
                .sorted((Comparator<? super Entry<C, Integer>>) (e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .map(e -> e.getKey())
                .collect(Collectors.toList())
                );
    }
    
//    /**
//     * <p>The list of sorted content text for {@link C} elements.
//     * 
//     * @return - unmodifiable list of sorted content lines
//     * @see VElement#toContent()
//     */
//    public List<C> sortedContent()
//    {        
//        List<String> content = new ArrayList<>();
//        // apply sort order (if element doesn't exist in map, don't sort)
//        elementSortOrderMap.entrySet().stream()
//                .sorted((Comparator<? super Entry<C, Integer>>) (e1, e2) -> 
//                {
//                    return e1.getValue().compareTo(e2.getValue());
//                })
//                .forEach(p -> 
//                {
//                    if (p.getKey() != null)
//                    {
//                        content.add(p.getKey());
//                    }
//                });
//        
//        return Collections.unmodifiableList(content);
//    }
    
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
     * @param <T> class of C in the lists
     * 
     * @param oldList
     * @param newList
     */
    public <T extends C> void replaceList(ObservableList<T> oldList, ObservableList<T> newList)
    {
        Iterator<T> newItemIterator = newList.iterator();
        Iterator<Integer> oldSortValueIterator = oldList.stream()
                .map(c -> elementSortOrderMap.get(c))
                .iterator();
        while (oldSortValueIterator.hasNext() && newItemIterator.hasNext())
        {
            T newItem = newItemIterator.next();
            Integer value = oldSortValueIterator.next();
            elementSortOrderMap.put(newItem, value);
        }
        oldList.clear();
    }
}
