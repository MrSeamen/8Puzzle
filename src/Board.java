import java.util.*;

public class Board {
    private String stringState;
    private final int side; //number of tiles per side
    private final int total; //total amount of boxes
    private int[][] boardState; //state space
    private final int[][] goalState = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
    private final String goalStringState = "012345678";
    private final LogClass logger = new LogClass(); //logger
    enum Direction {NORTH, SOUTH, EAST, WEST};
    public int maxNodes;

    Board(int size) {
        side = (int)Math.sqrt((size));
        assert side == (Math.sqrt(size)) : "Must have equal amount of blocks per side. (Perfect Square)";
        boardState = new int[side][side]; //representing puzzle
        total = size;
    }

    public int[][] getBoardState() {
        return boardState;
    }

    public int[][] getGoalState() {
        return goalState;
    }

    public String getStringState() {
        return stringState;
    }

    // setState <state>
    String setState(String state) {
        try {
            //if state is not null
            if (Objects.nonNull(state)) {
                char[] stateArray = state.toCharArray();
                //then if state doesnt have the all the proper values for the specified puzzle size
                if (!checkSequentialInput(stateArray)) {
                    //warn user about the puzzle possibly not working properly (log warning)
                    logger.log(LogClass.Methods.SETSTATE, "Nonsequential Input");
                    System.out.println("Nonsequential Input may cause 8 Puzzle to not function correctly.");
                }
                //go through each position and assign the correct value based on the input
                setValuesToBoard(state, stateArray);
            } else {
                //error: state given is null
                logger.log(LogClass.Methods.SETSTATE, "Null Input Given");
            }

        }
        catch (Exception e) {
            logger.log(LogClass.Methods.SETSTATE, "Unexpected Exception " + e + " at " + LogClass.Methods.SETSTATE);
        }
        updateStringState(boardState);
        return stringState;
    }

    /*
        updateStringState
        Helper method to update the String State
     */
    void updateStringState(int[][] state) {
        stringState = ""; //clear state
        for (int row = 0; row < side; row++) {
            for (int col = 0; col < side; col++) {
                //update string state
                stringState += state[row][col];
            }
        }
    }

    /*
        checkSequentialInput
        Helper method to setState
     */
    boolean checkSequentialInput(char[] state) {
        for(char character : state) {
            int value = Integer.parseInt(Character.toString(character));
            if (0 > value || value > total) { //value is outside the range of what the algorithm is expected to do
                return false;
            }
        }
        return true;
    }

    /*
        setValuesToBoard
        Helper method to setState
     */
    void setValuesToBoard(String state, char[] stateArray) {
        int counter = 0;
        for(int counterH = 0; counterH < side; counterH++) {
            for(int counterV = 0; counterV < side; counterV++) {
                boardState[counterH][counterV] = Integer.parseInt(Character.toString(stateArray[counter]));
                counter++;
            }
        }
    }

    // printState
    public String printState() {
        StringBuilder returnString = new StringBuilder();
        char[] stateArray = stringState.toCharArray();
        int counter = 0;
        //for each character in the string
        for(char c : stateArray) {
            //if the counter < max counter
            if (counter < side) {
                //print character + “ “
                returnString.append(c).append(" ");
            }
            //else
            else {
                counter = 1;
                //print “/n” + character + “ “
                returnString.append(" ").append(System.lineSeparator()).append(c).append(" ");
            }
        }
        System.out.println("Formatted Output: \n" + returnString);
        return returnString.toString();
    }

    // move <direction>
    public String move(Direction direction) {
        try {
            //If direction is not null
            if (Objects.nonNull(direction)) {
                //then if direction is a valid direction
                if (checkDirections(direction)) {
                    //then swap blank with character in direction
                    swap(getBlankPosition(), direction);
                    //else
                    this.printState();
                    return stringState;
                } else {
                    //error: invalid direction
                    logger.log(LogClass.Methods.MOVE, "Invalid Direction");
                    System.out.println("Could not move " + direction.toString() + ", move is not within board space.");
                }
            } else {
                //error: direction given is null
                logger.log(LogClass.Methods.MOVE, "Null Input Given");
            }
        } catch(Exception e){
            logger.log(LogClass.Methods.MOVE, "Unexpected Exception " + e + " at " + LogClass.Methods.MOVE);
        }
        System.out.print(stringState);
        return stringState;
    }

    void swap(int[] blankPosition, Direction direction) {
        switch(direction) {
            case NORTH:
                swapPosition(blankPosition, -1, 0);
                break;
            case SOUTH:
                swapPosition(blankPosition, 1, 0);
                break;
            case EAST:
                swapPosition(blankPosition, 0, 1);
                break;
            case WEST:
                swapPosition(blankPosition, 0, -1);
                break;
        }
        System.out.println("Moved " + direction);
    }

