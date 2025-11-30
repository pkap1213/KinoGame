import java.util.HashMap;
import java.util.Map;

/**
 * Manages payout calculations for different spot combinations
 */

public class PayoutTable {
    private static final Map<Integer, Map<Integer, Integer>> PAYOUTS = new HashMap<>();
    static {


        /*
         * https://nclottery.com/KenoHow
         */
        
        // 1 Spot
        Map<Integer, Integer> SpotOne = new HashMap<>();
        SpotOne.put(1, 2);
        PAYOUTS.put(1, SpotOne);

        // 4 Spot
        Map<Integer, Integer> SpotFour = new HashMap<>();
        SpotFour.put(2, 1);
        SpotFour.put(3, 5);
        SpotFour.put(4, 75);
        PAYOUTS.put(4, SpotFour);

        // 8 Spot
        Map<Integer, Integer> SpotEight = new HashMap<>();
        SpotEight.put(4, 2);
        SpotEight.put(5, 12);
        SpotEight.put(6, 50);
        SpotEight.put(7, 750);
        SpotEight.put(8, 10000);
        PAYOUTS.put(8, SpotEight);

        // 10 Spot
        Map<Integer, Integer> SpotTen = new HashMap<>();
        SpotTen.put(0, 5);
        SpotTen.put(5, 2);
        SpotTen.put(6, 15);
        SpotTen.put(7, 40);
        SpotTen.put(8, 450);
        SpotTen.put(9, 4250);
        SpotTen.put(10, 100000);
        PAYOUTS.put(10, SpotTen);
    }

    /*
     * Gets the payout based on the given number of spots and matches
     */
    public static int getPayout(int spots, int matches) {
        Map<Integer, Integer> payoutTable = PAYOUTS.get(spots);
        if (payoutTable == null) return 0;
        Integer payout = payoutTable.get(matches);
        if (payout == null) return 0;
        return payout;
    }
    
}
