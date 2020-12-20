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
package jfxtras.icalendarfx;

import java.util.List;

/**
 * <p>Interface for all calendar elements.</p>
 * 
 * @author David Bal
 *
 */
public interface VElement
{
    /**
     * <p>Returns the name of the component as it would appear in the iCalendar content line.</p>
     * <p>Examples:
     * <ul>
     * <li>VEVENT
     * <li>SUMMARY
     * <li>LANGUAGE
     * </ul>
     * </p>
     * @return - the component name
     */
    String name();
    
    /** Parse content line into calendar element.
     * If element contains children {@link #parseContent(String)} is invoked recursively to parse child elements also
     * 
     * @param content  calendar content string to parse
     * @return  log of information and error messages
     * @throws IllegalArgumentException  if calendar content is not valid, such as null
     */
//    @Deprecated
//    List<String> parseContent(String content) throws IllegalArgumentException;
        
    /**
     * Checks element to determine if necessary properties are set.
     * {@link #isValid()} is invoked recursively to test child elements if element is a parent
     * 
     * @return - true if component is valid, false otherwise
     */
    default boolean isValid() { return errors().isEmpty(); }
    
    /**
     * Produces a list of error messages indicating problems with calendar element
     * {@link #errors()} is invoked recursively to return errors of child elements in addition to errors in parent
     * 
     * @return - list of error messages
     */
    List<String> errors();
}
