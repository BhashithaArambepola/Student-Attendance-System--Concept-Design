package Controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class splashScreenController {
    public Label lblStatus;

    public void initialize() {
        establishDatBaseConnection();
    }

    private void establishDatBaseConnection() {
        lblStatus.setText("Establish Data Base Connection...");
        new Thread(() -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sipwin", "root", "78527852");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                if (e.getSQLState().equals("42000")) {
                    Platform.runLater(this::loadImportDataBaseForm);
                }
                e.printStackTrace();
            }
        }).start();

    }

    private void loadImportDataBaseForm() {
        try {
            Stage stage = new Stage();
            AnchorPane root = FXMLLoader.load(this.getClass().getResource("/View/importDataBaseForm.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle("First Time Boot");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(lblStatus.getScene().getWindow());
            stage.sizeToScene();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}




