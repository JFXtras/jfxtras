package jfxtras.icalendarfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.icalendarfx.components.SimpleVComponentFactory;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VPersonal;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.content.MultiLineContent;
import jfxtras.icalendarfx.itip.AbstractITIPFactory;
import jfxtras.icalendarfx.itip.DefaultITIPFactory;
import jfxtras.icalendarfx.itip.Processable;
import jfxtras.icalendarfx.properties.PropertyType;
import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.Method;
import jfxtras.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.calendar.Method.MethodType;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.icalendarfx.utilities.ICalendarUtilities;
import jfxtras.icalendarfx.utilities.UnfoldingStringIterator;

/**
 * iCalendar Object
 * RFC 5545, 3.4, page 50
 * 
 * Parent calendar object represents a collection of calendaring and scheduling information 
 * 
 * @author David Bal
 *
 */
public class VCalendar extends VParentBase
{
    // version of this project, not associated with the iCalendar specification version
//    public static String myVersion = "1.0";
    private static final String NAME = "VCALENDAR";
    @Override public String name() { return NAME; }
    private static final String FIRST_CONTENT_LINE = "BEGIN:" + NAME;
    private static final String LAST_CONTENT_LINE = "END:" + NAME;
    
    /*
     * Calendar properties
     */
    
    /**
     *  CALSCALE: RFC 5545 iCalendar 3.7.1. page 76
     * This property defines the calendar scale used for the
     * calendar information specified in the iCalendar object.
     * 
     * This project is based on the Gregorian calendar scale.
     * The Gregorian calendar scale is assumed if this property is not
     * specified in the iCalendar object.  It is expected that other
     * calendar scales will be defined in other specifications or by
     * future versions of iCalendar.
     * 
     * The default value is "GREGORIAN"
     * 
     * Example:
     * CALSCALE:GREGORIAN
     * */
    ObjectProperty<CalendarScale> calendarScaleProperty()
    {
        if (calendarScale == null)
        {
            calendarScale = new SimpleObjectProperty<CalendarScale>(this, CalendarProperty.CALENDAR_SCALE.toString());
            orderer().registerSortOrderProperty(calendarScale);
        }
        return calendarScale;
    }
    public CalendarScale getCalendarScale() { return (calendarScale == null) ? null : calendarScaleProperty().get(); }
    private ObjectProperty<CalendarScale> calendarScale;
    public void setCalendarScale(String calendarScale) { setCalendarScale(CalendarScale.parse(calendarScale)); }
    public void setCalendarScale(CalendarScale calendarScale) { calendarScaleProperty().set(calendarScale); }
    public VCalendar withCalendarScale(CalendarScale calendarScale)
    {
        setCalendarScale(calendarScale);
        return this;
    }
    public VCalendar withCalendarScale(String calendarScale)
    {
        setCalendarScale(calendarScale);
        return this;
    }

    /**
     * METHOD: RFC 5545 iCalendar 3.7.2. page 77
     * This property defines the iCalendar object method
     * associated with the calendar object
     * 
     * Example:
     * METHOD:REQUEST
     * */
    ObjectProperty<Method> methodProperty()
    {
        if (method == null)
        {
            method = new SimpleObjectProperty<Method>(this, CalendarProperty.METHOD.toString());
            orderer().registerSortOrderProperty(method);
        }
        return method;
    }
    public Method getMethod() { return (method == null) ? null : methodProperty().get(); }
    private ObjectProperty<Method> method;
    public void setMethod(String method) { setMethod(Method.parse(method)); }
    public void setMethod(Method method) { methodProperty().set(method); }
    public void setMethod(MethodType method) { methodProperty().set(new Method(method)); }
    public VCalendar withMethod(Method method)
    {
        setMethod(method);
        return this;
    }
    public VCalendar withMethod(MethodType method)
    {
        setMethod(method);
        return this;
    }
    public VCalendar withMethod(String method)
    {
        setMethod(method);
        return this;
    }
    
    /**
     * PRODID: RFC 5545 iCalendar 3.7.3. page 78
     * This property specifies the identifier for the product that
     * created the iCalendar object
     * 
     * This project is named JFxtras iCalendar
     * 
     * Example:
     * PRODID:-//JFxtras//JFXtras iCalendar 1.0//EN
     * */
    ObjectProperty<ProductIdentifier> productIdentifierProperty()
    {
        if (productIdentifier == null)
        {
            productIdentifier = new SimpleObjectProperty<ProductIdentifier>(this, CalendarProperty.PRODUCT_IDENTIFIER.toString());
            orderer().registerSortOrderProperty(productIdentifier);
        }
        return productIdentifier;
    }
    public ProductIdentifier getProductIdentifier() { return (productIdentifier == null) ? null : productIdentifierProperty().get(); }
    private ObjectProperty<ProductIdentifier> productIdentifier;
    public void setProductIdentifier(String productIdentifier) { setProductIdentifier(ProductIdentifier.parse(productIdentifier)); }
    public void setProductIdentifier(ProductIdentifier productIdentifier) { productIdentifierProperty().set(productIdentifier); }
    public VCalendar withProductIdentifier(ProductIdentifier productIdentifier)
    {
        setProductIdentifier(productIdentifier);
        return this;
    }
    public VCalendar withProductIdentifier(String productIdentifier)
    {
        setProductIdentifier(productIdentifier);
        return this;
    }
    
