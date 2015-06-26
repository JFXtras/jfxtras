package jfxtras.internal.scene.control.skin.agenda.base24hour;

import javafx.scene.shape.Rectangle;

/**
 * For whiting-out an appointment pane
 */
class HistoricalVisualizer extends Rectangle
{
	HistoricalVisualizer(AppointmentAbstractPane pane) // TBEE: pane must implement something to show its begin and end datetime, so this class knowns how to render itself?
	{
		// 100% overlay the pane
		setMouseTransparent(true);
		xProperty().set(0);
		yProperty().set(0);
		widthProperty().bind(pane.prefWidthProperty());
		heightProperty().bind(pane.prefHeightProperty());
		setVisible(false);
		getStyleClass().add("History");			
	}
}