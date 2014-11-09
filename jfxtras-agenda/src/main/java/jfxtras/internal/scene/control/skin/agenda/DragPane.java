package jfxtras.internal.scene.control.skin.agenda;

import javafx.scene.layout.Pane;

public class DragPane extends Pane {

	public DragPane(LayoutHelp layoutHelp) {
		prefWidthProperty().bind(layoutHelp.skinnable.widthProperty()); // the drag pane is the same size as the whole skin
		prefHeightProperty().bind(layoutHelp.skinnable.heightProperty());
	}
}
