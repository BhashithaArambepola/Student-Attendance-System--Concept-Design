package Controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class importDataBaseFormController {
    public RadioButton rdoRestore;
    public ToggleGroup abc;
    public TextField txtBrowse;
    public Button btnBrowse;
    public RadioButton rdoFirstTime;
    public Button btnOK;
    private SimpleObjectProperty<File> fileProperty;


    public void initialize() {
        txtBrowse.setEditable(false);
    }

    public void initialFileProperty(SimpleObjectProperty<File> fileProperty) {
        this.fileProperty = fileProperty;

    }

    public void btnBrowse_OnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a BackupFile");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Backup file", "*.sipwinBackup"));
        File file = fileChooser.showOpenDialog(btnOK.getScene().getWindow());
        txtBrowse.setText(file != null ? file.getAbsolutePath() : "");
        fileProperty.setValue(file);

    }

    public void btnOK_OnAction(ActionEvent actionEvent) {

        if (rdoFirstTime.isSelected()) {
            fileProperty.setValue(null);
        }
        ((Stage) (btnOK.getScene().getWindow())).close();
    }


}
