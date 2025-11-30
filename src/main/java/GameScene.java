import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Circle;
import javafx.scene.control.ContentDisplay;
import javafx.stage.Stage;

import java.util.*;
/**
 * Builds and manages the main game interface layout
 */

public class GameScene extends BorderPane {
    private final Map<Integer, Button> kenoNumberButtons = new HashMap<>();
    private Stage primaryStage;
    // Exposed UI handles for KenoGame to wire behavior
    private Button randomBtnField;                    // GO RANDOM! button
    private Button playBtnField;                      // PLAY! button
    private Button menuBtn;
    private Button themeBtn;
    private final List<StackPane> spotOptionPanes = new ArrayList<>();     // spot option panes (1,4,8,10)
    private final List<StackPane> drawingOptionPanes = new ArrayList<>();  // drawing option panes (1..4)
    private Label alertLabel;                         // styled alert label (same style as drawingsLabel)
    private Label earnedLabel;

    // GameScene Constructor
    public GameScene(Stage stage) {
        this.primaryStage = stage;
        setTop(createTopBar());
        setCenter(createCenter());
        setBottom(createBottomBar());
        SceneManager.updateSceneTheme(this);
        GameController controller = new GameController();
        new KenoGame(this, controller);
        
        menuBtn.setOnAction(e -> {
            MenuScene menuScene = new MenuScene(primaryStage);
            SceneManager.updateMenuTheme(menuScene);
            Scene scene = new Scene(menuScene, 900, 700);
            primaryStage.setScene(scene);
        });
        
    }