    /**
     *  VERSION: RFC 5545 iCalendar 3.7.4. page 79
     * This property specifies the identifier corresponding to the
     * highest version number or the minimum and maximum range of the
     * iCalendar specification that is required in order to interpret the
     * iCalendar object.
     * 
     * This project complies with version 2.0
     * 
     * Example:
     * VERSION:2.0
     * */
    ObjectProperty<Version> versionProperty()
    {
        if (version == null)
        {
            version = new SimpleObjectProperty<Version>(this, CalendarProperty.VERSION.toString());
            orderer().registerSortOrderProperty(version);
        }
        return version;
    }
    public Version getVersion() { return (version == null) ? null : versionProperty().get(); }
    private ObjectProperty<Version> version;
    public void setVersion(String version) { setVersion(Version.parse(version)); }
    public void setVersion(Version version) { versionProperty().set(version); }
    public VCalendar withVersion(Version version)
    {
        setVersion(version);
        return this;
    }
    public VCalendar withVersion(String version)
    {
        setVersion(version);
        return this;
    }
    public VCalendar withVersion()
    {
        setVersion(new Version());
        return this;
    }

    /**
     * Provides a framework for defining non-standard properties.
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
    public VCalendar withNonStandard(String...nonStandardProps)
    {
        List<NonStandardProperty> a = Arrays.stream(nonStandardProps)
                .map(c -> NonStandardProperty.parse(c))
                .collect(Collectors.toList());
        setNonStandard(FXCollections.observableArrayList(a));
        return this;
    }
    /**
     * Sets the value of the {@link #nonStandardProperty()}
     * 
     * @return - this class for chaining
     */
    public VCalendar withNonStandard(ObservableList<NonStandardProperty> nonStandardProps)
    {
        setNonStandard(nonStandardProps);
        return this;
    }
    /**
     * Sets the value of the {@link #nonStandardProperty()} from a vararg of {@link NonStandardProperty} objects.
     * 
     * @return - this class for chaining
     */    
    public VCalendar withNonStandard(NonStandardProperty...nonStandardProps)
    {
        setNonStandard(FXCollections.observableArrayList(nonStandardProps));
        return this;
    }
    
//    /**
//     *<p>Allows other properties registered
//     * with IANA to be specified in any calendar components.</p>
//     */
//    public ObjectProperty<ObservableList<IANAProperty>> ianaProperty()
//    {
//        if (ianaProps == null)
//        {
//            ianaProps = new SimpleObjectProperty<>(this, PropertyType.IANA_PROPERTY.toString());
//        }
//        return ianaProps;
//    }
//    public ObservableList<IANAProperty> getIana()
//    {
//        return (ianaProps == null) ? null : ianaProps.get();
//    }
//    private ObjectProperty<ObservableList<IANAProperty>> ianaProps;
//    public void setIana(ObservableList<IANAProperty> ianaProps)
//    {
//        if (ianaProps != null)
//        {
//            orderer().registerSortOrderProperty(ianaProps);
//        } else
//        {
//            orderer().unregisterSortOrderProperty(ianaProperty().get());
//        }
//        ianaProperty().set(ianaProps);
//    }
//    /**
//     * Sets the value of the {@link #IANAProperty()} by parsing a vararg of
//     * iCalendar content text representing individual {@link IANAProperty} objects.
//     * 
//     * @return - this class for chaining
//     */
//    public VCalendar withIana(String...ianaProps)
//    {
//        List<IANAProperty> a = Arrays.stream(ianaProps)
//                .map(c -> IANAProperty.parse(c))
//                .collect(Collectors.toList());
//        setIana(FXCollections.observableArrayList(a));
//        return this;
//    }
//    /**
//     * Sets the value of the {@link #IANAProperty()}
//     * 
//     * @return - this class for chaining
//     */
//    public VCalendar withIana(ObservableList<IANAProperty> ianaProps)
//    {
//        setIana(ianaProps);
//        return this;
//    }
//    /**
//     * Sets the value of the {@link #IANAProperty()} from a vararg of {@link IANAProperty} objects.
//     * 
//     * @return - this class for chaining
//     */ 
//    public VCalendar withIana(IANAProperty...ianaProps)
//    {
//        setIana(FXCollections.observableArrayList(ianaProps));
//        return this;
//    }
//    
    /*
     * Calendar Components
     */

    /** 
     * VEVENT: RFC 5545 iCalendar 3.6.1. page 52
     * 
     * A grouping of component properties that describe an event.
     * 
     */
    public ObservableList<VEvent> getVEvents() { return vEvents; }
    private ObservableList<VEvent> vEvents = FXCollections.observableArrayList();
//    private ObservableList<VEvent> vEvents = FXCollections.observableArrayList(
//            (VEvent v) -> new Observable[] { v.summaryProperty(), v.descriptionProperty(), v.dateTimeStartProperty(), v.recurrenceRuleProperty() });
    // TODO - ADD MORE PROPERTIES TO ABOVE EXTRACTOR
    public void setVEvents(ObservableList<VEvent> vEvents) { this.vEvents = vEvents; }
    public VCalendar withVEvent(ObservableList<VEvent> vEvents)
    {
        setVEvents(vEvents);
        return this;
    }
    public VCalendar withVEvent(String...vEvents)
    {
        Arrays.stream(vEvents).forEach(c -> getVEvents().add(VEvent.parse(c)));
        return this;
    }
    public VCalendar withVEvents(VEvent...vEvents)
    {
        getVEvents().addAll(vEvents);
        return this;
    }
  
    /** 
     * VTODO: RFC 5545 iCalendar 3.6.2. page 55
     * 
     * A grouping of component properties that describe a task that needs to be completed.
     * 
     */
    public ObservableList<VTodo> getVTodos() { return vTodos; }
    private ObservableList<VTodo> vTodos = FXCollections.observableArrayList();
    public void setVTodos(ObservableList<VTodo> vTodos) { this.vTodos = vTodos; }
    public VCalendar withVTodo(ObservableList<VTodo> vTodos)
    {
        setVTodos(vTodos);
        return this;
    }
    public VCalendar withVTodo(String...vTodos)
    {
        Arrays.stream(vTodos).forEach(c -> getVTodos().add(VTodo.parse(c)));
        return this;
    }
    public VCalendar withVTodos(VTodo...vTodos)
    {
        getVTodos().addAll(vTodos);
        return this;
    }
 
