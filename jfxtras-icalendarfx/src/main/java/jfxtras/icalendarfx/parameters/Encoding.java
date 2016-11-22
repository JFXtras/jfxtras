package jfxtras.icalendarfx.parameters;

import java.util.HashMap;
import java.util.Map;

import jfxtras.icalendarfx.parameters.Encoding.EncodingType;

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
public class Encoding extends ParameterBase<Encoding, EncodingType>
{
    public Encoding()
    {
        super(EncodingType.EIGHT_BIT);
    }
  
    public Encoding(EncodingType value)
    {
        super(value);
    }

    public Encoding(Encoding source)
    {
        super(source);
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
        Encoding parameter = new Encoding();
        parameter.parseContent(content);
        return parameter;
    }
}