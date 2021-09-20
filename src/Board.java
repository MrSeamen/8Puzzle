import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Board {
    private String stringState;
    private final int side; //number of tiles per side
    private final int total; //total amount of boxes
    private int[][] boardState; //state space
    private final int[][] goalState = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
    private final LogClass logger = new LogClass(); //logger
    private enum Direction {NORTH, SOUTH, EAST, WEST};

    Board(int size) {
        side = (int)Math.sqrt((size));
        assert side == (Math.sqrt(size)) : "Must have equal amount of blocks per side. (Perfect Square)";
        boardState = new int[side][side]; //representing puzzle
        total = size;
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
            logger.log(LogClass.Methods.SETSTATE, "Unexpected Exception");
        }
        updateStringState(boardState);
        return stringState;
    }

    /*
        updateStringState
        Helper method to update the String State
     */
    private void updateStringState(int[][] state) {
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
    private boolean checkSequentialInput(char[] state) {
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
    private void setValuesToBoard(String state, char[] stateArray) {
        for(int counterH = 0; counterH < side; counterH++) {
            for(int counterV = 0; counterV < side; counterV++) {
                boardState[counterH][counterV] = Integer.parseInt(Character.toString(stateArray[(counterH+counterV)]));
            }
        }
    }

    // printState
    public String printState() {
        System.out.println("Raw Input: " + stringState);
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
                returnString.append("/n").append(c).append(" ");
            }
        }
        System.out.println("Formatted Output: " + returnString);
        return returnString.toString();
    }

    // TODO: move <direction>
    String move(Direction direction) {
        try {
            //If direction is not null
            if (Objects.nonNull(direction)) {
                //then if direction is a valid direction
                if (checkDirections(direction)) {
                    //then swap blank with character in direction

                    //else

                    //error: invalid direction
                    //else
                } else {
                    //error: direction given is null
                    logger.log(LogClass.Methods.MOVE, "Null Input Given");
                }
            }
        } catch(Exception e){
            logger.log(LogClass.Methods.SETSTATE, "Unexpected Exception");
        }
        return stringState;
    }

    boolean checkDirections(Direction direction) {
        boolean validDirection = true;
        switch(direction) {
            case NORTH:
                break;
            case SOUTH:
                break;
            case EAST:
                break;
            case WEST:
                break;
        }
        return validDirection;
    }



    //randomizeState <n>
    String randomizeState(int n, Direction direction) {
        try {
            //lastDirection = lastdirection
            Direction lastDirection = direction;
            //newDirection = ""
            Direction newDirection;
            //directionlist of north south east west
            ArrayList<Direction> directionList = new ArrayList<>();
            //removedlist of “”
            Queue<Direction> removedList = new LinkedList<>();
            //if n <= 0
            if (n <= 0) {
                //then return state
                return stringState;
                //else
            } else {
                //if last direction is not null
                if (Objects.nonNull(direction)) {
                    //then remove last direction from direction list, add to removed list
                    directionList.remove(lastDirection);
                    directionList.add(removedList.poll());
                    removedList.add(lastDirection);
                    //routine takes (directionlist) and picks a direction
                    newDirection = randomizedDirection(directionList);
                    //move in direction
                    move(newDirection);
                    //randomizeState(n-1, lastdirection)
                    randomizeState(n-1, lastDirection);
                }
                //else
                else {
                    //error
                    logger.log(LogClass.Methods.RANDOMIZESTATE, "Null Input Given");
                }
            }
        } catch(Exception e) {
            logger.log(LogClass.Methods.SETSTATE, "Unexpected Exception");
        }
        return stringState;
    }

    //randomizedroutine ( dlist)
    Direction randomizedDirection(ArrayList<Direction> directionArrayList) {
        //If blank is touching wall
        //Remove directions that touch wall, add to removedlist
        //Pick a random number from 1-directionlist size
        //move(direction)
        //add directions from removedlist to directionlist, clear removedlist
    }

    // TODO: solveAStar <heuristic>
    void solveAStar(int heuristic) {
        //TODO: PSUEDOCODE
    }

    // TODO: solveBeam <k>
    void solveBeam(int k) {
        //TODO: PSUEDOCODE
    }

    // TODO: maxNodes <n>
    private void maxNodes(int n) {
        //TODO: PSUEDOCODE
    }

}
