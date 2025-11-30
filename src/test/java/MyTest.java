import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


/**
 * Unit tests for GameController and PayoutTable functionalities
 */

class MyTest {
    private GameController controller;

    @BeforeEach
    void setup() {
        controller = new GameController();
    }

    // Spot Selection Tests
    @Test
    @DisplayName("Test valid spot selection (1 spot)")
    void testValidSpotSelection1() {
        assertTrue(controller.setMaxSpots(1));
        assertEquals(1, controller.getMaxSpots());
    }

    @Test
    @DisplayName("Test valid spot selection (4 spots)")
    void testValidSpotSelection4() {
        assertTrue(controller.setMaxSpots(4));
        assertEquals(4, controller.getMaxSpots());
    }

    @Test
    @DisplayName("Test valid spot selection (8 spots)")
    void testValidSpotSelection8() {
        assertTrue(controller.setMaxSpots(8));
        assertEquals(8, controller.getMaxSpots());
    }

    @Test
    @DisplayName("Test valid spot selection (10 spots)")
    void testValidSpotSelection10() {
        assertTrue(controller.setMaxSpots(10));
        assertEquals(10, controller.getMaxSpots());
    }

    @Test
    @DisplayName("Test invalid spot selection")
    void testInvalidSpotSelection() {
        assertFalse(controller.setMaxSpots(5));
        assertEquals(0, controller.getMaxSpots());
    }

    // Drawing Selection Tests
    @Test
    @DisplayName("Test valid drawing selection (1)")
    void testValidDrawingSelection1() {
        assertTrue(controller.setNumberOfDrawings(1));
        assertEquals(1, controller.getNumberOfDrawings());
    }

    @Test
    @DisplayName("Test valid drawing selection (4)")
    void testValidDrawingSelection4() {
        assertTrue(controller.setNumberOfDrawings(4));
        assertEquals(4, controller.getNumberOfDrawings());
    }

    @Test
    @DisplayName("Test invalid drawing selection (0)")
    void testInvalidDrawingSelection0() {
        assertFalse(controller.setNumberOfDrawings(0));
        assertEquals(0, controller.getNumberOfDrawings());
    }

    @Test
    @DisplayName("Test invalid drawing selection (5)")
    void testInvalidDrawingSelection5() {
        assertFalse(controller.setNumberOfDrawings(5));
        assertEquals(0, controller.getNumberOfDrawings());
    }

    // Number Selection Tests
    @Test
    @DisplayName("Test selecting number without setting spots")
    void testSelectNumberWithoutSpots() {
        assertFalse(controller.selectNumber(1));
    }

    @Test
    @DisplayName("Test selecting invalid number (too low)")
    void testSelectInvalidNumberLow() {
        controller.setMaxSpots(4);
        assertFalse(controller.selectNumber(0));
    }

    @Test
    @DisplayName("Test selecting invalid number (too high)")
    void testSelectInvalidNumberHigh() {
        controller.setMaxSpots(4);
        assertFalse(controller.selectNumber(81));
    }

    @Test
    @DisplayName("Test selecting valid number")
    void testSelectValidNumber() {
        controller.setMaxSpots(4);
        assertTrue(controller.selectNumber(1));
        assertTrue(controller.getSelected().contains(1));
    }

    @Test
    @DisplayName("Test deselecting number")
    void testDeselectNumber() {
        controller.setMaxSpots(4);
        controller.selectNumber(1);
        assertTrue(controller.selectNumber(1));
        assertFalse(controller.getSelected().contains(1));
    }

    @Test
    @DisplayName("Test selecting more than max spots")
    void testSelectMoreThanMaxSpots() {
        controller.setMaxSpots(1);
        assertTrue(controller.selectNumber(1));
        assertFalse(controller.selectNumber(2));
    }

    // AutoPick Tests
    @Test
    @DisplayName("Test AutoPick with 1 spot")
    void testAutoPick1() {
        controller.setMaxSpots(1);
        controller.autoPick();
        assertEquals(1, controller.getSelected().size());
    }

    @Test
    @DisplayName("Test AutoPick with 4 spots")
    void testAutoPick4() {
        controller.setMaxSpots(4);
        controller.autoPick();
        assertEquals(4, controller.getSelected().size());
    }

    @Test
    @DisplayName("Test AutoPick with no spots set")
    void testAutoPickNoSpots() {
        controller.autoPick();
        assertEquals(0, controller.getSelected().size());
    }

    // Drawing Generation Tests
    @Test
    @DisplayName("Test drawing generation")
    void testDrawingGeneration() {
        controller.setNumberOfDrawings(2);
        controller.prepareDrawings();
        assertEquals(20, controller.getCurrentDraw().size());
    }

    @Test
    @DisplayName("Test next drawing")
    void testNextDrawing() {
        controller.setNumberOfDrawings(2);
        controller.prepareDrawings();
        assertTrue(controller.nextDrawing());
        assertFalse(controller.nextDrawing());
    }

    // Payout Tests
    @Test
    @DisplayName("Test 1 spot match payout")
    void testPayout1Spot() {
        assertEquals(2, PayoutTable.getPayout(1, 1));
        assertEquals(0, PayoutTable.getPayout(1, 0));
    }

    @Test
    @DisplayName("Test 4 spot match payout")
    void testPayout4Spot() {
        assertEquals(75, PayoutTable.getPayout(4, 4));
        assertEquals(5, PayoutTable.getPayout(4, 3));
        assertEquals(1, PayoutTable.getPayout(4, 2));
        assertEquals(0, PayoutTable.getPayout(4, 1));
    }

    @Test
    @DisplayName("Test 8 spot match payout")
    void testPayout8Spot() {
        assertEquals(10000, PayoutTable.getPayout(8, 8));
        assertEquals(750, PayoutTable.getPayout(8, 7));
        assertEquals(50, PayoutTable.getPayout(8, 6));
        assertEquals(12, PayoutTable.getPayout(8, 5));
        assertEquals(2, PayoutTable.getPayout(8, 4));
    }

    @Test
    @DisplayName("Test 10 spot match payout")
    void testPayout10Spot() {
        assertEquals(100000, PayoutTable.getPayout(10, 10));
        assertEquals(4250, PayoutTable.getPayout(10, 9));
        assertEquals(450, PayoutTable.getPayout(10, 8));
        assertEquals(40, PayoutTable.getPayout(10, 7));
        assertEquals(15, PayoutTable.getPayout(10, 6));
        assertEquals(2, PayoutTable.getPayout(10, 5));
        assertEquals(5, PayoutTable.getPayout(10, 0));
    }

    @Test
    @DisplayName("Test invalid payout combinations")
    void testInvalidPayouts() {
        assertEquals(0, PayoutTable.getPayout(2, 1));
        assertEquals(0, PayoutTable.getPayout(4, 5));
        assertEquals(0, PayoutTable.getPayout(8, 3));
    }
}