package jfxtras.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.icalendarfx.properties.PropertyType;
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
    public ObjectProperty<ObservableList<Attachment<?>>> attachmentsProperty()
    {
        if (attachments == null)
        {
            attachments = new SimpleObjectProperty<>(this, PropertyType.ATTACHMENT.toString());
        }
        return attachments;
    }
    @Override
    public ObservableList<Attachment<?>> getAttachments()
    {
        return (attachments == null) ? null : attachments.get();
    }
    private ObjectProperty<ObservableList<Attachment<?>>> attachments;
    @Override
    public void setAttachments(ObservableList<Attachment<?>> attachments)
    {
        if (attachments != null)
        {
            if ((this.attachments != null) && (this.attachments.get() != null))
            {
                // replace sort order in new list
                orderer().replaceList(attachmentsProperty().get(), attachments);
            }
            orderer().registerSortOrderProperty(attachments);
        } else
        {
            orderer().unregisterSortOrderProperty(attachmentsProperty().get());
        }
        attachmentsProperty().set(attachments);
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
    @Override public ObjectProperty<Summary> summaryProperty()
    {
        if (summary == null)
        {
            summary = new SimpleObjectProperty<>(this, PropertyType.SUMMARY.toString());
            orderer().registerSortOrderProperty(summary);
        }
        return summary;
    }
    @Override
    public Summary getSummary() { return (summary == null) ? null : summaryProperty().get(); }
    private ObjectProperty<Summary> summary;
    
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
