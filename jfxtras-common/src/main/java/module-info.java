module jfxtras.common {
    exports jfxtras.animation;
    exports jfxtras.css;
    exports jfxtras.css.converters;
    exports jfxtras.scene.layout;
    exports jfxtras.scene.layout.responsivepane;
    exports jfxtras.util;

    requires transitive javafx.controls;
    requires transitive javafx.fxml; // actually this is only needed for the test sources
}