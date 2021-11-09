import java.util.ArrayList;
import java.util.Objects;

public class AlgorithmSolver {
    //max nodes
    private int maxNodes;
    //node count
    private int nodeCount = 0;
    //current board
    private Board currentBoard;

    private final LogClass logger = new LogClass(); //logger

    public int getMaxNodes() {
        return maxNodes;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    private int calculateHueristic(Board board, int type) {
        int returnValue;
        if (type == 1) {
            //calculating h1 heuristic (misplaced tiles)
            int misplacedTiles = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board.getBoardState()[i][j] != board.getGoalState()[i][j]) {
                        misplacedTiles++;
                    }
                }
            }
            returnValue = misplacedTiles;
        } else {
            //calculating h2 heuristic (manhattan distances)
            int distance = 0;
            int a = 0;
            int b = 0 ;
            for (int k = 0; k < (board.getSide()*board.getSide()); k++) {
                for (int i = 0; i < board.getSide(); i++) {
                    for (int j = 0; j < board.getSide(); j++) {
                        switch(k) {
                            case 0:
                                a = 0;
                                b = 0;
                                break;
                            case 1:
                                a = 0;
                                b = 1;
                                break;
                            case 2:
                                a = 0;
                                b = 2;
                                break;
                            case 3:
                                a = 1;
                                b = 0;
                                break;
                            case 4:
                                a = 1;
                                b = 1;
                                break;
                            case 5:
                                a = 1;
                                b = 2;
                                break;
                            case 6:
                                a = 2;
                                b = 0;
                                break;
                            case 7:
                                a = 2;
                                b = 1;
                                break;
                            case 8:
                                a = 2;
                                b = 2;
                                break;
                        }
                        distance += Math.abs(b - j) + Math.abs(a - i);
                    }
                }
            }
            returnValue = distance;
        }
        return returnValue;
    }

    public void setCurrentBoard(String boardState) {
        currentBoard = new Board(boardState.length());
        currentBoard.setState(boardState);
    }

    //node subclass - contains board state, last move, path cost, heuristic cost amd type, parent state, and ID
    public class Node {
        private String parentState;
        private Board.Direction move; // move that got to this position
        private int pathCost;
        private int heuristicCost;
        private int heuristicType;
        private int nodeID;
        private Board state;

        public Node(String stringState, Board.Direction move, int pathCost, String parentState, int heuristicType) {
            assert Objects.nonNull(stringState) : "Current given board state is null";
            state = new Board(stringState.length());
            state.setState(stringState);
            if (!Objects.nonNull(parentState)) { //if initial state
                this.parentState = null;
            } else { //else child state
                this.parentState = parentState;
            }
            this.move = move;
            this.pathCost = pathCost;
            this.heuristicCost = calculateHueristic(state, heuristicType);
            this.heuristicType = heuristicType;
            nodeID = nodeCount++;
        }

        public int getHeuristicType() {
            return heuristicType;
        }

        public Board.Direction getMove() {
            return move;
        }

        public Board getState() {
            return state;
        }

        public String getParentState() { return parentState; }

        public int getPathCost() {
            return pathCost;
        }

        public int getHeuristicCost() {
            return heuristicCost;
        }

        public int getNodeID() {
            return nodeID;
        }

        //returns if this node is better than node n in f(n)
        public boolean compareTo(Node n) {
            return ((this.getPathCost() + this.getHeuristicCost()) <= (n.getPathCost() + n.getHeuristicCost()));
        }

        @Override
        public String toString() {
            return "State: " + getState().getStringState() + System.lineSeparator() +
                    "Previous Node: " + System.lineSeparator() + getParentState() + System.lineSeparator() +
                    "Move: " + getMove() + System.lineSeparator() +
                    "Path Cost: " + getPathCost() + System.lineSeparator() +
                    "Heuristic Type: " + getHeuristicType() + System.lineSeparator() +
                    "Heuristic Value: " + getHeuristicCost() + System.lineSeparator() +
                    "Node ID: " + getNodeID();
        }
    }

    // solveAStar <heuristic>
    public String solveAStar(int heuristic) {
        //A* Search(problem)
        try {
            if (Objects.nonNull(heuristic) && Objects.nonNull(maxNodes)) {
                System.out.println("Solving with A* with heuristic " + heuristic);
                //direction list
                ArrayList<Board.Direction> directionList = new ArrayList<>();
                //path cost from parent
                int cost = 0;
                //Open <- problem.start
                ArrayList<Node> Open = new ArrayList<>();
                //CurrentNode <- problem.start
                Node currentNode = new Node(getCurrentBoard().getStringState(), Board.Direction.INITIAL, cost, null, heuristic);
                Open.add(currentNode);
                //highest
                Node bestChoice = null;
                //Closed
                ArrayList<Node> Closed = new ArrayList<>();
                //Reached
                ArrayList<Node> Reached = new ArrayList<>();
                //While Open is not empty
                while (!Open.isEmpty()) {
                    if (!(Reached.size() > maxNodes)) {
                        for (Board.Direction d : currentNode.getState().getCardinalDirections()) {
                            //then remove last direction from direction list, add to removed list
                            if (currentNode.getState().checkDirections(d)) {
                                directionList.add(d);
                            }
                        }
                        //If CurrentNode = goal
                        if (currentNode.getState().getStringState().equals(currentNode.getState().getGoalStringState())) {
                            //Return CurrentNode
                            return currentNode.getState().getStringState();
                        }
                        //else
                        else {
                            //Reached <- currentNode
                            Reached.add(currentNode);
                            //Open <- CurrentNode.
                            System.out.println("Creating Children...");
                            for (Board.Direction d : directionList) {
                                Board child = new Board(currentNode.getState().getTotal());
                                child.setState(currentNode.getState().getStringState());
                                Open.add(new Node( child.move(d), d, ++cost, currentNode.getState().getStringState(), heuristic));
                            }
                            //Remove CurrentNode from Open to closed
                            //System.out.println(Open.get(0).toString()); //SHOULD BE STARTING NODE AT FIRST/PARENT NODE
                            Open.remove(currentNode);
                            System.out.println("Evaluating Children...");
                            ArrayList<Node> OpenCopy = new ArrayList<>(Open);
                            //System.out.println(OpenCopy);
                            //For each in Open
                            for (Node n : OpenCopy) {
                                //If child is in closed
                                //Calculate f(n) = g(n) +h(n)
                                int f = n.getPathCost() + n.getHeuristicCost();
                                //System.out.println(n.getState().getStringState() + " " + f + " " + n.getCost() + " " + n.getHeuristic());
                                //If highest is empty
                                if (!Objects.nonNull(bestChoice)) {
                                    //Then If CurrentNode.f(n) < child.f(n)
                                    //highest <- child
                                    bestChoice = new Node(n.getState().getStringState(), n.getMove(), n.getPathCost(), n.getParentState(), heuristic);
                                } else
                                    //else if highest.f(n) > child.f(n)
                                    if (!n.compareTo(currentNode)) {
                                        //highest <- child
                                        bestChoice = new Node(n.getState().getStringState(), n.getMove(), n.getPathCost(), n.getParentState(), heuristic);
                                    }
                                    //else
                                    else {
                                        //System.out.println(Open.peek().toString()); //SHOULD BE CHILD NODE
                                        //move child to closed
                                        Closed.add(n);
                                        Open.remove(n);
                                    }
                            }
                            //currentNode = highest
                            currentNode = new Node(bestChoice.getState().getStringState(), bestChoice.getMove(), bestChoice.getPathCost(), bestChoice.getParentState(), heuristic);
                            //update state space
                            currentBoard.move(currentNode.getMove());
                            //move currentNode to reached
                            Reached.add(new Node (currentNode.getState().getStringState(), currentNode.getMove(), currentNode.getPathCost(), currentNode.getParentState(), heuristic));
                            Open.remove(currentNode);
                            //System.out.println(bestChoice.getState().getStringState());
                            //clear working data
                            OpenCopy.clear();
                            directionList.clear();
                            bestChoice = null;

                            currentBoard.printState();
                        }
                    } else {
                        logger.log(LogClass.Methods.SOLVEASTAR, "Max Nodes Reached: " + maxNodes);
                        Open.clear();
                    }
                }
                System.out.println("Number of steps: " + Reached.size());
                System.out.println("Current State " + currentNode.getState().printState());
                return currentBoard.getStringState();
            } else {
                //error
                logger.log(LogClass.Methods.SOLVEASTAR, "Null Input Given");
            }
        } catch (Exception e) {
            logger.log(LogClass.Methods.SOLVEASTAR, "Unexpected Exception " + e + " at " + LogClass.Methods.SOLVEASTAR);
        }
        return currentBoard.getStringState();
    }

    boolean checkGoal(ArrayList<Node> nodeList) {
        for (Node n : nodeList) {
            if (n.getState().getStringState().equals(currentBoard.getGoalStringState())) {
                return true;
            }
        }
        return false;
    }
