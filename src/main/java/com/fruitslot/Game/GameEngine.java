package com.fruitslot.Game;

import com.fruitslot.Data.*;

import java.util.Random;

public class GameEngine {

    private static final int TOTAL_BET = 10;

    private final Random rng;
    private final ReelSet reelSet;
    private final Paytable paytable;

    /* ============================================================================
     * GameEngine
     * ----------------------------------------------------------------------------
     * Core engine responsible for running slot game logic.
     * Handles spinning reels, evaluating paylines, resolving wins, and triggering
     * bonus games.
     * ============================================================================
     */
    public GameEngine(Random rng, ReelSet reelSet, Paytable paytable) {
        this.rng        = rng;
        this.reelSet    = reelSet;
        this.paytable   = paytable;
    }

    /* ============================================================================
     * playRound
     * ----------------------------------------------------------------------------
     * Executes a full game round:
     * 1. Spins reels
     * 2. Builds visible window
     * 3. Evaluates base game paylines
     * 4. Checks and triggers bonus game if conditions are met
     *
     * @return Total win (base + bonus)
     * ============================================================================
     */
    public long playRound() {
        int[]       positions   = spin();
        Symbol[][]  window      = reelSet.getWindow(positions);
        long        baseWin     = evaluatePaylines(window);
        long        bonusWin    = 0;

        if (countSymbol(window, Symbol.B1) >= 3) {
            bonusWin = BonusGame.play(TOTAL_BET, rng);
        }

        return baseWin + bonusWin;
    }

    /* ============================================================================
     * spin
     * ----------------------------------------------------------------------------
     * Generates random reel stop positions for all reels.
     *
     * @return Array of reel positions (one per reel)
     * ============================================================================
     */
    public int[] spin() {
        int[] positions = new int[3];

        for (int i = 0; i < 3; i++) {
            ReelStrip reel = reelSet.getReel(i);
            positions[i] = rng.nextInt(reel.size());
        }

        return positions;
    }

    /* ============================================================================
     * evaluatePaylines
     * ----------------------------------------------------------------------------
     * Evaluates all paylines against the current slot window.
     *
     * Each valid line contributes its payout based on the paytable.
     *
     * @return Total base game winnings
     * ============================================================================
     */
    public long evaluatePaylines(Symbol[][] window) {

        long total = 0;

        for (int[][] line : Paylines.PAYLINES) {

            Symbol s0 = window[line[0][0]][line[0][1]];
            Symbol s1 = window[line[1][0]][line[1][1]];
            Symbol s2 = window[line[2][0]][line[2][1]];

            Symbol win = resolveWin(s0, s1, s2);

            if (win != null) {
                total += paytable.getPayout(win);
            }
        }

        return total;
    }

    /* ============================================================================
     * resolveWin
     * ----------------------------------------------------------------------------
     * Determines whether a 3-symbol line is a winning combination.
     *
     * Rules:
     * - All B1 symbols → bonus win
     * - Any partial B1 presence → invalid line
     * - W1 acts as a wild symbol
     * - Must match or be resolved via wild substitution
     *
     * @return Winning Symbol or null if no win
     * ============================================================================
     */
    public Symbol resolveWin(Symbol a, Symbol b, Symbol c) {

        Symbol[] line = {a, b, c};

        // --- 1. Bonus check (must be all B1) ---
        if (a == Symbol.B1 && b == Symbol.B1 && c == Symbol.B1) {
            return Symbol.B1;
        }

        // If any B1 exists but not all -> no win
        for (Symbol s : line) {
            if (s == Symbol.B1) {
                return null;
            }
        }

        // --- 2. Resolve normal / wild logic ---
        Symbol base = null;

        for (Symbol s : line) {

            if (s == Symbol.W1) continue;

            if (base == null) {
                base = s;
            } else if (base != s) {
                return null;
            }
        }

        // --- 3. All wilds case ---
        if (base == null) {
            return Symbol.W1;
        }

        // --- 4. Validate payout exists ---
        return paytable.getPayout(base) > 0 ? base : null;
    }

    /* ============================================================================
     * countSymbol
     * ----------------------------------------------------------------------------
     * Counts occurrences of a specific symbol in a grid.
     *
     * Used for bonus triggering logic (e.g. B1 scatter detection).
     * ============================================================================
     */
    public  int countSymbol(Symbol[][] grid, Symbol target) {
        int count = 0;

        for (Symbol[] row : grid) {
            for (Symbol s : row) {
                if (s == target) count++;
            }
        }

        return count;
    }

    /* ============================================================================
     * getTotalBet
     * ----------------------------------------------------------------------------
     * Returns the fixed total bet per round.
     * ============================================================================
     */
    public static int getTotalBet() {
        return TOTAL_BET;
    }
}