package com.fruitslot.Data;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

public class Paytable {

    private final Map<Symbol, Integer> payouts = new EnumMap<>(Symbol.class);

    /* ============================================================================
     * Paytable
     * ----------------------------------------------------------------------------
     * Default constructor for an empty paytable instance.
     * Typically used when the paytable will be populated later or not loaded
     * from JSON configuration.
     * ============================================================================
     */
    public Paytable() {}

    /* ============================================================================
     * Paytable (JsonNode constructor)
     * ----------------------------------------------------------------------------
     * Constructs the paytable from a JSON configuration node.
     *
     * Each key is mapped to a Symbol enum, and its value is stored as the payout.
     * ============================================================================
     */
    public Paytable(JsonNode root) {
        JsonNode payoutsNode = root.get("payouts");

        Iterator<String> keys = payoutsNode.fieldNames();

        while (keys.hasNext()) {
            String key = keys.next();
            int value = payoutsNode.get(key).asInt();

            Symbol symbol = Symbol.valueOf(key);
            payouts.put(symbol, value);
        }
    }

    /* ============================================================================
     * getPayout
     * ----------------------------------------------------------------------------
     * Returns the payout value associated with a given symbol.
     *
     * If the symbol does not exist in the paytable, returns 0.
     * ============================================================================
     */
    public int getPayout(Symbol symbol) {
        return payouts.getOrDefault(symbol, 0);
    }

    /* ============================================================================
     * isBonus
     * ----------------------------------------------------------------------------
     * Determines whether a symbol is a bonus-triggering symbol.
     *
     * Currently hardcoded to Symbol.B1.
     * ============================================================================
     */
    public boolean isBonus(Symbol symbol) {
        return symbol == Symbol.B1;
    }
}