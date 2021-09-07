# 8Puzzle Project by Simon Wang
8Puzzle Project by Simon Wang for CSDS 391 Introduction to Artificial Intelligence

**Overview**
The Program aims to use the A* Algorithm and Beam search Algorithm to solve an 8-Puzzle, which is a 3x3 grid filled with 8 blocks labeled from 1-8. The Algorithms are supposed to be able to solve the puzzle dynamically from any state to the goal state, which is when the blocks are ordered numerically.

**Data Structure**
The Puzzle will be represented by a 2D Array, in order to best visualize the puzzle. Another possibility is to represent the puzzle by a string, but that could get complicated easily by having to code for all 9 positions and knowing whether a move is valid or not, so it will only be used to have an easy way to print the state space.

**Methods**

setState <state> : Assigns the puzzle state to the format that <state> has specified.

printState : Prints the current state of the puzzle.

move <direction> : Moves the blank box in the specified direction, provided that there is not a border in the way.

randomizeState <n> : Randomizes the state by moving the blank box in a random direction <n> times using the move method.

**Functions**

solveAStar <heuristic> : Utilizes A* Search with a specific <heuristic> to find the quickest way to solve the puzzle.

solveBeam <k> : Utilizes Beam Search with a specified <k> to solve the puzzle.

maxNodes <n> : Specifies the number of max number of nodes allowed in a beam search.

