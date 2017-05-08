package jfxtras.icalendarfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.VParentBase;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VPersonal;
import jfxtras.icalendarfx.components.VTimeZone;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.content.MultiLineContent;
import jfxtras.icalendarfx.content.OrdererBase;
import jfxtras.icalendarfx.content.UnfoldingStringIterator;
import jfxtras.icalendarfx.itip.AbstractITIPFactory;
import jfxtras.icalendarfx.itip.DefaultITIPFactory;
import jfxtras.icalendarfx.itip.Processable;
import jfxtras.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.icalendarfx.properties.calendar.Method;
import jfxtras.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.icalendarfx.properties.calendar.Version;
import jfxtras.icalendarfx.properties.calendar.Method.MethodType;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.icalendarfx.utilities.DateTimeUtilities;

/**
 * iCalendar Object
 * RFC 5545, 3.4, page 50
 * 
 * Parent calendar object represents a collection of calendaring and scheduling information 
 * 
 * @author David Bal
 *
 */
public class VCalendar extends VParentBase<VCalendar>
{
    // version of this project, not associated with the iCalendar specification version
//    public static String myVersion = "1.0";
    private static final String NAME = "VCALENDAR";
    @Override public String name() { return NAME; }
    private static final String FIRST_CONTENT_LINE = BEGIN + NAME;
    private static final String LAST_CONTENT_LINE = END + NAME;
    
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
    public CalendarScale getCalendarScale() { return calendarScale; }
    private CalendarScale calendarScale;
    public void setCalendarScale(String calendarScale) { setCalendarScale(CalendarScale.parse(calendarScale)); }
    public void setCalendarScale(CalendarScale calendarScale)
    {
    	this.calendarScale = calendarScale;
    	orderChild(calendarScale);
	}
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
    public Method getMethod() { return method; }
    private Method method;
    public void setMethod(String method) { setMethod(Method.parse(method)); }
    public void setMethod(Method method)
    {
    	this.method = method;
    	orderChild(method);
	}
    public void setMethod(MethodType method) { setMethod(new Method(method)); }
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
    public ProductIdentifier getProductIdentifier() { return productIdentifier; }
    private ProductIdentifier productIdentifier;
    public void setProductIdentifier(String productIdentifier) { setProductIdentifier(ProductIdentifier.parse(productIdentifier)); }
    public void setProductIdentifier(ProductIdentifier productIdentifier)
    {
    	this.productIdentifier = productIdentifier;
    	orderChild(productIdentifier);
	}
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
    public Version getVersion() { return version; }
    private Version version;
    public void setVersion(String version) { setVersion(Version.parse(version)); }
    public void setVersion(Version version)
    {
    	this.version = version;
    	orderChild(version);
	}
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
    private List<NonStandardProperty> nonStandardProps;
    public List<NonStandardProperty> getNonStandard() { return nonStandardProps; }
    public void setNonStandard(List<NonStandardProperty> nonStandardProps)
    {
    	if (this.nonStandardProps != null)
    	{
    		this.nonStandardProps.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.nonStandardProps = nonStandardProps;
    	if (nonStandardProps != null)
    	{
    		nonStandardProps.forEach(c -> orderChild(c)); // order new elements
    	}
	}
    /**
     * Sets the value of the {@link #nonStandardProperty()}
     * 
     * @return - this class for chaining
     */
    public VCalendar withNonStandard(List<NonStandardProperty> nonStandardProps)
    {
    	if (getNonStandard() == null)
    	{
        	setNonStandard(new ArrayList<>());
    	}
    	getNonStandard().addAll(nonStandardProps);
    	if (nonStandardProps != null)
    	{
    		nonStandardProps.forEach(c -> orderChild(c));
    	}
        return this;
    }
    /**
     * Sets the value of the {@link #nonStandardProperty()} by parsing a vararg of
     * iCalendar content text representing individual {@link NonStandardProperty} objects.
     * 
     * @return - this class for chaining
     */
    public VCalendar withNonStandard(String...nonStandardProps)
    {
        List<NonStandardProperty> newElements = Arrays.stream(nonStandardProps)
                .map(c -> NonStandardProperty.parse(c))
                .collect(Collectors.toList());
        return withNonStandard(newElements);
    }
    /**
     * Sets the value of the {@link #nonStandardProperty()} from a vararg of {@link NonStandardProperty} objects.
     * 
     * @return - this class for chaining
     */    
    public VCalendar withNonStandard(NonStandardProperty...nonStandardProps)
    {
    	return withNonStandard(Arrays.asList(nonStandardProps));
    }
   
    /*
     * Calendar Components
     */

    /** 
     * VEVENT: RFC 5545 iCalendar 3.6.1. page 52
     * 
     * A grouping of component properties that describe an event.
     * 
     */
    public List<VEvent> getVEvents() { return vEvents; }
    private List<VEvent> vEvents;
    public void setVEvents(List<VEvent> vEvents)
    {
    	if (this.vEvents != null)
    	{
    		this.vEvents.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.vEvents = vEvents;
    	if (vEvents != null)
		{
    		vEvents.forEach(c -> orderChild(c)); // order new elements
		}
	}
    public VCalendar withVEvents(List<VEvent> vEvents)
    {
    	if (getVEvents() == null)
    	{
    		setVEvents(new ArrayList<>());
    	}
    	getVEvents().addAll(vEvents);
    	if (vEvents != null)
    	{
    		vEvents.forEach(c -> orderChild(c));
    	}
        return this;
    }
    public VCalendar withVEvents(String...vEvents)
    {
        List<VEvent> list = Arrays.stream(vEvents)
                .map(c -> VEvent.parse(c))
                .collect(Collectors.toList());
        return withVEvents(list);
    }
    public VCalendar withVEvents(VEvent...vEvents)
    {
        return withVEvents(Arrays.asList(vEvents));
    }
  
    /** 
     * VTODO: RFC 5545 iCalendar 3.6.2. page 55
     * 
     * A grouping of component properties that describe a task that needs to be completed.
     * 
     */
    public List<VTodo> getVTodos() { return vTodos; }
    private List<VTodo> vTodos;
    public void setVTodos(List<VTodo> vTodos)
    {
    	if (this.vTodos != null)
    	{
    		this.vTodos.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.vTodos = vTodos;
    	if (vTodos != null)
		{
    		vTodos.forEach(c -> orderChild(c)); // order new elements
		}
	}
    public VCalendar withVTodos(List<VTodo> vTodos)
    {
    	if (getVTodos() == null)
    	{
    		setVTodos(new ArrayList<>());
    	}
    	getVTodos().addAll(vTodos);
    	if (vTodos != null)
    	{
    		vTodos.forEach(c -> orderChild(c));
    	}
        return this;
    }
    public VCalendar withVTodos(String...vTodos)
    {
        List<VTodo> list = Arrays.stream(vTodos)
                .map(c -> VTodo.parse(c))
                .collect(Collectors.toList());
        return withVTodos(list);
    }
    public VCalendar withVTodos(VTodo...vTodos)
    {
    	return withVTodos(Arrays.asList(vTodos));
    }
 
    /** 
     * VJOURNAL: RFC 5545 iCalendar 3.6.3. page 57
     * 
     * A grouping of component properties that describe a task that needs to be completed.
     * 
     * @see VComponent
     * @see VJournal
     */
    public List<VJournal> getVJournals() { return vJournals; }
    private List<VJournal> vJournals;
    public void setVJournals(List<VJournal> vJournals)
    {
    	if (this.vJournals != null)
    	{
    		this.vJournals.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.vJournals = vJournals;
    	if (vJournals != null)
		{
    		vJournals.forEach(c -> orderChild(c)); // order new elements
		}
	}
    public VCalendar withVJournals(List<VJournal> vJournals)
    {
    	if (getVJournals() == null)
    	{
    		setVJournals(new ArrayList<>());
    	}
    	getVJournals().addAll(vJournals);
    	if (vJournals != null)
    	{
    		vJournals.forEach(c -> orderChild(c));
    	}
        return this;
	}
    public VCalendar withVJournals(String...vJournals)
    {
        List<VJournal> list = Arrays.stream(vJournals)
                .map(c -> VJournal.parse(c))
                .collect(Collectors.toList());
        return withVJournals(list);
    }
    public VCalendar withVJournals(VJournal...vJournals)
    {
    	return withVJournals(Arrays.asList(vJournals));
    }

    /** 
     * VFREEBUSY: RFC 5545 iCalendar 3.6.4. page 59
     * 
     * @see VFreeBusy
     */
    public List<VFreeBusy> getVFreeBusies() { return vFreeBusys; }
    private List<VFreeBusy> vFreeBusys;
    public void setVFreeBusys(List<VFreeBusy> vFreeBusys)
    {
    	if (this.vFreeBusys != null)
    	{
    		this.vFreeBusys.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.vFreeBusys = vFreeBusys;
    	if (vFreeBusys != null)
		{
    		vFreeBusys.forEach(c -> orderChild(c)); // order new elements
		}
	}
    public VCalendar withVFreeBusies(List<VFreeBusy> vFreeBusys)
    {
    	if (getVFreeBusies() == null)
    	{
    		setVFreeBusys(new ArrayList<>());
    	}
    	getVFreeBusies().addAll(vFreeBusys);
    	if (vFreeBusys != null)
    	{
    		vFreeBusys.forEach(c -> orderChild(c));
    	}
        return this;
	}
    public VCalendar withVFreeBusies(String...vFreeBusys)
    {
    	List<VFreeBusy> list = Arrays.stream(vFreeBusys)
    			.map(c -> VFreeBusy.parse(c))
                .collect(Collectors.toList());
    	return withVFreeBusies(list);
    }
    public VCalendar withVFreeBusies(VFreeBusy...vFreeBusys)
    {
    	return withVFreeBusies(Arrays.asList(vFreeBusys));
    }

    /** 
     * VTIMEZONE: RFC 5545 iCalendar 3.6.5. page 62
     * 
     * @see VTimeZone
     */
    public List<VTimeZone> getVTimeZones() { return vTimeZones; }
    private List<VTimeZone> vTimeZones;
    public void setVTimeZones(List<VTimeZone> vTimeZones)
    {
    	if (this.vTimeZones != null)
    	{
    		this.vTimeZones.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.vTimeZones = vTimeZones;
    	if (vTimeZones != null)
		{
    		vTimeZones.forEach(c -> orderChild(c)); // order new elements
		}
	}
    public VCalendar withVTimeZones(List<VTimeZone> vTimeZones)
    {
    	if (getVTimeZones() == null)
    	{
    		setVTimeZones(new ArrayList<>());
    	}
    	getVTimeZones().addAll(vTimeZones);
    	if (vTimeZones != null)
    	{
    		vTimeZones.forEach(c -> orderChild(c));
    	}
        return this;
	}
    public VCalendar withVTimeZones(String...vTimeZones)
    {
    	List<VTimeZone> list = Arrays.stream(vTimeZones)
    			.map(c -> VTimeZone.parse(c))
                .collect(Collectors.toList());
    	return withVTimeZones(list);
    }
    public VCalendar withVTimeZones(VTimeZone...vTimeZones)
    {
    	return withVTimeZones(Arrays.asList(vTimeZones));
    }
    
//    /**
//     * A convenience method that adds a VComponent to one of the Lists based on
//     * its type such as VEVENT, VTODO, etc.
//     * 
//     * @param newVComponent - VComponent to add
//     * @return  true if add was successful, false otherwise
//     */
//    @Deprecated
//    public boolean addVComponent(VComponent newVComponent)
//    {
//        if (newVComponent instanceof VEvent)
//        {
//        	System.out.println("getVEvents():" + getVEvents());
//            getVEvents().add((VEvent) newVComponent);
//        } else if (newVComponent instanceof VTodo)
//        {
//            getVTodos().add((VTodo) newVComponent);            
//        } else if (newVComponent instanceof VJournal)
//        {
//            getVJournals().add((VJournal) newVComponent);
//        } else if (newVComponent instanceof VFreeBusy)
//        {
//            getVFreeBusies().add((VFreeBusy) newVComponent);            
//        } else if (newVComponent instanceof VTimeZone)
//        {
//            getVTimeZones().add((VTimeZone) newVComponent);            
//        } else
//        {
//            throw new RuntimeException("Unsuppored VComponent type:" + newVComponent.getClass());
//        }
//        orderChild(newVComponent);
//        return true;
//    }
//    
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
            return getVEvents();
        } else if (vComponent instanceof VTodo)
        {
            return getVTodos();
        } else if (vComponent instanceof VJournal)
        {
            return getVJournals();
        } else if (vComponent instanceof VFreeBusy)
        {
            return getVFreeBusies();
        } else if (vComponent instanceof VTimeZone)
        {
            return getVTimeZones();
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
//        VPersonal<?> vComponent = (VPersonal<?>) SimpleVComponentFactory.emptyVComponent(contentText);
        VPersonal<?> vComponent = null; //TODO - FIX THIS
        List<String> contentLines = Arrays.asList(contentText.split(System.lineSeparator()));
        UnfoldingStringIterator unfoldedLines = new UnfoldingStringIterator(contentLines.iterator());
//        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(contentLines).iterator();
        boolean useRequestStatus = true;
//        vComponent.parseContent(unfoldedLines, useRequestStatus);
        vComponent.parseContent(unfoldedLines);
//        requestStatusErrors.stream().forEach(System.out::println);
        // TODO - only check conflict if opaque
        String conflict = (vComponent instanceof VEvent) ? DateTimeUtilities.checkScheduleConflict((VEvent) vComponent, getVEvents()) : null;
        if (conflict != null)
        {
            final List<RequestStatus> requestStatus;
            if (vComponent.getRequestStatus() == null)
            {
                requestStatus = new ArrayList<>();
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
            addChild(vComponent);
        }
        return vComponent;
    }
     
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
    
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (getProductIdentifier() == null)
        {
            errors.add("PRODID is not present.  PRODID is REQUIRED and MUST NOT occur more than once");
        }
        if (getVersion() == null)
        {
            errors.add("VERSION is not present.  VERSION is REQUIRED and MUST NOT occur more than once");
        }
        return errors;
    }

    
    /*
     * CONSTRUCTORS
     */
    /** Creates an empty VCalendar */
    public VCalendar()
    {
        setMethodProcessFactory(new DefaultITIPFactory());
//    	List<java.lang.reflect.Method> getters = ICalendarUtilities.collectGetters(getClass());
        orderer = new OrdererBase(this, getGetters());
        contentLineGenerator = new MultiLineContent(
                orderer,
                FIRST_CONTENT_LINE,
                LAST_CONTENT_LINE,
                1000);
    }
  
    /** Copy constructor */
    public VCalendar(VCalendar source)
    {
        this();
        source.copyChildrenInto(this);  
    }

    /*
     * OTHER METHODS
     */
        
//    @Override
//    public List<String> parseContent(String content)
//    {
//        Iterator<String> lineIterator = Arrays.asList(content.split(System.lineSeparator())).iterator();
//        return parseContent(lineIterator)
//                .entrySet()
//                .stream()
//                .flatMap(e -> e.getValue().stream().map(v -> e.getKey().name() + ":" + v))
//                .collect(Collectors.toList());
//    }

    /** Parse unfolded content line iterator into calendar object */
////    @Override
//	public Map<VElement, List<String>> parseContent(Iterator<String> unfoldedLineIterator)
//    {
//        boolean useResourceStatus = false;
//        return parseContent(unfoldedLineIterator, useResourceStatus);
//    }
    
//    /** Parse unfolded content lines into calendar object */
//    public List<Message> parseContent(Iterator<String> lineIterator, boolean collectErrorMessages)
//    {
//        List<String> vCalendarMessages = new ArrayList<>();
//        List<Message> messages = new ArrayList<>();
//        String firstLine = lineIterator.next();
//        if (! firstLine.equals("BEGIN:VCALENDAR"))
//        {
//            throw new IllegalArgumentException("Content lines must begin with BEGIN:VCALENDAR");
//        }
//        // wrap lineIterator in UnfoldingStringIterator decorator
////        int line = 0;
//        UnfoldingStringIterator unfoldedLineIterator = new UnfoldingStringIterator(lineIterator);
//        while (unfoldedLineIterator.hasNext())
//        {
//            String unfoldedLine = unfoldedLineIterator.next();
////            System.out.println("unfoldedLine:" + unfoldedLine);
////            if (line++ > 20000) System.exit(0);
//            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(unfoldedLine);
//            String propertyName = (nameEndIndex > 0) ? unfoldedLine.substring(0, nameEndIndex) : "";
//            
//            // Parse component
//            if (propertyName.equals("BEGIN"))
//            {
//                String componentName = unfoldedLine.substring(nameEndIndex+1);
////            	System.out.println(componentName);
//                VComponentBase newComponent = (VComponentBase) SimpleVComponentFactory.emptyVComponent(componentName);
////                Map<VElement, List<String>> newComponentMessages = newComponent.parseContent(unfoldedLineIterator, collectErrorMessages);
//                List<Message> newComponentMessages = newComponent.parseContent(unfoldedLineIterator);
//                // TODO - USE ELEMENTS STATICS
//                addChild(newComponent);
////                addVComponent(newComponent);
////                System.out.println(childrenUnmodifiable().size());
//                messages.addAll(newComponentMessages);
//
//                
////                String componentName = unfoldedLine.substring(nameEndIndex+1);
////                VComponent newComponent = SimpleVComponentFactory.emptyVComponent(componentName);
////                // TODO - USE ADD CHILD
////                System.out.println("new component");
////                Map<VElement, List<String>> newComponentMessages = newComponent.parseContent(unfoldedLineIterator, collectErrorMessages);
//////                addChild(newComponent);
////                addVComponent(newComponent);
////                messageMap.putAll(newComponentMessages);
//            } else if (propertyName.equals("END"))
//            {
//                break;
//            } else
//            { // parse calendar property
//                VChild child = null;
//                CalendarProperty elementType = CalendarProperty.enumFromName(propertyName);
//                if (elementType != null)
//                {
//                    child = elementType.parse(this, unfoldedLine);
//                } else if (unfoldedLine.contains(":"))
//                {
//                    //non-standard - check for X- prefix
//                    boolean isNonStandard = propertyName.substring(0, PropertyType.NON_STANDARD.toString().length()).equals(PropertyType.NON_STANDARD.toString());
//                    if (isNonStandard)
//                    {
//                    	child = NonStandardProperty.parse(unfoldedLine);
//                    	addChild(child);
//                    } else
//                    {
//                        // ignore unknown properties
//                        vCalendarMessages.add("Unknown property is ignored:" + unfoldedLine);
//                    }
//                } else
//                {
//                    vCalendarMessages.add("Unknown line is ignored:" + unfoldedLine);                    
//                }
//                if (child != null) 
//            	{
//                	vCalendarMessages.addAll(child.errors());
////                	addChild(child); // TODO - USE WHEN NO USING ENUM PARSE ANYMORE.
//            	}
//            }
//        }
//     // TODO - Log status messages if not using RequestStatus
////        messageMap.put(this, vCalendarMessages);
//        return messages;
//    }

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
        Iterator<String> unfoldedLineIterator = new UnfoldingStringIterator(br.lines().iterator());
//        Iterator<String> unfoldedLineIterator = br.lines().iterator();
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
//        vCalendar.parseContent(lines.iterator(), useResourceStatus);
        vCalendar.parseContent(lines.iterator());
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
    
	@Override
	protected boolean isContentValid(String valueContent)
	{
		boolean isElementValid = super.isContentValid(valueContent);
		if (! isElementValid) return false;
		boolean isBeginPresent = valueContent.startsWith(FIRST_CONTENT_LINE);
		if (! isBeginPresent) return false;
		int lastLineIndex = valueContent.lastIndexOf(System.lineSeparator());
		if (lastLineIndex == -1) return false;
		boolean isEndPresent = valueContent
				.substring(lastLineIndex)
				.equals(LAST_CONTENT_LINE);
		return ! isEndPresent;
	}

    /**
     * Creates a new VCalendar calendar component by parsing a String of iCalendar content lines
     *
     * @param content  the text to parse, not null
     * @return  the parsed VCalendar
     */
    public static VCalendar parse(String content)
    {
    	return VCalendar.parse(new VCalendar(), content);
    }
}
