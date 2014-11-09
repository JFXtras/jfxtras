package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDateTime;

public interface AgendaSkin {
	/**
	 * Complete refresh
	 */
	void refresh();
	
	/**
	 * Relayout the appointments
	 */
	void setupAppointments();
	
	/**
	 * 
	 * @param x screen coordinate
	 * @param y screen coordinate
	 * @return a localDateTime where a drop in the day section has nano seconds == 1, and a drop in a header (wholeday) section has nano seconds == 0
	 */
	LocalDateTime convertClickToDateTime(double x, double y);
}
