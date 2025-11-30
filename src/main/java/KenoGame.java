import java.util.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * Manages game UI interaction and animations
 */
public class KenoGame {
    private final GameController controller;
    private final GameScene gameScene;
    private Button continueButton;
    private Label selectedNumbersLabel;
    private Label drawnNumbersLabel;
    private Label matchedNumbersLabel;
    private Label oddsLabel;
    private static int totalEarnings = 0;
    private int lastRoundEarnings = 0;
    private Label roundEarningsLabel;

    // Constructor
    public KenoGame(GameScene scene, GameController controller) {
        this.gameScene = scene;
        this.controller = controller;
        setupRightPanel();
        attachUIHandlers();
        refreshGridFromController();
    }

    /**
     * Sets up the right info panel with labels and buttons
     */
    private void setupRightPanel() {
        VBox rightInfoPanel = new VBox(10);
        rightInfoPanel.setAlignment(Pos.TOP_CENTER);
        rightInfoPanel.setPadding(new Insets(20));
        rightInfoPanel.setPrefWidth(200);

        selectedNumbersLabel = createInfoLabel("Your Numbers:");
        drawnNumbersLabel = createInfoLabel("Drawn Numbers:");
        matchedNumbersLabel = createInfoLabel("Matches:");
        oddsLabel = createInfoLabel("Odds:");

        roundEarningsLabel = new Label("Round Earnings: $0");
        roundEarningsLabel.setTextFill(Color.web("#22c55e"));
        roundEarningsLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        rightInfoPanel.getChildren().add(roundEarningsLabel);

        Label earnedLabel = gameScene.getearnedLabel();
        if (earnedLabel != null) {
            earnedLabel.setText("EARNED:  $" + totalEarnings);
        }
        
        continueButton = new Button("CONTINUE");
        continueButton.setVisible(false);
        continueButton.setTextFill(Color.WHITE);
        continueButton.setFont(Font.font("System", FontWeight.BOLD, 20));
        continueButton.setBackground(new Background(new BackgroundFill(Color.web("#1a1a1a"), new CornerRadii(4), Insets.EMPTY)));
        continueButton.setPrefHeight(50);
        continueButton.setMaxWidth(Double.MAX_VALUE);
        continueButton.setAlignment(Pos.CENTER);

        rightInfoPanel.getChildren().addAll(
            selectedNumbersLabel, drawnNumbersLabel, 
            matchedNumbersLabel, oddsLabel, continueButton
        );

        gameScene.setRight(rightInfoPanel);
    }

    
    // Creates the info label
    private Label createInfoLabel(String prefix) {
        Label label = new Label(prefix);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        label.setWrapText(true);
        return label;
    }

    // Updates the earnings display
    private void updateEarningsDisplay() {
        roundEarningsLabel.setText("Round Earnings: $" + lastRoundEarnings);
        Label earnedLabel = gameScene.getearnedLabel();
        if (earnedLabel != null) {
            earnedLabel.setText("EARNED:  $" + totalEarnings);
        }
    }

    /**
     * Runs the animation sequence for current drawing
     */
    private void runDrawingAnimation() {
        updateOddsDisplay();
        updateSelectedNumbersDisplay();
        
        SequentialTransition drawingSequence = new SequentialTransition();
        ArrayList<Integer> drawing = controller.getCurrentDraw();
        
        for(int i = 1; i <= 80; i++) {
            int num = i;
            PauseTransition pause = new PauseTransition(Duration.millis(50));
            pause.setOnFinished(e -> {
                clearHighlights();
                Button btn = gameScene.getKenoNumberButtons().get(num);
                if(btn != null) {
                    btn.setBorder(new Border(new BorderStroke(
                        Color.RED, BorderStrokeStyle.SOLID, 
                        new CornerRadii(4), new BorderWidths(2)
                    )));
                }
            });
            drawingSequence.getChildren().add(pause);
        }

        // Handles matches between selected and drawn
        drawingSequence.setOnFinished(e -> {
            clearHighlights();
            Set<Integer> matches = controller.getMatches();
            for(Integer num : drawing) {
                Button btn = gameScene.getKenoNumberButtons().get(num);
                if(btn != null) {
                    btn.setBackground(new Background(new BackgroundFill(
                        Color.RED, new CornerRadii(4), Insets.EMPTY)));
                    btn.setTextFill(Color.WHITE);
                }
                if(matches.contains(num)) {
                    Button matchedBtn = gameScene.getKenoNumberButtons().get(num);
                    if(matchedBtn != null) {
                        matchedBtn.setBackground(new Background(new BackgroundFill(
                            Color.GREEN, new CornerRadii(4), Insets.EMPTY)));
                        matchedBtn.setTextFill(Color.WHITE);
                    }
                }
            }

            updateDrawnNumbersDisplay();
            updateMatchedNumbersDisplay();
            int matchesTotal = controller.getMatches().size();
            lastRoundEarnings = controller.getPayout(matchesTotal);
            totalEarnings += lastRoundEarnings;
            updateEarningsDisplay();

            if(controller.getCurrentDrawingNumber() < controller.getTotalDrawings()) {
                continueButton.setVisible(true);
            } else {
                controller.endDrawingSession();
                lockLeftControls(false);
                showAlert("All drawings complete!", false);
            }
        });

        drawingSequence.play();
    }

