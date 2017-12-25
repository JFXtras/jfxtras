package jfxtras.scene.control.trial;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import jfxtras.scene.control.AccordionPane;

public class AccordionDemo extends Application {

	public static void main(final String[] args) {
		Application.launch(AccordionDemo.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		AccordionPane lHarmonica = new AccordionPane();
		lHarmonica.addTab("test1", new Label("test 1"));
		lHarmonica.addTab("test2", createTree(5) );
		lHarmonica.addTab("test3", createTree(500));

		// show
		primaryStage.setScene(new Scene(lHarmonica, 1000, 500));
		primaryStage.sizeToScene();
		primaryStage.show();
	}
	
	private TreeView<String> createTree(int size) {
		TreeItem<String> rootItem = new TreeItem<String>("Tree " + size);
		rootItem.setExpanded(true);
		for (int i = 0; i < size; i++) {
			rootItem.getChildren().add(new TreeItem<String>("Item " + i));
		}
		TreeView<String> treeView = new TreeView<String>(rootItem);
		return treeView;
	}
}
