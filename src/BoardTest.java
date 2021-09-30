import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class BoardTest {

    public Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board(9);
    }

    @Test
    public void setStateTest() {
        //set board as a custom arrangement
        assertEquals("574638201", board.setState("574638201"));
        //set board as arrangement in order
        assertEquals("012345678", board.setState("012345678"));
        //set board as above with one tiny change
        assertEquals("312045678", board.setState("312045678"));
    }

    //New Line character does not work properly when printing for some unknown reason, thus causing these tests to fail
    @Test
    public void printStateTest() {
        //set board as a custom arrangement, then test print
        board.setState("574638201");
        assertEquals("5 7 4 " + System.lineSeparator() + "6 3 8 " + System.lineSeparator() + "2 0 1 ", board.printState());
        //set board as arrangement in order, then test print
        board.setState("012345678");
        assertEquals("0 1 2 " + System.lineSeparator() + "3 4 5 " + System.lineSeparator() + "6 7 8 ", board.printState());
        //set board as above with one tiny change, then test print
        board.setState("312045678");
        assertEquals("3 1 2 " + System.lineSeparator() + "0 4 5 " + System.lineSeparator() + "6 7 8 ", board.printState());
    }

    @Test
    public void randomizeStateTest() {
        Random random = new Random();
        board.setState("012345678");
        int randomNum = random.nextInt(19)+1;
        System.out.println("Randomize " + randomNum);
        //randomize board, check to make sure board isnt the same as regular
        assertNotEquals("012345678", board.randomizeState(randomNum));
    }

    @Test
    public void move() {
        board.setState("123405678");
        assertEquals("123450678", board.move(Board.Direction.EAST));
        board.setState("123405678");
        assertEquals("123475608", board.move(Board.Direction.SOUTH));
        board.setState("123405678");
        assertEquals("103425678", board.move(Board.Direction.NORTH));
        board.setState("123405678");
        assertEquals("123045678", board.move(Board.Direction.WEST));
    }

    @Test
    public void solveAStar() {
        board.maxNodes(50);
        //set state to a simple solveable state
        board.setState("012345678");
        board.randomizeState(5);
        assertEquals("012345678", board.solveAStar(1)); //Heuristic 1
        board.setState("012345678");
        board.randomizeState(5);
        assertEquals("012345678", board.solveAStar(2)); //Heuristic 1
        //set state to a more complex but solveable state
        board.setState("012345678");
        board.randomizeState(20);
        assertEquals("012345678", board.solveAStar(1));
        board.setState("012345678");
        board.randomizeState(20);
        assertEquals("012345678", board.solveAStar(2));
        //set state to a really complex but solveable state
        board.setState("012345678");
        board.randomizeState(50);
        assertEquals("012345678", board.solveAStar(1));
        board.setState("012345678");
        board.randomizeState(50);
        assertEquals("012345678", board.solveAStar(2));
        //set state to an unsolveable state
        board.setState("123456780");
        assertNotEquals("012345678", board.solveAStar(1));
        assertNotEquals("012345678", board.solveAStar(2));

    }

    @Test
    public void solveBeam() {
        board.maxNodes(50);
        //set state to a simple solveable state
        board.setState("012345678");
        board.randomizeState(5);
        board.solveBeam(4);
        assertEquals("012345678", board.solveBeam(4));
        //set state to a more complex but solveable state
        board.setState("012345678");
        board.randomizeState(20);
        board.solveBeam(4);
        assertEquals("012345678", board.solveBeam(4));
        //set state to a really complex but solveable state
        board.setState("012345678");
        board.randomizeState(50);
        assertEquals("012345678", board.solveBeam(4));
        board.solveBeam(4);
        //set state to an unsolveable state
         board.setState("123456780");
         assertNotEquals("012345678", board.solveBeam(4));
    }
}