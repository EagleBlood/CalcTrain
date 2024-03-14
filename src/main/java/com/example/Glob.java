package com.example;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Glob {

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
}
