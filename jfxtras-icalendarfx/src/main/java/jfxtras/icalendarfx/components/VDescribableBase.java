package jfxtras.icalendarfx.components;

import java.util.List;

import jfxtras.icalendarfx.components.VCommon;
import jfxtras.icalendarfx.components.VComponent;
import jfxtras.icalendarfx.components.VDescribable;
import jfxtras.icalendarfx.components.VDescribableBase;
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
 *
 * @param <T> - concrete subclass
 * 
 */
public abstract class VDescribableBase<T> extends VCommon<T> implements VDescribable<T>
{
    /**
     * This property provides the capability to associate a document object with a calendar component.
     * 
     *<p>Example:  The following is an example of this property:
     *<ul>
     *<li>ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com
     *<li>ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/<br>
     *  reports/r-960812.ps
     *</ul>
     *</p>
     */
    @Override
    public List<Attachment<?>> getAttachments() { return attachments; }
    private List<Attachment<?>> attachments;
    @Override
    public void setAttachments(List<Attachment<?>> attachments)
    {
    	if (this.attachments != null)
    	{
    		this.attachments.forEach(e -> orderChild(e, null)); // remove old elements
    	}
    	this.attachments = attachments;
    	if (attachments != null)
    	{
    		attachments.forEach(c -> orderChild(c));
    	}
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
    @Override
    public Summary getSummary() { return summary; }
    private Summary summary;
    @Override
	public void setSummary(Summary summary)
    {
    	orderChild(this.summary, summary);
    	this.summary = summary;
	}
    
    /*
     * CONSTRUCTORS
     */
    VDescribableBase()
    {
        super();
    }

    VDescribableBase(VDescribableBase<T> source)
    {
        super(source);
    }
}
