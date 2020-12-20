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

import jfxtras.icalendarfx.parameters.ParameterEnumBasedWithUnknown;
import jfxtras.icalendarfx.parameters.Relationship;
import jfxtras.icalendarfx.parameters.Relationship.RelationshipType;
import jfxtras.icalendarfx.utilities.StringConverter;

/**
 * RELTYPE
 * Relationship Type
 * RFC 5545, 3.2.15, page 25
 * 
 * To specify the type of hierarchical relationship associated
 *  with the calendar component specified by the property.
 * 
 * Example:
 * RELATED-TO;RELTYPE=SIBLING:19960401-080045-4000F192713@
 *  example.com
 * 
 * @author David Bal
 *
 */
public class Relationship extends ParameterEnumBasedWithUnknown<Relationship, RelationshipType>
{
	private static final StringConverter<RelationshipType> CONVERTER = new StringConverter<RelationshipType>()
    {
        @Override
        public String toString(RelationshipType object)
        {
            return object.toString();
        }

        @Override
        public RelationshipType fromString(String string)
        {
            return RelationshipType.valueOfWithUnknown(string.toUpperCase());
        }
    };
    
    public Relationship()
    {
        super(RelationshipType.PARENT, CONVERTER); // default value
    }
  
    public Relationship(RelationshipType value)
    {
        super(value, CONVERTER);
    }
    
    public Relationship(Relationship source)
    {
        super(source, CONVERTER);
    }
    
    public enum RelationshipType
    {
        PARENT,
        CHILD,
        SIBLING,
        UNKNOWN;
        
        /** get enum from name */
        public static RelationshipType valueOfWithUnknown(String propertyName)
        {
            RelationshipType match;
            try
            {
                match = valueOf(propertyName);
            } catch (Exception e)
            {
                match = UNKNOWN;
            }
            return match;
        }
    }
    
    public static Relationship parse(String content)
    {
    	return Relationship.parse(new Relationship(), content);
    }
}
