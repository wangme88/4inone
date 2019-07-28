package group_0661.gamecentre.gestures;

/**
 * Interface implemented for games that support calls.
 */
interface Undoable<T> {

    /**
     * Remove the first element from stack of previous states
     *
     * @return the stored slidingtiles state
     */
    public T pop();

    /**
     * Add slidingtiles state to the stack of previous states
     *
     * @param obj the slidingtiles state to be added to the stack
     */
    public void push(T obj);
}
