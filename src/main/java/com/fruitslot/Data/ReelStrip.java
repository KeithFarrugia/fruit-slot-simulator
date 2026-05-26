package com.fruitslot.Data;

import java.util.List;

public class ReelStrip {
    java.util.ArrayList<Symbol> reel;

    /* ============================================================================
     * ReelStrip
     * ----------------------------------------------------------------------------
     * Default constructor.
     * Initialises an empty reel strip.
     * Typically used before loading symbol data.
     * ============================================================================
     */
    ReelStrip(){
        this.reel = new java.util.ArrayList<>();;
    }

    /* ============================================================================
     * ReelStrip (parameterised constructor)
     * ----------------------------------------------------------------------------
     * Constructs a reel strip from a predefined list of symbols.
     *
     * @param reel Ordered list of symbols representing the reel strip
     * ============================================================================
     */
    ReelStrip(java.util.ArrayList<Symbol> reel){
        this.reel = reel;
    }
    
    /* ============================================================================
     * wrap
     * ----------------------------------------------------------------------------
     * Wraps an index around the reel size to support circular indexing.
     *
     * Ensures negative or overflow positions map correctly within bounds.
     * ============================================================================
     */
    private int wrap(int i) {
        int size = reel.size();
        return (i % size + size) % size;
    }

    /* ============================================================================
     * getSet
     * ----------------------------------------------------------------------------
     * Returns a vertical window of 3 consecutive symbols from the reel.
     *
     * Used to construct the visible slot machine grid.
     * ============================================================================
     */
    public List<Symbol> getSet(int pos) {
        return List.of(
            reel.get(wrap(pos    )),
            reel.get(wrap(pos + 1)),
            reel.get(wrap(pos + 2))
        );
    }

    /* ============================================================================
     * size
     * ----------------------------------------------------------------------------
     * Returns the total number of symbols in the reel strip.
     * ============================================================================
     */
    public int size() {
        return reel.size();
    }
}