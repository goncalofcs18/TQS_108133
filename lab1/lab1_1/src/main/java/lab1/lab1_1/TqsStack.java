package lab1.lab1_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.LinkedList;

@SpringBootApplication
public class TqsStack <T> {

	private LinkedList <T> stack;
	private Integer bound = null;

	public TqsStack() {
		this.stack = new LinkedList<T>();
	}

	public TqsStack(int bound) {
		this.stack = new LinkedList<T>();
		this.bound = bound;
	}

	public void push(T x){
		this.stack.addLast(x);
        if (this.bound != null && this.stack.size() > this.bound) {
            throw new IllegalStateException();
        }
	};

	public T pop(){
		T x = this.stack.getLast();
		this.stack.removeLast();
		return x;
	};

	public T peek(){
		T x = this.stack.getLast();
		return x;
	};

	public boolean isEmpty(){
		return this.stack.isEmpty();
	};

	public int size(){
		return this.stack.size();
	};

	public int getBound(){
		return this.bound;
	}

}