    /**
     * Clears all highlights from number buttons
     */
    private void clearHighlights() {
        for(Button btn : gameScene.getKenoNumberButtons().values()) {
            btn.setBorder(null);
            int btnValue = Integer.parseInt(btn.getText());
            if(!controller.getSelected().contains(btnValue)) {
                btn.setBackground(new Background(new BackgroundFill(Color.web("#181818"), new CornerRadii(4), Insets.EMPTY)));
                btn.setTextFill(Color.WHITE);
            } else {
                btn.setBackground(new Background(new BackgroundFill(Color.web("#eab308"), new CornerRadii(4), Insets.EMPTY)));
                btn.setTextFill(Color.BLACK);
            }
        }
    }

    /*
     * Various functions to update display labels based on end results
     */

    private void updateOddsDisplay() {
        double odds = controller.getOddsForSpots();
        oddsLabel.setText(String.format("Odds: 1 in %.2f", odds));
    }

    private void updateSelectedNumbersDisplay() {
        selectedNumbersLabel.setText("Your Numbers: " + controller.getSelected().toString());
    }

    private void updateDrawnNumbersDisplay() {
        drawnNumbersLabel.setText("Drawn Numbers: " + controller.getCurrentDraw().toString());
    }

    private void updateMatchedNumbersDisplay() {
        Set<Integer> matches = controller.getMatches();
        matchedNumbersLabel.setText("Matches: " + matches.toString() + " (" + matches.size() + " numbers)");
    }

    /**
     * Attaches event handlers for a large majority of UI functions, like spot, drawings,
     * play, continue, random, etc.
     */

    private void attachUIHandlers() {
        ArrayList<StackPane> spotButtons = new ArrayList<>(gameScene.getSpotOptionPanes());
        int[] spotValues = new int[]{1,4,8,10};
        for (int index = 0; index < spotButtons.size(); index++) {
            int spotValue = spotValues[index];
            StackPane spotPane = spotButtons.get(index);
            spotPane.setOnMouseClicked(event -> {
                boolean ok = controller.setMaxSpots(spotValue);
                if (!ok) {
                    return;
                }
                for (StackPane buttonPane : spotButtons) {
                    buttonPane.setBackground(new Background(new BackgroundFill(SceneManager.getDarkColor(), new CornerRadii(4), Insets.EMPTY)));
                }
                spotPane.setBackground(new Background(new BackgroundFill(Color.web("#eab308"), new CornerRadii(4), Insets.EMPTY)));
                refreshGridFromController();
                setGridEnablement(true);
                hideAlert();
            });
        }

        if (continueButton != null) {
            continueButton.setOnAction(event -> {
                continueButton.setVisible(false);
                boolean hasMore = controller.nextDrawing();
                if(hasMore) {
                    runDrawingAnimation();
                } else {
                    controller.endDrawingSession();
                    lockLeftControls(false);
                    showAlert("All drawings complete!", false);
                }
            });
        }

        ArrayList<StackPane> drawingButtons = new ArrayList<>(gameScene.getDrawingOptionPanes());
        for (int index = 0; index < drawingButtons.size(); index++) {
            final int drawingValue = index + 1;
            StackPane drawingPane = drawingButtons.get(index);
            drawingPane.setOnMouseClicked(event -> {
                boolean ok = controller.setNumberOfDrawings(drawingValue);
                if (!ok) {
                    return;
                }
                for (StackPane buttonPane : drawingButtons) {
                    buttonPane.setBackground(new Background(new BackgroundFill(SceneManager.getDarkColor(), new CornerRadii(4), Insets.EMPTY)));
                }
                drawingPane.setBackground(new Background(new BackgroundFill(Color.web("#eab308"), new CornerRadii(4), Insets.EMPTY)));
                hideAlert();
            });
        }

        Button randomButton = gameScene.getRandomButton();
        if (randomButton != null) {
            randomButton.setOnAction(event -> {
                if (controller.getMaxSpots() == 0) {
                    showAlert("Pick number of spots first", true);
                    return;
                }
                controller.autoPick();
                refreshGridFromController();
                showAlert("Auto-filled " + controller.getMaxSpots() + " spots", false);
            });
        }

        Button playButton = gameScene.getPlayButton();
        if (playButton != null) {
            playButton.setOnAction(event -> {
                if (controller.getMaxSpots() == 0) {
                    showAlert("Choose spot(s) before playing!", true);
                    return;
                }
                if (controller.getSelected().size() != controller.getMaxSpots()) {
                    showAlert("Fill your bet card (or GO RANDOM!)", true);
                    return;
                }
                if (controller.getNumberOfDrawings() == 0) {
                    showAlert("Choose number of drawings!", true);
                    return;
                }
                showAlert("", false);
                controller.startDrawing();
                controller.prepareDrawings();
                lockLeftControls(true);
                runDrawingAnimation();
            });
        }

        Map<Integer, Button> numberButtons = gameScene.getKenoNumberButtons();
        for (Map.Entry<Integer, Button> entry : numberButtons.entrySet()) {
            int number = entry.getKey();
            Button numberButton = entry.getValue();
            numberButton.setOnAction(event -> {
                boolean ok = controller.selectNumber(number);
                if (!ok) {
                    showAlert("Cannot select more than " + controller.getMaxSpots() + " spots!", true);
                    return;
                }
                refreshGridFromController();
                hideAlert();
            });
            numberButton.setDisable(controller.getMaxSpots() == 0);
        }
    }

