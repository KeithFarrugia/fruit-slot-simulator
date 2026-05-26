package com.fruitslot.Data;

public enum Symbol {
    W1("Wild"   , 0),
    H1("Seven"  , 1),
    H2("Bell"   , 2),
    H3("Bar"    , 3),
    L1("Banana" , 4),
    L2("Orange" , 5),
    L3("Plum"   , 6),
    L4("Cherry" , 7),
    B1("Bonus"  , 8);

    private final String    name;
    private final int       val;

    /* ============================================================================
     * Symbol
     * ----------------------------------------------------------------------------
     * Represents all possible symbols in the slot machine.
     *
     * Each symbol has:
     * - A display name (for UI / readability)
     * - A numeric identifier (for indexing or debugging purposes)
     *
     * Symbols are grouped into:
     * - High value symbols (H1–H3)
     * - Low value symbols (L1–L4)
     * - Special symbols (Wild, Bonus)
     * ============================================================================
     */
    Symbol(String name, int val) {
        this.name   = name;
        this.val    = val;
    }

    /* ============================================================================
     * getName
     * ----------------------------------------------------------------------------
     * Returns the human-readable name of the symbol.
     * ============================================================================
     */
    public String getName() {
        return name;
    }

    /* ============================================================================
     * getVal
     * ----------------------------------------------------------------------------
     * Returns the internal numeric identifier of the symbol.
     *
     * Useful for debugging, logging, or compact representations.
     * ============================================================================
     */
    public int getVal() {
        return val;
    }
}