import java.lang.reflect.Array;
import java.util.*;

public class Board {
    private String stringState;
    private final int side; //number of tiles per side
    private final int total; //total amount of boxes
    private int[][] boardState; //state space
    private final LogClass logger = new LogClass(); //logger
    enum Direction {NORTH, SOUTH, EAST, WEST, INITIAL, FINAL};
    ArrayList<Direction> directions = new ArrayList<>();

    Board(int size) {
        side = (int)Math.sqrt((size));
        assert side == (Math.sqrt(size)) : "Must have equal amount of blocks per side. (Perfect Square)";
        boardState = new int[side][side]; //representing puzzle
        total = size;
        directions.add(Direction.NORTH);
        directions.add(Direction.SOUTH);
        directions.add(Direction.EAST);
        directions.add(Direction.WEST);
        makeGoalState(side);
        makeGoalStringState(side);
    }

    public int getSide() {
        return side;
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

    public String getGoalStringState() { return goalStringState; }

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
                counter = 0;
                //print “/n” + character + “ “
                returnString.append(System.lineSeparator()).append(c).append(" ");
            }
            counter++;
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
                    //this.printState();
                    return stringState;
                } else {
                    //error: invalid direction
                    logger.log(LogClass.Methods.MOVE, "Invalid Direction: " + direction);
                    //System.out.println("Could not move " + direction.toString() + ", move is not within board space.");
                    //this.printState();
                }
            } else {
                //error: direction given is null
                logger.log(LogClass.Methods.MOVE, "Null Input Given");
            }
        } catch(Exception e){
            logger.log(LogClass.Methods.MOVE, "Unexpected Exception " + e + " at " + LogClass.Methods.MOVE + " with direction " + direction);
        }
        //this.printState();
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
        //System.out.println("Moved " + direction);
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
        //System.out.println("checkDirections " + validDirection);
        return switch (direction) {
            case NORTH -> checkPosition(getBlankPosition(), -1, 0);
            case SOUTH -> checkPosition(getBlankPosition(), 1, 0);
            case EAST -> checkPosition(getBlankPosition(), 0, 1);
            case WEST -> checkPosition(getBlankPosition(), 0, -1);
            default -> false;
        };
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
        int deltaY = blankPos[0] + vertical;
        int deltaX = blankPos[1] + horizontal;
        //System.out.println("position: " + "vert: " + Integer.toString(blankPos[0]) + "+" + Integer.toString(vertical) + "=" + Integer.toString(deltaY) + " horz:"+ Integer.toString(blankPos[1]) + "+" + Integer.toString(horizontal) + "=" + Integer.toString(deltaX));
        boolean returnValue = false;
        if (horizontal == 1 || horizontal == -1) {
            returnValue = (0 <= deltaX && deltaX < side);
        } else if (vertical == 1 || vertical == -1) {
            returnValue = (0 <= deltaY && deltaY < side);
        }
        return returnValue;
    }

    //randomizeState <n>
    public String randomizeState(int n) {
        try {
            //lastDirection = lastdirection
            Direction lastPositionDirection = null;
            //newDirection = ""
            Direction newDirection;
            //directionlist of north south east west
            ArrayList<Direction> directionList = new ArrayList<>();

            ArrayList<Direction> path = new ArrayList<>();

            //if last direction is not null
            if (Objects.nonNull(n)) {
                for (int i = 0; i < n; i++) {
                    //System.out.println("Move " + (i+1) + ": " + getStringState());
                    //remove any nonvalid directions
                    for(Direction d : directions) {
                        //then remove last direction from direction list, add to removed list
                        if (checkDirections(d) && d!=lastPositionDirection) {
                            directionList.add(d);
                        }
                    }
                    //System.out.println(directionList.toString());
                    //routine takes (directionlist) and picks a direction
                    newDirection = randomizedDirection(directionList);
                    path.add(newDirection);
                    //move in direction
                    move(newDirection);
                    switch(newDirection) {
                        case EAST -> lastPositionDirection = Direction.WEST;
                        case WEST -> lastPositionDirection = Direction.EAST;
                        case NORTH -> lastPositionDirection = Direction.SOUTH;
                        case SOUTH -> lastPositionDirection = Direction.NORTH;
                    }
                    directionList.clear();
                }
            }
            //else
            else {
                //error
                logger.log(LogClass.Methods.RANDOMIZESTATE, "Null Input Given");
            }
            System.out.println("Path: " + path);
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
        int randomNum = random.nextInt(directionArrayList.size());
        return directionArrayList.get(randomNum);
    }
}
