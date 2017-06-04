package jfxtras.scene.layout.responsivepane;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class ResponsivePaneFXMLTestBasicController {
	
	// to allow the test to access this class
	static ResponsivePaneFXMLTestBasicController responsivePaneFXMLTestBasicController = null;

	@FXML Node layout3_0in;
	@FXML HBox tablet;
	@FXML HBox width_3_0in;
	@FXML HBox size100_0cmL;
	@FXML HBox size100_0cm;
	
	public ResponsivePaneFXMLTestBasicController() {
		ResponsivePaneFXMLTestBasicController.responsivePaneFXMLTestBasicController = this;
	}
	
	@FXML
    public void initialize() {
    }
}
