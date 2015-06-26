package jfxtras.internal.scene.control.skin.agenda.base24hour;

import javafx.scene.layout.Pane;

class DragPane extends Pane {

	DragPane(LayoutHelp layoutHelp) {
		prefWidthProperty().bind(layoutHelp.skinnable.widthProperty()); // the drag pane is the same size as the whole skin
		prefHeightProperty().bind(layoutHelp.skinnable.heightProperty());
	}
}
