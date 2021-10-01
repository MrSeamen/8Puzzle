import java.util.Scanner;

public class InputReader {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ReadFile fileReader = new ReadFile();
        Board puzzle = new Board(9);

        System.out.println("Enter Directory and File name with file extension .txt");
        System.out.println("*Example*: C:\\Users\\userName\\textFile.txt");
        String fileName = scan.nextLine();
        System.out.println("Executing following commands.");
        fileReader.readFile(fileName);
        System.out.println();

        for (String s : fileReader.list) {
            if (s.startsWith("setState")) {
                System.out.println("Setting State...");
                puzzle.setState(s.substring(9).replace(" ", "").replace("b", "0"));
            }
            else if (s.startsWith("printState")) {
                System.out.println("Printing State...");
                puzzle.printState();
            }
            else if (s.startsWith("move")) {
                System.out.println("Moving...");
                Board.Direction direction = null;
                switch(s.substring(5)) {
                    case "up" :
                        direction = Board.Direction.NORTH;
                        break;
                    case "down" :
                        direction = Board.Direction.SOUTH;
                        break;
                    case "right" :
                        direction = Board.Direction.EAST;
                        break;
                    case "left" :
                        direction = Board.Direction.WEST;
                        break;
                }
                puzzle.move(direction);
            }
            else if (s.startsWith("randomizeState")) {
                System.out.println("Randomizing...");
                puzzle.randomizeState(Integer.parseInt(s.substring(15)));
            }
            else if (s.startsWith("solve A-star")) {
                System.out.println("Using A-star search to solve...");
                puzzle.solveAStar(Integer.parseInt(s.substring(13)));
            }
            else if (s.startsWith("solve beam")) {
                System.out.println("Using beam search to solve...");
                puzzle.solveBeam(Integer.parseInt(s.substring(11)));
            }
            else if (s.startsWith("maxNodes")) {
                System.out.println("Setting max nodes allowed: " + s.substring(9));
                puzzle.maxNodes(Integer.parseInt(s.substring(9)));
            }
            else {
                throw new IllegalArgumentException("Unknown command.");
            }
        }
    }
}