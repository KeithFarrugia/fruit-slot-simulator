# Fruit Slot Game Simulator

This project is a simulator for a fruit slot machine game. It runs a large number of spins and compares simulated results against theoretical RTP and probability values.

## Requirements
* Java 21 

## Project Structure

```bash
src/            # Java source code
setup.json      # Game configuration (reels, paytable, etc.)
pom.xml         # Maven build file
target/         # Compiled output (generated)
```



## How to Build
From the project root:

```bash
mvn clean package
```

This will compile the project and generate a JAR file in:

```bash
target/
```

## How to Run

After building, run the simulator with:
```bash
mvn exec:java -Dexec.mainClass=com.fruitslot.Simulator -Dexec.args="setup.json"
```


## Example Output


```bash
Simulating 10,000,000 rounds...

  Progress:  10% - Current RTP: 97.62%
  Progress:  20% - Current RTP: 96.86%
  Progress:  30% - Current RTP: 96.42%
  Progress:  40% - Current RTP: 96.25%
  Progress:  50% - Current RTP: 96.17%
  Progress:  60% - Current RTP: 95.98%
  Progress:  70% - Current RTP: 96.09%
  Progress:  80% - Current RTP: 96.12%
  Progress:  90% - Current RTP: 96.13%
  Progress: 100% - Current RTP: 96.14%

==========================================================================
 Simulated Rounds     :         10,000,000 | Bet/Round: 10
 Total Realized Win   :         96,143,236 | Elapsed:   6.15s
--------------------------------------------------------------------------
 Core Metric  | Simulated Value  | Expected Value  
--------------------------------------------------------------------------
 RTP Total    |         96.14% |         96.15%
 RTP Base     |         76.36% |         76.27%
 RTP Bonus    |         19.78% |         19.88%
 Bonus Freq   | 1 in    118.86 | 1 in    118.75
 Avg Bonus    |         235.11 |         236.08
 Avg Coin     |           6.71 |           6.75
 Avg Dice     |           3.50 |           3.50
--------------------------------------------------------------------------
 Symbol Line  | Sim Hit Prob     | Exp Hit Prob    
--------------------------------------------------------------------------
 W1(2000)     |       0.000660% |       0.000650%
 H1(800)      |       0.011264% |       0.011046%
 H2(400)      |       0.096908% |       0.096816%
 H3(80)       |       0.648572% |       0.649123%
 L1(60)       |       0.207600% |       0.207277%
 L2(20)       |       1.204862% |       1.207277%
 L3(16)       |       0.685828% |       0.685510%
 L4(12)       |       0.351672% |       0.350227%
==========================================================================
```