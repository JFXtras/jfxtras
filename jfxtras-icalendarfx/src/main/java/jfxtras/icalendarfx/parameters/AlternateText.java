package jfxtras.icalendarfx.parameters;

import java.net.URI;

/**
 * 3.2.1.  Alternate Text Representation

   Parameter Name:  ALTREP

   Purpose:  To specify an alternate text representation for the
      property value.

   Format Definition:  This property parameter is defined by the
      following notation:

     altrepparam = "ALTREP" "=" DQUOTE uri DQUOTE

   Description:  This parameter specifies a URI that points to an
      alternate representation for a textual property value.  A property
      specifying this parameter MUST also include a value that reflects



Desruisseaux                Standards Track                    [Page 14]
 
RFC 5545                       iCalendar                  September 2009


      the default representation of the text value.  The URI parameter
      value MUST be specified in a quoted-string.

         Note: While there is no restriction imposed on the URI schemes
         allowed for this parameter, Content Identifier (CID) [RFC2392],
         HTTP [RFC2616], and HTTPS [RFC2818] are the URI schemes most
         commonly used by current implementations.

   Example:

       DESCRIPTION;ALTREP="CID:part3.msg.970415T083000@example.com":
        Project XYZ Review Meeting will include the following agenda
         items: (a) Market Overview\, (b) Finances\, (c) Project Man
        agement

      The "ALTREP" property parameter value might point to a "text/html"
      content portion.

       Content-Type:text/html
       Content-Id:<part3.msg.970415T083000@example.com>

       <html>
         <head>
          <title></title>
         </head>
         <body>
           <p>
             <b>Project XYZ Review Meeting</b> will include
             the following agenda items:
             <ol>
               <li>Market Overview</li>
               <li>Finances</li>
               <li>Project Management</li>
             </ol>
           </p>
         </body>
       </html>
  RFC 5545                       iCalendar                  September 2009
 * 
 * @author David Bal
 */
public class AlternateText extends ParameterBase<AlternateText, URI>
{
    /** Create new AlternateText with property value set to input parameter */
    public AlternateText(URI value)
    {
        super(value);
    }

    /** Create deep copy of source AlternateText */
    public AlternateText(AlternateText source)
    {
        super(source);
    }

    /** Create default Summary with no value set */
    public AlternateText()
    {
        super();
    }
    
    /** Create new AlternateText by parsing unfolded calendar content */
    public static AlternateText parse(String content)
    {
        AlternateText parameter = new AlternateText();
        parameter.parseContent(content);
        return parameter;
    }
}
