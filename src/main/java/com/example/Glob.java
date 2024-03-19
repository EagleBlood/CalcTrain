package com.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Glob {

    private static final String PREFS_FILE = "config/app_prefs.properties";

    public void showWarningAlert(String title, String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showInformationAlert(String title, String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showErrorAlert(String title, String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @SuppressWarnings("exports")
    public void highlightPane(GridPane view, StackPane pane, int i, int j) {
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

    public void resetWindow(Stage stage, String fxmlFilePath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSwitchState(CheckMenuItem customSwitch, String prefKey, String expectedValue) {
        Properties appPrefs = new Properties();
        try {
            // Load the existing properties
            appPrefs.load(new FileInputStream(PREFS_FILE));
    
            // Update the state of the customSwitch based on the property
            customSwitch.setSelected(expectedValue.equals(appPrefs.getProperty(prefKey)));
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
