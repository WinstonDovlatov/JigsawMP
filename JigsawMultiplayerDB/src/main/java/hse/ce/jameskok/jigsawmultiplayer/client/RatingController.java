package hse.ce.jameskok.jigsawmultiplayer.client;

import hse.ce.jameskok.jigsawmultiplayer.GameResult;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public final class RatingController implements Initializable {
    @FXML
    private TableView<GameResult> ratingTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] columnNames = {"username", "date", "score", "duration"};
        for(int i=0; i<columnNames.length;++i){
            ratingTable.getColumns().get(i).setCellValueFactory(new PropertyValueFactory<>(columnNames[i]));
        }
    }

    public void setData(ArrayList<GameResult> results){
        Platform.runLater(() -> ratingTable.getItems().addAll(results));
    }
}
