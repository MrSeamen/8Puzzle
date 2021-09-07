import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    public void setUp() throws Exception {
        board = new Board(3);
    }

    public void setStateTest() {
        //set board as a custom arrangement

        //set board as arrangement in order

        //set board as above with one tiny change
    }

    public void printStateTest() {
        //set board as a custom arrangement, then test print

        //set board as arrangement in order, then test print

        //set board as above with one tiny change, then test print
    }

    public void randomizeStateTest() {
        //randomize board, check to make sure board isnt the same as regular
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