module hse.ce.jameskok.jigsawmultiplayer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;
    requires java.sql;

    opens hse.ce.jameskok.jigsawmultiplayer to javafx.fxml;
    exports hse.ce.jameskok.jigsawmultiplayer;
    exports hse.ce.jameskok.jigsawmultiplayer.client;
    opens hse.ce.jameskok.jigsawmultiplayer.client to javafx.fxml;

    exports hse.ce.jameskok.jigsawmultiplayer.server;
    opens hse.ce.jameskok.jigsawmultiplayer.server to javafx.fxml;
}