/**
 * AgendaSkinSwitcher.java
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

package jfxtras.scene.control.agenda;

import javafx.scene.control.Tooltip;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaysFromDisplayedSkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.layout.HBox;

/**
 * = AgendaSkinSwitcher
 * 
 * This controls renders a small icon menu, where the user can select which skin to use in the associated Agenda control.
 * [source,java]
 * --
 *     // create Agenda
 *     Agenda agenda = new Agenda();
 *     somePane.getChildren().add(agenda);
 *
 *     // create AgendaSkinSwitcher
 *     AgendaSkinSwitcher agendaSkinSwitcher = new AgendaSkinSwitcher();
 *     somePane.getChildren().add(agendaSkinSwitcher);
 * --
 * 
 */
public class AgendaSkinSwitcher extends HBox {

	/**
	 * 
	 */
	public AgendaSkinSwitcher(Agenda agenda) {
		this.agenda = agenda;
		getStyleClass().add(AgendaSkinSwitcher.class.getSimpleName());
		
		getChildren().add(createWeekButton(agenda));
		getChildren().add(createDayButton(agenda));
		getChildren().add(createDayDynamicButton(agenda));
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
	private ImageViewButton createDayDynamicButton(Agenda agenda) {
		ImageViewButton button = createIcon("dayDynamic", "Dynamic days view");
		button.setOnMouseClicked( (actionEvent) -> {
			agenda.setSkin(new AgendaDaysFromDisplayedSkin(agenda));
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
