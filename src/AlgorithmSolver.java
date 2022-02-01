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
        int returnValue;
        if (type == 1) {
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
        } else if (type == 2) {
            //calculating h2 heuristic (manhattan distances)
            int distance = 0;
            int a = 0;
            int b = 0 ;
            for (int k = 0; k < (board.getSide()*board.getSide()); k++) {
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
            }
            for (int i = 0; i < board.getSide(); i++) {
                for (int j = 0; j < board.getSide(); j++) {
                    distance += Math.abs(b - j) + Math.abs(a - i);
                }
            }
            returnValue = distance;
        } else {
            return -1;
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
                case SOUTH:
                    parentState.move(Board.Direction.NORTH);
                case EAST:
                    parentState.move(Board.Direction.WEST);
                case WEST:
                    parentState.move(Board.Direction.EAST);
            }
            this.pathCost = pathCost;
            assert heuristicCost != -1;
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

        public int getNodeID() {
            return nodeID;
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
                Node currentNode = new Node(startingBoard.getStringState(), Board.Direction.INITIAL, cost, calculateHueristic(startingBoard, heuristic), ++nodeID);
                //highest
                Node bestChoice;
                //add currentNode to Open
                Open.add(currentNode);
                //While Open is not empty
                while (!(Reached.size() > maxNodes)) {
                    if (!Open.isEmpty()) {
                        //If CurrentNode = goal
                        if (currentNode.getCurrentState().equals(goalStringState)) {
                            System.out.println("Number of steps: " + Reached.size());
                            System.out.println("Current Node: " + currentNode);
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
                                int childCost = cost;
                                Open.add(new Node( child.getStringState(), d, ++childCost, calculateHueristic(child, heuristic), ++nodeID));
                            }
                            //Remove CurrentNode from Open to closed

                            //Closed <- currentNode
                            Reached.add(currentNode);
                            Open.remove(currentNode);

                            //For each in Open
                            //If child is in closed
                            //If highest is empty
                            //Then If CurrentNode.f(n) > child.f(n)
                            //highest <- child
                            //else if highest.f(n) > child.f(n)
                            //else
                            //move child to closed
                            //currentNode = highest

                            //TODO: remove all states already been through
                            Open.removeAll(Reached);

                            //update state space
                            currentNode = Open.peek();
                        }
                    } else {
                        logger.log(LogClass.Methods.SOLVEASTAR, "Max Nodes Reached: " + maxNodes);
                        Open.clear();
                    }
                }
                System.out.println("Number of steps: " + Reached.size());
                System.out.println("Current State: " + System.lineSeparator() + currentNode.getCurrentState());
                return currentNode;
            } else {
                //error
                logger.log(LogClass.Methods.SOLVEASTAR, "Null Input Given");
            }
        } catch (Exception e) {
            logger.log(LogClass.Methods.SOLVEASTAR, "Unexpected Exception " + e + " at " + LogClass.Methods.SOLVEASTAR);
        }
        return null;
    }
/*
    boolean checkGoal(ArrayList<Node> nodeList) {
        for (Node n : nodeList) {
            if (n.getState().getStringState().equals(currentBoard.getGoalStringState())) {
                return true;
            }
        }
        return false;
    }

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
