package jfxtras.icalendarfx.properties.component.descriptive;

import java.net.URI;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.icalendarfx.components.VAlarm;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.parameters.Encoding;
import jfxtras.icalendarfx.parameters.FormatType;
import jfxtras.icalendarfx.parameters.OtherParameter;
import jfxtras.icalendarfx.parameters.ParameterType;
import jfxtras.icalendarfx.parameters.ValueParameter;
import jfxtras.icalendarfx.parameters.Encoding.EncodingType;
import jfxtras.icalendarfx.properties.PropAttachment;
import jfxtras.icalendarfx.properties.PropertyBase;
import jfxtras.icalendarfx.properties.ValueType;
import jfxtras.icalendarfx.properties.component.misc.NonStandardProperty;

/**
   <p>Property Name:  ATTACH</p>

   <p>Purpose:  This property provides the capability to associate a
      document object with a calendar component.</p>

   <p>Value Type:  The default {@link ValueType value type} for this property is {@link ValueType.UNIFORM_RESOURCE_IDENTIFIER URI}.  The
      value type can also be set to {@link ValueType.Binary BINARY} to indicate inline binary
      encoded content information.</p>

   <p>Property Parameters:  IANA, {@link NonStandardProperty non-standard},
      {@link Encoding inline encoding}, and {@link ValueParameter value
      data type property} parameters can be specified on this property.
      The {@link FormatType format type parameter} can be specified on this property and is
      RECOMMENDED for inline binary encoded content information.</p>

   <p>Conformance:  This property can be specified multiple times in a
      {@link VEvent VEVENT}, {@link VTodo VTODO}, {@link VJournal VJOURNAL}, or {@link VAlarm VALARM} calendar component with
      the exception of AUDIO alarm that only allows this property to
      occur once.</p>

   <p>Description:  This property is used in {@link VEvent VEVENT}, {@link VTodo VTODO}, and
      {@link VJournal VJOURNAL} calendar components to associate a resource (e.g.,
      document) with the calendar component.  This property is used in
      {@link VAlarm VALARM} calendar components to specify an audio sound resource or
      an email message attachment.  This property can be specified as a
      URI pointing to a resource or as inline binary encoded content.</p>

      <p>When this property is specified as inline binary encoded content,
      calendar applications MAY attempt to guess the media type of the
      resource via inspection of its content if and only if the media
      type of the resource is not given by the {@link FormatType FMTTYPE} parameter.  If
      the media type remains unknown, calendar applications SHOULD treat
      it as type "application/octet-stream".</p>

  <p>Format Definition:  This property is defined by the following notation:
  <ul>
  <li>attach
    <ul>
    <li>{@link Attachment ATTACH} attachparam ( ":" {@link URI uri} )
      <ul>
      <li>";" {@link Encoding ENCODING} "=" {@link EncodingType#BASE64 BASE64}
      <li>";" {@link ValueParameter VALUE} "=" {@link ValueType#BINARY BINARY}
      <li>":" binary
      <li>CRLF
      </ul>
    </ul>
  <li>attachparam
    <ul>
    <li>The following is OPTIONAL for a URI value, RECOMMENDED for a BINARY value, and MUST NOT occur more than once.
      <ul>
      <li>";" {@link FormatType fmttypeparam}
      </ul>
    <li>The following are OPTIONAL, and MAY occur more than once.
      <ul>
      <li>";" {@link OtherParameter}
      </ul>
    </ul>
  </ul>
  
  <p>Example:  The following is an example of this property:
  <ul>
  <li>ATTACH:CID:jsmith.part3.960817T083000.xyzMail@example.com
  <li>ATTACH;FMTTYPE=application/postscript:ftp://example.com/pub/<br>
        reports/r-960812.ps
  </ul>
  </p>
  3.8.1.1.  Attachment  RFC 5545                       iCalendar                  September 2009
 * 
 * @author David Bal
 */
