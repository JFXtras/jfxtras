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

import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import jfxtras.internal.scene.control.skin.agenda.base24hour.AgendaSkinTimeScale24HourAbstract;
import jfxtras.scene.control.agenda.Agenda;

/**
 * @author Tom Eugelink
 */
public class AgendaDaysFromTodaySkin extends AgendaSkinTimeScale24HourAbstract {
	
	/**
	 * 
	 */
	public AgendaDaysFromTodaySkin(Agenda control) {
		super(control);
		
		construct();
	}
	
	/**
	 * 
	 */
	private void construct() {
		// days back
		daysBackSlider = new Slider(-20.0, 0.0, (double)daysBackDefault); // TBEERNOT: make the -20 CSS configurable
		daysBackSlider.snapToTicksProperty().set(true);
		daysBackSlider.majorTickUnitProperty().set(1.0);
		daysBackSlider.minorTickCountProperty().set(0);
		daysBackSlider.showTickLabelsProperty().set(true);
		daysBackSlider.prefWidthProperty().bind(borderPane.widthProperty().divide(2.0));
		daysBackSlider.valueProperty().addListener( (observable) -> {
			System.out.println("back " + daysBackSlider.getValue());
			if (daysBackSlider.valueChangingProperty().get() == false) {
				System.out.println("back reconstruct");
				reconstruct();
			}
		});
		
		// days forward
		daysForwardSlider = new Slider(0.0, 20.0, (double)daysForwardDefault); // TBEERNOT: make the 20 CSS configurable
		daysForwardSlider.snapToTicksProperty().set(true);
		daysForwardSlider.majorTickUnitProperty().set(1.0);
		daysForwardSlider.minorTickCountProperty().set(0);
		daysForwardSlider.showTickLabelsProperty().set(true);
		daysForwardSlider.prefWidthProperty().bind(borderPane.widthProperty().divide(2.0));
		daysForwardSlider.valueProperty().addListener( (observable) -> {
			System.out.println("forward " + daysForwardSlider.getValue());
			if (daysForwardSlider.valueChangingProperty().get() == false) {
				System.out.println("forward reconstruct");
				reconstruct();
			}
		});
		
		// put the sliders at the bottom
		borderPane.setBottom(new HBox(daysBackSlider, daysForwardSlider));
	}
	private Slider daysBackSlider;
	private Slider daysForwardSlider;
	final private int daysBackDefault = -1;
	final private int daysForwardDefault = 6;
	
	/**
	 * 
	 */
	protected void reconstruct() {
		super.reconstruct();
		
		// put the sliders at the bottom
		borderPane.setBottom(new HBox(daysBackSlider, daysForwardSlider));
	}

	/**
	 * Assign a calendar to each day, so it knows what it must draw.
	 */
	protected List<LocalDate> determineDisplayedLocalDates()
	{
		// the result 
		List<LocalDate> lLocalDates = new ArrayList<>();
		
		// get slider positions
		int lStartOffset = (daysBackSlider == null ? daysBackDefault : (int)daysBackSlider.valueProperty().get()); 
		System.out.println("lStartOffset = "  + lStartOffset);
		int lEndOffset = (daysForwardSlider == null ? daysForwardDefault : (int)daysForwardSlider.valueProperty().get());
		System.out.println("lEndOffset = "  + lEndOffset);
		LocalDate lStartLocalDate = LocalDate.now();
		for (int i = lStartOffset; i < lEndOffset + 1; i++) { // + 1 = always show today
			lLocalDates.add(lStartLocalDate.plusDays(i));
		}
		
		// done
		return lLocalDates;
	}
}
