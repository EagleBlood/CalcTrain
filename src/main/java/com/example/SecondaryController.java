package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;

public class SecondaryController {
    @FXML
    private TableView<TicketPrice> table;

    @FXML
    private TableColumn<TicketPrice, Double> fromColumn;

    @FXML
    private TableColumn<TicketPrice, Double> toColumn;

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
    private Button mainButton;

    @FXML
    private AnchorPane mainView;

    @FXML
    public void initialize() {
        table.setEditable(true);

        fromColumn.setCellValueFactory(new PropertyValueFactory<>("fromStation"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("toStation"));
        firstClassColumn.setCellValueFactory(new PropertyValueFactory<>("firstClassPrice"));
        secClassColumn.setCellValueFactory(new PropertyValueFactory<>("secondClassPrice"));

        fromColumn.setCellFactory(column -> {
            TextFieldTableCell<TicketPrice, Double> cell = new TextFieldTableCell<>(new DoubleStringConverter());
            cell.editingProperty().addListener((obs, wasEditing, isNowEditing) -> {
                if (isNowEditing) {
                    // Add the 'editing' style class when the cell enters editing mode
                    cell.getStyleClass().add("editing");
                } else {
                    // Remove the 'editing' style class when the cell exits editing mode
                    cell.getStyleClass().remove("editing");
                }
            });
            return cell;
        });
        toColumn.setCellFactory(column -> {
            TextFieldTableCell<TicketPrice, Double> cell = new TextFieldTableCell<>(new DoubleStringConverter());
            cell.editingProperty().addListener((obs, wasEditing, isNowEditing) -> {
                if (isNowEditing) {
                    // Add the 'editing' style class when the cell enters editing mode
                    cell.getStyleClass().add("editing");
                } else {
                    // Remove the 'editing' style class when the cell exits editing mode
                    cell.getStyleClass().remove("editing");
                }
            });
            return cell;
        });
        
        firstClassColumn.setCellFactory(column -> {
            TextFieldTableCell<TicketPrice, Double> cell = new TextFieldTableCell<>(new DoubleStringConverter());
            cell.editingProperty().addListener((obs, wasEditing, isNowEditing) -> {
                if (isNowEditing) {
                    // Add the 'editing' style class when the cell enters editing mode
                    cell.getStyleClass().add("editing");
                } else {
                    // Remove the 'editing' style class when the cell exits editing mode
                    cell.getStyleClass().remove("editing");
                }
            });
            return cell;
        });
        
        secClassColumn.setCellFactory(column -> {
            TextFieldTableCell<TicketPrice, Double> cell = new TextFieldTableCell<>(new DoubleStringConverter());
            cell.editingProperty().addListener((obs, wasEditing, isNowEditing) -> {
                if (isNowEditing) {
                    // Add the 'editing' style class when the cell enters editing mode
                    cell.getStyleClass().add("editing");
                } else {
                    // Remove the 'editing' style class when the cell exits editing mode
                    cell.getStyleClass().remove("editing");
                }
            });
            return cell;
        });

        fromColumn.setOnEditCommit(event -> {
            TicketPrice ticket = event.getRowValue();
            ticket.setFromStation(event.getNewValue());
        });

        toColumn.setOnEditCommit(event -> {
            TicketPrice ticket = event.getRowValue();
            ticket.setToStation(event.getNewValue());
        });

        firstClassColumn.setOnEditCommit(event -> {
            TicketPrice ticket = event.getRowValue();
            ticket.setFirstClassPrice(event.getNewValue());
        });

        secClassColumn.setOnEditCommit(event -> {
            TicketPrice ticket = event.getRowValue();
            ticket.setSecondClassPrice(event.getNewValue());
        });
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
        Stage stage = (Stage) table.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void createJsonFile() {
        if (table.getItems().isEmpty() || inputCouchette.getText().isEmpty() || inputSleepClass.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please ensure at least one record is present and both inputs below are filled.");
            alert.showAndWait();
            return;
        }
    
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

    @FXML
    public void importJsonFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open JSON File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                String content = new String(Files.readAllBytes(selectedFile.toPath()));
                JSONObject jsonObject = new JSONObject(content);

                table.getItems().clear();

                JSONArray jsonArray = jsonObject.getJSONArray("tableData");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject ticketObject = jsonArray.getJSONObject(i);
                    TicketPrice ticket = new TicketPrice(
                        ticketObject.getDouble("fromStation"),
                        ticketObject.getDouble("toStation"),
                        ticketObject.getDouble("firstClassPrice"),
                        ticketObject.getDouble("secondClassPrice")
                    );
                    table.getItems().add(ticket);
                }

                inputCouchette.setText(jsonObject.getString("couchette"));
                inputSleepClass.setText(jsonObject.getString("sleepClass"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("exports")
    public AnchorPane getAnchorPane() {
        return mainView;
    }
}