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

    @Test
    public void printStateTest() {
        //set board as a custom arrangement, then test print
        board.setState("574638201");
        assertEquals("5 7 4 \n6 3 8 \n2 0 1 ", board.printState());
        //set board as arrangement in order, then test print
        board.setState("012345678");
        assertEquals("0 1 2 \n3 4 5 \n6 7 8 ", board.printState());
        //set board as above with one tiny change, then test print
        board.setState("312045678");
        assertEquals("3 1 2 \n0 4 5 \n6 7 8 ", board.printState());
    }

    @Test
    public void randomizeStateTest() {
        Random random = new Random();
        board.setState("012345678");
        //randomize board, check to make sure board isnt the same as regular
        assertNotEquals("012345678", board.randomizeState(random.nextInt(19)+1));
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
/*

    @Test
    void solveAStar() {
        //set state to a simple solveable state

        //set state to a more complex but solveable state

        //set state to an unsolveable state
    }

    @Test
    void solveBeam() {
        //set state to a simple solveable state

        //set state to a more complex but solveable state

        //set state to an unsolveable state
    }*/
}