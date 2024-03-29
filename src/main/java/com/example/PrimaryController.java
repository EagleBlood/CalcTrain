package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PrimaryController {

    final double CLASS_FIRST_CLASS = 0.18;
    final double CLASS_SECOND_CLASS = 0.8;
    final double CLASS_COUCHETTE = 0.04;
    final double CLASS_SLEEPING = 0.02;
    final int PANE_PADDING = 15;
    private static final String THEME_PREF_KEY = "theme";
    private static final String DARK_THEME = "styleDark.css";
    private static final String LIGHT_THEME = "style.css";
    private static final String PREFS_FILE = "config/app_prefs.properties";

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
    private GridPane viewTickets1;

    @FXML
    private GridPane viewTickets2;

    @FXML
    private GridPane viewTickets3;

    @FXML
    private GridPane viewTickets4;

    @FXML
    private Button importButton;

    @FXML
    private TabPane tabValues;

    @FXML
    private VBox menu;

    @FXML
    private SplitPane splitPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem clearMenuButton;

    @FXML
    private CheckMenuItem hideMenuSwitch;

    @FXML
    private CheckMenuItem themeMenuSwitch;

    @FXML
    private MenuItem exitMenuButton;

    @FXML
    private MenuItem aboutMenuButton;

    @FXML
    private AnchorPane mainView;

    @FXML
    private Button buttonIconEnlarge;

    @FXML
    private Region regionIconEnlarge;

    @FXML
    private HBox wholeMenuBar;


    private List<TextField[]> textFieldsList = new ArrayList<>();
    private int hboxCount = 0;
    private List<TicketPrice> ticketPrices;
    private double couchette;
    private double sleepClass;
    private String filePath = null;
    private File lastOpenedDirectory = null;
    private boolean isMaximized = false;
    private double xOffset = 0;
    private double yOffset = 0;

    /* TODO - refractor all aletrs to a global class
     * TODO - style the alert boxes
     * TODO - add a progress bar / circle to show the progress of the calculation
     * TODO - add a icon
     * TODO - add a splash screen
     * TODO - fix the lightmode styling
    */

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

        // make window draggable
        wholeMenuBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        wholeMenuBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        // Load the current theme from app_prefs.properties
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(PREFS_FILE)) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String currentTheme = props.getProperty(THEME_PREF_KEY, LIGHT_THEME);
        themeMenuSwitch.setSelected(currentTheme.equals(DARK_THEME));


        // Set the style of the main view
        URL darkStyle = getClass().getResource(DARK_THEME);
        URL lightStyle = getClass().getResource(LIGHT_THEME);

        if (darkStyle == null || lightStyle == null) {
            System.out.println("Could not find one or both of the stylesheets");
            return;
        }

        String darkStyleExternalForm = darkStyle.toExternalForm();
        String lightStyleExternalForm = lightStyle.toExternalForm();

        if (currentTheme.equals(DARK_THEME)) {
            mainView.getStylesheets().remove(lightStyleExternalForm);
            mainView.getStylesheets().add(darkStyleExternalForm);
        } else {
            mainView.getStylesheets().remove(darkStyleExternalForm);
            mainView.getStylesheets().add(lightStyleExternalForm);
        }
    }
    
    @FXML
    private void switchToJSONCreator() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
        Parent root = loader.load();

        SecondaryController secondaryController = loader.getController();

        AnchorPane secondaryView = secondaryController.getAnchorPane();

        // Load the current theme from app_prefs.properties
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(PREFS_FILE)) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String currentTheme = props.getProperty(THEME_PREF_KEY);
        URL themeURL = getClass().getResource(currentTheme);
        if (themeURL != null) {
            secondaryView.getStylesheets().add(themeURL.toExternalForm());
        }

        Stage secondaryStage = new Stage();
        //secondaryStage.initStyle(StageStyle.UNDECORATED);
        secondaryStage.setTitle("Create Tariff");
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
    public void addLabelsToGrid(GridPane view, GridPane viewTickets, List<double[]> values, double classes) {
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
            rowLabelView.getStyleClass().add("paneHeaderText");
    
            StackPane rowPaneView = new StackPane(rowLabelView);
            rowPaneView.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
            rowPaneView.getStyleClass().add("paneHeader");
    
            GridPane.setHalignment(rowPaneView, HPos.CENTER);
            GridPane.setValignment(rowPaneView, VPos.CENTER);
    
            view.add(rowPaneView, 0, i + 1);
    
            // Row label for viewTickets
            Label rowLabelTickets = new Label(i + "");
            rowLabelTickets.setMaxWidth(Double.MAX_VALUE);
            rowLabelTickets.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
            rowLabelTickets.setAlignment(Pos.CENTER);
            rowLabelTickets.getStyleClass().add("paneHeaderText");
    
            StackPane rowPaneTickets = new StackPane(rowLabelTickets);
            rowPaneTickets.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
            rowPaneTickets.getStyleClass().add("paneHeader");
    
            GridPane.setHalignment(rowPaneTickets, HPos.CENTER);
            GridPane.setValignment(rowPaneTickets, VPos.CENTER);
    
            viewTickets.add(rowPaneTickets, 0, i + 1);
    
            for (int j = 0; j < values.size(); j++) {
                // Column label for view
                if (i == 0) {
                    Label columnLabelView = new Label(j + "");
                    columnLabelView.setMaxWidth(Double.MAX_VALUE);
                    columnLabelView.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
                    columnLabelView.setAlignment(Pos.CENTER);
                    columnLabelView.getStyleClass().add("paneHeaderText");
    
                    StackPane columnPaneView = new StackPane(columnLabelView);
                    columnPaneView.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
                    columnPaneView.getStyleClass().add("paneHeader");
    
                    GridPane.setHalignment(columnPaneView, HPos.CENTER);
                    GridPane.setValignment(columnPaneView, VPos.CENTER);
    
                    view.add(columnPaneView, j + 1, 0);
    
                    // Column label for viewTickets
                    Label columnLabelTickets = new Label(j + "");
                    columnLabelTickets.setMaxWidth(Double.MAX_VALUE);
                    columnLabelTickets.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
                    columnLabelTickets.setAlignment(Pos.CENTER);
                    columnLabelTickets.getStyleClass().add("paneHeaderText");
    
                    StackPane columnPaneTickets = new StackPane(columnLabelTickets);
                    columnPaneTickets.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
                    columnPaneTickets.getStyleClass().add("paneHeader");
    
                    GridPane.setHalignment(columnPaneTickets, HPos.CENTER);
                    GridPane.setValignment(columnPaneTickets, VPos.CENTER);
    
                    viewTickets.add(columnPaneTickets, j + 1, 0);
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

                    viewTickets.add(diagonalPane, j + 1, i + 1);

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
                        ticketPriceLabel.getStyleClass().add("appTextField");
                    
                        StackPane ticketPricePane = new StackPane(ticketPriceLabel);
                        ticketPricePane.setPadding(new Insets(PANE_PADDING, PANE_PADDING, PANE_PADDING, PANE_PADDING));
                    
                        viewTickets.add(ticketPricePane, j + 1, i + 1);

                        GridPane.setFillWidth(ticketPricePane, true);
                        GridPane.setFillHeight(ticketPricePane, true);

                        highlightPane(viewTickets, ticketPricePane, i, j);

                       
                    } else { // TODO - needs a better way of handling price out of range best to not generate whole grid if no matching ticket price found
                        setErrorMesseage = true;
                        //System.out.println("No matching ticket price found.");
                    }

                    String formattedResult = String.valueOf((int)pairResult);
                    label = new Label(formattedResult);
                    label.setAlignment(Pos.CENTER);
                    label.setMaxWidth(Double.MAX_VALUE);
                    label.setMinWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
                    label.getStyleClass().add("appTextField");
                }

                if(setErrorMesseage == true)
                {
                    viewTickets.getChildren().clear();
                    Label viewTicketsLabel = new Label("No matching ticket price found.");
                    viewTicketsLabel.getStyleClass().add("appTextFieldBold");
                    viewTickets.add(viewTicketsLabel, 0, 0);
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
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("dialog-pane");

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

        viewTickets1.getChildren().clear();
        viewTickets2.getChildren().clear();
        viewTickets3.getChildren().clear();
        viewTickets4.getChildren().clear();
    
        addLabelsToGrid(view1, viewTickets1, values, CLASS_FIRST_CLASS);
        addLabelsToGrid(view2, viewTickets2, values, CLASS_SECOND_CLASS);
        addLabelsToGrid(view3, viewTickets3, values, CLASS_COUCHETTE);
        addLabelsToGrid(view4, viewTickets4, values, CLASS_SLEEPING);
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

    // Menu items

    @FXML
    private void handleClearMenuButtonAction(ActionEvent event) {
        try {
            MenuItem menuItem = (MenuItem) event.getSource();
            ContextMenu contextMenu = menuItem.getParentPopup();
            if (contextMenu == null) return;

            Window window = contextMenu.getOwnerWindow();
            if (window instanceof Stage) {
                Stage stage = (Stage) window;

                FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
                Scene scene = new Scene(loader.load());

                Properties props = new Properties();
                try (FileInputStream in = new FileInputStream(PREFS_FILE)) {
                    props.load(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String theme = props.getProperty(THEME_PREF_KEY, LIGHT_THEME);
                URL themeURL = getClass().getResource(theme);
                if (themeURL != null) {
                    scene.getStylesheets().add(themeURL.toExternalForm());
                }

                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHideMenuSwitchAction(ActionEvent event) {
        if (menu.isVisible()) {
            menu.setVisible(false);
            menu.setManaged(false);
            splitPane.setDividerPositions(0);
        } else {
            menu.setVisible(true);
            menu.setManaged(true);
            splitPane.setDividerPositions(0.4);
        }
    }

    @FXML
    private void handleThemeMenuSwitchAction(ActionEvent event) {
        URL darkStyle = getClass().getResource(DARK_THEME);
        URL lightStyle = getClass().getResource(LIGHT_THEME);

        if (darkStyle == null || lightStyle == null) {
            System.out.println("Could not find one or both of the stylesheets");
            return;
        }

        String darkStyleExternalForm = darkStyle.toExternalForm();
        String lightStyleExternalForm = lightStyle.toExternalForm();

        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(PREFS_FILE)) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String currentTheme = props.getProperty(THEME_PREF_KEY, LIGHT_THEME);

        if (currentTheme.equals(DARK_THEME)) {
            mainView.getStylesheets().remove(darkStyleExternalForm);
            mainView.getStylesheets().add(lightStyleExternalForm);
            props.setProperty(THEME_PREF_KEY, LIGHT_THEME);
        } else {
            mainView.getStylesheets().remove(lightStyleExternalForm);
            mainView.getStylesheets().add(darkStyleExternalForm);
            props.setProperty(THEME_PREF_KEY, DARK_THEME);
        }

        try (FileOutputStream out = new FileOutputStream(PREFS_FILE)) {
            props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExitMenuButtonAction(ActionEvent event) {
        Stage stage = (Stage) mainView.getScene().getWindow();
        stage.close();
    }

   @FXML
    private void handleAboutMenuButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Price Calculator");

        Hyperlink hyperlink = new Hyperlink();
        hyperlink.setText("https://github.com/EagleBlood/CalcTrain");
        hyperlink.setOnAction(e -> {
            Application application = new Application() {
                @Override
                public void start(Stage stage) throws Exception {
                    // This is a no-op. We only need the Application for HostServices.
                }
            };
            application.getHostServices().showDocument(hyperlink.getText());
        });

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("dialog-pane");
        dialogPane.setContent(hyperlink);

        alert.showAndWait();
    }

    @FXML
    private void handleMinimizeButtonAction(ActionEvent event) {
        Stage stage = (Stage) mainView.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleMaximizeButtonAction(ActionEvent event) {
        Stage stage = (Stage) mainView.getScene().getWindow();

        if (isMaximized) {
            // Restore to windowed mode
            buttonIconEnlarge.getStyleClass().setAll("icon-buttonMaximize");
            regionIconEnlarge.getStyleClass().setAll("iconMaximize");
            stage.setFullScreen(false);
            isMaximized = false;
        } else {
            // Maximize the window
            buttonIconEnlarge.getStyleClass().setAll("icon-buttonRestore");
            regionIconEnlarge.getStyleClass().setAll("iconRestore");
            stage.setFullScreen(true);
            isMaximized = true;
        }
    }
}
