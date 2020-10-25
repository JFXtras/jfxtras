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
 * DISCLAIMED. IN NO EVENT SHALL JFXRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.parameters;

import java.util.HashMap;
import java.util.Map;

import jfxtras.icalendarfx.parameters.Encoding;
import jfxtras.icalendarfx.parameters.VParameterBase;
import jfxtras.icalendarfx.parameters.Encoding.EncodingType;
import jfxtras.icalendarfx.properties.component.descriptive.Attachment;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * ENCODING
 * Inline Encoding
 * RFC 5545, 3.2.7, page 18
 * 
 * To specify an alternate inline encoding for the property value.
 * 
 * Example:
 * ATTACH;FMTTYPE=text/plain;ENCODING=BASE64;VALUE=BINARY:TG9yZW
 *  0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2ljaW
 *  5nIGVsaXQsIHNlZCBkbyBlaXVzbW9kIHRlbXBvciBpbmNpZGlkdW50IHV0IG
 *  xhYm9yZSBldCBkb2xvcmUgbWFnbmEgYWxpcXVhLiBVdCBlbmltIGFkIG1pbm
 *  ltIHZlbmlhbSwgcXVpcyBub3N0cnVkIGV4ZXJjaXRhdGlvbiB1bGxhbWNvIG
 *  xhYm9yaXMgbmlzaSB1dCBhbGlxdWlwIGV4IGVhIGNvbW1vZG8gY29uc2VxdW
 *  F0LiBEdWlzIGF1dGUgaXJ1cmUgZG9sb3IgaW4gcmVwcmVoZW5kZXJpdCBpbi
 *  B2b2x1cHRhdGUgdmVsaXQgZXNzZSBjaWxsdW0gZG9sb3JlIGV1IGZ1Z2lhdC
 *  BudWxsYSBwYXJpYXR1ci4gRXhjZXB0ZXVyIHNpbnQgb2NjYWVjYXQgY3VwaW
 *  RhdGF0IG5vbiBwcm9pZGVudCwgc3VudCBpbiBjdWxwYSBxdWkgb2ZmaWNpYS
 *  BkZXNlcnVudCBtb2xsaXQgYW5pbSBpZCBlc3QgbGFib3J1bS4=
 * 
 * @author David Bal
 * @see Attachment
 */
public class Encoding extends VParameterBase<Encoding, EncodingType>
{
	private static final StringConverter<EncodingType> CONVERTER = new StringConverter<EncodingType>()
    {
        @Override
        public String toString(EncodingType object)
        {
            return object.toString();
        }

        @Override
        public EncodingType fromString(String string)
        {
            return EncodingType.enumFromName(string.toUpperCase());
        }
    };
    
    public Encoding()
    {
        super(EncodingType.EIGHT_BIT, CONVERTER);
    }
  
    public Encoding(EncodingType value)
    {
        super(value, CONVERTER);
    }

    public Encoding(Encoding source)
    {
        super(source, CONVERTER);
    }
    
    public enum EncodingType
    {
        EIGHT_BIT ("8BIT"),
        BASE64 ("BASE64");
        
        private static Map<String, EncodingType> enumFromNameMap = makeEnumFromNameMap();
        private static Map<String, EncodingType> makeEnumFromNameMap()
        {
            Map<String, EncodingType> map = new HashMap<>();
            EncodingType[] values = EncodingType.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static EncodingType enumFromName(String propertyName)
        {
            return enumFromNameMap.get(propertyName.toUpperCase());
        }
        
        private String name;
        @Override public String toString() { return name; }
        EncodingType(String name)
        {
            this.name = name;
        }
    }
    
    public static Encoding parse(String content)
    {
    	return Encoding.parse(new Encoding(), content);
    }
}