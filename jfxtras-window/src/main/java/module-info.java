module jfxtras.window {
    exports jfxtras.scene.control.window;

    opens jfxtras.internal.scene.control.skin.window to javafx.controls;
    requires transitive jfxtras.common;
}
