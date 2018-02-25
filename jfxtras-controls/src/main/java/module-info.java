module jfxtras.controls {
    exports jfxtras.scene.control;

    exports jfxtras.internal.scene.control.skin to jfxtras.agenda;

    requires transitive jfxtras.fxml;
    requires transitive jfxtras.common;
    requires transitive java.logging;
}