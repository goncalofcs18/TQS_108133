package lab1.lab1_1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import lab1.lab1_1.TqsStack;    

import org.junit.jupiter.api.Test;

public class TqsStackTests {

    private TqsStack<String> myStack;
    private TqsStack<String> myStack_bounded;

    @BeforeEach
    public void start() {
        this.myStack = new TqsStack<>();
        this.myStack_bounded = new TqsStack<>(5);
    }

    @AfterEach
    public void end() {
        this.myStack = null;
    }

    @DisplayName("a")
    @Test
    public void emptyOnContruction() {
        assertTrue(myStack.isEmpty(), "Stack is not empty on construction");
    }

    @DisplayName("b")
    @Test
    public void SizeZeroOnConstruction() {
        assertEquals(0, myStack.size(), "Stack size is not zero" );
    }

    @DisplayName("c")
    @Test
    public void pushOneIsNotEmpty() {
        myStack.push("Maria");

        assertFalse( myStack.isEmpty(), "Stack is empty after one push");
        assertEquals(1, myStack.size(), "Stack size is not correct");
    }

    @DisplayName("d")
    @Test
    public void TestCase1() {
        myStack.push("Maria");
        myStack.push("João");

        Object x = myStack.pop();

        assertEquals("João", x, "Pop does not return the last element");
        assertEquals(1, myStack.size(), "Stack size is not correct after pop");
    }

    @DisplayName("e")
    @Test
    public void TestCase2() {
        myStack.push("Maria");
        myStack.push("João");

        Object x = myStack.peek();

        assertEquals("João", x, "Peek does not return the last element");
        assertEquals(2, myStack.size(), "Stack size is not correct after peek");
    }

    @DisplayName("f")
    @Test
    public void TestCase3() {
        myStack.push("Maria");
        myStack.push("João");

        myStack.pop();
        myStack.pop();

        assertTrue(myStack.isEmpty(), "Stack is not empty after pops");
        assertEquals(0, myStack.size(), "Stack size is not 0 after pops");

    }

    @DisplayName("g")
    @Test
    public void exceptionPop() {
        assertThrows(NoSuchElementException.class, () -> {myStack.pop();}, "NoSuchElementException not thrown after pop on empty stack.");
    }

    @DisplayName("h")
    @Test
    public void exceptionPeek() {
        assertThrows(NoSuchElementException.class, () -> {
            myStack.peek();
            
        }
            , "NoSuchElementException not thrown after peek on empty stack.");
    }

    @DisplayName("i")
    @Test
    public void bounded() {
        for(int i = 0; i< myStack_bounded.getBound(); i++){
            this.myStack_bounded.push("Maria");
        }

        assertThrows(IllegalStateException.class, () -> {myStack_bounded.push("João");}, "IllegalStateException not thrown after pushed more objects than limit.");

    }
    
}
