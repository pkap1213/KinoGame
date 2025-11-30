import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * Manages theme switching and color schemes
 */
public class SceneManager {
    private static final Color BROWN_THEME_PRIMARY = Color.web("#44331F");
    private static final Color BROWN_THEME_SECONDARY = Color.web("#6f3e18");
    private static final Color BROWN_THEME_DARK = Color.web("#3e270f");
    private static final Color BROWN_THEME_DARKER = Color.web("#2b2113");
    private static final Color BROWN_THEME_BUTTON = Color.web("#1a1a1a");
    private static final Color BROWN_THEME_SELECTED = Color.web("#eab308");

    private static final Color BLUE_THEME_PRIMARY = Color.web("#006994");
    private static final Color BLUE_THEME_SECONDARY = Color.web("#0086BB");
    private static final Color BLUE_THEME_DARK = Color.web("#004D6D");
    private static final Color BLUE_THEME_DARKER = Color.web("#003B54");
    private static final Color BLUE_THEME_BUTTON = Color.web("#1a1a1a");
    private static final Color BLUE_THEME_SELECTED = Color.web("#00BFFF");

    private static boolean isBlueTheme = false;


    /*
     * Getters for the various color palettes of the UI
     */

    public static Color getPrimaryColor() {
        if (isBlueTheme) {
            return BLUE_THEME_PRIMARY;
        } else {
            return BROWN_THEME_PRIMARY;
        }
    }

    public static Color getSecondaryColor() {
        if (isBlueTheme) {
            return BLUE_THEME_SECONDARY;
        } else {
            return BROWN_THEME_SECONDARY;
        }
    }

    public static Color getDarkColor() {
        if (isBlueTheme) {
            return BLUE_THEME_DARK;
        } else {
            return BROWN_THEME_DARK;
        }
    }

    public static Color getDarkerColor() {
        if (isBlueTheme) {
            return BLUE_THEME_DARKER;
        } else {
            return BROWN_THEME_DARKER;
        }
    }

    public static Color getButtonColor() {
        if (isBlueTheme) {
            return BLUE_THEME_BUTTON;
        } else {
            return BROWN_THEME_BUTTON;
        }
    }

    public static Color getSelectedColor() {
        if (isBlueTheme) {
            return BLUE_THEME_SELECTED;
        } else {
            return BROWN_THEME_SELECTED;
        }
    }

    /**
     * Toggles between blue and brown themes
     */    
    public static void toggleTheme() {
        isBlueTheme = !isBlueTheme;
    }

    public static boolean isBlueTheme() {
        return isBlueTheme;
    }

    /**
     * Updates all game scene elements to current theme
     */
    public static void updateSceneTheme(GameScene gameScene) {

        gameScene.setBackground(new Background(new BackgroundFill(getPrimaryColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        gameScene.updateTopBarTheme();
        gameScene.updateTopBarButtonsTheme();

        gameScene.updateGridTheme();

        gameScene.updateButtonsTheme();

        gameScene.updateOptionsTheme();

        gameScene.updateBottomBarTheme();
    }

    /**
     * Updates all menu scene elements to current theme
     */
    public static void updateMenuTheme(MenuScene menuScene) {

        menuScene.setBackground(new Background(new BackgroundFill(getPrimaryColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        menuScene.updateContentTheme();
        menuScene.updateTopBarTheme();
        menuScene.updateButtonsTheme();
    }
}