    /** 
     * VJOURNAL: RFC 5545 iCalendar 3.6.3. page 57
     * 
     * A grouping of component properties that describe a task that needs to be completed.
     * 
     * @see VComponent
     * @see VJournal
     */
    public ObservableList<VJournal> getVJournals() { return vJournals; }
    private ObservableList<VJournal> vJournals = FXCollections.observableArrayList();
    public void setVJournals(ObservableList<VJournal> vJournals) { this.vJournals = vJournals; }
    public VCalendar withVJournal(ObservableList<VJournal> vJournals) { setVJournals(vJournals); return this; }
    public VCalendar withVJournal(String...vJournals)
    {
        Arrays.stream(vJournals).forEach(c -> getVJournals().add(VJournal.parse(c)));
        return this;
    }
    public VCalendar withVJournals(VJournal...vJournals)
    {
        getVJournals().addAll(vJournals);
        return this;
    }

    /** 
     * VFREEBUSY: RFC 5545 iCalendar 3.6.4. page 59
     * 
     * @see VFreeBusy
     */
    public ObservableList<VFreeBusy> getVFreeBusies() { return vFreeBusys; }
    private ObservableList<VFreeBusy> vFreeBusys = FXCollections.observableArrayList();
    public void setVFreeBusys(ObservableList<VFreeBusy> vFreeBusys) { this.vFreeBusys = vFreeBusys; }
    public VCalendar withVFreeBusy(ObservableList<VFreeBusy> vFreeBusys) { setVFreeBusys(vFreeBusys); return this; }
    public VCalendar withVFreeBusy(String...vFreeBusys)
    {
        Arrays.stream(vFreeBusys).forEach(c -> getVFreeBusies().add(VFreeBusy.parse(c)));
        return this;
    }
    public VCalendar withVFreeBusys(VFreeBusy...vFreeBusys)
    {
        getVFreeBusies().addAll(vFreeBusys);
        return this;
    }

    /** 
     * VTIMEZONE: RFC 5545 iCalendar 3.6.5. page 62
     * 
     * @see VTimeZone
     */
    public ObservableList<VTimeZone> getVTimeZones() { return vTimeZones; }
    private ObservableList<VTimeZone> vTimeZones = FXCollections.observableArrayList();
    public void setVTimeZones(ObservableList<VTimeZone> vTimeZones) { this.vTimeZones = vTimeZones; }
    public VCalendar withVTimeZones(ObservableList<VTimeZone> vTimeZones) { setVTimeZones(vTimeZones); return this; }
    public VCalendar withVTimeZones(String...vTimeZones)
    {
        Arrays.stream(vTimeZones).forEach(c -> getVTimeZones().add(VTimeZone.parse(c)));
        return this;
    }
    public VCalendar withVTimeZones(VTimeZone...vTimeZones)
    {
        getVTimeZones().addAll(vTimeZones);
        return this;
    }
    
    /**
     * A convenience method that adds a VComponent to one of the ObservableLists based on
     * its type such as VEVENT, VTODO, etc.
     * 
     * @param newVComponent - VComponent to add
     * @return  true if add was successful, false otherwise
     */
    public boolean addVComponent(VComponent newVComponent)
    {
        if (newVComponent instanceof VEvent)
        {
            getVEvents().add((VEvent) newVComponent);
        } else if (newVComponent instanceof VTodo)
        {
            getVTodos().add((VTodo) newVComponent);            
        } else if (newVComponent instanceof VJournal)
        {
            getVJournals().add((VJournal) newVComponent);
        } else if (newVComponent instanceof VFreeBusy)
        {
            getVFreeBusies().add((VFreeBusy) newVComponent);            
        } else if (newVComponent instanceof VTimeZone)
        {
            getVTimeZones().add((VTimeZone) newVComponent);            
        } else
        {
            throw new RuntimeException("Unsuppored VComponent type:" + newVComponent.getClass());
        }
        return true;
    }
    
    public boolean removeVComponent(VComponent vComponent)
    {
        if (vComponent instanceof VEvent)
        {
            return getVEvents().remove(vComponent);
        } else if (vComponent instanceof VTodo)
        {
            return getVTodos().remove(vComponent);            
        } else if (vComponent instanceof VJournal)
        {
            return getVJournals().remove(vComponent);
        } else if (vComponent instanceof VFreeBusy)
        {
            return getVFreeBusies().remove(vComponent);            
        } else if (vComponent instanceof VTimeZone)
        {
            return getVTimeZones().remove(vComponent);            
        } else
        {
            throw new RuntimeException("Unsuppored VComponent type:" + vComponent.getClass());
        }
    }
    
    /** Create a VComponent by parsing component text and add it to the appropriate list 
     * @see #addVComponent(VComponent)*/
    @Deprecated // not worth having
    public void addVComponent(String contentText)
    {
        VComponent vComponent = SimpleVComponentFactory.emptyVComponent(contentText);
        vComponent.parseContent(contentText);
        addVComponent(vComponent);
    }
    
    /** Add a collection of {@link VComponent} to the correct ObservableList based on
    * its type, such as VEVENT, VTODO, etc.
     * 
     * @param newVComponents  collection of {@link VComponent} to add
     * @return  true if add was successful, false otherwise
     */
    public boolean addAllVComponents(Collection<? extends VComponent> newVComponents)
    {
        return newVComponents.stream().map(v -> addVComponent(v)).allMatch(b -> true);
    }
    