    /**
     * Enables/disables the number grid
     */
    private void setGridEnablement(boolean enabled) {
        for (Button button : gameScene.getKenoNumberButtons().values()) {
            boolean shouldDisable = !enabled;
            button.setDisable(shouldDisable);
        }
    }

    /**
     * Locks left control panel functionality during drawings
     */

    private void lockLeftControls(boolean lock) {
        ArrayList<StackPane> spotPanes = new ArrayList<>(gameScene.getSpotOptionPanes());
        ArrayList<StackPane> drawingPanes = new ArrayList<>(gameScene.getDrawingOptionPanes());
        
        for (StackPane spotPane : spotPanes) {
            spotPane.setDisable(lock);
        }
        for (StackPane drawingPane : drawingPanes) {
            drawingPane.setDisable(lock);
        }
        
        Button randomButton = gameScene.getRandomButton();
        if (randomButton != null) {
            randomButton.setDisable(lock);
        }
        
        Button playButton = gameScene.getPlayButton();
        if (playButton != null) {
            playButton.setDisable(lock);
        }

        if (lock) {
            setGridEnablement(false);
        } else {
            boolean hasSpots = controller.getMaxSpots() != 0;
            setGridEnablement(hasSpots);
        }
    }

    /**
     * Updates grid UI from controller state
     */
    private void refreshGridFromController() {
        Set<Integer> selectedNumbers = controller.getSelected();
        for (Map.Entry<Integer, Button> entry : gameScene.getKenoNumberButtons().entrySet()) {
            Button numberButton = entry.getValue();
            if (selectedNumbers.contains(entry.getKey())) {
                numberButton.setBackground(new Background(new BackgroundFill(Color.web("#eab308"), new CornerRadii(4), Insets.EMPTY)));
                numberButton.setTextFill(Color.BLACK);
                numberButton.setBorder(null);
            } else {
                numberButton.setBackground(new Background(new BackgroundFill(Color.web("#181818"), new CornerRadii(4), Insets.EMPTY)));
                numberButton.setTextFill(Color.WHITE);
                numberButton.setBorder(null);
            }
            boolean shouldDisable = controller.getMaxSpots() == 0 || controller.isDrawingActive();
            numberButton.setDisable(shouldDisable);
        }
    }

    /*
     * Displays an alert message on the game scene from message parameter, can be error or nonerror
     */
    public void showAlert(String message, boolean isError) {
        Label alertLabel = gameScene.getAlertLabel();
        if (alertLabel == null) {
            return;
        }
        alertLabel.setText(message);
        alertLabel.setVisible(true);
        Color textColor;
        if (isError) {
            textColor = Color.web("#ff6666");
        } else {
            textColor = Color.web("#ffffff");
        }
        alertLabel.setTextFill(textColor);
    }

    /*
     * Hides the alert message that is displayeed from before
     */
    public void hideAlert() {
        Label alertLabel = gameScene.getAlertLabel();
        if (alertLabel == null) {
            return;
        }
        alertLabel.setVisible(false);
    }
}
