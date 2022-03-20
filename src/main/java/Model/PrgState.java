package Model;

import Exceptions.EmptyStackException;
import Exceptions.MyException;
import Model.ADT.*;
import Model.Statement.IStmt;
import Model.Value.StringValue;
import Model.Value.Value;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.util.List;

public class PrgState {
    int id;
    //use a static field and a static synchronized method to manage the id
    private static int availableID=1;

    private static synchronized int getAvailableID(){
        int save= availableID;
        availableID++;
        return save;
    }

    MyIStack<IStmt> exeStack;
    MyIDictionary<String, Value> symTable;
    MyIDictionary<StringValue, BufferedReader> fileTable;  //filename (string) denotes path to file, StringValue-unique key
    MyIList<Value> out;
    IStmt originalProgram; //optional field, but good to have
    //Heap is a dictionary of mappings (address, content) where the address is an integer (the index of a location in the heap) while the content is a Value
    MyIHeap<Integer,Value> heap;
    MyIHeap<Integer, Pair<Integer, List<Integer>>> semaphore;

    //constructor
    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symtbl, MyIList<Value> ot, MyIDictionary<StringValue, BufferedReader> filetbl, MyIHeap<Integer,Value> heap,IStmt prg, MyIHeap<Integer,Pair<Integer,List<Integer>>> semaf) {
        id=PrgState.getAvailableID();
        exeStack = stk;       //Execution Stack (ExeStack): a stack of statements to execute the current program
        symTable = symtbl;    //Table of Symbols (SymTable): a table which keeps the variables values (int v=5; dict: v:4)
        out = ot;           //Output (Out): that keeps all the messages printed by the toy program
        fileTable = filetbl;
        this.heap=heap;
        this.semaphore=semaf;
        originalProgram = prg.deepCopy();  //recreate the entire original prg
        stk.push(prg);
    }

    @Override
    public String toString() {
        StringBuilder txt=new StringBuilder();
        txt.append(id);
        txt.append("\nExeStack:\n");
        txt.append(exeStack);
        txt.append("\nSymTable:\n");
        txt.append(symTable);
        txt.append("\nOut:\n");
        txt.append(out);
        txt.append("\nFileTable:\n");
        for(StringValue file:this.fileTable.getKeys()){
            txt.append(file).append("\n");
        }
        txt.append("Heap:\n");
        txt.append(heap);
        txt.append("Semaphore:\n");
        txt.append(semaphore);
        return txt.toString();
    }

    public MyIStack<IStmt> getStk() {
        return exeStack;
    }

    public MyIDictionary<String, Value> getSymTable() {
        return symTable;
    }

    public MyIList<Value> getOutList() {
        return out;
    }

    public MyIDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public MyIHeap<Integer,Value> getHeap(){return heap;}

    public int getId() {
        return id;
    }

    public MyIHeap<Integer, Pair<Integer, List<Integer>>> getSemaphore() {
        return this.semaphore;
    }

    // returns true when the exeStack is not empty and false otherwise.
    public boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public PrgState oneStep() throws MyException {
        if (exeStack.isEmpty())
            throw new EmptyStackException("prgstate stack is empty");
        IStmt crtStmt =exeStack.pop();
        return crtStmt.execute(this);
    }
}

