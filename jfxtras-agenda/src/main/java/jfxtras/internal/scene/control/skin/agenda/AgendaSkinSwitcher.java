package jfxtras.internal.scene.control.skin.agenda;

import javafx.scene.control.Tooltip;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.layout.HBox;

// TBEERNOT: should we be able to switch dates as well?
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
			unbindSkin();
			clearStyleClass();
			agenda.setSkin(new AgendaWeekSkin(agenda));
		});
		return button;
	}
	
	private ImageViewButton createWeekButton(Agenda agenda) {
		ImageViewButton button = createIcon("day", "Day view");
		button.setOnMouseClicked( (actionEvent) -> {
			unbindSkin();
			clearStyleClass();
			agenda.setSkin(new AgendaDaySkin(agenda));
		});
		return button;
	}

	private void clearStyleClass() {
		agenda.getStyleClass().clear();
		agenda.getStyleClass().add(Agenda.class.getSimpleName());
	}
	
	private void unbindSkin() {
		if (agenda.getSkin() != null) {
			((AgendaSkin)agenda.getSkin()).unbindFromSkinnable();
		}
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
