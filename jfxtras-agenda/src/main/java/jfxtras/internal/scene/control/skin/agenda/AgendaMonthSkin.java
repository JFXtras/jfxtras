package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDateTime;

import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class AgendaMonthSkin extends SkinBase<Agenda>
implements AgendaSkin {

	protected AgendaMonthSkin(Agenda arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setupAppointments() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LocalDateTime convertClickInSceneToDateTime(double x, double y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void print(PrinterJob job) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public Node getNodeForPopup(Appointment appointment) {
        // TODO Auto-generated method stub
        return null;
    }

}
