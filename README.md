# Chromatic Adventurer: Intelligent Graph Coloring

A classification-driven system for solving the NP-complete chromatic number problem, developed as a university group project.

---

## Highlights

- Solves graph coloring exactly, not heuristically: recognized graph structures get an instant optimal answer, everything else goes through an exact backtracking search
- Classification-driven algorithm selection from 10 specialized approaches (trees, bipartite, planar graphs, and more) based on structural graph properties
- Exact backtracking solver that proves optimality by iteratively tightening color-count bounds — MRV & LCV heuristics, forward checking, DSATUR upper bounds, and clique lower bounds narrow the search, with failure at k colors formally proving k+1 is optimal
- Parallelized backtracking search: each thread explores an independent branch of the search tree, tracking per-vertex color availability with BitSets for O(1) constraint checks
- Interactive JavaFX visualizer for generating or loading graphs and watching them get colored

## How It Works

1. **Classification** — the graph is tested against a set of structural checks (connectivity, cycles, degree sequence, bipartiteness, planarity, and more), ordered from cheapest to most expensive to compute.
2. **Instant exact solve** — if the graph matches a recognized structure (tree, star, cycle, complete, wheel, forest, bipartite, planar), a dedicated closed-form algorithm colors it directly and optimally, no search required.
3. **Exact fallback** — if nothing matches, the general solver kicks in: a greedy pass and DSATUR give an upper bound, a clique search gives a lower bound, and a parallel backtracking search (MRV vertex ordering, LCV color ordering, forward checking) tightens the gap between them until it proves the optimal color count, or a 2-minute time limit is reached.

Note: the chordal and split graph detectors currently exist as algorithms but the automatic structural-classification checks for them are unimplemented placeholders, so those two cases are only reachable through the general fallback solver, not the fast path.

## Setup and Launch

**Prerequisites:** JDK 22, Maven (or use the included `mvnw` wrapper)

```bash
git clone https://github.com/kabelah/ChromaticAdventurer.git
cd ChromaticAdventurer
mvn clean javafx:run
```

This launches the JavaFX GUI directly — there's no separate build/run step, the Maven JavaFX plugin handles both.

## Using the App

1. From the main menu, click **Start New Graph**.
2. Either generate a random graph by specifying a number of vertices and edges, or load one from a file.
3. The app classifies the graph, runs the appropriate coloring algorithm, and renders the result — each vertex colored so no two adjacent vertices share a color.

## Project Structure

```
src/main/java/org/example/phase3/
├── Evaluation/          # Graph classification (GraphTester, GraphScorer, GraphType)
├── SpecialGraphCases/   # The 10 specialized exact algorithms (Tree, Bipartite, Cycle, ...)
├── DefaultGraphCases/   # DSATUR heuristic, used to seed the fallback solver's upper bound
├── phase1algo/          # The general exact solver: bounds, parallel backtracking, MRV/LCV
├── Generation/          # Random graph generation
├── GUI/                 # JavaFX screens, controls, and the graph painter/visualizer
└── Run/                 # Application entry point
```

`Phase 1 OUTSIDE SOURCE ROOT/` and `Phase2 OUTSIDE SOURCE ROOT/` are earlier phases of the same university assignment, kept for reference. They're separate standalone Maven projects, not part of the buildable app above.

## Tech Stack

Java 22, JavaFX, Maven, multi-threading

---

## About This Project

This project was originally developed as part of a university group assignment and has been migrated from my university GitLab account. Due to this migration, commit history from fellow contributors was lost.

Built with a team of 5 as part of a university course at Maastricht University. Credit is shared equally with all group members.