    /** Add a varargs of {@link VComponent} to the correct ObservableList based on
    * its type, such as VEVENT, VTODO, etc.
     * 
     * @param newVComponents  collection of {@link VComponent} to add
     * @return  true if add was successful, false otherwise
     */
    public boolean addAllVComponents(VComponent... newVComponents)
    {
        return addAllVComponents(Arrays.asList(newVComponents));
    }
    
//    public boolean removeVComponent(VComponent vComponent)
//    {
//        if (vComponent instanceof VEvent)
//        {
//            return getVEvents().remove(vComponent);
//        } else if (vComponent instanceof VTodo)
//        {
//            return getVTodos().remove(vComponent);
//        } else if (vComponent instanceof VJournal)
//        {
//            return getVJournals().remove(vComponent);
//        } else if (vComponent instanceof VFreeBusy)
//        {
//            return getVFreeBusies().remove(vComponent);
//        } else if (vComponent instanceof VTimeZone)
//        {
//            return getVTimeZones().remove(vComponent);
//        } else
//        {
//            throw new RuntimeException("Unsuppored VComponent type:" + vComponent.getClass());
//        }
//    }
    
    /** Convenience method that returns all {@link VComponent VComponents} regardless of type (e.g.
     * {@link VEvent}, {@link VTodo}, etc.) 
     * @return  unmodifiable list of all {@link VComponent VComponents}
     */
    public List<VComponent> getAllVComponents()
    {
        List<VComponent> vComponents = new ArrayList<>();
        vComponents.addAll(getVEvents());
        vComponents.addAll(getVTodos());
        vComponents.addAll(getVJournals());
        vComponents.addAll(getVFreeBusies());
        vComponents.addAll(getVTimeZones());
        return Collections.unmodifiableList(vComponents);
    }
    
    /** Convenience method that returns all {@link VComponent VComponents} regardless of type (e.g.
     * {@link VEvent}, {@link VTodo}, etc.) 
     * 
     * @return  unmodifiable list of all {@link VComponent VComponents}
     */
    public List<? extends VComponent> getVComponents(Class<? extends VComponent> vComponentClass)
    {
        if (vComponentClass.equals(VEvent.class))
        {
            return getVEvents();
        } else if (vComponentClass.equals(VTodo.class))
        {
            return getVTodos();            
        } else if (vComponentClass.equals(VJournal.class))
        {
            return getVJournals();            
        } else if (vComponentClass.equals(VFreeBusy.class))
        {
            return getVFreeBusies();            
        } else if (vComponentClass.equals(VTimeZone.class))
        {
            return getVTimeZones();            
        } else
        {
            throw new RuntimeException("Unsuppored VComponent type:" + vComponentClass);
        }
    }
    
    /**
     * A convenience method that returns parent list of the {@link VComponent} parameter.
     * Returns null if component is not in any {@link VComponent} list.
     * 
     * @param vComponent - VComponent to look up
     */
    public List<? extends VComponent> getVComponents(VComponent vComponent)
    {
        if (vComponent instanceof VEvent)
        {
            return (getVEvents().contains(vComponent)) ? getVEvents() : null;
        } else if (vComponent instanceof VTodo)
        {
            return (getVTodos().contains(vComponent)) ? getVTodos() : null;
        } else if (vComponent instanceof VJournal)
        {
            return (getVJournals().contains(vComponent)) ? getVJournals() : null;
        } else if (vComponent instanceof VFreeBusy)
        {
            return (getVFreeBusies().contains(vComponent)) ? getVFreeBusies() : null;
        } else if (vComponent instanceof VTimeZone)
        {
            return (getVTimeZones().contains(vComponent)) ? getVTimeZones() : null;
        } else
        {
            throw new RuntimeException("Unsuppored VComponent type:" + vComponent.getClass());
        }
    }
    
    
    /** set AbstractITIPFactory to handle processing input VCalendar based on {@link Method} */
    public void setMethodProcessFactory(AbstractITIPFactory iTIPFactory)
    {
        this.iTIPFactory = iTIPFactory;
    }
    /** get AbstractITIPFactory to handle processing input VCalendar based on {@link Method} */
    public AbstractITIPFactory getITIPFactory()
    {
        return iTIPFactory;
    }
    private AbstractITIPFactory iTIPFactory;
    
    /**
     * Process the exchange of iCalendar object according to the iTIP methods identifies in RFC 5546
     * based on the methods in {@link #getITIPFactory()}
     * 
     * @param iTIPMessages  iTIP VCalendars to process with {@link Method} populated
     * @return - log of process operation
     */
    public List<String> processITIPMessage(VCalendar... iTIPMessages)
    {
        return processITIPMessage(Arrays.asList(iTIPMessages));
    }
    
    /**
     * Process the exchange of iCalendar object according to the iTIP methods identifies in RFC 5546
     * based on the methods in {@link #getITIPFactory()}
     * 
     * @param iTIPMessages  iTIP VCalendars to process with {@link Method} populated
     * @return - log of process operation
     */
    public List<String> processITIPMessage(Collection<VCalendar> iTIPMessages)
    {
        List<String> log = new ArrayList<>();
        iTIPMessages.forEach(message ->
        {
            final Processable methodProcess;
            if (message.getMethod() == null)
            { // default to PUBLISH method if not present
                methodProcess = getITIPFactory().getITIPMessageProcess(MethodType.PUBLISH);
 //               throw new IllegalArgumentException("VCalendar to be processed MUST have the METHOD property populated");
            } else
            {
                MethodType method = message.getMethod().getValue();
                methodProcess = getITIPFactory().getITIPMessageProcess(method);
            }
            List<String> methodLog = methodProcess.process(this, message);
            log.addAll(methodLog);
        });
        return log;
    }
    
