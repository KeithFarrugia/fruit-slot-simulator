package com.fruitslot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitslot.Data.Paytable;
import com.fruitslot.Data.ReelSet;
import com.fruitslot.Data.Symbol;
import com.fruitslot.Game.BonusGame;
import com.fruitslot.Game.GameEngine;
import com.fruitslot.Game.Paylines;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class Simulator {

    private static final int ROUNDS         = 10_000_000;
    private static final int TOTAL_BET      = GameEngine.getTotalBet();
    private static final long TOTAL_COMBOS  = 153_900L;

    // Theoretical values from assignment sheets
    private static final double EXP_RTP_TOTAL  = 0.9615;
    private static final double EXP_RTP_BASE   = 0.7627;
    private static final double EXP_RTP_BONUS  = 0.1988;
    private static final double EXP_BONUS_FREQ = 118.75;
    private static final double EXP_AVG_BONUS  = 236.08;
    private static final double EXP_AVG_COIN   = 6.7452;
    private static final double EXP_AVG_DICE   = 3.5;

    private static final Map<Symbol, Long> EXP_HITS = 
            new EnumMap<>(Symbol.class);

    static {

        /* ============================================================================
         * EXP_HITS
         * ----------------------------------------------------------------------------
         * Stores theoretical hit counts for each symbol.
         *
         * Used for validating simulated symbol probabilities against expected model
         * distributions derived from assignment specifications.
         * ============================================================================
         */

        EXP_HITS.put(Symbol.W1, 1L);
        EXP_HITS.put(Symbol.H1, 17L);
        EXP_HITS.put(Symbol.H2, 149L);
        EXP_HITS.put(Symbol.H3, 999L);
        EXP_HITS.put(Symbol.L1, 319L);
        EXP_HITS.put(Symbol.L2, 1858L);
        EXP_HITS.put(Symbol.L3, 1055L);
        EXP_HITS.put(Symbol.L4, 539L);
    }

    /* ============================================================================
     * main
     * ----------------------------------------------------------------------------
     * Entry point for the simulation.
     *
     * Responsibilities:
     * - Load configuration (reels, paytable, bonus settings)
     * - Run Monte Carlo simulation over ROUNDS
     * - Collect statistical metrics (RTP, bonus frequency, symbol hits)
     * - Output performance comparison against theoretical expectations
     * ============================================================================
     */
    public static void main(String[] args) throws Exception {

        String path = args.length > 0 ? args[0] : "setup.json";
        JsonNode root = new ObjectMapper().readTree(new File(path));

        ReelSet reelSet     = new ReelSet(root);
        Paytable paytable   = new Paytable(root);
        BonusGame.init(root);

        Random rng          = new Random(42);
        GameEngine engine   = new GameEngine(rng, reelSet, paytable);

        long totalWin       = 0, baseWin        = 0, bonusWin = 0;
        long bonusTriggers  = 0, winningRounds  = 0;
        long totalCoin      = 0, totalDice      = 0;

        Map<Symbol, Long> symbolHits = new EnumMap<>(Symbol.class);
        for (Symbol s : Symbol.values()) symbolHits.put(s, 0L);

        System.out.printf("Simulating %,d rounds...%n%n", ROUNDS);
        long t0 = System.currentTimeMillis();

        for (int round = 0; round < ROUNDS; round++) {

            int[] positions = engine.spin();
            Symbol[][] window = reelSet.getWindow(positions);

            long bw = 0;

            for (int[][] line : Paylines.PAYLINES) {

                Symbol win = engine.resolveWin(
                    window[line[0][0]][line[0][1]],
                    window[line[1][0]][line[1][1]],
                    window[line[2][0]][line[2][1]]
                );

                if (win != null) {
                    int payout = paytable.getPayout(win);
                    if (payout > 0) {
                        bw += payout;
                        symbolHits.merge(win, 1L, Long::sum);
                    }
                }
            }

            baseWin += bw;

            long bonW = 0;

            if (engine.countSymbol(window, Symbol.B1) >= 3) {
                bonusTriggers++;
                int[] res = BonusGame.playDetailed(TOTAL_BET, rng);
                bonW = res[0];
                totalCoin += res[1];
                totalDice += res[2];
                bonusWin  += bonW;
            }

            long roundWin = bw + bonW;
            totalWin += roundWin;

            if (roundWin > 0) winningRounds++;

            if ((round + 1) % (ROUNDS / 10) == 0) {
                printProgress(round + 1, totalWin);
            }
        }

        long elapsed = System.currentTimeMillis() - t0;

        printResultsTable(
            totalWin, baseWin, bonusWin, bonusTriggers,
            winningRounds, totalCoin, totalDice,
            symbolHits, paytable, elapsed
        );
    }

    /* ============================================================================
     * printProgress
     * ----------------------------------------------------------------------------
     * Displays live simulation progress and current RTP estimate.
     * ============================================================================
     */
    private static void printProgress(int round, long totalWin) {
        long bet = (long) round * TOTAL_BET;
        double rtp = totalWin * 100.0 / bet;

        System.out.printf(
            "  Progress: %3d%% - Current RTP: %.2f%%%n",
            round * 100 / ROUNDS, rtp
        );
    }

    /* ============================================================================
     * printResultsTable
     * ----------------------------------------------------------------------------
     * Outputs final simulation statistics and compares against theoretical values.
     *
     * Includes:
     * - RTP breakdown (total, base, bonus)
     * - Bonus statistics
     * - Symbol hit probabilities
     * ============================================================================
     */
    private static void printResultsTable(
        long totalWin, long baseWin,
        long bonusWin, long bonusTriggers, long winningRounds,
        long totalCoin, long totalDice,
        Map<Symbol, Long> symbolHits,
        Paytable paytable,
        long elapsed
    ) {

        long totalBet   = (long) ROUNDS * TOTAL_BET;
        double rtp      = (double) totalWin / totalBet;
        double rtpBase  = (double) baseWin / totalBet;
        double rtpBon   = (double) bonusWin / totalBet;

        double avgCoin = bonusTriggers > 0 ? (double)totalCoin / bonusTriggers : 0;
        double avgDice = bonusTriggers > 0 ? (double)totalDice / bonusTriggers : 0;
        double avgBon  = bonusTriggers > 0 ? (double)bonusWin  / bonusTriggers : 0;
        double bonFreq = (double) ROUNDS / Math.max(bonusTriggers, 1);

        System.out.println("\n" + "═".repeat(74));

        System.out.printf(" %-20s : %,18d | Bet/Round: %d%n",
                "Simulated Rounds", ROUNDS, TOTAL_BET);

        System.out.printf(" %-20s : %,18d | Elapsed:   %.2fs%n",
                "Total Realized Win", totalWin, elapsed / 1000.0);

        System.out.println("─".repeat(74));

        System.out.printf(" %-12s | %-16s | %-16s%n",
                "Core Metric", "Simulated Value", "Expected Value");

        System.out.println("─".repeat(74));

        printRow("RTP Total" , rtp     * 100, EXP_RTP_TOTAL * 100, "%13.2f%%");
        printRow("RTP Base"  , rtpBase * 100, EXP_RTP_BASE  * 100, "%13.2f%%");
        printRow("RTP Bonus" , rtpBon  * 100, EXP_RTP_BONUS * 100, "%13.2f%%");
        printRow("Bonus Freq", bonFreq      , EXP_BONUS_FREQ     , "1 in %9.2f");
        printRow("Avg Bonus" , avgBon       , EXP_AVG_BONUS      , "%14.2f");
        printRow("Avg Coin"  , avgCoin      , EXP_AVG_COIN       , "%14.2f");
        printRow("Avg Dice"  , avgDice      , EXP_AVG_DICE       , "%14.2f");

        System.out.println("-".repeat(74));

        System.out.printf(" %-12s | %-16s | %-16s%n",
                "Symbol Line", "Sim Hit Prob", "Exp Hit Prob");

        System.out.println("─".repeat(74));

        long paylineChecks = (long) ROUNDS * Paylines.count();

        for (Symbol s : Symbol.values()) {

            if (s == Symbol.B1) continue;

            long hits = symbolHits.getOrDefault(s, 0L);
            long expHits = EXP_HITS.getOrDefault(s, 0L);

            double simP = (double) hits / paylineChecks;
            double expP = (double) expHits / TOTAL_COMBOS;

            String name = String.format("%s(%d)", s.name(), paytable.getPayout(s));

            System.out.printf(" %-12s | %14.6f%% | %14.6f%%%n",
                    name, simP * 100, expP * 100);
        }

        System.out.println("=".repeat(74));
    }

    /* ============================================================================
     * printRow
     * ----------------------------------------------------------------------------
     * Utility method for formatted table output.
     * ============================================================================
     */
    private static void printRow(
        String label, double sim, double exp, String format
    ) {
        System.out.printf(" %-12s | " + format + " | " + format + "%n",
                label, sim, exp);
    }
}