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
package jfxtras.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.content.UnfoldingStringIterator;

public class ReadICSFileTest
{
    @Test
    @Ignore // TBEERNOT see what is going wrong here
    public void canReadICSFile1() throws IOException
    {
        String fileName = "Yahoo_Sample_Calendar.ics";
        URL url = getClass().getResource(fileName);
        Path icsFilePath = Paths.get(url.getFile());
        
//        BufferedReader br = Files.newBufferedReader(icsFilePath);
//        UnfoldingStringIterator unfoldedLineIterator = new UnfoldingStringIterator(br.lines().iterator());
//        List<String> expectedLines = new ArrayList<>();
//        unfoldedLineIterator.forEachRemaining(line -> expectedLines.add(line));
//        String expectedUnfoldedContent = expectedLines.stream().collect(Collectors.joining(System.lineSeparator()));
//        br.close();
//        System.out.println(expectedUnfoldedContent);
        
        boolean useResourceStatus = false;
        VCalendar vCalendar = VCalendar.parseICalendarFile(icsFilePath, useResourceStatus);
//        System.out.println(vCalendar);
        assertEquals(7554, vCalendar.toString().length());
        assertEquals(7, vCalendar.getVEvents().size());
        assertEquals(1, vCalendar.getVTimeZones().size());
        int subcomponents = vCalendar.getVTimeZones().get(0).getStandardOrDaylight().size();
        assertEquals(9, subcomponents);
    }
    
    @Test
    @Ignore // TBEERNOT see what is going wrong here
    public void canReadICSFile2() throws IOException, InterruptedException
    {
        String fileName = "mathBirthdays.ics";       
        URL url = getClass().getResource(fileName);
        Path icsFilePath = Paths.get(url.getFile());
        BufferedReader br = Files.newBufferedReader(icsFilePath);
        
        UnfoldingStringIterator unfoldedLineIterator = new UnfoldingStringIterator(br.lines().iterator());
        List<String> expectedLines = new ArrayList<>();
        unfoldedLineIterator.forEachRemaining(line -> expectedLines.add(line));
        String expectedUnfoldedContent = expectedLines.stream().collect(Collectors.joining(System.lineSeparator()));
        br.close();

        long t1 = System.currentTimeMillis();
        VCalendar vCalendar = VCalendar.parse(icsFilePath);
        long t2 = System.currentTimeMillis();
//        System.out.println(t2-t1);
        
        long t3 = System.currentTimeMillis();
        Iterator<String> contentIterator = Arrays.stream(vCalendar.toString().split(System.lineSeparator())).iterator();
        UnfoldingStringIterator unfoldedContentLineIterator = new UnfoldingStringIterator(contentIterator);
        List<String> contentLines = new ArrayList<>();
        unfoldedContentLineIterator.forEachRemaining(line -> contentLines.add(line));
        String unfoldedContent = contentLines.stream().collect(Collectors.joining(System.lineSeparator()));
        long t4 = System.currentTimeMillis();
//        System.out.println(t4-t3);

        assertEquals(expectedUnfoldedContent, unfoldedContent);
        assertEquals(13217, expectedLines.size());
    }
}