    /**
     * Process the exchange of iCalendar object according to the iTIP methods identifies in RFC 5546.
     * Input string can contain multiple iTIP VCALENDAR messages.
     * 
     * @param iTIPMessages  iTIP VCalendar Message strings
     * @return - log of process operation
     */
    public List<String> processITIPMessage(String iTIPMessages)
    {
        List<String> log = new ArrayList<>();
        List<String> iTIPMessageList = new ArrayList<>();
        StringBuilder builder = new StringBuilder(1000);
        for(String line : iTIPMessages.split(System.lineSeparator()))
        {
            if (line.equals("BEGIN:VCALENDAR") && builder.length() > 0)
            {
                iTIPMessageList.add(builder.toString());
                builder = new StringBuilder(1000);
            }
            builder.append(line + System.lineSeparator());
        }
        // handle last VCalendar message
        if (builder.length() > 0)
        {
            iTIPMessageList.add(builder.toString());
        }
        iTIPMessageList.forEach(message -> 
        {
            List<String> methodLog = processITIPMessage(VCalendar.parse(message));
            log.addAll(methodLog);
        });
        return log;
    }
    
    /**
     * Parse component text to new VComponent with {@link RequestStatus REQUEST-STATUS} properties containing 
     * the result of the process, such as success message or error report.
     * 
     * @param contentText  iCalendar content lines
     * @return  the created VComponent with {@link RequestStatus REQUEST-STATUS} populated to indicate success or failuer.
     */
    @Deprecated // replace with addVCalendar
    public VComponent importVComponent(String contentText)
    {
        VPersonal<?> vComponent = (VPersonal<?>) SimpleVComponentFactory.emptyVComponent(contentText);
        List<String> contentLines = Arrays.asList(contentText.split(System.lineSeparator()));
        UnfoldingStringIterator unfoldedLines = new UnfoldingStringIterator(contentLines.iterator());
//        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(contentLines).iterator();
        boolean useRequestStatus = true;
        vComponent.parseContent(unfoldedLines, useRequestStatus);
//        requestStatusErrors.stream().forEach(System.out::println);
        // TODO - only check conflict if opaque
        String conflict = (vComponent instanceof VEvent) ? DateTimeUtilities.checkScheduleConflict((VEvent) vComponent, getVEvents()) : null;
        if (conflict != null)
        {
            final ObservableList<RequestStatus> requestStatus;
            if (vComponent.getRequestStatus() == null)
            {
                requestStatus = FXCollections.observableArrayList();
                vComponent.setRequestStatus(requestStatus);
            } else
            {
                requestStatus = vComponent.getRequestStatus();
            }
            // remove success REQUEST-STATUS message, if present
            Iterator<RequestStatus> rsIterator = requestStatus.iterator();
            while (rsIterator.hasNext())
            {
                RequestStatus rs = rsIterator.next();
                if (rs.getValue().substring(0, 3).equals("2.0"))
                {
                    rsIterator.remove();
                }
            }
            requestStatus.add(RequestStatus.parse("4.1;Event conflict with " + conflict));
        }
        
        final boolean isVComponentValidToAdd;
        if (vComponent.getRequestStatus() == null)
        {
            isVComponentValidToAdd = true;
        } else
        {
            isVComponentValidToAdd = ! vComponent.getRequestStatus().stream()
                    .map(s -> s.getValue())
                    .anyMatch(s -> (s.charAt(0) == '3') || (s.charAt(0) == '4')); // error codes start with 3 or 4
        }
        
        if (isVComponentValidToAdd)
        {
            addVComponent(vComponent);
        }
        return vComponent;
    }
    
    // TODO - NEED TO GO IN DATE TIME UTILITIES
    /*
     * need list of recurrences and duration from vComponent
     * make list of recurrences and duration for all other opaque VEvents
     * check if ANY recurrences are in between any others
     */
//    private final static int CONFLICT_CHECK_QUANTITY = 100;
//    private boolean checkScheduleConflict(VEvent vComponent)
//    {
//        // must be opaque to cause conflict, opaque is default
//        TimeTransparencyType newTransparency = (vComponent.getTimeTransparency() == null) ? TimeTransparencyType.OPAQUE : vComponent.getTimeTransparency().getValue();
//        if (newTransparency == TimeTransparencyType.TRANSPARENT)
//        {
//            return false;
//        }
//        
//        LocalDate dtstart = LocalDate.from(vComponent.getDateTimeStart().getValue());
//        TemporalAmount duration = vComponent.getActualDuration();
//        
//        // Make list of Pairs containing start and end temporals
//        List<Pair<Temporal,Temporal>> eventTimes = new ArrayList<>();
//        for (VEvent v : getVEvents())
//        {
//            // can only conflict with opaque events, opaque is default
//            TimeTransparencyType myTransparency = (v.getTimeTransparency() == null) ? TimeTransparencyType.OPAQUE : v.getTimeTransparency().getValue();
//            if (myTransparency == TimeTransparencyType.OPAQUE)
//            {
//                Temporal myDTStart = v.getDateTimeStart().getValue().with(dtstart);
//                TemporalAmount actualDuration = v.getActualDuration();
//                v.streamRecurrences(myDTStart)
//                        .limit(CONFLICT_CHECK_QUANTITY)
//                        .forEach(t -> eventTimes.add(new Pair<>(t, t.plus(actualDuration))));                
//            }
//        }
//        
//        /* Check for conflicts:
//         * Start and End must NOT:
//         *  Be after an event start
//         *  Be before same event end
//         *  Is new start before the existing end
//         *  Is new start after the existing start
//         */
//        return vComponent.streamRecurrences()
//                .limit(CONFLICT_CHECK_QUANTITY)
//                .anyMatch(newStart ->
//                {
//                    Temporal newEnd = newStart.plus(duration);
//                    return eventTimes.stream().anyMatch(p ->
//                    {
//                        Temporal existingStart = p.getKey();
//                        Temporal existingEnd = p.getValue();
//                        // test start
//                        boolean isAfter = DateTimeUtilities.isAfter(newStart, existingStart);
//                        if (isAfter)
//                        {
//                            return DateTimeUtilities.isBefore(newStart, existingEnd);
//                        }
//                        // test end
//                        boolean isAfter2 = DateTimeUtilities.isAfter(newEnd, existingStart);
//                        if (isAfter2)
//                        {
//                            return DateTimeUtilities.isBefore(newEnd, existingEnd);
//                        }
//                        return false;
//                    });
//                });
//    }
    
