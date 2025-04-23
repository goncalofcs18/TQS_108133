package tqs.lab5;

import java.util.Stack;

public class RpnCalculator {
    private Stack<Integer> stack = new Stack<>();

    public void push(Object arg) {
        if (arg instanceof Integer) {
            stack.push((Integer) arg);
        } else if (arg instanceof String) {
            String op = (String) arg;
            int b = stack.pop();
            int a = stack.pop();
            switch (op) {
                case "+" -> stack.push(a + b);
                case "-" -> stack.push(a - b);
                case "*" -> stack.push(a * b);
                default -> throw new UnsupportedOperationException("Unsupported operation: " + op);
            }
        }
    }

    public Number value() {
        return stack.peek();
    }
}