    /**
     * Creates the top navigation bar with menu and theme controls
     */
    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(12));
        topBar.setSpacing(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setBackground(new Background(new BackgroundFill(Color.web("#6f3e18"), CornerRadii.EMPTY, Insets.EMPTY)));
        topBar.setPrefHeight(80);

        menuBtn = new Button("MENU");
        menuBtn.setFont(Font.font("System", FontWeight.BOLD, 28));
        menuBtn.setTextFill(Color.WHITE);
        menuBtn.setBackground(new Background(new BackgroundFill(Color.web("#3e270f"), new CornerRadii(4), Insets.EMPTY)));
        menuBtn.setPadding(new Insets(8, 14, 8, 14));

        Button themeBtn = new Button("NEW LOOK");
        themeBtn.setFont(Font.font("System", FontWeight.BOLD, 28));
        themeBtn.setTextFill(Color.WHITE);
        themeBtn.setBackground(new Background(new BackgroundFill(SceneManager.getDarkColor(), new CornerRadii(4), Insets.EMPTY)));
        themeBtn.setPadding(new Insets(8, 14, 8, 14));
        this.themeBtn = themeBtn;
        themeBtn.setOnAction(e -> {
            SceneManager.toggleTheme();
            if (SceneManager.isBlueTheme()) {
                themeBtn.setText("OLD LOOK");
            } else {
                themeBtn.setText("NEW LOOK");
            }
            SceneManager.updateSceneTheme(this);
        });
        HBox leftBox = new HBox(10,menuBtn,themeBtn);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("KENO");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Barlow Semi Condensed", FontWeight.BOLD, 48));
        HBox centerBox = new HBox(title);
        centerBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(centerBox, Priority.ALWAYS);

        earnedLabel = new Label("EARNED:  $0");
        earnedLabel.setTextFill(Color.WHITE);
        earnedLabel.setFont(Font.font("Barlow Semi Condensed", FontWeight.BOLD, 28));
        HBox rightBox = new HBox(earnedLabel);
        rightBox.setAlignment(Pos.CENTER_RIGHT);



        topBar.getChildren().addAll(leftBox, centerBox, rightBox);

        return topBar;
    }

    /*
     * Updates the top bar background to current theme
     */
    public void updateTopBarTheme() {
        HBox topBar = (HBox) getTop();
        if (topBar != null) {
            topBar.setBackground(new Background(new BackgroundFill(SceneManager.getSecondaryColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    /*
     * Updates the top bar buttons to current theme
     */
    public void updateTopBarButtonsTheme() {
        HBox topBar = (HBox) getTop();
        if (topBar != null && !topBar.getChildren().isEmpty()) {
            HBox leftBox = (HBox) topBar.getChildren().get(0);
            for (javafx.scene.Node node : leftBox.getChildren()) {
                if (node instanceof Button) {
                    Button btn = (Button) node;
                    btn.setBackground(new Background(new BackgroundFill(SceneManager.getDarkColor(), new CornerRadii(4), Insets.EMPTY)));
                    btn.setTextFill(Color.WHITE);
                }
            }
        }
    }

    /*
     * Updates the bottom bar background to current theme
     */
    public void updateBottomBarTheme() {
        HBox bottomBar = (HBox) getBottom();
        if (bottomBar != null) {
            bottomBar.setBackground(new Background(new BackgroundFill(SceneManager.getSecondaryColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    /**
     * Creates the main game grid and left control panel
     */    
    private Region createCenter() {
        BorderPane centerContent = new BorderPane();

        VBox leftMenu = new VBox();
        leftMenu.setAlignment(Pos.TOP_CENTER);
        leftMenu.setSpacing(20);
        leftMenu.setPadding(new Insets(20));
        leftMenu.setPrefWidth(240);

        VBox spotsPicker = new VBox(10);
        spotsPicker.setAlignment(Pos.CENTER);

        StackPane numberContainer = new StackPane();
        Circle numberCircle = new Circle(20, Color.web("#840606"));
        Label numberLabel = new Label("1");
        numberLabel.setTextFill(Color.WHITE);
        numberLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        numberContainer.getChildren().addAll(numberCircle, numberLabel);

        Label spotsLabel = new Label("NUMBER OF SPOTS:");
        spotsLabel.setTextFill(Color.WHITE);
        spotsLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        spotsLabel.setMinWidth(190);
        spotsLabel.setMaxWidth(170);
        spotsLabel.setAlignment(Pos.CENTER);
        spotsLabel.setWrapText(false);


        HBox spotsOptions = new HBox(10);
        spotsOptions.setAlignment(Pos.CENTER);

        for (int spot : new int[]{1, 4, 8, 10}) {
            StackPane spotContainer = new StackPane();
            spotContainer.setBackground(new Background(new BackgroundFill(Color.web("#3e270f"), new CornerRadii(4), Insets.EMPTY)));
            spotContainer.setPrefWidth(45);
            spotContainer.setPrefHeight(45);
            spotContainer.setPadding(new Insets(5));

            Label spotOption = new Label(String.valueOf(spot));
            spotOption.setTextFill(Color.WHITE);
            spotOption.setFont(Font.font("System", FontWeight.BOLD, 20));
            spotContainer.getChildren().add(spotOption);

            spotsOptions.getChildren().add(spotContainer);
            spotOptionPanes.add(spotContainer);
        }

        Button randomBtn = new Button("GO RANDOM!");
        this.randomBtnField = randomBtn;

        randomBtn.setTextFill(Color.WHITE);
        randomBtn.setFont(Font.font("System", FontWeight.BOLD, 20));
        randomBtn.setBackground(new Background(new BackgroundFill(Color.web("#1a1a1a"), new CornerRadii(4), Insets.EMPTY)));
        randomBtn.setPrefHeight(50);
        randomBtn.setMaxWidth(Double.MAX_VALUE);
        randomBtn.setAlignment(Pos.CENTER);
        randomBtn.setContentDisplay(ContentDisplay.CENTER);

        spotsPicker.getChildren().addAll(numberContainer, spotsLabel, spotsOptions, randomBtn);

        VBox drawingsPicker = new VBox(10);
        drawingsPicker.setAlignment(Pos.CENTER);

        StackPane numberContainer2 = new StackPane();
        Circle numberCircle2 = new Circle(20, Color.web("#840606"));
        Label numberLabel2 = new Label("2");
        numberLabel2.setTextFill(Color.WHITE);
        numberLabel2.setFont(Font.font("System", FontWeight.BOLD, 32));
        numberContainer2.getChildren().addAll(numberCircle2, numberLabel2);

        Label drawingsLabel = new Label("NUMBER OF DRAWINGS:");
        drawingsLabel.setTextFill(Color.WHITE);
        drawingsLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        drawingsLabel.setMinWidth(190);
        drawingsLabel.setMaxWidth(170);
        drawingsLabel.setAlignment(Pos.CENTER);
        drawingsLabel.setWrapText(false);
        HBox drawingsOptions = new HBox(10);
        drawingsOptions.setAlignment(Pos.CENTER);

        for (int drawing : new int[]{1, 2, 3, 4}) {
            StackPane drawingContainer = new StackPane();
            drawingContainer.setBackground(new Background(new BackgroundFill(Color.web("#3e270f"), new CornerRadii(4), Insets.EMPTY)));
            drawingContainer.setPrefWidth(45);
            drawingContainer.setPrefHeight(45);
            drawingContainer.setPadding(new Insets(5));

            Label drawingOption = new Label(String.valueOf(drawing));
            drawingOption.setTextFill(Color.WHITE);
            drawingOption.setFont(Font.font("System", FontWeight.BOLD, 20));
            drawingContainer.getChildren().add(drawingOption);

            drawingsOptions.getChildren().add(drawingContainer);
            drawingOptionPanes.add(drawingContainer);
        }

        drawingsPicker.getChildren().addAll(numberContainer2, drawingsLabel, drawingsOptions);

        Button playButton = new Button("PLAY!");
        this.playBtnField = playButton;

        playButton.setTextFill(Color.WHITE);
        playButton.setFont(Font.font("System", FontWeight.BOLD, 25));
        playButton.setBackground(new Background(new BackgroundFill(Color.web("#1a1a1a"), new CornerRadii(4), Insets.EMPTY)));
        playButton.setPrefHeight(50);
        playButton.setMaxWidth(Double.MAX_VALUE);
        playButton.setAlignment(Pos.CENTER);
        playButton.setContentDisplay(ContentDisplay.CENTER);


        GridPane numberGrid = new GridPane();
        numberGrid.setHgap(6);
        numberGrid.setVgap(6);
        numberGrid.setAlignment(Pos.CENTER);
        numberGrid.setPadding(new Insets(12));


        int cols = 8;
        int rows = 10;
        int number = 1;


        double btnSize = 42;
        double fontSize = 22;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button numBtn = new Button(String.valueOf(number));
                numBtn.setPrefWidth(btnSize);
                numBtn.setMinWidth(Region.USE_PREF_SIZE);
                numBtn.setMaxWidth(Region.USE_PREF_SIZE);

                numBtn.setPrefHeight(btnSize);
                numBtn.setMinHeight(Region.USE_PREF_SIZE);
                numBtn.setMaxHeight(Region.USE_PREF_SIZE);

                numBtn.setFont(Font.font("System", FontWeight.BOLD, fontSize));
                numBtn.setTextFill(Color.WHITE);
                numBtn.setBackground(new Background(new BackgroundFill(Color.web("#181818"), new CornerRadii(4), Insets.EMPTY)));
                numBtn.setPadding(Insets.EMPTY);

                numBtn.setUserData(number);
                kenoNumberButtons.put(number, numBtn);

                numBtn.setOnAction(e -> {
                    BackgroundFill fill = numBtn.getBackground().getFills().get(0);
                    Color current = (Color) fill.getFill();
                    if (current.equals(Color.web("#181818"))) {
                        numBtn.setBackground(new Background(new BackgroundFill(Color.web("#eab308"), new CornerRadii(4), Insets.EMPTY)));
                        numBtn.setTextFill(Color.BLACK);
                    } else {
                        numBtn.setBackground(new Background(new BackgroundFill(Color.web("#181818"), new CornerRadii(4), Insets.EMPTY)));
                        numBtn.setTextFill(Color.WHITE);
                    }
                });

                numberGrid.add(numBtn, col, row);
                number++;
            }
        }

        alertLabel = new Label("");
        alertLabel.setTextFill(Color.WHITE);
        alertLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        alertLabel.setWrapText(true);
        alertLabel.setVisible(false);
        alertLabel.setTextAlignment(TextAlignment.CENTER);
        centerContent.setCenter(numberGrid);

        Insets pad = numberGrid.getPadding();
        double hgap = numberGrid.getHgap();
        double vgap = numberGrid.getVgap();

        // some math to get grid dimensions
        double gridWidth = cols * btnSize + (cols - 1) * hgap + pad.getLeft() + pad.getRight();
        double gridHeight = rows * btnSize + (rows - 1) * vgap + pad.getTop() + pad.getBottom();

        Rectangle gridBg = new Rectangle(gridWidth*1.1, gridHeight*1.05);
        gridBg.setArcWidth(20);
        gridBg.setArcHeight(20);
        gridBg.setFill(Color.web("#2b2113"));


        StackPane centerWrapper = new StackPane(gridBg, numberGrid);
        centerWrapper.setPadding(new Insets(0));

        centerContent.setCenter(centerWrapper);
        leftMenu.getChildren().addAll(spotsPicker, drawingsPicker, playButton, alertLabel);
        centerContent.setLeft(leftMenu);

        return centerContent;
    }

    /*
     * Updates the game grid background to current theme
     */

    public void updateGridTheme() {
        BorderPane centerContent = (BorderPane) getCenter();
        if (centerContent != null) {
            StackPane centerWrapper = (StackPane) centerContent.getCenter();
            if (centerWrapper != null && !centerWrapper.getChildren().isEmpty()) {
                Rectangle gridBg = (Rectangle) centerWrapper.getChildren().get(0);
                gridBg.setFill(SceneManager.getDarkerColor());
            }
        }
    }

    /*
    * Updates the number buttons to current theme
    */
    public void updateButtonsTheme() {
        for (Button btn : kenoNumberButtons.values()) {
            if (!btn.getBackground().getFills().isEmpty()) {
                Color currentFill = (Color) btn.getBackground().getFills().get(0).getFill();
                if (currentFill.equals(Color.web("#181818"))) {
                    btn.setBackground(new Background(new BackgroundFill(SceneManager.getButtonColor(), new CornerRadii(4), Insets.EMPTY)));
                }
            }
        }
    }

    /*
     * Updates the spot and drawing option panes to current theme
     */
    public void updateOptionsTheme() {
        for (StackPane pane : spotOptionPanes) {
            pane.setBackground(new Background(new BackgroundFill(SceneManager.getDarkColor(), new CornerRadii(4), Insets.EMPTY)));
        }
        for (StackPane pane : drawingOptionPanes) {
            pane.setBackground(new Background(new BackgroundFill(SceneManager.getDarkColor(), new CornerRadii(4), Insets.EMPTY)));
        }
    }

    /**
     * Creates bottom status bar of the interface
     */
    private HBox createBottomBar() {
        HBox bottomBar = new HBox();
        bottomBar.setPrefHeight(48);
        bottomBar.setBackground(new Background(new BackgroundFill(Color.web("#6f3e18"), CornerRadii.EMPTY, Insets.EMPTY)));
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(8, 12, 8, 12));
        return bottomBar;
    }

    /*
     * Getters for UI elements to be used by KenoGame
    */

    public Map<Integer, Button> getKenoNumberButtons() { return kenoNumberButtons; }
    public Button getRandomButton() { return randomBtnField; }
    public Button getPlayButton() { return playBtnField; }
    public Button getThemeButton() {return themeBtn;}
    public List<StackPane> getSpotOptionPanes() { return Collections.unmodifiableList(spotOptionPanes); }
    public List<StackPane> getDrawingOptionPanes() { return Collections.unmodifiableList(drawingOptionPanes); }
    public Label getAlertLabel() { return alertLabel; }
    public Label getearnedLabel(){return earnedLabel;}
}