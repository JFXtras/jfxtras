package jfxtras.icalendarfx.properties.component.misc;

import java.text.DecimalFormat;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.components.VFreeBusy;
import jfxtras.icalendarfx.components.VJournal;
import jfxtras.icalendarfx.components.VTodo;
import jfxtras.icalendarfx.properties.PropBaseLanguage;

/**
 * REQUEST-STATUS
 * RFC 5545, 3.8.8.3, page 141
 * 
 * This property defines the status code returned for a scheduling request.
 * 
 * Examples:
 * REQUEST-STATUS:2.0;Success
 * REQUEST-STATUS:3.1;Invalid property value;DTSTART:96-Apr-01
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 */
public class RequestStatus extends PropBaseLanguage<String, RequestStatus>
{
    /** Hierarchical, numeric return status code
     * 
   +--------+----------------------------------------------------------+
   | Short  | Longer Return Status Description                         |
   | Return |                                                          |
   | Status |                                                          |
   | Code   |                                                          |
   +--------+----------------------------------------------------------+
   | 1.xx   | Preliminary success.  This class of status code          |
   |        | indicates that the request has been initially processed  |
   |        | but that completion is pending.                          |
   |        |                                                          |
   | 2.xx   | Successful.  This class of status code indicates that    |
   |        | the request was completed successfully.  However, the    |
   |        | exact status code can indicate that a fallback has been  |
   |        | taken.                                                   |
   |        |                                                          |
   | 3.xx   | Client Error.  This class of status code indicates that  |
   |        | the request was not successful.  The error is the result |
   |        | of either a syntax or a semantic error in the client-    |
   |        | formatted request.  Request should not be retried until  |
   |        | the condition in the request is corrected.               |
   |        |                                                          |
   | 4.xx   | Scheduling Error.  This class of status code indicates   |
   |        | that the request was not successful.  Some sort of error |
   |        | occurred within the calendaring and scheduling service,  |
   |        | not directly related to the request itself.              |
   +--------+----------------------------------------------------------+
    */
    // TODO - APPLY RULES FROM RFC 5546 TO CREATE CORRECT STATUS CODES
    public Double getStatusCode() { return statusCode; }
    private Double statusCode;
    public void setStatusCode(Double statusCode)
    {
    	this.statusCode = statusCode;
    	updateValue();
	}
    public RequestStatus withStatusCode(Double statusCode) { setStatusCode(statusCode); return this; }
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.0#");
    
    /** Textual status description */
    public String getDescription() { return description; }
    private String description;
    public void setDescription(String description)
    {
    	this.description = description;
    	updateValue();
	}
    public RequestStatus withDescription(String description) { setDescription(description); return this; }

    /** Textual exception data.  For example, the offending property name and value or complete property line. */
    public String getException() { return exception; }
    private String exception;
    public void setException(String exception) { this.exception = exception; }
    public RequestStatus withException(String exception) { setException(exception); return this; }
    
    public RequestStatus(RequestStatus source)
    {
        super(source);
    }
    
    public RequestStatus()
    {
        super();
    }
    
    /** Applies string converter only to description and exception parts of the RequestStatus property
     * This leaves the semicolon delimiters unescaped
     */
    @Override
    protected String valueContent()
    {
        StringBuilder builder = new StringBuilder(100);
        builder.append(DECIMAL_FORMAT.format(getStatusCode()) + ";");
        builder.append(getConverter().toString(getDescription()));
        if (getException() != null)
        {
            builder.append(";" + getConverter().toString(getException()));
        }
        return builder.toString();        
    }
    
    @Override
	public void setValue(String value)
    {
    	super.setValue(value);
        int codeIndex = value.indexOf(';');
        setStatusCode(Double.parseDouble(value.substring(0, codeIndex)));
        int descriptionIndex = value.indexOf(';',codeIndex+1);
        if (descriptionIndex > 0)
        {
            setDescription(value.substring(codeIndex+1, descriptionIndex));
            setException(value.substring(descriptionIndex+1));
        } else
        {
            setDescription(value.substring(codeIndex+1));            
        }
    }
    
    private void updateValue()
    {
        StringBuilder builder = new StringBuilder(100);
        builder.append(DECIMAL_FORMAT.format(getStatusCode()) + ";");
        builder.append(getDescription());
        if (getException() != null)
        {
            builder.append(";" + getException());
        }
        super.setValue(builder.toString());
    }
    
    public static RequestStatus parse(String content)
    {
    	return RequestStatus.parse(new RequestStatus(), content);
    }
}
