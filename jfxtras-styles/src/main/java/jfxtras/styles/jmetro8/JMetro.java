package jfxtras.styles.jmetro8;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class JMetro {
    public enum Style {
        LIGHT,
        DARK;

        private String getStyleSheetFileName() {
            String stylesheet = null;
            switch (this) {
                case LIGHT:
                    stylesheet = "JMetroLightTheme.css";
                    break;
                case DARK:
                    stylesheet = "JMetroDarkTheme.css";
                    break;
            }
            return stylesheet;
        }
    }

    private Style style;

    public JMetro(Style style) {
        this.style = style;
    }

    public void applyTheme(Scene scene){
        scene.getStylesheets().add(JMetro.class.getResource(style.getStyleSheetFileName()).toExternalForm());
    }

    public void applyTheme(Parent parent){
        parent.getStylesheets().add(JMetro.class.getResource(style.getStyleSheetFileName()).toExternalForm());
    }


}

