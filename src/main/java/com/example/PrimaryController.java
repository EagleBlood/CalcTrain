package com.example;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PrimaryController {

    final double CLASS_FIRST_CLASS = 0.18;
    final double CLASS_SECOND_CLASS = 0.8;
    final double CLASS_COUCHETTE = 0.04;
    final double CLASS_SLEEPING = 0.02;
    final int PANE_PADDING = 15;

    @FXML
    private Label arrCount;

    @SuppressWarnings("rawtypes")
    @FXML
    private Spinner fame;

    @FXML
    private VBox input;

    @FXML
    private GridPane view1;

    @FXML
    private GridPane view2;

    @FXML
    private GridPane view3;

    @FXML
    private GridPane view4;

    @FXML
    private GridPane view_tickets1;

    @FXML
    private GridPane view_tickets2;

    @FXML
    private GridPane view_tickets3;

    @FXML
    private GridPane view_tickets4;

    @FXML
    private Button importButton;

    @FXML
    private TabPane tabValues;


    private List<TextField[]> textFieldsList = new ArrayList<>();
    private int hboxCount = 0;
    private List<TicketPrice> ticketPrices;
    private double couchette;
    private double sleepClass;
    private String filePath = null;
    private File lastOpenedDirectory = null;

    @SuppressWarnings("unchecked")
    @FXML
    public void initialize() {
        if (fame != null) {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1);
            fame.setValueFactory(valueFactory);
        }
    
        if (input != null) {
            addHBox();
            addHBox();
            arrCount.setText(String.valueOf(hboxCount));
        }
    }
    
    @FXML
    private void switchToJSONCreator() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("secondary.fxml"));

        Stage secondaryStage = new Stage();
        secondaryStage.setScene(new Scene(root));
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.showAndWait();
    }

    @FXML
    private void addHBox() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10.0);
    
        Label label = new Label(String.valueOf(hboxCount) + ".");
        label.setMinWidth(20);
        label.setAlignment(Pos.CENTER_LEFT);
        label.getStyleClass().add("appTextFieldBold");
        hbox.getChildren().add(label);
    
        String[] prompts = {"Dist", "Pop", "Time"};
        TextField[] textFields = new TextField[3];
        for (int i = 0; i < 3; i++) {
            TextField textField = new TextField();
            textField.setPromptText(prompts[i]);
            textField.getStyleClass().add("appInputFieldMain");
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    textField.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            });
            hbox.getChildren().add(textField);
            textFields[i] = textField;
        }
    
        input.getChildren().add(hbox);
        textFieldsList.add(textFields);
    
        hboxCount++;
        arrCount.setText(String.valueOf(hboxCount));
    }

    @FXML
    private void removeHBox() {
        if (input.getChildren().size() > 2) { // 2 added in init hboxes
            hboxCount--;
            arrCount.setText(String.valueOf(hboxCount));

            input.getChildren().remove(input.getChildren().size() - 1);
            textFieldsList.remove(textFieldsList.size() - 1);
        }
    }

    public List<double[]> getValues() {
        List<double[]> values = new ArrayList<>();
        for (TextField[] textFields : textFieldsList) {
            double[] arr = new double[3];
            for (int i = 0; i < 3; i++) {
                String text = textFields[i].getText();
                if (text.isEmpty()) {
                    return null;
                }
                arr[i] = Double.parseDouble(text);
            }
            values.add(arr);
        }

        return values;
    }

    public List<TicketPrice> getValuesAsTicketPrices() {
    List<TicketPrice> values = new ArrayList<>();
    try {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject jsonObject = new JSONObject(content);

        couchette = jsonObject.getDouble("couchette");
        sleepClass = jsonObject.getDouble("sleepClass");

        JSONArray tableData = jsonObject.getJSONArray("tableData");
        for (int i = 0; i < tableData.length(); i++) {
            JSONObject data = tableData.getJSONObject(i);
            double fromStation = data.getDouble("fromStation");
            double toStation = data.getDouble("toStation");
            double secondClassPrice = data.getDouble("secondClassPrice");
            double firstClassPrice = data.getDouble("firstClassPrice");             
            values.add(new TicketPrice(fromStation, toStation, firstClassPrice, secondClassPrice));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return values;
}

    @FXML
    private void handleImportButtonAction(ActionEvent event) {
        filePath = null;
        getJSONFilePath();
    }

    private String getJSONFilePath() {       
        if (filePath != null) {
            return filePath;
        }
    
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
    
        // Set the initial directory to the last opened directory
        if (lastOpenedDirectory != null) {
            fileChooser.setInitialDirectory(lastOpenedDirectory);
        }
    
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Store the directory of the selected file
            lastOpenedDirectory = selectedFile.getParentFile();
    
            String fileName = selectedFile.getName();
            importButton.setText(fileName.substring(0, fileName.lastIndexOf('.')));
            filePath = selectedFile.getPath();
            return filePath;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("No File Selected");
            alert.setContentText("Please select a file before proceeding.");
            alert.showAndWait();
            importButton.setText("Import Tariff");
            return "";
        }
    }

    @SuppressWarnings("exports")
    public void addLabelsToGrid(GridPane view, GridPane view_tickets, List<double[]> values, double classes) {
        TicketPrice matchingTicketPrice = null;
        ticketPrices = getValuesAsTicketPrices();
        double finalTicketPrice = 0;
        boolean setErrorMesseage = false;

        for (int i = 0; i < values.size(); i++) {
            // Row label for view
            Label rowLabelView = new Label(i + "");
            rowLabelView.setMaxWidth(Double.MAX_VALUE);
            rowLabelView.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
            rowLabelView.setAlignment(Pos.CENTER);
    
            StackPane rowPaneView = new StackPane(rowLabelView);
            rowPaneView.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
            rowPaneView.getStyleClass().add("paneHeader");
    
            GridPane.setHalignment(rowPaneView, HPos.CENTER);
            GridPane.setValignment(rowPaneView, VPos.CENTER);
    
            view.add(rowPaneView, 0, i + 1);
    
            // Row label for view_tickets
            Label rowLabelTickets = new Label(i + "");
            rowLabelTickets.setMaxWidth(Double.MAX_VALUE);
            rowLabelTickets.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
            rowLabelTickets.setAlignment(Pos.CENTER);
    
            StackPane rowPaneTickets = new StackPane(rowLabelTickets);
            rowPaneTickets.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
            rowPaneTickets.getStyleClass().add("paneHeader");
    
            GridPane.setHalignment(rowPaneTickets, HPos.CENTER);
            GridPane.setValignment(rowPaneTickets, VPos.CENTER);
    
            view_tickets.add(rowPaneTickets, 0, i + 1);
    
            for (int j = 0; j < values.size(); j++) {
                // Column label for view
                if (i == 0) {
                    Label columnLabelView = new Label(j + "");
                    columnLabelView.setMaxWidth(Double.MAX_VALUE);
                    columnLabelView.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
                    columnLabelView.setAlignment(Pos.CENTER);
    
                    StackPane columnPaneView = new StackPane(columnLabelView);
                    columnPaneView.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
                    columnPaneView.getStyleClass().add("paneHeader");
    
                    GridPane.setHalignment(columnPaneView, HPos.CENTER);
                    GridPane.setValignment(columnPaneView, VPos.CENTER);
    
                    view.add(columnPaneView, j + 1, 0);
    
                    // Column label for view_tickets
                    Label columnLabelTickets = new Label(j + "");
                    columnLabelTickets.setMaxWidth(Double.MAX_VALUE);
                    columnLabelTickets.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
                    columnLabelTickets.setAlignment(Pos.CENTER);
    
                    StackPane columnPaneTickets = new StackPane(columnLabelTickets);
                    columnPaneTickets.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
                    columnPaneTickets.getStyleClass().add("paneHeader");
    
                    GridPane.setHalignment(columnPaneTickets, HPos.CENTER);
                    GridPane.setValignment(columnPaneTickets, VPos.CENTER);
    
                    view_tickets.add(columnPaneTickets, j + 1, 0);
                }

                // Label   
                Label label;
                StackPane pane = new StackPane();
                pane.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
    
                if (i == j) {
                    label = new Label("");
                    pane.getStyleClass().add("bg");

                    StackPane diagonalPane = new StackPane(label);
                    diagonalPane.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
                    diagonalPane.getStyleClass().add("bg");

                    view_tickets.add(diagonalPane, j + 1, i + 1);

                    GridPane.setFillWidth(diagonalPane, true);
                    GridPane.setFillHeight(diagonalPane, true);
                } else {
                    double[] A = values.get(i);
                    double[] B = values.get(j);
    
                    if (A.length < 3 || B.length < 3) {
                        System.out.println("Error: Each array should have 3 elements.");
                        return;
                    }
                    double distance = (double)B[0] - A[0];

                    for (TicketPrice ticketPrice : ticketPrices) {
                        if (distance >= ticketPrice.getFromStation() && distance < ticketPrice.getToStation()) {
                            matchingTicketPrice = ticketPrice;
                            break;
                        }
                    }

                    double pairResult = ( ((double)A[1] + B[1]) / 600000 * ( distance / (B[2] - A[2]) ) * (Integer)fame.getValue() * 1.6 ) * classes;

                    if (matchingTicketPrice != null) {
                        if (classes == CLASS_FIRST_CLASS) {
                            finalTicketPrice = pairResult * matchingTicketPrice.getFirstClassPrice();
                        } else if (classes == CLASS_SECOND_CLASS) {
                            finalTicketPrice = pairResult * matchingTicketPrice.getSecondClassPrice();
                        } else if (classes == CLASS_COUCHETTE) {
                            finalTicketPrice = pairResult * (couchette + matchingTicketPrice.getSecondClassPrice());
                        } else if (classes == CLASS_SLEEPING) {
                            finalTicketPrice = pairResult * (sleepClass + matchingTicketPrice.getSecondClassPrice());
                        } else {
                            System.out.println("Invalid train class.");
                        }
                    
                        // New label with the value of finalTicketPrice
                        String formattedTicketPrice = String.format("%.2f", finalTicketPrice);
                        Label ticketPriceLabel = new Label(formattedTicketPrice);
                        ticketPriceLabel.setAlignment(Pos.CENTER);
                        ticketPriceLabel.setMaxWidth(Double.MAX_VALUE);
                        ticketPriceLabel.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
                    
                        StackPane ticketPricePane = new StackPane(ticketPriceLabel);
                        ticketPricePane.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
                    
                        view_tickets.add(ticketPricePane, j + 1, i + 1);

                        GridPane.setFillWidth(ticketPricePane, true);
                        GridPane.setFillHeight(ticketPricePane, true);

                        highlightPane(view_tickets, ticketPricePane, i, j);

                       
                    } else { // TODO - needs a better way of handling price out of range best to not generate whole grid if no matching ticket price found
                        setErrorMesseage = true;
                        //System.out.println("No matching ticket price found.");
                    }

                    String formattedResult = String.valueOf((int)pairResult);
                    label = new Label(formattedResult);
                    label.setAlignment(Pos.CENTER);
                    label.setMaxWidth(Double.MAX_VALUE);
                    label.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
                }

                if(setErrorMesseage == true)
                {
                    view_tickets.getChildren().clear();
                    view_tickets.add(new Label("No matching ticket price found."), 0, 0);
                }

                pane.getChildren().add(label);
                GridPane.setHalignment(pane, HPos.CENTER);
                GridPane.setValignment(pane, VPos.CENTER);
        
                // Add the pane to the GridPane
                view.add(pane, j + 1, i + 1);
                GridPane.setFillWidth(pane, true);
                GridPane.setFillHeight(pane, true);
                
                // Highlight the pane when clicked
                highlightPane(view, pane, i, j);
            }
        }
    }

    @FXML
    public void sumValues() {
        List<double[]> values = getValues();

        if (values == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("No Values");
            alert.setContentText("Please fill all fields before proceeding.");
            alert.showAndWait();
            return;
        }

        if (importButton.getText().equals("Import Tariff")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("No Tariff Imported");
            alert.setContentText("Please import a tariff before proceeding.");
            alert.showAndWait();
            return;
        }
    
        view1.getChildren().clear();
        view2.getChildren().clear();
        view3.getChildren().clear();
        view4.getChildren().clear();

        view_tickets1.getChildren().clear();
        view_tickets2.getChildren().clear();
        view_tickets3.getChildren().clear();
        view_tickets4.getChildren().clear();
    
        addLabelsToGrid(view1, view_tickets1, values, CLASS_FIRST_CLASS);
        addLabelsToGrid(view2, view_tickets2, values, CLASS_SECOND_CLASS);
        addLabelsToGrid(view3, view_tickets3, values, CLASS_COUCHETTE);
        addLabelsToGrid(view4, view_tickets4, values, CLASS_SLEEPING);
    }

    private void highlightPane(GridPane view, StackPane pane, int i, int j) {
        pane.setOnMouseClicked(event -> {
            if (pane.getStyleClass().contains("bg")) {
                return;
            }
    
            boolean isHighlighted = pane.getStyleClass().contains("highlight");
    
            view.getChildren().forEach(node -> {
                if (node instanceof StackPane) {
                    StackPane stackPaneNode = (StackPane) node;
                    stackPaneNode.getStyleClass().remove("highlight");
                    stackPaneNode.getStyleClass().remove("highlightValue");
                    if (!stackPaneNode.getChildrenUnmodifiable().isEmpty() && stackPaneNode.getChildrenUnmodifiable().get(0) instanceof Label) {
                        stackPaneNode.getChildrenUnmodifiable().get(0).getStyleClass().remove("highlightValueText");
                    }
                }
            });
    
            if (isHighlighted) {
                return;
            }
    
            for (Node node : view.getChildren()) {
                if ((GridPane.getRowIndex(node) <= i + 1 && GridPane.getColumnIndex(node) == j + 1) ||
                    (GridPane.getColumnIndex(node) <= j + 1 && GridPane.getRowIndex(node) == i + 1)) {
                    if (node instanceof StackPane) {
                        node.getStyleClass().add("highlight");
                    }
                }
            }
    
            pane.getStyleClass().add("highlightValue");
    
            if (!pane.getChildrenUnmodifiable().isEmpty() && pane.getChildrenUnmodifiable().get(0) instanceof Label) {
                pane.getChildrenUnmodifiable().get(0).getStyleClass().add("highlightValueText");
            }
        });
    }
    
}