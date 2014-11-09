package jfxtras.internal.scene.control.skin.agenda;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import jfxtras.util.NodeUtil;

public class TimeScale24Hour extends Pane {

	public TimeScale24Hour(Pane pane, LayoutHelp layoutHelp) {
		this.pane = pane;
		this.layoutHelp = layoutHelp;
		
		// position
		layoutXProperty().set(0);
		layoutYProperty().set(0);
		prefWidthProperty().bind(pane.widthProperty());
		prefHeightProperty().bind(pane.heightProperty());

		// make completely transparent for all events
		setMouseTransparent(true);
		
		// add contents
		addTimeScale();
	}
	final Pane pane;
	final LayoutHelp layoutHelp;
	
	private void addTimeScale() {
		
		// draw hours
		for (int lHour = 0; lHour < 24; lHour++)
		{
			// hour line
			{
				Line l = new Line(0,10,100,10);
				l.setId("hourLine" + lHour);
				l.getStyleClass().add("HourLine");
				l.startXProperty().set(0.0);
				l.startYProperty().bind( NodeUtil.snapXY(layoutHelp.hourHeighProperty.multiply(lHour)) );
				l.endXProperty().bind( NodeUtil.snapXY(pane.widthProperty()));
				l.endYProperty().bind( NodeUtil.snapXY(l.startYProperty()));
				getChildren().add(l);
			}
			// half hour line
			{
				Line l = new Line(0,10,100,10);
				l.setId("halfHourLine" + lHour);
				l.getStyleClass().add("HalfHourLine");
				l.startXProperty().bind( NodeUtil.snapXY(layoutHelp.timeWidthProperty));
				l.endXProperty().bind( NodeUtil.snapXY(pane.widthProperty()));
				l.startYProperty().bind( NodeUtil.snapXY(layoutHelp.hourHeighProperty.multiply(lHour + 0.5)));
				l.endYProperty().bind( NodeUtil.snapXY(l.startYProperty()));
				getChildren().add(l);
			}
			// hour text
			{
				Text t = new Text(lHour + ":00");
				t.xProperty().bind(layoutHelp.timeWidthProperty.subtract(t.getBoundsInParent().getWidth()).subtract(layoutHelp.timeColumnWhitespaceProperty.get() / 2));
				t.yProperty().bind(layoutHelp.hourHeighProperty.multiply(lHour));
				t.setTranslateY(t.getBoundsInParent().getHeight()); // move it under the line
				t.getStyleClass().add("HourLabel");
				t.setFontSmoothingType(FontSmoothingType.LCD);
				getChildren().add(t);
			}
		}

	}
}
