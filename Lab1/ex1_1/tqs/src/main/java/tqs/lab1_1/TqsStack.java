package tqs.lab1_1;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class TqsStack<T> {

    private final ArrayList<T> elements;

    public TqsStack() {
        elements = new ArrayList<>();
    }

    public void push(T item) {
        elements.add(item);
    }

    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return elements.remove(elements.size() - 1);
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return elements.get(elements.size() - 1);
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public T popTopN(int n) {
        if (n <= 0 || n > elements.size()) {
            throw new NoSuchElementException("Invalid n: " + n);
        }
        for (int i = 0; i < n - 1; i++) {
            elements.remove(elements.size() - 1);
        }
        return elements.remove(elements.size() - 1);
    }
}
