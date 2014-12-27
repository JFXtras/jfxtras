package jfxtras.scene.control.agenda;

import javafx.scene.control.Tooltip;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.layout.HBox;

public class AgendaSkinSwitcher extends HBox {

	public AgendaSkinSwitcher(Agenda agenda) {
		this.agenda = agenda;
		getStyleClass().add(AgendaSkinSwitcher.class.getSimpleName());
		
		getChildren().add(createWeekButton(agenda));
		getChildren().add(createDayButton(agenda));
	}
	final Agenda agenda;
	
	private ImageViewButton createDayButton(Agenda agenda) {
		ImageViewButton button = createIcon("week", "Week view");
		button.setOnMouseClicked( (actionEvent) -> {
			agenda.switchSkin(new AgendaWeekSkin(agenda));
		});
		return button;
	}
	
	private ImageViewButton createWeekButton(Agenda agenda) {
		ImageViewButton button = createIcon("day", "Day view");
		button.setOnMouseClicked( (actionEvent) -> {
			agenda.switchSkin(new AgendaDaySkin(agenda));
		});
		return button;
	}
	
	/**
	 * 
	 * @param popup
	 * @return
	 */
	private ImageViewButton createIcon(String type, String tooltip) {
		ImageViewButton imageView = new ImageViewButton();
		imageView.getStyleClass().add(type + "-icon");
		imageView.setPickOnBounds(true);
		Tooltip.install(imageView, new Tooltip(tooltip));
		return imageView;
	}

}
