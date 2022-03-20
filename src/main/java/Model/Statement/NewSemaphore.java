package Model.Statement;

import Exceptions.InvalidTypeException;
import Exceptions.MyException;
import Exceptions.VariableException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.ADT.MyISemaphore;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class NewSemaphore implements IStmt{
    //newSemaphore(var,exp1)
    String var;
    Exp exp1;
    private static int newfreelocation=1;

    public NewSemaphore(String var, Exp e1){
        this.var=var;
        this.exp1=e1;
    }

    private static synchronized int freelocation(){
        return newfreelocation++;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIHeap<Integer, Value> heap= state.getHeap();
        MyIDictionary<String,Value> symtbl= state.getSymTable();
        // - evaluate the expression exp1 using SymTable1 and Heap1
        //and let be number1 the result of this evaluation
        //If number1 is not an integer then print an error and stop the execution.
        Value val1=exp1.eval(symtbl,heap);
        if(!val1.getType().equals(new IntType()))
            throw new InvalidTypeException("NewSemaphore: The expression evaluation is not of type INT");

        int number1=((IntValue) val1).getValue();

        MyIHeap<Integer, Pair<Integer, List<Integer>>> semaphore=state.getSemaphore();
        //SemaphoreTable2 = SemaphoreTable1 synchronizedUnion {newfreelocation->(number1,empty list)}
        int freeloc=freelocation();
        synchronized (state.getSemaphore())
        {
            semaphore.add(freeloc, new Pair<>(number1, new ArrayList<>()));
        }

        // if var exists in SymTable1 and has the type int then SymTable2 = update(SymTable1,var, newfreelocation) else throws an error
        if(symtbl.isDefined(var)){
            Type typId = (symtbl.lookup(var)).getType();
            if (typId.equals(new IntType())) {
                IntValue free = new IntValue(freeloc);
                symtbl.update(var, free);
            }
            else
                throw new InvalidTypeException("NewSemaphore: var not of type int");
        }
        else
            throw new VariableException("NewSemaphore: var doesn't exist in symtbl");

        //Note that you must use the lock mechanisms of the host language
        //Java over the SemaphoreTable in order to add a new semaphore to the table.
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        //Implement the method typecheck for the statement
        //createSemaphore(var, exp1) to verify if both var and exp1 have the type int.
        Type typevar = typeEnv.lookup(var);
        Type typexp = exp1.typecheck(typeEnv);
        if (typevar.equals(typexp) && typevar.equals(new IntType()))
            return typeEnv;
        else
            throw new MyException("NewSemaphore: typecheck error");
    }

    @Override
    public IStmt deepCopy() {
        return new NewSemaphore(var,exp1.deepCopy());
    }

    @Override
    public String toString(){
        return "NewSemaphore("+this.var+","+this.exp1.toString()+")";
    }
}
