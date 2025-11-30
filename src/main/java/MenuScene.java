import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.application.Platform;

/**
 * Builds and manages the menu interface
 */
public class MenuScene extends BorderPane {
    private Stage primaryStage;

    // Menu Constructor
    public MenuScene(Stage stage) {
        this.primaryStage = stage;
        setupUI();
        SceneManager.updateMenuTheme(this);
    }

    /**
     * Creates and configures the complete menu UI
     */
    private void setupUI() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(12));
        topBar.setSpacing(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setBackground(new Background(new BackgroundFill(Color.web("#6f3e18"), CornerRadii.EMPTY, Insets.EMPTY)));
        topBar.setPrefHeight(80);

        Label welcomeLabel = new Label("WELCOME TO KENO!");
        welcomeLabel.setFont(Font.font("Barlow Semi Condensed", FontWeight.BOLD, 48));
        welcomeLabel.setTextFill(Color.WHITE);
        topBar.getChildren().add(welcomeLabel);
        
        VBox mainContent = new VBox(30);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(40));

        HBox centerContent = new HBox(40);
        centerContent.setAlignment(Pos.CENTER);
        
        VBox howToPlayBox = new VBox(15);
        howToPlayBox.setAlignment(Pos.TOP_CENTER);
        howToPlayBox.setPadding(new Insets(20));
        howToPlayBox.setBackground(new Background(new BackgroundFill(Color.web("#2b2113"), new CornerRadii(8), Insets.EMPTY)));
        
        Label howToPlayTitle = new Label("HOW TO PLAY");
        howToPlayTitle.setFont(Font.font("Barlow Semi Condensed", FontWeight.BOLD, 32));
        howToPlayTitle.setTextFill(Color.WHITE);
        howToPlayTitle.setAlignment(Pos.CENTER);
        howToPlayTitle.setMaxWidth(Double.MAX_VALUE); 
        howToPlayTitle.setTextAlignment(TextAlignment.CENTER); 

        VBox instructionsBox = new VBox(10);
        instructionsBox.setAlignment(Pos.TOP_LEFT);
        
        String[] instructions = {
            "1. PICK THE NUMBER OF SPOTS YOU\n    WANT TO CHOOSE",
            "2. SELECT YOUR NUMBERS OR GO\n    RANDOM",
            "3. PICK THE NUMBER OF DRAWINGS",
            "4. THEN PRESS PLAY, PRESSING\n    CONTINUE AFTER EACH DRAWING.\n    THE MORE NUMBERS YOU MATCH,\n    THE MORE YOU WIN!"
        };
        
        for (String text : instructions) {
            Label instruction = new Label(text);
            instruction.setFont(Font.font("Barlow Semi Condensed", FontWeight.BOLD, 20));
            instruction.setTextFill(Color.WHITE);
            instructionsBox.getChildren().add(instruction);
        }
        
        howToPlayBox.getChildren().addAll(howToPlayTitle, instructionsBox);
        
        VBox oddsBox = new VBox(15);
        oddsBox.setAlignment(Pos.TOP_CENTER);
        oddsBox.setPadding(new Insets(20));
        oddsBox.setBackground(new Background(new BackgroundFill(Color.web("#2b2113"), new CornerRadii(8), Insets.EMPTY)));
        
        Label oddsTitle = new Label("ODDS");
        oddsTitle.setFont(Font.font("Barlow Semi Condensed", FontWeight.BOLD, 32));
        oddsTitle.setTextFill(Color.WHITE);
        oddsTitle.setAlignment(Pos.CENTER);
        oddsTitle.setMaxWidth(Double.MAX_VALUE);
        oddsTitle.setTextAlignment(TextAlignment.CENTER);

        VBox oddsList = new VBox(10);
        oddsList.setAlignment(Pos.TOP_LEFT);
        
        String[] odds = {
            "1 SPOT : 1 IN 4.00",
            "4 SPOTS : 1 IN 3.86",
            "8 SPOTS : 1 IN 9.97",
            "10 SPOTS : 1 IN 9.05"
        };
        
        for (String text : odds) {
            Label oddsLine = new Label(text);
            oddsLine.setFont(Font.font("Barlow Semi Condensed", FontWeight.BOLD, 20));
            oddsLine.setTextFill(Color.WHITE);
            oddsList.getChildren().add(oddsLine);
        }
        
        oddsBox.getChildren().addAll(oddsTitle, oddsList);
        
        centerContent.getChildren().addAll(howToPlayBox, oddsBox);
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button quitButton = new Button("QUIT");
        styleButton(quitButton);
        quitButton.setOnAction(e -> Platform.exit());
        
        Button playButton = new Button("PLAY!");
        styleButton(playButton);
        
        // Switches scenes on click to game scene
        playButton.setOnAction(e -> {
            GameScene gameScene = new GameScene(primaryStage);
            SceneManager.updateSceneTheme(gameScene);

            Button themeBtn = gameScene.getThemeButton();
            if (themeBtn != null) {
                if (SceneManager.isBlueTheme()) {
                    themeBtn.setText("OLD LOOK");
                } else {
                    themeBtn.setText("NEW LOOK");
                }
            }

            Scene scene = new Scene(gameScene, 900, 700);
            primaryStage.setScene(scene);
        });
                
        buttonBox.getChildren().addAll(quitButton, playButton);
        
        mainContent.getChildren().addAll(centerContent, buttonBox);
        
        setTop(topBar);
        setCenter(mainContent);
        setBackground(new Background(new BackgroundFill(Color.web("#44331F"), CornerRadii.EMPTY, Insets.EMPTY)));
    }
    

    /*
     * Updates the top bar to current theme
    */

    public void updateTopBarTheme() {
        HBox topBar = (HBox) getTop();
        if (topBar != null) {
            topBar.setBackground(new Background(new BackgroundFill(SceneManager.getSecondaryColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    /*
     * Updates the menu buttons to current theme
     */

    public void updateButtonsTheme() {
        VBox mainContent = (VBox) getCenter();
        if (mainContent != null && mainContent.getChildren().size() > 1) {
            HBox buttonBox = (HBox) mainContent.getChildren().get(1);
            for (javafx.scene.Node node : buttonBox.getChildren()) {
                if (node instanceof Button) {
                    Button btn = (Button) node;
                    btn.setBackground(new Background(new BackgroundFill(SceneManager.getButtonColor(), new CornerRadii(4), Insets.EMPTY)));
                    btn.setTextFill(Color.WHITE);
                }
            }
        }
    }

    /**
     * Applies consistent visual styling to menu buttons
     */

    private void styleButton(Button button) {
        button.setFont(Font.font("Barlow Semi Condensed", FontWeight.BOLD, 30));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(Color.web("#1a1a1a"), new CornerRadii(4), Insets.EMPTY)));
        button.setPadding(new Insets(15, 40, 15, 40));
        
        // hover effects
        button.setOnMouseEntered(e -> button.setBackground(new Background(
            new BackgroundFill(Color.web("#2d2d2d"), new CornerRadii(4), Insets.EMPTY))));
        button.setOnMouseExited(e -> button.setBackground(new Background(
            new BackgroundFill(Color.web("#1a1a1a"), new CornerRadii(4), Insets.EMPTY))));
    }


    /**
     * Updates menu content boxes for current theme
    */
    public void updateContentTheme() {
        HBox topBar = (HBox) getTop();
        if (topBar != null) {
            topBar.setBackground(new Background(new BackgroundFill(
                SceneManager.getSecondaryColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }

        VBox mainContent = (VBox) getCenter();
        if (mainContent != null && !mainContent.getChildren().isEmpty()) {
            HBox centerContent = (HBox) mainContent.getChildren().get(0);
            if (centerContent != null) {
                for (javafx.scene.Node node : centerContent.getChildren()) {
                    if (node instanceof VBox) {
                        VBox box = (VBox) node;
                        box.setBackground(new Background(new BackgroundFill(
                            SceneManager.getDarkerColor(), new CornerRadii(8), Insets.EMPTY)));
                    }
                }
            }
        }
    }
}