    void swapPosition(int[] blankPos, int vertical, int horizontal) {
        //System.out.println("blank" + boardState[blankPos[0]][blankPos[1]] + " at " + Integer.toString(blankPos[0]) + Integer.toString(blankPos[1]));
        int holdingValue = boardState[blankPos[0] + vertical][blankPos[1] + horizontal];
        //System.out.println("holding value" + holdingValue + " at " + Integer.toString(blankPos[0] + vertical) + Integer.toString(blankPos[1] + horizontal));
        boardState[blankPos[0]][blankPos[1]] = holdingValue;
        //System.out.println("confirmed move of holding value " + boardState[blankPos[0]][blankPos[1]] + " to " + Integer.toString(blankPos[0]) + Integer.toString(blankPos[1]));
        boardState[blankPos[0] + vertical][blankPos[1] + horizontal] = 0;
        //System.out.println("confirmed move of blank " + boardState[vertical + blankPos[0]][horizontal + blankPos[1]] + " to "+ Integer.toString(blankPos[0] + vertical) + Integer.toString(blankPos[1] + horizontal));
        updateStringState(boardState);
        //System.out.println(stringState);
    }

    boolean checkDirections(Direction direction) {
        boolean validDirection = false;
        switch(direction) {
            case NORTH:
                validDirection = checkPosition(getBlankPosition(), -1, 0);
                break;
            case SOUTH:
                validDirection = checkPosition(getBlankPosition(), 1, 0);
                break;
            case EAST:
                validDirection = checkPosition(getBlankPosition(), 0, 1);
                break;
            case WEST:
                validDirection = checkPosition(getBlankPosition(), 0, -1);
                break;
        }
        return validDirection;
    }

    int[] getBlankPosition() {
        int[] position = new int[2];
        for (int row = 0; row < side; row++) {
            for (int col = 0; col < side; col++) {
                if (boardState[row][col] == 0) {
                    position[0] = row;
                    position[1] = col;
                }
            }
        }
        return position;
    }

    boolean checkPosition(int[] blankPos, int vertical, int horizontal) {
        int deltaX = blankPos[0] + horizontal;
        int deltaY = blankPos[1] + vertical;
        return ((0 <= deltaX || deltaX < side) && (0 <= deltaY || deltaY < side));
    }

    //randomizeState <n>
    public String randomizeState(int n) {
        try {
            //lastDirection = lastdirection
            Direction lastDirection = null;
            //newDirection = ""
            Direction newDirection;
            //directionlist of north south east west
            ArrayList<Direction> directionList = new ArrayList<>();
            directionList.add(Direction.NORTH);
            directionList.add(Direction.SOUTH);
            directionList.add(Direction.EAST);
            directionList.add(Direction.WEST);
            //removedlist of “”
            ArrayList<Direction> removedList = new ArrayList<>();
            //if last direction is not null
            if (Objects.nonNull(n)) {
                for (int i = 0; i < n; i++) {
                    //remove any nonvalid directions
                    for(Direction d : directionList) {
                        if(!checkDirections(d)) {
                            removedList.add(d);
                            directionList.remove(d);
                        }
                    }
                    //then remove last direction from direction list, add to removed list
                    if(directionList.contains(lastDirection)) {
                        removedList.add(lastDirection);
                        directionList.remove(lastDirection);
                    }
                    //routine takes (directionlist) and picks a direction
                    newDirection = randomizedDirection(directionList);
                    //move in direction
                    move(newDirection);
                    lastDirection = newDirection;
                    //readd directions
                    for(Direction d : removedList) {
                        directionList.add(d);
                    }
                    removedList.clear();
                }
            }
            //else
            else {
                //error
                logger.log(LogClass.Methods.RANDOMIZESTATE, "Null Input Given");
            }
        } catch(Exception e) {
            logger.log(LogClass.Methods.RANDOMIZESTATE, "Unexpected Exception " + e + " at " + LogClass.Methods.RANDOMIZESTATE);
        }
        updateStringState(boardState);
        return stringState;
    }

