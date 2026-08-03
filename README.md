# Chromatic Adventurer: Intelligent Graph Coloring

A classification-driven system for approaching the NP-complete graph coloring problem, developed as a university group project.

---

## Technical Highlights

- Approaches the NP-complete chromatic number problem through heuristic-driven approximation
- Classification-driven algorithm selection from 10 specialized approaches (trees, bipartite, planar graphs, and more)
- Parallel backtracking with MRV & LCV heuristics, forward checking, DSATUR upper bounds, and clique lower bounds
- Reduced graph search space by 50% through pruning and bounds
- Achieved 90% optimal solutions within a 2-minute time limit across benchmark graphs
- Thread-pool execution with BitSet data structures for O(1) color availability checks

## Tech Stack

Java, JavaFX, multi-threading

---

## About This Project

This project was originally developed as part of a university group assignment and has been migrated from my university GitLab account. Due to this migration, commit history from fellow contributors was lost.

Built with a team of 5 as part of a university course at Maastricht University. Credit is shared equally with all group members.

---

## Project Structure

This is the 3rd and final phase of the project. Phases 1 and 2 are included in the repository but require extraction from outside the source root to run.
