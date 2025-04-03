package tqs.lab1_1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;


import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class _TqsStackTest {

    private TqsStack<String> stack;

    @BeforeEach
    public void setUp() {
        stack = new TqsStack<>();
    }

    @Test
    public void stackIsEmptyOnConstruction() {
        assertTrue(stack.isEmpty());
    }

    @Test
    public void stackHasSizeZeroOnConstruction() {
        assertEquals(0, stack.size());
    }

    @Test
    public void stackNotEmptyAfterPushes() {
        stack.push("one");
        stack.push("two");
        assertFalse(stack.isEmpty());
        assertEquals(2, stack.size());
    }

    @Test
    public void pushThenPopReturnsSameElement() {
        stack.push("element");
        String popped = stack.pop();
        assertEquals("element", popped);
    }

    @Disabled("desativado para testar cobertura")
    @Test
    public void pushThenPeekReturnsSameElementWithoutRemoving() {
        stack.push("top");
        String peeked = stack.peek();
        assertEquals("top", peeked);
        assertEquals(1, stack.size());
    }

    @Test
    public void afterNPopsStackIsEmptyAndHasSizeZero() {
        stack.push("a");
        stack.push("b");
        stack.push("c");
        stack.pop();
        stack.pop();
        stack.pop();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    public void popOnEmptyStackThrowsException() {
        assertThrows(NoSuchElementException.class, () -> {
            stack.pop();
        });
    }

    @Test
    public void peekOnEmptyStackThrowsException() {
        assertThrows(NoSuchElementException.class, () -> {
            stack.peek();
        });
    }
}