    /**
     * Import new VComponent with {@link RequestStatus REQUEST-STATUS} properties containing 
     * the result of the process, such as success message or error report.
     * 
     * @param contentText  iCalendar content lines
     * @return  list of error messages if import failed, null if successful
     */
    @Deprecated
    public List<String> importVComponent(VComponent newVComponent)
    {
        throw new RuntimeException("not implemented");
    }
    
    private Map<String, List<VDisplayable<?>>> uidComponentsMap = new HashMap<>(); // public for testing
    /**
     * Map of Related Components - UID is key and List of all related VComponents is value.
     * Note: if you only want child components you need to filter the list to only include components
     * that have a RECURRENCE-ID
     */
    public Map<String, List<VDisplayable<?>>> uidComponentsMap() { return Collections.unmodifiableMap(uidComponentsMap); }
    
    /**
     * RecurrenceID listener
     * notifies parents when a child component with recurrenceID is created or removed
     * also maintains {@link #uidToComponentMap}
     */
    private ListChangeListener<VDisplayable<?>> displayableListChangeListener = (ListChangeListener.Change<? extends VDisplayable<?>> change) ->
    {

        while (change.next())
        {
//            System.out.println("remove orphans here1:" + change.wasReplaced() + " " + change.wasAdded() + " " + change.wasRemoved());
            if (change.wasReplaced())
            {
//                System.out.println("replaced:" + change); 
            } else if (change.wasAdded())
            {
                change.getAddedSubList().forEach(vComponent -> 
                {
                    // set recurrence children callback (VComponents having RecurrenceIDs and matching UID to a recurrence parent)
                    vComponent.setRecurrenceChildrenListCallBack( (c) ->
                    {
                        if (c.getUniqueIdentifier() == null) return null;
                        return uidComponentsMap.get(c.getUniqueIdentifier().getValue())
                                .stream()
                                .filter(v -> v.getRecurrenceId() != null) // keep only children objects
                                .collect(Collectors.toList());
                    });
                    // set recurrence parent callback (the VComponent with matching UID and no RECURRENCEID)
                    vComponent.setRecurrenceParentListCallBack( (c) ->
                    {
                        if (c.getUniqueIdentifier() == null) return null;
                        return uidComponentsMap.get(c.getUniqueIdentifier().getValue())
                                .stream()
                                .filter(v -> v.getRecurrenceId() == null) // parents don't have RECURRENCEID
                                .findAny()
                                .orElse(null);
                    });
                    // add VComponent to map
                    if (vComponent.getUniqueIdentifier() != null)
                    {
                        String uid = vComponent.getUniqueIdentifier().getValue();
                        final List<VDisplayable<?>> relatedComponents;
                        if (uidComponentsMap.get(uid) == null)
                        {
                            relatedComponents = new ArrayList<>();
                            uidComponentsMap.put(uid, relatedComponents);
                        } else
                        {
                            relatedComponents = uidComponentsMap.get(uid);
                        }
                        relatedComponents.add(vComponent);
                    }
//                    deleteOrphans(vComponent);
                });
            } else if (change.wasRemoved())
            {
//                System.out.println("remove orphans here2:");
                change.getRemoved().forEach(vComponent -> 
                {
                    String uid = vComponent.getUniqueIdentifier().getValue();
                    List<VDisplayable<?>> relatedComponents = uidComponentsMap.get(uid);
                    if (relatedComponents != null)
                    {
                        relatedComponents.remove(vComponent);
//                        System.out.println("remove orphans here3:");
                        if (relatedComponents.isEmpty())
                        {
                            uidComponentsMap.remove(uid);
                        }
                    }
                    // TODO - CHECK FOR ORPHANED CHILDREN
                    // NEED TO MAKE SURE A NEWLY ADDED COMPONENT CAN CLAIM SOME CHILDREN
                });         
            } else
            {
                System.out.println(change);
                // CHECK FOR ORPHANED CHILDREN HERE
//                int fromIndex = change.getFrom();
//                int toIndex = change.getTo();
////                change.reset();
//                change.getList().subList(fromIndex, toIndex).forEach(v -> System.out.println(v.getRecurrenceRule()));
//                change.getList().forEach(v -> System.out.println(System.identityHashCode(v)));
                // SOME OTHER LISTENERS MUST NOT HAVE FIRED YET CAUSING TOCONTENT TO BE WRONG
                // Maybe I can check for orphans anyway.
                
//                change.getRemoved().forEach(System.out::println);
//                System.exit(0);
            }
        }
//        change.getList().forEach(v -> System.out.println(v));
    };
    
//    // Assumes the component is the only one with the children attached to it.
//    // If a replace operation happened, the old component must be removed, but not its
//    /*
//     * If modify
//     */
//    @Deprecated // causes UnsupportedOperationException when called from displayableListChangeListener
//    // Move to VDisplayable
//    public void deleteOrphans(VDisplayable<?> vDisplayable)
//    {
//        boolean isParent = vDisplayable.getRecurrenceId() == null;
////        List<VDisplayable<?>> orhpanedChildren = Collections.emptyList(); // initialize with empty list
//        if (isParent)
//        {
//            final String uid = vDisplayable.getUniqueIdentifier().getValue();
//            List<VDisplayable<?>> orhpanedChildren = uidComponentsMap().get(uid)
//                    .stream()
//                    .filter(v -> v.getRecurrenceId() != null)
//                    .filter(v -> 
//                    {
//                        Temporal myRecurrenceID = v.getRecurrenceId().getValue();
//                        Temporal cacheStart = vDisplayable.recurrenceCache().getClosestStart(myRecurrenceID);
//                        Temporal nextRecurrenceDateTime = vDisplayable.getRecurrenceRule().getValue()
//                                .streamRecurrences(cacheStart)
//                                .filter(t -> ! DateTimeUtilities.isBefore(t, myRecurrenceID))
//                                .findFirst()
//                                .orElseGet(() -> null);
//                        return ! Objects.equals(nextRecurrenceDateTime, myRecurrenceID);
//                    })
//                    .collect(Collectors.toList());
//            System.out.println("orhpanedChildren:" + orhpanedChildren);
//            if (! orhpanedChildren.isEmpty())
//            {
//                getVComponents(vDisplayable.getClass()).removeAll(orhpanedChildren);
//            }
//        }
//    }
    
//    /*
//     * SORT ORDER FOR CHILD ELEMENTS
//     */
//    final private Orderer orderer;
//    @Override
//    public Orderer orderer() { return orderer; }
    
//    private Callback<VElement, Void> copyChildElementCallback = (child) ->
//    {
//        CalendarElementType type = CalendarElementType.enumFromClass(child.getClass());
//        if (type != null)
//        { // Note: if type is null then element is a subcomponent such as a VALARM, STANDARD or DAYLIGHT and copying happens in subclasses
//            type.copyChild(child, this);
//        }
//        return null;
//    };
    