    //randomizedroutine ( dlist)
    Direction randomizedDirection(ArrayList<Direction> directionArrayList) {
        Random random = new Random();
        //Pick a random number from 1-directionlist size
        int randomNum = random.nextInt(directionArrayList.size()-1);
        return directionArrayList.get(randomNum);
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
            for (int k = 0; k < (side*side)-1; k++) {
                for (int i = 0; i < side; i++) {
                    for (int j = 0; j < side; j++) {
                        if (board.getBoardState()[i][j] == k) {
                            if (a > 1) {
                                a = 0;
                                b++;
                            } else {
                                a++;
                            }
                            distance += Math.abs(i - b) + Math.abs(j - a);
                        }
                    }
                }
            }
            returnValue = distance;
        }
        return returnValue;
    }

    public class Node {
        private Board state = new Board(side*side);
        private Node previousNode;
        private Direction move;
        private int cost;
        private int heuristic;

        public Node getPreviousNode() {
            return previousNode;
        }

        public Direction getMove() {
            return move;
        }

        public Board getState() {
            return state;
        }

        public int getCost() {
            return cost;
        }

        public int getHeuristic() {
            return heuristic;
        }

        public Node(String stringState, Node previousNode, Direction move, int cost, int heuristic) {
            if (!Objects.nonNull(move)) {
                state.setState(stringState);
            } else {
                state.setState(stringState);
                state.move(move);
            }
            this.previousNode = previousNode;
            this.move = move;
            this.cost = cost;
            this.heuristic = calculateHueristic(state, heuristic);
        }

        //returns if this node is better than node n in f(n)
        public boolean compareTo(Node n) {
            return (this.getCost() + this.getHeuristic() < n.getCost() + n.getHeuristic());
        }

        @Override
        public String toString() {
            return state.getStringState() + " " + getPreviousNode() + " " + getMove() + " " + getCost() + " " + getCost();
        }
    }

    // solveAStar <heuristic>
    public String solveAStar(int heuristic) {
        //A* Search(problem)
        try {
            if (Objects.nonNull(heuristic)) {
                System.out.println("Solving with A* with heuristic " + heuristic);
                //direction list
                ArrayList<Direction> directionList = new ArrayList<>();
                directionList.add(Direction.NORTH);
                directionList.add(Direction.SOUTH);
                directionList.add(Direction.EAST);
                directionList.add(Direction.WEST);
                //removedlist of “”
                ArrayList<Direction> removedList = new ArrayList<>();
                //path cost from parent
                int cost = 0;
                //Open <- problem.start
                Queue<Node> Open = new LinkedList<>();
                //CurrentNode <- problem.start
                Node currentNode = new Node(this.stringState, null, null, cost, heuristic);
                Open.add(currentNode);
                //highest
                Node bestChoice = null;
                //Closed
                ArrayList<Node> Closed = new ArrayList<>();
                //Reached
                ArrayList<Node> Reached = new ArrayList<>();
                //While Open is not empty
                while (!Open.isEmpty()) {
                    //If CurrentNode = goal
                    for(Direction d : directionList) {
                        if(!checkDirections(d)) {
                            removedList.add(d);
                            directionList.remove(d);
                        }
                    }
                    if (currentNode.getState().stringState.equals(goalStringState)) {
                        //Return CurrentNode
                        return currentNode.getState().stringState;
                    }
                    //else
                    else {
                        //Reached <- currentNode
                        Reached.add(currentNode);
                        //Open <- CurrentNode.children
                        for(Direction d : directionList) {
                            Open.add(new Node(currentNode.getState().getStringState(), currentNode, d, ++cost, heuristic));
                        }
                        //Remove CurrentNode from Open to closed
                        System.out.println(Open.peek().toString()); //SHOULD BE STARTING NODE AT FIRST/PARENT NODE
                        Closed.add(Open.poll());
                        //For each in Open
                        for(Node n : Open) {
                            //If child is in closed
                            if (Closed.contains(n)) {
                                //Then break
                                break;
                            }
                            //Calculate f(n) = g(n) +h(n)
                            int f = n.getCost() + n.getHeuristic();
                            //If highest is empty
                            if (!Objects.nonNull(bestChoice)) {
                                //Then If CurrentNode.f(n) < child.f(n)
                                //highest <- child
                                bestChoice = new Node(n.getState().getStringState(), n.getPreviousNode(), n.getMove(), n.getCost(), heuristic);
                            } else
                            //else if highest.f(n) > child.f(n)
                            if(!bestChoice.compareTo(n)) {
                                //highest <- child
                                bestChoice = new Node(n.getState().getStringState(), n.getPreviousNode(), n.getMove(), n.getCost(), heuristic);
                            }
                            //else
                            else {
                                System.out.println(Open.peek().toString()); //SHOULD BE CHILD NODE
                                //move child to closed
                                Closed.add(Open.poll());
                                //move currentNode to reached
                                //currentNode = highest
                                currentNode = new Node(bestChoice.getState().getStringState(), bestChoice.getPreviousNode(), bestChoice.getMove(), bestChoice.getCost(), heuristic);
                            }
                        }
                    }
                    for(Direction d : removedList) {
                        directionList.add(d);
                        removedList.remove(d);
                    }
                }
                for(Node n : Reached) {
                    System.out.println(n.toString());
                }
                return bestChoice.toString();
            } else {
                //error
                logger.log(LogClass.Methods.SOLVEASTAR, "Null Input Given");
            }
        } catch (Exception e) {
            logger.log(LogClass.Methods.SOLVEASTAR, "Unexpected Exception " + e + " at " + LogClass.Methods.SOLVEASTAR);
        }
        updateStringState(this.getBoardState());
        return this.getStringState();
    }

    // solveBeam <k>
    public String solveBeam(int k) {
        //state array of k size
        //successor states list
        //while goal not found
            //for state in state array
                //if state is goal state, return state
                //else generate child
                    // successor states <- child
            //for each successor state
                //best states array of k size
                //if successor evaluation function > worst successor state evaluation function
                    //remove worst state, add successor state
            //clear successor state
        return this.getStringState();
    }

    // maxNodes <n>
     public void maxNodes(int n) {
        //set max nodes = n
        maxNodes = n;
    }
}
