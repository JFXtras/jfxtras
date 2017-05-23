package jfxtras.icalendarfx.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VDisplayable;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.properties.component.descriptive.Description;

/**
 * VJOURNAL
 * Journal Component
 * RFC 5545, 3.6.3, page 57
 * 
 *    A "VJOURNAL" calendar component is a grouping of
      component properties that represent one or more descriptive text
      notes associated with a particular calendar date.  The "DTSTART"
      property is used to specify the calendar date with which the
      journal entry is associated.  Generally, it will have a DATE value
      data type, but it can also be used to specify a DATE-TIME value
      data type.  Examples of a journal entry include a daily record of
      a legislative body or a journal entry of individual telephone
      contacts for the day or an ordered list of accomplishments for the
      day.  The "VJOURNAL" calendar component can also be used to
      associate a document with a calendar date.

      The "VJOURNAL" calendar component does not take up time on a
      calendar.  Hence, it does not play a role in free or busy time
      searches -- it is as though it has a time transparency value of
      TRANSPARENT.  It is transparent to any such searches.

      The "VJOURNAL" calendar component cannot be nested within another
      calendar component.  However, "VJOURNAL" calendar components can
      be related to each other or to a "VEVENT" or to a "VTODO" calendar
      component, with the "RELATED-TO" property.

   Example:  The following is an example of the "VJOURNAL" calendar
      component:

       BEGIN:VJOURNAL
       UID:19970901T130000Z-123405@example.com
       DTSTAMP:19970901T130000Z
       DTSTART;VALUE=DATE:19970317
       SUMMARY:Staff meeting minutes
       DESCRIPTION:1. Staff meeting: Participants include Joe\,
         Lisa\, and Bob. Aurora project plans were reviewed.
         There is currently no budget reserves for this project.
         Lisa will escalate to management. Next meeting on Tuesday.\n
        2. Telephone Conference: ABC Corp. sales representative
         called to discuss new printer. Promised to get us a demo by
         Friday.\n3. Henry Miller (Handsoff Insurance): Car was
         totaled by tree. Is looking into a loaner car. 555-2323
         (tel).
       END:VJOURNAL
 *
 * @author David Bal
 *
 */
public class VJournal extends VDisplayable<VJournal>
{
    /**
     * DESCRIPTION:
     * RFC 5545 iCalendar 3.8.1.12. page 84
     * This property provides a more complete description of the
     * calendar component than that provided by the "SUMMARY" property.
     * Example:
     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
     *  design.\nHappy Face Conference Room. Phoenix design team
     *  MUST attend this meeting.\nRSVP to team leader.
     *  
     *  VJournal can have multiple description properties.
     */
    public List<Description> getDescriptions() { return descriptions; }
    private List<Description> descriptions;
    public void setDescriptions(List<Description> descriptions)
    {
    	if (this.descriptions != null)
    	{
    		this.descriptions.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.descriptions = descriptions;
    	if (descriptions != null)
    	{
    		descriptions.forEach(c -> orderChild(c)); // order new elements
    	}
	}
    public VJournal withDescriptions(List<Description> descriptions)
    {
    	if (getDescriptions() == null)
    	{
    		setDescriptions(new ArrayList<>());
    	}
    	getDescriptions().addAll(descriptions);
    	if (descriptions != null)
    	{
    		descriptions.forEach(c -> orderChild(c));
    	}
    	return this;
	}
    public VJournal withDescriptions(String...descriptions)
    {
        List<Description> list = Arrays.stream(descriptions)
                .map(c -> Description.parse(c))
                .collect(Collectors.toList());
        return withDescriptions(list);
    }
    public VJournal withDescriptions(Description...descriptions)
    {
    	return withDescriptions(Arrays.asList(descriptions));
    }
    
	@Override
	public List<VJournal> calendarList()
	{
		if (getParent() != null)
		{
			VCalendar cal = (VCalendar) getParent();
			return cal.getVJournals();
		}
		return null;
	}
    
    /*
     * CONSTRUCTORS
     */
    public VJournal() { super(); }
    
    /** Copy constructor */
    public VJournal(VJournal source)
    {
        super(source);
    }
    
    @Override
    public List<String> errors()
    {
        return Collections.unmodifiableList(super.errors());
    }
    
    /**
     * Creates a new VJournal calendar component by parsing a String of iCalendar content lines
     *
     * @param content  the text to parse, not null
     * @return  the parsed VJournal
     */
    public static VJournal parse(String content)
    {
    	return VJournal.parse(new VJournal(), content);
    }
}
