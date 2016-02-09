/**
 * AgendaSkin.java
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

import java.time.LocalDateTime;

import javafx.print.PrinterJob;
import javafx.scene.Node;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public interface AgendaSkin {
	/**
	 * Complete refresh
	 */
	void refresh();
	
	/**
	 * Recreate the appointments
	 */
	void setupAppointments();
	
	/**
	 * 
	 * @param x scene coordinate
	 * @param y scene coordinate
	 * @return a localDateTime equivalent of the click location, where a drop in the day section has nano seconds == 1, and a drop in a header (wholeday) section has nano seconds == 0
	 */
	LocalDateTime convertClickInSceneToDateTime(double x, double y);
	
	/**
	 * Finds rendered node for appointment.  The node can be used as the owner for a popup.
	 * or finding its x, y coordinates.
	 * 
	 * @param appointment
	 * @return rendered node that represents appointment
	 */
	Node getNodeForPopup(Appointment appointment);
	
	public void print(PrinterJob job);
}
