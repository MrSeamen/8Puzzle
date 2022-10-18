import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class AlgorithmSolver {

    public AlgorithmSolver(int maxNodes, int boardSize) {
        maxNodes(maxNodes);
        makeGoalState(boardSize);
        makeGoalStringState(boardSize);
    }

    //max nodes
    private int maxNodes;
    //node count
    private int nodeID;

    private String goalStringState;

    private int[][] goalState;

    private final LogClass logger = new LogClass(); //logger

    public int getMaxNodes() {
        return maxNodes;
    }

    private int calculateHueristic(Board board, int type) {
        int returnValue = -1;
        switch(type) {
            case 1:
                //calculating h1 heuristic (misplaced tiles)
                int misplacedTiles = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (board.getBoardState()[i][j] != goalState[i][j]) {
                            misplacedTiles++;
                        }
                    }
                }
                returnValue = misplacedTiles;
                break;
            case 2:
                //calculating h2 heuristic (manhattan distances)
                int distance = 0;
                for (int i = 0; i < board.getSide(); i++) {
                    for (int j = 0; j < board.getSide(); j++) {
                        distance += Math.abs(board.getBoardState()[i][j] - goalState[i][j]);
                    }
                }
                returnValue = distance;
                break;
        }
        return returnValue;
    }

    private void makeGoalState(int side) {
        int totalBoxes = side*side;
        int counter = 0;
        goalState = new int [side][side];
        for (int row = 0; row < side; row++) {
            for (int col = 0; col < side; col++) {
                if (counter < totalBoxes) {
                    goalState[row][col] = counter++;
                }
            }
        }
    }

    private void makeGoalStringState(int side) {
        int totalBoxes = side*side;
        String returnString = "";
        for (int i = 0; i < totalBoxes; i++) {
            returnString += i;
        }
        goalStringState = returnString;
    }

    //node subclass - contains board state, last move, path cost, heuristic cost amd type, parent state, and ID
    public class Node {
        private final Board state;
        private final Board.Direction move; // move that got to this position
        private final Board parentState;
        private final int pathCost;
        private final int heuristicCost;
        private final int evalFunction;
        private final int nodeID;

        public Node(String currentState, Board.Direction move, int pathCost, int heuristicCost, int nodeID) {
            state = new Board(currentState.length());
            state.setState(currentState);
            this.move = move;
            parentState = new Board(currentState.length());
            parentState.setState(currentState);
            switch(move) {
                case NORTH:
                    parentState.move(Board.Direction.SOUTH);
                    break;
                case SOUTH:
                    parentState.move(Board.Direction.NORTH);
                    break;
                case EAST:
                    parentState.move(Board.Direction.WEST);
                    break;
                case WEST:
                    parentState.move(Board.Direction.EAST);
                    break;
            }
            this.pathCost = pathCost;
            this.heuristicCost = heuristicCost;
            evalFunction = pathCost + heuristicCost;
            this.nodeID = nodeID;
        }

        public Board getBoard() {
            return state;
        }

        public String getCurrentState() {
            return state.getStringState();
        }

        public int getPathCost() {
            return pathCost;
        }

        public int getEvalFunction() {
            return evalFunction;
        }

        public Board.Direction getMove() {
            return move;
        }

        public int getHeuristicCost() { return heuristicCost; }

        public boolean equals(Node node) {
            return this.getBoard().getStringState().equals(node.getBoard().getStringState()) //same state
                    && this.getMove() == node.getMove() //implies same parent
                    && this.getHeuristicCost() == node.getHeuristicCost(); //same heuristic used
        }

        @Override
        public String toString() {
            return "State: " + state.getStringState() + System.lineSeparator() +
                    "Previous Node: " + parentState.getStringState() + System.lineSeparator() +
                    "Move: " + move + System.lineSeparator() +
                    "Path Cost: " + pathCost + System.lineSeparator() +
                    "Heuristic Value: " + heuristicCost + System.lineSeparator() +
                    "Evaluation Function: " + evalFunction + System.lineSeparator() +
                    "Node ID: " + nodeID;
        }
    }

    public class nodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node node1, Node node2) {
            if (node1.getEvalFunction() > node2.getEvalFunction()) {
                return 1;
            } else if (node1.getEvalFunction() < node2.getEvalFunction()){
                return -1;
            }
            return 0;
        }
    }

    // solveAStar <heuristic>
    public Node solveAStar(Board startingBoard, int heuristic) {
        //A* Search(problem)
        try {
            if (Objects.nonNull(heuristic) && Objects.nonNull(startingBoard) && Objects.nonNull(maxNodes)) {
                System.out.println("Solving with A* with heuristic " + heuristic);
                //path cost from parent
                nodeID = 0;
                int cost = 0;
                //Open <- problem.start // Accessible at current state
                PriorityQueue<Node> Open = new PriorityQueue<>(new nodeComparator());
                //Closed  - Opened but not used or accessible at current state
                ArrayList<Node> Closed = new ArrayList<>();
                //Reached - Part of path
                ArrayList<Node> Reached = new ArrayList<>();
                //CurrentNode <- problem.start
                Node currentNode;
                //add currentNode to Open
                Open.add(new Node(startingBoard.getStringState(), Board.Direction.INITIAL, cost, calculateHueristic(startingBoard, heuristic), ++nodeID));
                //While Open is not empty
                while (!(Reached.size() > maxNodes)) {
                    if (!Open.isEmpty()) {
                        //removes all the elements that were not picked
                        currentNode = Open.poll();
                        Closed.addAll(Open);
                        Open.clear();
                        //If CurrentNode = goal
                        if (currentNode.getCurrentState().equals(goalStringState)) {
                            System.out.println("Number of steps: " + Reached.size());
                            System.out.println("Current Node: " + System.lineSeparator() + currentNode);
                            //Return CurrentNode
                            return currentNode;
                        }
                        //else
                        else {
                            //direction list
                            ArrayList<Board.Direction> directionList = new ArrayList<>();
                            for (Board.Direction d : currentNode.getBoard().directions) {
                                //then remove last direction from direction list, add to removed list
                                if (currentNode.getBoard().checkDirections(d)) {
                                    directionList.add(d);
                                }
                            }
                            for (Board.Direction d : directionList) {
                                //creating a theoretical board to manipulate
                                Board child = new Board((int) Math.pow(currentNode.getBoard().getSide(), 2));
                                child.setState(currentNode.getBoard().getStringState());
                                child.move(d);
                                int parentCost = currentNode.getPathCost();
                                //Comparator in Open automatically compares f(n)
                                Open.add(new Node( child.getStringState(), d, ++parentCost, calculateHueristic(child, heuristic), ++nodeID));
                            }

                            //For each in Open
                            ArrayList<Node> markedNodes = new ArrayList<>();
                            for (Node n1 : Open) {
                                boolean isInClosed = false;
                                //If child is in closed
                                for (Node n2 : Closed) {
                                    if (n1.equals(n2)) {
                                        isInClosed = true;
                                    }
                                }
                                if (isInClosed) {
                                    markedNodes.add(n1);
                                }
                            }
                            Open.removeAll(markedNodes);
                            //Closed <- currentNode
                            Reached.add(currentNode);
                        }
                    } else {
                        logger.log(LogClass.Methods.SOLVEASTAR, "No available paths to solutions");
                    }
                }
                System.out.println("Number of steps: " + Reached.size());
                System.out.println("Current State: " + System.lineSeparator() + Open.peek());
                return Open.poll();
            } else {
                //error
                logger.log(LogClass.Methods.SOLVEASTAR, "Null Input Given");
            }
        } catch (Exception e) {
            logger.log(LogClass.Methods.SOLVEASTAR, "Unexpected Exception " + e + " at " + LogClass.Methods.SOLVEASTAR);
        }
        //error state returned as it is unexpected that the function will reach here
        logger.log(LogClass.Methods.SOLVEASTAR, "Unexpected Behavior");
        Node error = new Node("000000000", Board.Direction.NULL, -1, 0, -1);
        System.out.println(error);
        return error;
    }

    boolean checkGoal(PriorityQueue<Node> nodeList) {
        for (Node n : nodeList) {
            if (n.getBoard().getStringState().equals(goalStringState)) {
                return true;
            }
        }
        return false;
    }

    // solveBeam <k>
    public String solveBeam(Board startingBoard, int k, int heuristic) {
        //direction list
        ArrayList<Board.Direction> directionList = new ArrayList<>();
        //state array of k size
        PriorityQueue<Node> beam = new PriorityQueue<>(k, new nodeComparator()); //REMEMBER TO CHECK FOR MAX OF K
        //arraylist of reached
        ArrayList<Node> reached = new ArrayList<>();
        //Closed  - Opened but not used or accessible at current state
        ArrayList<Node> closed = new ArrayList<>();
        //cost
        int cost = 0, nodeId = 1;
        //successor states list
        PriorityQueue<Node> successors = new PriorityQueue<>(k, new nodeComparator());
        //initial
        Node initial = new Node(startingBoard.getStringState(), Board.Direction.INITIAL, cost, calculateHueristic(startingBoard, heuristic), nodeId);
        boolean nodeBreak = false;
        beam.add(initial);
        try {
            //while goal not found
            while (!checkGoal(successors) || !nodeBreak) {
                if (beam.size() + reached.size() + successors.size() <= maxNodes) {
                    //for state in state array
                    for (Node n : beam) {
                        for (Board.Direction d : directionList) {
                            //then remove last direction from direction list, add to removed list
                            if (n.getBoard().checkDirections(d)) {
                                directionList.add(d);
                            }
                        }
                        //if state is goal state, return state
                        if (n.getCurrentState().equals(goalStringState)) {
                            return n.getCurrentState();
                        }
                        //else generate child
                        else {
                            // successor states <- child
                            for (Board.Direction d : directionList) {
                                successors.add(new Node(n.getBoard().move(d), d, ++cost, calculateHueristic(n.getBoard(), heuristic), ++nodeId));
                            }
                        }
                        reached.add(n);
                        //For each in Open
                        ArrayList<Node> markedNodes = new ArrayList<>();
                        for (Node n1 : successors) {
                            boolean isInClosed = false;
                            //If child is in closed
                            for (Node n2 : closed) {
                                if (n1.equals(n2)) {
                                    isInClosed = true;
                                }
                            }
                            if (isInClosed) {
                                markedNodes.add(n1);
                            }
                        }
                        successors.removeAll(markedNodes);
                        beam.add(successors.poll());
                        closed.addAll(successors);
                        successors.clear();
                    }
                } else {
                    logger.log(LogClass.Methods.SOLVEBEAM, "Max Nodes Reached: " + maxNodes);
                    nodeBreak = true;
                }
            }
        } catch (Exception e) {
            logger.log(LogClass.Methods.SOLVEBEAM, "Unexpected Exception " + e + " at " + LogClass.Methods.SOLVEBEAM);
        }
        return beam.peek().getCurrentState();
    }

    // maxNodes <n>
    public void maxNodes(int n) {
        //set max nodes = n
        maxNodes = n;
    }
}
