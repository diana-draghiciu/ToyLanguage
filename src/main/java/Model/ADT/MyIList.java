package Model.ADT;

import Exceptions.MyException;

import java.util.ArrayList;
import java.util.Vector;

public interface MyIList<T>{

    //Returns the element at the specified position in this list.
    public T get(int index);

    public Vector<T> getContent();

    //Returns the number of elements in this list.
    public int size();

    //Inserts the specified element at the specified position in this list.
    public void add(int index, T element) throws MyException;

    //Removes the element at the specified position in this list.
    public T remove(int index) throws MyException;

    //Returns true if this list contains no elements.
    public boolean isEmpty();

    public String toString();
}
