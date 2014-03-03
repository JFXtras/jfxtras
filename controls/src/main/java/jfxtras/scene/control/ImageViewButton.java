package jfxtras.scene.control;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * An ImageView which lets the mouse change so it indicates being clickable
 * @author Tom Eugelink
 *
 */
public class ImageViewButton  extends ImageView {
	
	public ImageViewButton()
	{
		super();
		construct();
	}

	public ImageViewButton(Image i) {
		super(i);
		construct();
	}
	
	private void construct() {
		
		setPickOnBounds(true);
		setOnMouseEntered(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (!mouseEvent.isPrimaryButtonDown())
				{						
					ImageViewButton.this.setCursor(Cursor.HAND);
				}
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (!mouseEvent.isPrimaryButtonDown())
				{
					ImageViewButton.this.setCursor(Cursor.DEFAULT);
				}
			}
		});
	}
}