package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDateTime;

import javafx.print.PrinterJob;

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
	 * @param x screen coordinate
	 * @param y screen coordinate
	 * @return a localDateTime equivalent of the clickj location, where a drop in the day section has nano seconds == 1, and a drop in a header (wholeday) section has nano seconds == 0
	 */
	LocalDateTime convertClickToDateTime(double x, double y);
	
	public void print(PrinterJob job);
}
