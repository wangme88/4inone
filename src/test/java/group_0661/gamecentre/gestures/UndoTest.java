package group_0661.gamecentre.gestures;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UndoTest {
    private Undo testUndoUnlimited;
    private Undo testUndoLimited;
    private int[] testObj = {1, 1};

    @Before
    public void setUp() {
        testUndoUnlimited = new Undo(3, true);
        testUndoLimited = new Undo(3, false);
    }

    @Test
    public void pop() {
        testUndoLimited.push(testObj);
        assertEquals(testObj, testUndoLimited.pop());
        assertEquals(0, testUndoLimited.getSize());
        for (int i = 0; i < 3; i++) {
            testUndoLimited.push(testObj);
        } for (int i = 0; i < 2; i++) {
            testUndoLimited.pop();
        }
        assertEquals(1, testUndoLimited.getSize());
    }

    @Test
    public void push() {
        for (int i = 0; i < 6; i++) {
            testUndoUnlimited.push(testObj);
        }
        assertEquals(6, testUndoUnlimited.getSize());
        for (int i = 0; i < 6; i++) {
            testUndoLimited.push(testObj);
        }
        assertEquals(3, testUndoLimited.getSize());
    }

    @Test
    public void getSize() {
        assertEquals(0, testUndoUnlimited.getSize());
        testUndoUnlimited.push(testObj);
        assertEquals(1, testUndoUnlimited.getSize());
        testUndoUnlimited.push(testObj);
        testUndoUnlimited.push(testObj);
        assertEquals(3, testUndoUnlimited.getSize());
        testUndoUnlimited.pop();
        assertEquals(2, testUndoUnlimited.getSize());
    }
}