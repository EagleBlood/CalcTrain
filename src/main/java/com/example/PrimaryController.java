package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PrimaryController {

    final double CLASS_FIRST_CLASS = 0.18;
    final double CLASS_SECOND_CLASS = 0.8;
    final double CLASS_COUCHETTE = 0.04;
    final double CLASS_SLEEPING = 0.02;
    final int PANE_PADDING = 15;

    @FXML
    private Label arrCount;

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

    private List<TextField[]> textFieldsList = new ArrayList<>();
    private int hboxCount = 0;

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9, 1);
        fame.setValueFactory(valueFactory);
       
        addHBox();
        addHBox();

        arrCount.setText(String.valueOf(hboxCount));
    }
    
    @FXML
    private void addHBox() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10.0);

        Label label = new Label(String.valueOf(hboxCount) + ".");
        label.setMinWidth(25);
        label.setAlignment(Pos.CENTER);
        hbox.getChildren().add(label);

        TextField[] textFields = new TextField[3];
        for (int i = 0; i < 3; i++) {
            TextField textField = new TextField();
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
        if (input.getChildren().size() > 3) { // 2 vboxes and sth
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
                if (text == null || text.isEmpty()) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Input Error");
                    alert.setContentText("Please fill all the text fields!");
                    alert.showAndWait();
                    return null;
                }
                arr[i] = Double.parseDouble(text);
            }
            values.add(arr);
        }

        return values;
    }

    public void addLabelsToGrid(GridPane view, List<double[]> values, double classes) {
        for (int i = 0; i < values.size(); i++) {
            // Row label
            Label rowLabel = new Label(i + "");
            rowLabel.setMaxWidth(Double.MAX_VALUE);
            rowLabel.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
            rowLabel.setAlignment(Pos.CENTER);

            StackPane rowPane = new StackPane(rowLabel);
            rowPane.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
            rowPane.getStyleClass().add("paneHeader");

            GridPane.setHalignment(rowPane, HPos.CENTER);
            GridPane.setValignment(rowPane, VPos.CENTER);

            view.add(rowPane, 0, i + 1);
    
            for (int j = 0; j < values.size(); j++) {
                // Column label
                if (i == 0) {
                    Label columnLabel = new Label(j + "");
                    columnLabel.setMaxWidth(Double.MAX_VALUE);
                    columnLabel.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
                    columnLabel.setAlignment(Pos.CENTER);

                    StackPane columnPane = new StackPane(columnLabel);
                    columnPane.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
                    columnPane.getStyleClass().add("paneHeader");

                    GridPane.setHalignment(columnPane, HPos.CENTER);
                    GridPane.setValignment(columnPane, VPos.CENTER);

                    view.add(columnPane, j + 1, 0);
                }

                // Label   
                Label label;
                StackPane pane = new StackPane();
                pane.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
    
                if (i == j) {
                    label = new Label("");
                    pane.getStyleClass().add("bg");
                } else {
                    double[] A = values.get(i);
                    double[] B = values.get(j);
    
                    if (A.length < 3 || B.length < 3) {
                        System.out.println("Error: Each array should have 3 elements.");
                        return;
                    }
                    double pairResult = ( ((double)A[1] + B[1]) / 600000 * ( ((double)B[0] - A[0]) / (B[2] - A[2]) ) * (Integer)fame.getValue() * 1.6 ) * classes;
                    String formattedResult = String.valueOf((int)pairResult);
                    label = new Label(formattedResult);
                    label.setAlignment(Pos.CENTER);
                    label.setMaxWidth(Double.MAX_VALUE);
                    label.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
                }
    
                pane.getChildren().add(label);
                GridPane.setHalignment(pane, HPos.CENTER);
                GridPane.setValignment(pane, VPos.CENTER);
    
                // Add the pane to the GridPane
                view.add(pane, j + 1, i + 1);
    
                GridPane.setFillWidth(pane, true);
                GridPane.setFillHeight(pane, true);
                
                
                // Highlight functionality
                final int finalI = i;
                final int finalJ = j;
    
                // Add click listener to the pane
                pane.setOnMouseClicked(event -> {
                    if (pane.getStyleClass().contains("bg")) {
                        return;
                    }
                    
                    boolean isHighlighted = pane.getStyleClass().contains("highlight");

                    // Clear previous highlights
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

                    // If the pane was already highlighted, don't highlight it again
                    if (isHighlighted) {
                        return;
                    }

                    // Highlight the row and column up to the selected cell
                    for (Node node : view.getChildren()) {
                        if ((GridPane.getRowIndex(node) <= finalI + 1 && GridPane.getColumnIndex(node) == finalJ + 1) ||
                            (GridPane.getColumnIndex(node) <= finalJ + 1 && GridPane.getRowIndex(node) == finalI + 1)) {
                            if (node instanceof StackPane) {
                                node.getStyleClass().add("highlight");
                            }
                        }
                    }

                    // Highlight the selected cell
                    pane.getStyleClass().add("highlightValue");
                    
                    // Highlighted text style
                    if (!pane.getChildrenUnmodifiable().isEmpty() && pane.getChildrenUnmodifiable().get(0) instanceof Label) {
                        pane.getChildrenUnmodifiable().get(0).getStyleClass().add("highlightValueText");
                    }
                });
            }
    
        }
    }

    @FXML
    public void sumValues() {
        List<double[]> values = getValues();
    
        view1.getChildren().clear();
        view2.getChildren().clear();
        view3.getChildren().clear();
        view4.getChildren().clear();
    
        addLabelsToGrid(view1, values, CLASS_FIRST_CLASS);
        addLabelsToGrid(view2, values, CLASS_SECOND_CLASS);
        addLabelsToGrid(view3, values, CLASS_COUCHETTE);
        addLabelsToGrid(view4, values, CLASS_SLEEPING);
    }
    
}