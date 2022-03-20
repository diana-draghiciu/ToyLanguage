package Model.ADT;

import Exceptions.InvalidIndexException;
import Exceptions.MyException;

import java.util.ArrayList;
import java.util.Vector;

public class MyList<T> implements MyIList<T> {
    Vector<T> list;

    //Constructor
    public MyList()
    {
        this.list = new Vector<>();
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public Vector<T> getContent() {
        return this.list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void add(int index, T element) throws MyException{
        try {
            list.add(index, element);
        }
        catch(Exception ex){
            throw new InvalidIndexException(ex.getMessage());
        }
    }

    @Override
    public T remove(int index) throws MyException{
        if(index>=this.size() && index<0)
            throw new InvalidIndexException("Index out of range!");
        return list.remove(index);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString(){
        StringBuilder txt=new StringBuilder();
        this.list.forEach(T->txt.append(T).append("\n"));
        return txt.toString();
    }
}