    @Override
    @Deprecated
    protected Callback<VChild, Void> copyIntoCallback()
    {        
        return (child) ->
        {
            CalendarComponent type = CalendarComponent.enumFromClass(child.getClass());
            if (type != null)
            {
                type.copyChild(child, this);
            } else
            {
                CalendarProperty property = CalendarProperty.enumFromClass(child.getClass());
                if (property != null)
                {
                    property.copyChild(child, this);
                }
            }
            return null;
        };
    }
    
    @Override
    public void copyInto(VParent destination)
    {
        super.copyInto(destination);
        childrenUnmodifiable().forEach((childSource) -> 
        {
            CalendarComponent componentType = CalendarComponent.enumFromClass(childSource.getClass());
            if (componentType != null)
            {
                componentType.copyChild(childSource, (VCalendar) destination);
            } else
            {
                CalendarProperty propertyType = CalendarProperty.enumFromClass(childSource.getClass());
                if (propertyType != null)
                {
                    propertyType.copyChild(childSource, (VCalendar) destination);
                }
            }
        });
    }
    
    /*
     * CONSTRUCTORS
     */
    
    public VCalendar()
    {
        setMethodProcessFactory(new DefaultITIPFactory());
        addListeners();
        setContentLineGenerator(new MultiLineContent(
                orderer(),
                FIRST_CONTENT_LINE,
                LAST_CONTENT_LINE,
                1000));
//        setVersion(new Version());
    }
  
    /** Copy constructor */
    public VCalendar(VCalendar source)
    {
        this();
        source.copyInto(this);
//        copyChildrenFrom(source);    
    }

    /*
     * OTHER METHODS
     */
    
    private void addListeners()
    {
        // listeners to keep map to related components from UID string
        getVEvents().addListener(displayableListChangeListener);
        getVTodos().addListener(displayableListChangeListener);
        getVJournals().addListener(displayableListChangeListener);

        // Sort order listeners
        orderer().registerSortOrderProperty(getVEvents());
        orderer().registerSortOrderProperty(getVTodos());
        orderer().registerSortOrderProperty(getVJournals());
        orderer().registerSortOrderProperty(getVTimeZones());
        orderer().registerSortOrderProperty(getVFreeBusies());
    }
    
    @Override
    public String toString()
    {
        return super.toString() + " " + toContent();
    }
    
    @Override
    public List<String> parseContent(String content)
    {
        Iterator<String> lineIterator = Arrays.asList(content.split(System.lineSeparator())).iterator();
        return parseContent(lineIterator)
                .entrySet()
                .stream()
                .flatMap(e -> e.getValue().stream().map(v -> e.getKey().name() + ":" + v))
                .collect(Collectors.toList());
    }

    /** Parse unfolded content line iterator into calendar object */
    public Map<VElement, List<String>> parseContent(Iterator<String> unfoldedLineIterator)
    {
        boolean useResourceStatus = false;
        return parseContent(unfoldedLineIterator, useResourceStatus);
    }
    
