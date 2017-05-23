package jfxtras.icalendarfx.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.icalendarfx.properties.component.descriptive.Summary;

/**
 * <p>{@link VComponent} with the following properties
 * <ul>
 * <li>{@link Attachment ATTACH}
 * <li>{@link Summary SUMMARY}
 * </ul>
 * </p>
 * 
 * @author David Bal
 */
public interface VDescribable<T> extends VComponent
{
    /**
     * <p>This property provides the capability to associate a
     * document object with a calendar component.</p>
     * 
     *<p>Example:  The following is an example of this property:
     *<ul>
     *<li>ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com
     *<li>ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/<br>
     *   reports/r-960812.ps
     *</ul>
     *</p>
     */
    List<Attachment<?>> getAttachments();
    void setAttachments(List<Attachment<?>> properties);
    /**
     * Sets the value of the {@link #attachmentsProperty()}.
     * 
     * @return - this class for chaining
     */
    default T withAttachments(List<Attachment<?>> attachments)
    {
    	if (getAttachments() == null)
    	{
    		setAttachments(new ArrayList<>());
    	}
    	getAttachments().addAll(attachments);
    	if (attachments != null)
    	{
    		attachments.forEach(c -> orderChild(c));
    	}
        return (T) this;
    }
    /**
     * Sets the value of the {@link #attachmentsProperty()} by parsing a vararg of
     * iCalendar content text representing individual {@link Attachment} objects.
     * 
     * @return - this class for chaining
     */
    default T withAttachments(String...attachments)
    {
        List<Attachment<?>> list = Arrays.stream(attachments)
                .map(c -> (Attachment<?>) Attachment.parse(c))
                .collect(Collectors.toList());
        return withAttachments(list);
    }
    /**
     * Sets the value of the {@link #attachmentsProperty()} from a vararg of {@link Attachment} objects.
     * 
     * @return - this class for chaining
     */
    default T withAttachments(Attachment<?>...attachments)
    {
    	return withAttachments(Arrays.asList(attachments));
    }
    
    /**
     *<p>This property defines a short summary or subject for the calendar component</p>
     * 
     *<p>Example:  The following is an example of this property:
     *<ul>
     *<li>SUMMARY:Department Party
     *</ul>
     *</p>
     */
    Summary getSummary();
    default void setSummary(String summary)
    {
        setSummary(Summary.parse(summary));
    }
    void setSummary(Summary summary);
    /**
     * Sets the value of the {@link #summaryProperty()}
     * 
     * @return - this class for chaining
     */
    default T withSummary(Summary summary)
    {
        setSummary(summary);
        return (T) this;
    }
    /**
     * Sets the value of the {@link #summaryProperty()} by parsing iCalendar content text.
     * 
     * @return - this class for chaining
     */
    default T withSummary(String summary)
    {
        setSummary(summary);
        return (T) this;
    }
}
