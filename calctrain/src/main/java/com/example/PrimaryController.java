package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PrimaryController {

    @FXML
    private VBox input;

    @FXML
    private GridPane view1;

    private List<TextField[]> textFieldsList = new ArrayList<>();
    private int hboxCount = 1;

    @FXML
    public void initialize() {
        addHBox();
        addHBox();
    }

    // FIX heigh of hbox with labels
    // ADD scrolloable functionality inside the gridpane view1
    // ADD paddings for the gridpane view1
    
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
    }

    @FXML
    private void removeHBox() {
        if (input.getChildren().size() > 2) { // 2 vboxes
            input.getChildren().remove(input.getChildren().size() - 1);
            textFieldsList.remove(textFieldsList.size() - 1);
            hboxCount--;
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

    public int sumValues() {
        List<double[]> values = getValues();
        if (values == null || values.size() < 2) {
            System.out.println("Error: At least 2 values are required.");
            return -1;
        }
    
        double result = 0;
        int temp = 600000;
        double temp2 = 1.6;
        int fame = 3;
        double variaty = 0.18;
    
        // Clear the GridPane
        view1.getChildren().clear();
    
        /*for(int i=0; i < values.size(); i++){
            System.out.println(i + ": " + Arrays.toString(values.get(i)));
        }*/
    
        for (int i = 0; i < values.size(); i++) {
            // Add row label
            view1.add(new Label("Pair " + i), 0, i + 1);
    
            for (int j = 0; j < values.size(); j++) {
                // Add column label
                if (i == 0) {
                    view1.add(new Label("Pair " + j), j + 1, 0);
                }
    
                Label label;
                if (i == j) {
                    label = new Label("X");
                } else {
                    double[] A = values.get(i);
                    double[] B = values.get(j);
    
                    if (A.length < 3 || B.length < 3) {
                        System.out.println("Error: Each array should have 3 elements.");
                        return -1;
                    }
                    double pairResult = ( ((double)A[1] + B[1]) / temp * ( ((double)B[0] - A[0]) / (B[2] - A[2]) ) * fame * temp2 ) * variaty;
                    result += pairResult;
                    //System.out.println("Pair " + i + j + ": " + (int)pairResult);

                    String formattedResult = String.valueOf((int)pairResult);
                    label = new Label(formattedResult);
                }
    
                // Add the label to the GridPane
                view1.add(label, j + 1, i + 1);
            }
        }
    
        System.out.println("Result: " + (int)result);
        return (int)result;
    }
    
}