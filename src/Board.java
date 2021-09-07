
public class Board {

    Board(int size) {
        //TODO: Construct the Board and make sure it's a valid size
    }

    // TODO: setState <state>
    String setState(String state) {
        //if state is not null

            //then if state has the all the proper values for the specified puzzle size

                    //then go through each position and assign the correct value based on the input

                //else warn user about the puzzle possibly not working properly (log warning)

                //go through each position and assign the correct value based on the input
        
        //else

            //error: state given is null

    }

    // TODO: printState
    public String printState() {
        String state = "";
        //for each character in the string

            //if the counter < max counter

                //print character + “ “

            //else

                //print “/n” + character + “ “

        return state;
    }

    // TODO: move <direction>
    String move(String direction) {
        //If direction is not null

            //then if direction is a valid direction

                    //then swap blank with character in direction

                //else

                    //error: invalid direction
        //else

                //error: direction given is null

    }

    // TODO: randomizeState <n>
    String randomizeState(int n) {
        //Counter = n

        //lastDirection = “”

        //directionlist of north south east west

        //removedlist of “”

        //For I of n

            //If lastDirection is not “”

                //Remove last direction from directionlist, add to removedlist

                //Randomized routine(directionlist, removedlist)

            //else

                //randomized routine(directionlist, removedlist)

                //Reset directionlist and removed list


    }

    //randomizedroutine ( dlist, rlist)
    //If blank is touching wall
    //Remove directions that touch wall, add to removedlist
    //Pick a random number from 1-directionlist size
    //move(direction)
    //add directions from removedlist to directionlist, clear removedlist

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
