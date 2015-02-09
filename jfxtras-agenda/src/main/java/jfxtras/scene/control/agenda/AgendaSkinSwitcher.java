package jfxtras.scene.control.agenda;

import javafx.scene.control.Tooltip;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.layout.HBox;

public class AgendaSkinSwitcher extends HBox {

	/**
	 * 
	 */
	public AgendaSkinSwitcher(Agenda agenda) {
		this.agenda = agenda;
		getStyleClass().add(AgendaSkinSwitcher.class.getSimpleName());
		
		getChildren().add(createWeekButton(agenda));
		getChildren().add(createDayButton(agenda));
	}
	final Agenda agenda;
	
	// When JFxtras is based on 1.8.0_40+: @Override 
	public String getUserAgentStylesheet() {
		return AgendaSkinSwitcher.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + AgendaSkinSwitcher.class.getSimpleName() + ".css").toExternalForm();
	}
	
	/**
	 * 
	 */
	private ImageViewButton createDayButton(Agenda agenda) {
		ImageViewButton button = createIcon("week", "Week view");
		button.setOnMouseClicked( (actionEvent) -> {
			agenda.setSkin(new AgendaWeekSkin(agenda));
		});
		return button;
	}

	/**
	 * 
	 */
	private ImageViewButton createWeekButton(Agenda agenda) {
		ImageViewButton button = createIcon("day", "Day view");
		button.setOnMouseClicked( (actionEvent) -> {
			agenda.setSkin(new AgendaDaySkin(agenda));
		});
		return button;
	}
	
	/**
	 * 
	 */
	// TODO: As of 1.8.0_40 CSS files are added in the scope of a control, this class does not fall under the Agenda control, so it must have its own stylesheet. 
	private ImageViewButton createIcon(String type, String tooltip) {
		ImageViewButton imageView = new ImageViewButton();
		imageView.getStyleClass().add(type + "-icon");
		imageView.setPickOnBounds(true);
		Tooltip.install(imageView, new Tooltip(tooltip));
		return imageView;
	}

}
