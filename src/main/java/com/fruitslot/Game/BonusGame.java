package com.fruitslot.Game;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

public class BonusGame {

    private static int diceSides = 6;
    private static int[] coinValues;
    private static int[] coinWeights;
    private static int totalWeight;

    /* ============================================================================
     * init
     * ----------------------------------------------------------------------------
     * Initialises the bonus game configuration from a JSON file.
     *
     * Coin values and their selection weights are stored for weighted random
     * selection during bonus gameplay.
     * ============================================================================
     */
    public static void init(JsonNode root) {

        JsonNode valuesNode  = root.get("coins");
        JsonNode weightsNode = root.get("weights");

        coinValues = new int[valuesNode.size()];
        coinWeights = new int[weightsNode.size()];

        for (int i = 0; i < valuesNode.size(); i++) {
            coinValues[i] = valuesNode.get(i).asInt();
            coinWeights[i] = weightsNode.get(i).asInt();
        }

        int sum = 0;
        for (int w : coinWeights) sum += w;
        totalWeight = sum;
    }

    /* ============================================================================
     * play
     * ----------------------------------------------------------------------------
     * Executes a full bonus game round.
     *
     * Process:
     * - Selects a weighted coin value
     * - Rolls a dice (1–6)
     * - Computes final payout: dice × coin × totalBet
     *
     * @return bonus payout amount
     * ============================================================================
     */
    public static long play(int totalBet, Random rng) {

        int coin = selectCoin(rng);
        int dice = rng.nextInt(diceSides) + 1;

        return (long) dice * coin * totalBet;
    }
    
    /* ============================================================================
     * playDetailed
     * ----------------------------------------------------------------------------
     * Executes a bonus round and returns detailed results for analytics.
     *
     * array containing:
     *  [0] total payout
     *  [1] selected coin value
     *  [2] dice roll result
     * ============================================================================
     */
    public static int[] playDetailed(int totalBet, Random rng) {
        int coin = selectCoin(rng);
        int dice = rng.nextInt(diceSides) + 1;
        return new int[]{ dice * coin * totalBet, coin, dice };
    }

    /* ============================================================================
     * selectCoin
     * ----------------------------------------------------------------------------
     * Performs weighted random selection of a coin value based on configured
     * probability weights.
     *
     * Uses cumulative distribution sampling.
     * ============================================================================
     */
    private static int selectCoin(Random rng) {

        int roll = rng.nextInt(totalWeight);
        int cumulative = 0;

        for (int i = 0; i < coinValues.length; i++) {
            cumulative += coinWeights[i];

            if (roll < cumulative) {
                return coinValues[i];
            }
        }

        return coinValues[coinValues.length - 1];
    }
}