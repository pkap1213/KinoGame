
import java.util.*;

/**
 * Controls the game logic and state management for the Keno game
 */
public class GameController {
    private int spotsToPlay = 0;       
    private int drawingsToPlay = 0;   
    private boolean drawingsStarted = false;

    private final Set<Integer> selectedNumbers = new LinkedHashSet<>(); 
    private final Random rng = new Random();

    private static final Map<Integer, Double> ODDS_MAP = new HashMap<>();

    /*
    * https://nclottery.com/KenoHow
    */
    
    static {
        ODDS_MAP.put(1, 4.00);
        ODDS_MAP.put(4, 3.86);
        ODDS_MAP.put(8, 9.97);
        ODDS_MAP.put(10, 9.05);
    }

    private ArrayList<Integer> currentDrawing;
    private int currentDrawingIndex;
    private ArrayList<ArrayList<Integer>> allDrawings;

    // Constructor
     public GameController() {
        currentDrawing = new ArrayList<>();
        allDrawings = new ArrayList<>();
    }

    /**
     * Sets the number of spots player can select (1,4,8,10)
     * Returns true if valid spots number and game not started
     */
    public boolean setMaxSpots(int spots) {
        if (drawingsStarted){
            return false;
        }

        if (spots == 1 || spots == 4 || spots == 8 || spots == 10) {
            spotsToPlay = spots;
            selectedNumbers.clear();
            return true;
        }
        return false;
    }
    
    public int getMaxSpots() {
        return spotsToPlay; 
    }

    /**
     * Sets the number of drawings to play (1-4)
     * Returns true if valid drawings number and game not started
     */
    public boolean setNumberOfDrawings(int d) {
        if (drawingsStarted) return false;
        if (d >= 1 && d <= 4) {
            drawingsToPlay = d;
            return true;
        }
        return false;
    }

    public int getNumberOfDrawings() {
        return drawingsToPlay; 
    }


    /**
     * Selects/deselects a number on the grid
     * Returns true if selection was successful
     */
    public boolean selectNumber(int n) {
        if (drawingsStarted){
            return false;
        }
        if (spotsToPlay == 0){
            return false;
        }
        if (n < 1 || n > 80){
            return false;
        }

        if (selectedNumbers.contains(n)) {
            selectedNumbers.remove(n);
            return true;
        } else {
            if (selectedNumbers.size() >= spotsToPlay){
                return false;
            }
            selectedNumbers.add(n);
            return true;
        }
    }

    public Set<Integer> getSelected() {
        return Collections.unmodifiableSet(selectedNumbers);
    }

    /**
     * Automatically picks random numbers based on spots setting
     */

    public void autoPick() {
        if (drawingsStarted || spotsToPlay == 0){
            return;
        }
        selectedNumbers.clear();
        ArrayList<Integer> pool = new ArrayList<>();
        for (int i = 1; i <= 80; i++){
            pool.add(i);
        }
        Collections.shuffle(pool, rng);
        for (int i = 0; i < spotsToPlay; i++){
            selectedNumbers.add(pool.get(i));
        }
    }

    // Resets the grid
    public void resetGrid() {
        selectedNumbers.clear();
    }

    /**
     * Begins drawings numbers for the round if valid spots and drawings are set
    */
    public void startDrawing() {
        if (spotsToPlay == 0 || drawingsToPlay == 0) {
            System.out.println("Pick spots and drawings first");
            return;
        }
        drawingsStarted = true;
        System.out.println("Drawings started");
    }

    public void endDrawingSession() {
        drawingsStarted = false;
    }

    public boolean isDrawingActive() {
        return drawingsStarted; 
    }

    /**
     * Generates all drawings for the current session
     */
    public void prepareDrawings() {
        allDrawings.clear();
        for(int i = 0; i < drawingsToPlay; i++) {
            ArrayList<Integer> drawing = new ArrayList<>();
            ArrayList<Integer> pool = new ArrayList<>();
            for(int n = 1; n <= 80; n++) {
                pool.add(n);
            }
            Collections.shuffle(pool, rng);
            for(int j = 0; j < 20; j++) {
                drawing.add(pool.get(j));
            }
            allDrawings.add(drawing);
        }
        currentDrawingIndex = 0;
        if(!allDrawings.isEmpty()) {
            currentDrawing = allDrawings.get(0);
        } else {
            currentDrawing = new ArrayList<>();
        }
    }

    public ArrayList<Integer> getCurrentDraw() {
        return currentDrawing;
    }

    /**
     * Advances to next drawing if available
     * Returns true if there was another drawing
     */
    public boolean nextDrawing() {
        currentDrawingIndex++;
        if(currentDrawingIndex < allDrawings.size()) {
            currentDrawing = allDrawings.get(currentDrawingIndex);
            return true;
        }
        return false;
    }

    /**
     * Returns set of numbers that match between selection and current draw
     */
    public Set<Integer> getMatches() {
        Set<Integer> matched = new HashSet<>(selectedNumbers);
        matched.retainAll(new HashSet<>(currentDrawing));
        return matched;
    }

    /*
     * Returns the odds based on spots selected
     */
    public double getOddsForSpots() {
        Double odds = ODDS_MAP.get(spotsToPlay);
        if (odds == null) {
            return 0.0;
        }
        return odds;
    }

    public int getCurrentDrawingNumber() {
        return currentDrawingIndex + 1;
    }

    public int getTotalDrawings() {
        return allDrawings.size();
    }
    /**
     * Calculates payout based on spots played and matches, uses PayoutTable class for values
     */
    public int getPayout(int matches) {
        return PayoutTable.getPayout(spotsToPlay, matches);
    }
}
