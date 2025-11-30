
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;

/**
 * Main application class that launches the Keno game application
 */

public class KinoApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Kino Game");

        GameScene root = new GameScene(primaryStage);
        root.setBackground(new Background(new BackgroundFill(Color.web("#44331F"), CornerRadii.EMPTY, Insets.EMPTY)));

		MenuScene menuScene = new MenuScene(primaryStage);
    	Scene Menu = new Scene(menuScene, 900, 700);

		primaryStage.setScene(Menu);
        primaryStage.show();
		
				
		
	}

}
