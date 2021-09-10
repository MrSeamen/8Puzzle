import java.util.logging.Level;
import java.util.logging.Logger;

/*
    Class: Logger
    author: Simon Wang
    date: September 9, 2021
    Routines to log messages
 */

class LogClass {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public enum Methods {SETSTATE, PRINTSTATE, MOVE, RANDOMIZESTATE, SOLVEASTAR, SOLVEBEAM, MAXNODES}
    /**
     * Logs the messages for any errors or bad data.
     * @param message Message of log
     */
    public void log(Methods method, String message) {
        LOGGER.log(Level.INFO, message, method);
    }
}
