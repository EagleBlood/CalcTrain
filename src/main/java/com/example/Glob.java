package com.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Glob {

    public final double CLASS_FIRST_CLASS = 0.18;
    public final double CLASS_SECOND_CLASS = 0.8;
    public final double CLASS_COUCHETTE = 0.04;
    public final double CLASS_SLEEPING = 0.02;
    public final String THEME_PREF_KEY = "theme";
    public final String DARK_THEME = "styleDark.css";
    public final String LIGHT_THEME = "style.css";

    public final String NAMES_PREF_KEY = "names";
    public final String LABEL_NAMES = "label";
    public final String TOWN_NAMES = "town";
    public final String PREFS_FILE = "config/app_prefs.properties";

    public void showWarningAlert(String title, String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ((Stage)alert.getDialogPane().getScene().getWindow()).initStyle(StageStyle.UNDECORATED);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("styleDark.css").toExternalForm());
        dialogPane.getStyleClass().add("DialogPane");

        alert.showAndWait();
    }

    public void showInformationAlert(String title, String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
    
        ((Stage)alert.getDialogPane().getScene().getWindow()).initStyle(StageStyle.UNDECORATED);
    
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/example/styleDark.css").toExternalForm());
        dialogPane.getStyleClass().add("DialogPane");
    
        alert.showAndWait();
    }
    
    public void showErrorAlert(String title, String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
    
        ((Stage)alert.getDialogPane().getScene().getWindow()).initStyle(StageStyle.UNDECORATED);
    
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/example/styleDark.css").toExternalForm());
        dialogPane.getStyleClass().add("DialogPane");
    
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

    public List<TicketPrice> getValuesAsTicketPrices(String filePath, double couchette, double sleepClass) {
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

    @SuppressWarnings("exports")
    public List<TownValues> getValues(List<Node[]> listOfNodes, CheckMenuItem checkMenuItem) {
        List<TownValues> values = new ArrayList<>();
        for (Node[] nodes : listOfNodes) {
            if (checkMenuItem.isSelected()) {
                if (nodes.length < 4) {
                    System.out.println("Returning null because nodes.length < 4");
                    return null;
                }
                String townName = ((TextField) nodes[0]).getText();
                double[] arr = new double[3];
                for (int i = 0; i < 3; i++) {
                    if (nodes[i + 1] == null) {
                        System.out.println("Skipping because nodes[" + (i + 1) + "] is null");
                        continue;
                    }
                    String text = ((TextField) nodes[i + 1]).getText();
                    if (text.isEmpty()) {
                        System.out.println("Returning null because text is empty");
                        return null;
                    }
                    arr[i] = Double.parseDouble(text);
                }
                values.add(new TownValues(townName, arr));
            } else {
                String townName;
                if (nodes[0] instanceof Label) {
                    townName = ((Label) nodes[0]).getText();
                } else if (nodes[0] instanceof TextField) {
                    townName = ((TextField) nodes[0]).getText();
                } else {
                    throw new RuntimeException("Unexpected type: " + nodes[0]);
                }
                double[] arr = new double[3];
                for (int i = 0; i < 3; i++) {
                    if (nodes[i] == null) {
                        System.out.println("Skipping because nodes[" + i + "] is null");
                        continue;
                    }
                    String text = ((TextField) nodes[i]).getText();
                    if (text.isEmpty()) {
                        System.out.println("Returning null because text is empty");
                        return null;
                    }
                    arr[i] = Double.parseDouble(text);
                }
                values.add(new TownValues(townName, arr));
            }
        }
        System.out.println("Returning values: " + values);
        return values;
    }
    
}
