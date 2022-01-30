package Controller;

import Db.DBConnection;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class splashScreenController {
    public Label lblStatus;
    private File file;


    public void initialize() {
        establishDatBaseConnection();
    }

    private void establishDatBaseConnection() {
        lblStatus.setText("Establish Data Base Connection...");
        new Thread(() -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sipwin_attendance", "root", "78527852");
                sleep(100);

                Platform.runLater(() -> lblStatus.setText("Setting up the UI..."));
                sleep(100);

                Platform.runLater(() -> {
                    loadLoginForm(connection);
                });

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
            SimpleObjectProperty<File> fileProperty = new SimpleObjectProperty<>();

            Stage stage = new Stage();
//

            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/View/importDataBaseForm.fxml"));
            AnchorPane root = fxmlLoader.load();
            importDataBaseFormController controller = fxmlLoader.getController();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.setTitle("First Time Boot");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(lblStatus.getScene().getWindow());
            stage.sizeToScene();
            stage.showAndWait();
            file = fileProperty.getValue();


            if (fileProperty.getValue() == null) {
                lblStatus.setText("Creating New DataBase...");
                new Thread(() -> {
                    try {
                        sleep(100);
                        Platform.runLater(() -> lblStatus.setText("Loading Data Base Script..."));

                        InputStream is = this.getClass().getResourceAsStream("/Asset/dataBaseScript.sql");
                        byte[] buffer = new byte[is.available()];
                        is.read(buffer);
                        String script = new String(buffer);
                        sleep(100);

                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?allowMultiQueries=true", "root", "78527852");
                        Platform.runLater(() -> lblStatus.setText("Executed dataBase Script..."));
                        Statement statement = connection.createStatement();
                        statement.execute(script);
                        connection.close();
                        sleep(100);

                        Platform.runLater(() -> lblStatus.setText("Obtaining a new Dat Base Connection.."));
                        DriverManager.getConnection("jdbc:mysql://localhost:3306/sipwin_attendance", "root", "78527852");
                        sleep(100);

                        DBConnection.getInstance().init(connection);

                        Platform.runLater(() -> {
                            lblStatus.setText("Setting up the Ui...");
                            sleep(100);

                            loadCreateAdminForm();

                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }).start();
            } else {
                /*ToDo: Restore the Backup*/

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadCreateAdminForm(){
      try {
          Stage stage=new Stage();
          AnchorPane load = FXMLLoader.load(this.getClass().getResource("/View/createAdminForm.fxml"));
          Scene scene=new Scene(load);

          stage.setScene(scene);
          stage.setTitle("Create Admin");
          stage.setResizable(false);
          stage.centerOnScreen();
          stage.sizeToScene();
          stage.show();
      }catch (Exception e){
          e.printStackTrace();
      }
    }

    private void loadLoginForm(Connection connection){
        DBConnection.getInstance().init(connection);

        try {
            Stage stage = new Stage();
            AnchorPane root = FXMLLoader.load(this.getClass().getResource("/View/loginForm.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Log In");
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.sizeToScene();
            stage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}




