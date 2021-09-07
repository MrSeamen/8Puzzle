import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    public void setUp() throws Exception {
        board = new Board(3);
    }

    public void setStateTest() {
        //set board as a custom arrangement
        assertEquals("574638201", board.setState("574638201"));
        //set board as arrangement in order
        assertEquals("012345678", board.setState("012345678"));
        //set board as above with one tiny change
        assertEquals("312045678", board.setState("312045678"));
    }

    public void printStateTest() {
        //set board as a custom arrangement, then test print
        board.setState("574638201");
        assertEquals("5 7 4 n/6 3 8 n/2 0 1 ", board.printState());
        //set board as arrangement in order, then test print
        board.setState("012345678");
        assertEquals("0 1 2 n/3 4 5 n/6 7 8 ", board.printState());
        //set board as above with one tiny change, then test print
        board.setState("312045678");
        assertEquals("3 1 2 n/0 4 5 n/6 7 8 ", board.printState());
    }

    public void randomizeStateTest() {
        Random random = new Random();
        //randomize board, check to make sure board isnt the same as regular
        assertNotEquals("012345678", board.randomizeState(random.nextInt(19)+1));
    }

    public void solveAStarTest() {
        //set state to a simple solveable state

        //set state to a more complex but solveable state

        //set state to an unsolveable state
    }

    public void solveBeamTest() {
        //set state to a simple solveable state

        //set state to a more complex but solveable state

        //set state to an unsolveable state
    }
}