/*
    // solveBeam <k>
    public String solveBeam(int k) {
        //direction list
        ArrayList<Board.Direction> directionList = new ArrayList<>();
        //state array of k size
        ArrayList<Node> beam = new ArrayList<>(); //REMEMBER TO CHECK FOR MAX OF K
        //arraylist of reached
        ArrayList<Node> reached = new ArrayList<>();
        //cost
        int cost = 0;
        //successor states list
        ArrayList<Node> successors = new ArrayList<>();
        //initial
        Node initial = new Node(currentBoard.getStringState(), Board.Direction.INITIAL, 0, null, 1);
        boolean nodeBreak= false;
        beam.add(initial);
        try {
            //while goal not found
            while (!checkGoal(successors) || !nodeBreak) {
                if (beam.size() + reached.size() <= maxNodes) {
                    for (Board.Direction d : directions) {
                        //then remove last direction from direction list, add to removed list
                        if (checkDirections(d)) {
                            directionList.add(d);
                        }
                    }
                    //for state in state array
                    for (Node state : beam) {
                        for (Board.Direction d : directions) {
                            //then remove last direction from direction list, add to removed list
                            if (checkDirections(d)) {
                                directionList.add(d);
                            }
                        }
                        //if state is goal state, return state
                        if (state.getState().getStringState().equals(getGoalStringState())) {
                            return state.getState().getStringState();
                        }
                        //else generate child
                        else {
                            // successor states <- child
                            for (Board.Direction d : directionList) {
                                successors.add(new Node(state.getState().getStringState(), state, d, ++cost, 1));
                            }
                        }
                    }
                    ArrayList<Node> bestStates = new ArrayList<>(); //REMEMBER TO KEEP UNDER MAXNODES SIZE
                    //for each successor state
                    for (Node child : successors) {
                        //best states array of k size
                        //if successor evaluation function > worst successor state evaluation function
                        if (bestStates.isEmpty() || bestStates.size() < k) {
                            bestStates.add(child);
                        } else if (bestStates.size() >= k) {
                            //remove worst state, add successor state
                            Node lowest = null;
                            for (Node n : bestStates) {
                                if (!Objects.nonNull(lowest)) {
                                    lowest = new Node(n.getState().getStringState(), n.getPreviousNode(), n.getMove(), n.getCost(), 1);
                                } else {
                                    if (!n.compareTo(lowest)) {
                                        lowest = new Node(n.getState().getStringState(), n.getPreviousNode(), n.getMove(), n.getCost(), 1);
                                    }
                                }
                            }
                            bestStates.remove(lowest);
                            bestStates.add(child);
                        }
                    }

                    ArrayList<Node> worstStates = new ArrayList<>();
                    ArrayList<Node> worstStatesCopy = new ArrayList<>();
                    for (int i = 0; i < bestStates.size(); i++) {
                        worstStates.add(beam.get(i));
                    }
                    beam.removeAll(worstStates);
                    worstStatesCopy.addAll(worstStates);
                    for (Node node : worstStatesCopy) {
                        Node highest = new Node(node.getState().getStringState(), node.getPreviousNode(), node.getMove(), node.getCost(), 1);
                        Node swap = null;
                        for (Node n : beam) {
                            if (node.compareTo(n)) {
                                highest = new Node(node.getState().getStringState(), node.getPreviousNode(), node.getMove(), node.getCost(), 1);
                                swap = new Node(n.getState().getStringState(), n.getPreviousNode(), n.getMove(), n.getCost(), 1);
                            }
                        }
                        worstStates.remove(highest);
                        beam.remove(swap);
                        worstStates.add(swap);
                        beam.add(highest);
                    }
                    directionList.clear();
                    //clear successor state
                    successors.clear();
                } else {
                    logger.log(LogClass.Methods.SOLVEBEAM, "Max Nodes Reached: " + maxNodes);
                    nodeBreak = true;
                }
            }
        } catch (Exception e) {
            logger.log(LogClass.Methods.SOLVEBEAM, "Unexpected Exception " + e + " at " + LogClass.Methods.SOLVEBEAM);
        }
        return getStringState();
    }
    */

    // maxNodes <n>
    public void maxNodes(int n) {
        //set max nodes = n
        maxNodes = n;
    }
}
