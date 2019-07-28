package group_0661.gamecentre.gestures;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The undo system specific to the board.
 */
public class Undo implements Serializable, Undoable<int[]>{

    /**
     * The arrayList of states stored as an array of integers.
     */
    private ArrayList<int[]> States;

    /**
     * The maximum number of undo calls permitted after each move.
     */
    private int undoLimit;

    /**
     * Check if the number of undo calls is unlimited.
     */
    private boolean limit;

    /**
     * A new undo system that keeps track of previously made moves.
     *

     * @param undoLimit the number of permitted undo calls.
     * @param unlimited check if there is unlimited number of undo calls.
     */
    public Undo(int undoLimit, boolean unlimited) {
        States = new ArrayList<>();
        this.undoLimit = undoLimit;
        this.limit = unlimited;
    }

    /**
     * Remove the first element in the arrayList.
     *
     * @return the integer array for the previous move.
     */
    public int[] pop() {
        return States.remove(0);
    }

    /**
     * Add a new previous state to the front of the arrayList, remove the last state in the arrayList
     * if exceeds limit.
     *
     * @param obj the state being pushed to the stack.
     */
    public void push(int[] obj) {
        States.add(0, obj);
        if (States.size() > undoLimit & !limit) {
            States.remove(undoLimit);
        }
    }

    /**
     * Get the size of array list
     *
     * @return the number of elements in the arrayList
     */
    public int getSize() {
        return States.size();
    }

}