public class Attachment<T> extends PropertyBase<T, Attachment<T>> implements PropAttachment<T>
{
    /**
    * FMTTYPE: Format type parameter
    * RFC 5545, 3.2.8, page 19
    * specify the content type of a referenced object.
    */
   @Override
   public FormatType getFormatType() { return (formatType == null) ? null : formatType.get(); }
   @Override
   public ObjectProperty<FormatType> formatTypeProperty()
   {
       if (formatType == null)
       {
           formatType = new SimpleObjectProperty<>(this, ParameterType.FORMAT_TYPE.toString());
           orderer().registerSortOrderProperty(formatType);
       }
       return formatType;
   }
   private ObjectProperty<FormatType> formatType;
   @Override
   public void setFormatType(FormatType formatType)
   {
       if (formatType != null)
       {
           formatTypeProperty().set(formatType);
       }
   }
   public Attachment<T> withFormatType(FormatType format) { setFormatType(format); return this; }
   public Attachment<T> withFormatType(String format) { setFormatType(FormatType.parse(format)); return this; }
   
   /**
    * ENCODING: Incline Encoding
    * RFC 5545, 3.2.7, page 18
    * 
    * Specify an alternate inline encoding for the property value.
    * Values can be "8BIT" text encoding defined in [RFC2045]
    *               "BASE64" binary encoding format defined in [RFC4648]
    *
    * If the value type parameter is ";VALUE=BINARY", then the inline
    * encoding parameter MUST be specified with the value" ;ENCODING=BASE64".
    */
   @Override
   public Encoding getEncoding() { return (encoding == null) ? null : encoding.get(); }
   @Override
   public ObjectProperty<Encoding> encodingProperty()
   {
       if (encoding == null)
       {
           encoding = new SimpleObjectProperty<>(this, ParameterType.INLINE_ENCODING.toString());
           orderer().registerSortOrderProperty(encoding);
       }
       return encoding;
   }
   private ObjectProperty<Encoding> encoding;
   @Override
   public void setEncoding(Encoding encoding)
   {
       if (encoding.getValue() != EncodingType.BASE64)
       {
           throw new IllegalArgumentException("Attachment property only allows ENCODING to be set to" + EncodingType.BASE64);
       }

       if (encoding != null)
       {
           encodingProperty().set(encoding);
       }
   }
   public Attachment<T> withEncoding(Encoding encoding) { setEncoding(encoding); return this; }
   public Attachment<T> withEncoding(EncodingType encoding) { setEncoding(new Encoding(encoding)); return this; }

   
   /*
    * CONSTRUCTORS
    */
   
   /** Create a Binary Attachment by setting property value to String input parameter */
   public Attachment(Class<T> clazz, String contentLine)
   {
       super(clazz, contentLine);
       clazz.cast(getValue()); // ensure value class type matches parameterized type
   }
   
   /** Create deep copy of source Attachment */
   public Attachment(Attachment<T> source)
   {
       super(source);
   }
   
   /** Create new Attendee with property value set to input parameter<br> 
    * Note: This constructor has no type checking.  Use {@link #Attachment(Class, String)} constructor for type checking */
   public Attachment(T value)
   {
       super(value);
   }
   
   /** Create default Attachment with no value set */
   Attachment()
   {
       super();
   }
   
   @Override
   protected void setConverterByClass(Class<T> clazz)
   {
       if (clazz.equals(URI.class))
       {
           setConverter(ValueType.UNIFORM_RESOURCE_IDENTIFIER.getConverter());
       } else if (clazz.equals(String.class))
       {
           setConverter(ValueType.BINARY.getConverter());           
       } else
       {
           throw new IllegalArgumentException("Only parameterized types of URI and String supported.");           
       }
   }

   @Override
   public List<String> errors()
   {
       List<String> errors = super.errors();
       boolean isBase64Type = (getEncoding() == null) ? false : getEncoding().getValue() != EncodingType.BASE64;
       boolean isBinaryValue = (getValueType() == null) ? false : getValueType().getValue() != ValueType.BINARY;
       if (isBinaryValue && ! isBase64Type)
       { /* If the value type parameter is ";VALUE=BINARY", then the inline
           encoding parameter MUST be specified with the value
           ";ENCODING=BASE64". */
           errors.add("If value is BINARY then encoding MUST be BASE64 not:" + getEncoding().getValue());
       }
       return errors;
   }
   
   /** Create new Attachment by parsing unfolded calendar content */
   public static <U> Attachment<U> parse(String string)
   {
       Attachment<U> property = new Attachment<U>();
       property.parseContent(string);
       return property;
   }
}
