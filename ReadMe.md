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