    /** Parse unfolded content lines into calendar object */
    public Map<VElement, List<String>> parseContent(Iterator<String> lineIterator, boolean collectErrorMessages)
    {
        List<String> vCalendarMessages = new ArrayList<>();
        Map<VElement, List<String>> messageMap = new HashMap<>();
        String firstLine = lineIterator.next();
        if (! firstLine.equals("BEGIN:VCALENDAR"))
        {
            throw new IllegalArgumentException("Content lines must begin with BEGIN:VCALENDAR");
        }
        // wrap lineIterator in UnfoldingStringIterator decorator
        UnfoldingStringIterator unfoldedLineIterator = new UnfoldingStringIterator(lineIterator);
        while (unfoldedLineIterator.hasNext())
        {
            String unfoldedLine = unfoldedLineIterator.next();
            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(unfoldedLine);
            String propertyName = (nameEndIndex > 0) ? unfoldedLine.substring(0, nameEndIndex) : "";
            
            // Parse component
            if (propertyName.equals("BEGIN"))
            {
                String componentName = unfoldedLine.substring(nameEndIndex+1);
                VComponent newComponent = SimpleVComponentFactory.emptyVComponent(componentName);
                Map<VElement, List<String>> newComponentMessages = newComponent.parseContent(unfoldedLineIterator, collectErrorMessages);
                addVComponent(newComponent);
                messageMap.putAll(newComponentMessages);
//                messages.addAll(myMessages);
            } else if (propertyName.equals("END"))
            {
                break;
            } else
            { // parse calendar property
                VChild child = null;
                CalendarProperty elementType = CalendarProperty.enumFromName(propertyName);
                if (elementType != null)
                {
                    child = elementType.parse(this, unfoldedLine);
                } else if (unfoldedLine.contains(":"))
                {
                    //non-standard - check for X- prefix
                    boolean isNonStandard = propertyName.substring(0, PropertyType.NON_STANDARD.toString().length()).equals(PropertyType.NON_STANDARD.toString());
                    if (isNonStandard)
                    {
                        child = CalendarProperty.NON_STANDARD.parse(this, unfoldedLine);
                    } else
                    {
                        // ignore unknown properties
                        vCalendarMessages.add("Unknown property is ignored:" + unfoldedLine);
                    }
                } else
                {
                    vCalendarMessages.add("Unknown line is ignored:" + unfoldedLine);                    
                }
                if (child != null) vCalendarMessages.addAll(child.errors());
            }
        }
     // TODO - Log status messages if not using RequestStatus
        messageMap.put(this, vCalendarMessages);
        return messageMap;
    }

//    // multi threaded
//    /** Parse content lines into calendar object */
//    // TODO - TEST THIS - MAY NOT MAINTAIN ORDER
//    // TODO - FIX THIS - DOESN'T WORK, DOESN'T GET CALENDARY PROPERTIES
//    public void parseContentMulti(Iterator<String> lineIterator)
//    {
//        // Callables to generate components
//        ExecutorService service = Executors.newWorkStealingPool();
////        Map<Integer, Callable<Object>> taskMap = new LinkedHashMap<>();
//        Integer order = 0;
//        List<Callable<Object>> tasks = new ArrayList<>();
//        
//        String firstLine = lineIterator.next();
//        if (! firstLine.equals("BEGIN:VCALENDAR"))
//        {
//            throw new IllegalArgumentException("Content lines must begin with BEGIN:VCALENDAR");
//        }
//        while (lineIterator.hasNext())
//        {
//            String line = lineIterator.next();
//            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(line);
//            String propertyName = line.substring(0, nameEndIndex);
//            
//            // Parse component
//            if (propertyName.equals("BEGIN"))
//            {
//                String componentName = line.substring(nameEndIndex+1);
//                List<String> myLines = new ArrayList<>(20);
//                myLines.add(line);
//                final String endLine = "END:" + componentName;
//                while (lineIterator.hasNext())
//                {
//                    String myLine = lineIterator.next();
//                    myLines.add(myLine);
//                    if (myLine.equals(endLine))
//                    {
//                        Integer myOrder = order;
//                        order += 100;
//                        Runnable vComponentRunnable = () -> 
//                        {
//                            CalendarComponent elementType = CalendarComponent.valueOf(componentName);
//                            VElement component = elementType.parse(this, myLines);
////                            orderer().elementSortOrderMap().put((VChild) component, myOrder);
//                        };
////                        taskMap.put(order, Executors.callable(vComponentRunnable));
//                        tasks.add(Executors.callable(vComponentRunnable));
//                        break;
//                    }
//                }
//                
//            // parse calendar properties (ignores unknown properties)
//            } else
//            {
//                CalendarComponent elementType = CalendarComponent.enumFromName(propertyName);
//                if (elementType != null)
//                {
//                    VElement property = elementType.parse(this, Arrays.asList(line));
////                    orderer().elementSortOrderMap().put((VChild) property, order);
////                    order += 100;
//                }
//            }
//        }
//        
//        try
//        {
////            List<Callable<Object>> tasks = taskMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
//            service.invokeAll(tasks);
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//    }
    
    public static VCalendar parse(Reader reader) throws IOException
    {
        BufferedReader br = new BufferedReader(reader);
        UnfoldingStringIterator unfoldedLineIterator = new UnfoldingStringIterator(br.lines().iterator());
//        UnfoldingBufferedReader unfoldingReader = new UnfoldingBufferedReader(reader);
//        Iterator<String> unfoldedLineIterator = unfoldingReader.lines().iterator();
        VCalendar vCalendar = new VCalendar();
        vCalendar.parseContent(unfoldedLineIterator);
//        unfoldingReader.close();
        return vCalendar;
    }
    
    /**
     * Creates a new VCalendar from an ics file
     * 
     * @param icsFilePath  path of ics file to parse
     * @return  Created VCalendar
     * @throws IOException
     */
    public static VCalendar parse(Path icsFilePath) throws IOException
    {
        return parse(Files.newBufferedReader(icsFilePath));
    }
    
    /**
     * Creates a new VCalendar from an ics file
     * 
     * @param icsFilePath  path of ics file to parse
     * @return  Created VCalendar
     * @throws IOException
     */
    // TODO - REMOVE useResourceStatus
    public static VCalendar parseICalendarFile(Path icsFilePath, boolean useResourceStatus) throws IOException
    {
        BufferedReader br = Files.newBufferedReader(icsFilePath);
        List<String> lines = br.lines().collect(Collectors.toList());
//        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(lines).iterator();
        VCalendar vCalendar = new VCalendar();
        vCalendar.parseContent(lines.iterator(), useResourceStatus);
        return vCalendar;
    }
    
    /**
     * Creates a new VCalendar from an ics file
     * 
     * @param icsFilePath  path of ics file to parse
     * @return  Created VCalendar
     * @throws IOException
     */
    // TODO - REMOVE useResourceStatus
    public static VCalendar parseICalendarFile(Path icsFilePath) throws IOException
    {
        BufferedReader br = Files.newBufferedReader(icsFilePath);
        List<String> lines = br.lines().collect(Collectors.toList());
//        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(lines).iterator();
        VCalendar vCalendar = new VCalendar();
        vCalendar.parseContent(lines.iterator());
        return vCalendar;
    }

    public static VCalendar parse(String contentLines)
    {
        VCalendar c = new VCalendar();
        c.parseContent(contentLines);
        return c;
    }
}
