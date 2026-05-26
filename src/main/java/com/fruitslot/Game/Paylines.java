package com.fruitslot.Game;

public class Paylines {

    /* ============================================================================
     * PAYLINES
     * ----------------------------------------------------------------------------
     * Defines all winning paylines for the slot machine.
     *
     * Each payline is represented as a sequence of 3 coordinates:
     * {row, column} pairs mapping onto the 3x3 slot grid.
     * ============================================================================
     */
    public static final int[][][] PAYLINES = {
        { {0,0}, {0,1}, {0,2} },  // Payline 1 - top row
        { {1,0}, {1,1}, {1,2} },  // Payline 2 - middle row
        { {2,0}, {2,1}, {2,2} },  // Payline 3 - bottom row
        { {0,0}, {1,1}, {2,2} },  // Payline 4 - \ diagonal 
        { {0,2}, {1,1}, {2,0} },  // Payline 5 - / diagonal 
    };

    /* ============================================================================
     * count
     * ----------------------------------------------------------------------------
     * Returns the total number of active paylines defined in the system.
     * ============================================================================
     */
    public static int count() {
        return PAYLINES.length;
    }
}