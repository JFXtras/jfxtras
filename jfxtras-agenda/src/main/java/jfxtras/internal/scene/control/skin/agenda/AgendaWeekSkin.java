/**
 * AgendaWeekSkin.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jfxtras.internal.scene.control.skin.agenda.base24hour.AgendaSkinTimeScale24HourAbstract;
import jfxtras.scene.control.agenda.Agenda;

/**
 * @author Tom Eugelink
 */
public class AgendaWeekSkin extends AgendaSkinTimeScale24HourAbstract<AgendaWeekSkin> {
	
	/**
	 * 
	 */
	public AgendaWeekSkin(Agenda control) {
		super(control);
	}
	
	/**
	 * Assign a calendar to each day, so it knows what it must draw.
	 */
	protected List<LocalDate> determineDisplayedLocalDates()
	{
		// the result 
		List<LocalDate> lLocalDates = new ArrayList<>();
		
		// 7 days stating at the first day of week
		LocalDate lStartLocalDate = getFirstDayOfWeekLocalDate();
		for (int i = 0; i < 7; i++) {
			lLocalDates.add(lStartLocalDate.plusDays(i));
		}
		
		// done
		return lLocalDates;
	}

	
	/**
	 * get the date of the first day of the week
	 */
	private LocalDate getFirstDayOfWeekLocalDate()
	{
		Locale lLocale = getSkinnable().getLocale();
		WeekFields lWeekFields = WeekFields.of(lLocale);
		int lFirstDayOfWeek = lWeekFields.getFirstDayOfWeek().getValue();
		LocalDate lDisplayedDateTime = getSkinnable().getDisplayedLocalDateTime().toLocalDate();
		int lCurrentDayOfWeek = lDisplayedDateTime.getDayOfWeek().getValue();

		if (lFirstDayOfWeek <= lCurrentDayOfWeek) {
			lDisplayedDateTime = lDisplayedDateTime.plusDays(-lCurrentDayOfWeek + lFirstDayOfWeek);
		}
		else {
			lDisplayedDateTime = lDisplayedDateTime.plusDays(-lCurrentDayOfWeek - (7-lFirstDayOfWeek));
		}
		
		return lDisplayedDateTime;
	}

}
