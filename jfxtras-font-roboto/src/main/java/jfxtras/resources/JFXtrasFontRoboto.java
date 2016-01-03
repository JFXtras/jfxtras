package jfxtras.resources;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JFXtrasFontRoboto {
	private static String cssPath() {
		return JFXtrasFontRoboto.class.getResource("/jfxtras-font-roboto.css").toExternalForm();
	}
	
	private static void addCSSPathToStylesheets(Stage stage) {
		addCSSPathToStylesheets(stage.getScene());
	}
	
	private static void addCSSPathToStylesheets(Scene scene) {
		String cssPath = cssPath();
	    ObservableList<String> stylesheets = scene.getStylesheets();
	    if (!stylesheets.contains(cssPath)) {
	    	stylesheets.add(cssPath());
	    }
	    for (String familyName : javafx.scene.text.Font.getFamilies()) {
	    	System.out.println("TBEE family " + familyName);
	    }
	    for (String familyName : javafx.scene.text.Font.getFontNames()) {
	    	System.out.println("TBEE font " + familyName);
	    }
	}
	
	public static void load() {
		if (javafx.scene.text.Font.getFontNames("Roboto").isEmpty()) {
		    for (AvailableFaces f : AvailableFaces.values()) {
			    javafx.scene.text.Font.loadFont(JFXtrasFontRoboto.class.getResource("/" + f.getFilename()).toExternalForm(), 12);
		    }
		    for (String familyName : javafx.scene.text.Font.getFamilies()) {
		    	System.out.println("TBEE family " + familyName);
		    }
		    for (String familyName : javafx.scene.text.Font.getFontNames()) {
		    	System.out.println("TBEE font " + familyName);
		    }
		}
	}
	
	public enum AvailableFaces {
		// TBEERNOT The commented out fonts have the same family name and then are not accessible for JavaFX
		RobotoBlack("Roboto Black", "Roboto-Black.ttf"),
		//RobotoBlackItalic("Roboto Black Italic", "Roboto-BlackItalic.ttf"),
		RobotoBold("Roboto", "Roboto-Bold.ttf"),
		//RobotoBoldItalic("Roboto Bold Italic", "Roboto-BoldItalic.ttf"),
		//RobotoItalic("Roboto Italic", "Roboto-Italic.ttf"),
		RobotoLight("Roboto Light", "Roboto-Light.ttf"),
		RobotoLightItalic("Roboto Black", "Roboto-LightItalic.ttf"),
		RobotoMedium("Roboto Medium", "Roboto-Medium.ttf"),
		//RobotoMediumItalic("Roboto Medium Italic", "Roboto-MediumItalic.ttf"),
		//RobotoRegular("Roboto Regular", "Roboto-Regular.ttf"),
		RobotoThin("Roboto Thin", "Roboto-Thin.ttf"),
		//RobotoThinItalic("Roboto Thin Italic", "Roboto-ThinItalic.ttf"),
		RobotoCondensedBold("Roboto Condensed", "RobotoCondensed-Bold.ttf"),
		//RobotoCondensedBoldItalic("Roboto Condensed Bold Italic", "RobotoCondensed-BoldItalic.ttf"),
		//RobotoCondensedItalic("Roboto Condensed Italic", "RobotoCondensed-Italic.ttf"),
		RobotoCondensedLight("Roboto Condensed Light", "RobotoCondensed-Light.ttf"),
		//RobotoCondensedLightItalic("Roboto Condensed Light Italic", "RobotoCondensed-LightItalic.ttf"),
		//RobotoCondensedRegular("Roboto Condensed Regular", "RobotoCondensed-Regular.ttf")
		;
		
		AvailableFaces(String fontname, String filename) {
			this.fontname = fontname;
			this.filename = filename;
		}
		final private String fontname;
		final private String filename;
		
		public String getFontname() {
			return this.fontname;
		}
		String getFilename() {
			return this.filename;
		}
	}
}
