package jfxtras.internal.scene.control.skin.agenda;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.agenda.Agenda;

public class AgendaSkinSwitcher extends VBox {

	public AgendaSkinSwitcher(Agenda agenda) {
		this.agenda = agenda;
		
		getChildren().add(createWeekButton(agenda));
		getChildren().add(createDayButton(agenda));
	}
	final Agenda agenda;
	
	private Button createDayButton(Agenda agenda) {
		Button button = new Button("7");
		button.setOnAction( (actionEvent) -> {
			agenda.setSkin(new AgendaWeekSkin(agenda));
		});
		return button;
	}
	
	private Button createWeekButton(Agenda agenda) {
		Button button = new Button("1");
		button.setOnAction( (actionEvent) -> {
			agenda.setSkin(new AgendaDaySkin(agenda));
		});
		return button;
	}
}
