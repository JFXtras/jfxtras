package jfxtras.internal.scene.control.skin.agenda.basedaylist;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.internal.scene.control.skin.agenda.AllAppointments;
import jfxtras.internal.scene.control.skin.agenda.basedaylist.LayoutHelp;

public class DayPane {
	public DayPane(LocalDate localDate, AllAppointments allAppointments, LayoutHelp layoutHelp) {
		this.localDateObjectProperty.set(localDate);
		this.allAppointments = allAppointments;
		this.layoutHelp = layoutHelp;
		construct();
	}
	final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
	final AllAppointments allAppointments;
	final LayoutHelp layoutHelp;
	
	private void construct() {
		
	}
}
