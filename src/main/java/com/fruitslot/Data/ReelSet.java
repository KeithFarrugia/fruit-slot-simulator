package com.fruitslot.Data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class ReelSet {

    ArrayList<ReelStrip> reelset;

    /* ============================================================================
     * ReelSet
     * ----------------------------------------------------------------------------
     * Default constructor.
     * Initialises an empty list of reel strips.
     * Typically used when constructing manually or before loading configuration.
     * ============================================================================
     */
    ReelSet() {
        reelset = new ArrayList<ReelStrip>();
    }

    /* ============================================================================
     * ReelSet (manual constructor)
     * ----------------------------------------------------------------------------
     * Constructs a ReelSet from an existing list of ReelStrip objects.
     * Useful for testing or prebuilt configurations.
     * ============================================================================
     */
    ReelSet(ArrayList<ReelStrip> reelset) {
        this.reelset = reelset;
    }

    /* ============================================================================
     * ReelSet (JsonNode constructor)
     * ----------------------------------------------------------------------------
     * Constructs a ReelSet from a JSON configuration.
     *
     * Each reel is parsed into a list of Symbol values and wrapped in a ReelStrip.
     * ============================================================================
     */
    public ReelSet(JsonNode root) {
        reelset = new ArrayList<>(3);

        reelset.add(new ReelStrip(parseReel(root.get("reel1"))));
        reelset.add(new ReelStrip(parseReel(root.get("reel2"))));
        reelset.add(new ReelStrip(parseReel(root.get("reel3"))));
    }

    /* ============================================================================
     * parseReel
     * ----------------------------------------------------------------------------
     * Converts a JSON array of symbol strings into a list of Symbol enums.
     * ============================================================================
     */
    private ArrayList<Symbol> parseReel(JsonNode reelNode) {
        ArrayList<Symbol> symbols = new ArrayList<>();

        for (JsonNode s : reelNode) {
            symbols.add(Symbol.valueOf(s.asText()));
        }

        return symbols;
    }

    /* ============================================================================
     * getWindow
     * ----------------------------------------------------------------------------
     * Generates the visible 3x3 slot window based on reel positions.
     *
     * Each reel contributes a vertical column of 3 symbols.
     * ============================================================================
     */
    public Symbol[][] getWindow(int[] positions) {

        if (positions.length != 3) {
            throw new IllegalArgumentException(
                "Must provide exactly 3 reel positions"
            );
        }

        Symbol[][] grid = new Symbol[3][3];

        for (int reel_id = 0; reel_id < 3; reel_id++) {
            List<Symbol> window = reelset.get(reel_id).getSet(positions[reel_id]);

            grid[0][reel_id] = window.get(0);
            grid[1][reel_id] = window.get(1);
            grid[2][reel_id] = window.get(2);
        }

        return grid;
    }

    /* ============================================================================
     * printGrid
     * ----------------------------------------------------------------------------
     * Utility method for debugging.
     * Prints a 3x3 symbol grid in a readable format.
     * ============================================================================
     */
    public static void printGrid(Symbol[][] grid) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                System.out.print(grid[row][col] + "\t");
            }
            System.out.println();
        }
    }

    /* ============================================================================
     * getReel
     * ----------------------------------------------------------------------------
     * Returns the ReelStrip at the specified index.
     * ============================================================================
     */
    public ReelStrip getReel(int index) {
        return reelset.get(index);
    }

    /* ============================================================================
     * size
     * ----------------------------------------------------------------------------
     * Returns the number of reels in the set.
     * ============================================================================
     */
    public int size() {
        return reelset.size();
    }
}