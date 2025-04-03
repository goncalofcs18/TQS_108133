package tqs.lab1_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TqsStackAITest {

    private TqsStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new TqsStack<>();
    }

    @Test
    void testPushAndSize() {
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.size());
    }

    @Test
    void testIsEmptyAfterPushAndPop() {
        assertTrue(stack.isEmpty());
        stack.push(42);
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test
    void testPopReturnsCorrectElement() {
        stack.push(100);
        stack.push(200);
        assertEquals(200, stack.pop());
        assertEquals(100, stack.pop());
    }

    @Test
    void testPeekReturnsTopWithoutRemoving() {
        stack.push(9);
        stack.push(99);
        assertEquals(99, stack.peek());
        assertEquals(2, stack.size());
    }

    @Test
    void testPopOnEmptyThrows() {
        assertThrows(NoSuchElementException.class, () -> stack.pop());
    }

    @Test
    void testPeekOnEmptyThrows() {
        assertThrows(NoSuchElementException.class, () -> stack.peek());
    }

    @Test
    void testPopTopNReturnsCorrectNthElement() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        int result = stack.popTopN(3); // Deve descartar 5, 4 e devolver 3
        assertEquals(3, result);
        assertEquals(2, stack.size()); // Restam 1 e 2
    }
}
