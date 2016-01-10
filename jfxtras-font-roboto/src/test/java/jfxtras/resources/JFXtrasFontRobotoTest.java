package jfxtras.resources;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jfxtras.test.AssertNode;
import jfxtras.test.AssertNode.A;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

public class JFXtrasFontRobotoTest extends JFXtrasGuiTest {
	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Locale.setDefault(Locale.ENGLISH);
		box = new VBox();
		box.setPrefSize(300, 300);
		return box;
	}
	private VBox box;

	/**
	 * 
	 */
	@Test
	public void loadAll()
	{
		// load the fonts and set one on the label
		TestUtil.runThenWaitForPaintPulse( () -> {
			JFXtrasFontRoboto.loadAll();
			
	        Label l = new Label("xxxxx");
	        l.setStyle("-fx-font-family: 'Roboto Italic'; -fx-font-size:32pt;");
			box.getChildren().clear();
	        box.getChildren().add(l);
		});
		
		// assert the size
		generateXYWH();
		new AssertNode(find(".label")).assertXYWH(0.0, 0.0, 102.0, 59.0, 0.01);
		
		// set another on the label
		TestUtil.runThenWaitForPaintPulse( () -> {
			
	        Label l = new Label("xxxxx");
	        l.setStyle("-fx-font-family: 'Roboto Medium'; -fx-font-size:32pt;");
			box.getChildren().clear();
	        box.getChildren().add(l);
		});
		
		// assert the size
		generateXYWH();
		new AssertNode(find(".label")).assertXYWH(0.0, 0.0, 109.0, 59.0, 0.01);
	}
	
	// =============================================================================================================================================================================================================================
	// SUPPORT

	List<String> EXCLUDED_CLASSES = java.util.Arrays.asList();
	
	private void generateXYWH() {
		Node node = find(".label");
		AssertNode.generateSource("find(\".label\")", node, EXCLUDED_CLASSES, false, A.XYWH);
	}
}
