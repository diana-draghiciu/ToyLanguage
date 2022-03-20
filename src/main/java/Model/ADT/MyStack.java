package Model.ADT;

import Exceptions.EmptyStackException;
import Exceptions.MyException;

import java.util.Stack;

public class MyStack<T> implements MyIStack<T> {
    Stack<T> stack;

    //Constructor
    public MyStack() {
        stack = new Stack<T>();
    }

    //Removes the object at the top of this stack and returns that object as the value of this function.
    @Override
    public T pop() throws MyException {
        try {
            return stack.pop();
        } catch (Exception ex) {
            throw new EmptyStackException("No element left to remove!");
        }
    }

    //Pushes an item onto the top of this stack.
    @Override
    public void push(T v) {
        stack.push(v);
    }

    //Tests if this stack is empty.
    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public Stack<T> getContent() {
        return this.stack;
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        for (int i = this.stack.size() - 1; i >= 0; i--)
            txt.append(this.stack.get(i)).append("\n");
        return txt.toString();
    }
}
