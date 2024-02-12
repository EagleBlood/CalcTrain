package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;

public class SecondaryController {
    @FXML
    private TableView<TicketPrice> table;

    @FXML
    private TableColumn<TicketPrice, String> fromColumn;

    @FXML
    private TableColumn<TicketPrice, String> toColumn;

    @FXML
    private TableColumn<TicketPrice, Double> firstClassColumn;

    @FXML
    private TableColumn<TicketPrice, Double> secClassColumn;

    @FXML
    private TextField inputFrom;

    @FXML
    private TextField inputTo;

    @FXML
    private TextField inputFirstClass;

    @FXML
    private TextField inputSecClass;

    @FXML
    private TextField inputCouchette;

    @FXML
    private TextField inputSleepClass;

    @FXML
    public void initialize() {
        // Set the cell value factories
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("fromStation"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("toStation"));
        firstClassColumn.setCellValueFactory(new PropertyValueFactory<>("firstClassPrice"));
        secClassColumn.setCellValueFactory(new PropertyValueFactory<>("secondClassPrice"));
    }

    @FXML
    public void addRecord() {
        if (inputFrom.getText().isEmpty() || inputTo.getText().isEmpty() || 
            inputFirstClass.getText().isEmpty() || inputSecClass.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled!");

            alert.showAndWait();
        } else {
            // Get the input values
            double from = Double.parseDouble(inputFrom.getText());
            double to = Double.parseDouble(inputTo.getText());
            double firstClassPrice = Double.parseDouble(inputFirstClass.getText());
            double secClassPrice = Double.parseDouble(inputSecClass.getText());

            TicketPrice ticket = new TicketPrice(from, to, firstClassPrice, secClassPrice);

            table.getItems().add(ticket);

            inputFrom.clear();
            inputTo.clear();
            inputFirstClass.clear();
            inputSecClass.clear();

            inputFrom.requestFocus();
        }
    }

    @FXML
    public void removeRecord() {
        if (!table.getItems().isEmpty()) {
            int lastIndex = table.getItems().size() - 1;
            table.getItems().remove(lastIndex);
        }
    }

    @FXML
    public void cancelAndReturn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/primary.fxml"));
            Scene primaryScene = new Scene(loader.load());

            Stage stage = (Stage) table.getScene().getWindow();

            stage.setScene(primaryScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   @FXML
public void createJsonFile() {
    JSONObject jsonObject = new JSONObject();
    JSONArray jsonArray = new JSONArray();

    for (TicketPrice ticket : table.getItems()) {
        JSONObject ticketObject = new JSONObject();
        ticketObject.put("fromStation", ticket.getFromStation());
        ticketObject.put("toStation", ticket.getToStation());
        ticketObject.put("firstClassPrice", ticket.getFirstClassPrice());
        ticketObject.put("secondClassPrice", ticket.getSecondClassPrice());

        jsonArray.put(ticketObject);
    }

    jsonObject.put("tableData", jsonArray);
    jsonObject.put("couchette", inputCouchette.getText());
    jsonObject.put("sleepClass", inputSleepClass.getText());

    // Create a FileChooser
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save as");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
    File selectedFile = fileChooser.showSaveDialog(null);

    if (selectedFile != null) {
        try {
            Files.write(selectedFile.toPath(), jsonObject.toString().getBytes());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("JSON file created successfully!");

            alert.showAndWait();

            cancelAndReturn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
}