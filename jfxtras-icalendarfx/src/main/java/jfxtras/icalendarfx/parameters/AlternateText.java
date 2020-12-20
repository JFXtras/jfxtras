/**
 * Copyright (c) 2011-2020, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.parameters;

import java.net.URI;

import jfxtras.icalendarfx.parameters.AlternateText;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.utilities.StringConverter;
import jfxtras.icalendarfx.utilities.StringConverters;

/**
 * <pre>
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
 * </pre>
 * @author David Bal
 */
public class AlternateText extends VParameterBase<AlternateText, URI>
{
	private static final StringConverter<URI> CONVERTER = StringConverters.uriConverterWithQuotes();
	
    /** Create new AlternateText with property value set to input parameter */
    public AlternateText(URI value)
    {
        super(value, CONVERTER);
    }

    /** Create deep copy of source AlternateText */
    public AlternateText(AlternateText source)
    {
        super(source, CONVERTER);
    }

    /** Create default Summary with no value set */
    public AlternateText()
    {
        super(CONVERTER);
    }
    
    public static AlternateText parse(String content)
    {
    	return AlternateText.parse(new AlternateText(), content);
    }
}
