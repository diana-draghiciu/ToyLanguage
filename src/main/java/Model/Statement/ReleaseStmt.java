package Model.Statement;

import Exceptions.InvalidTypeException;
import Exceptions.MyException;
import Exceptions.VariableException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.ADT.MyISemaphore;
import Model.PrgState;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;
import javafx.util.Pair;

import java.util.List;

public class ReleaseStmt implements IStmt{
    private String var;

    public ReleaseStmt(String var){
        this.var=var;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symtbl=state.getSymTable();
        MyIHeap<Integer, Pair<Integer, List<Integer>>> semaphore=state.getSemaphore();

        // If var is not in SymTable or has not int type then print an error message and terminate the execution.
        if(!symtbl.isDefined(var))
            throw new VariableException("Release: Variable not defined in symtbl");

        //- foundIndex=lookup(SymTable,var).

        Value typId=symtbl.lookup(var);
        if (!typId.getType().equals(new IntType()))
            throw new InvalidTypeException("Release: Variable not of type INT");

        int foundIndex=((IntValue)typId).getValue();

       // - if foundIndex is not an index in the SemaphoreTable then print an error message and terminate the execution
        if(!semaphore.isDefined(foundIndex))
            throw new VariableException("Release: Found Index not defined in semaphore");
        else{
            // else
            // - retrieve the entry for that foundIndex, as SemaphoreTable[foundIndex]== (N1,List1)
            Pair<Integer, List<Integer>> semaphoreValue = semaphore.getValueContent(foundIndex);
            List<Integer> List1 = semaphoreValue.getValue();
            Integer N1 = semaphoreValue.getKey();
            //- if(the identifier of the current PrgState is in List1) then
            if(List1.contains(state.getId()))
            {
                //- remove the identifier of the current PrgState from List1
                List1.remove(Integer.valueOf(state.getId()));
            }
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        // Implement the method typecheck for the statement
        //release(var) to verify if var has the type int.
        Type typevar = typeEnv.lookup(var);
        if(typevar.equals(new IntType()))
            return typeEnv;
        else
            throw new InvalidTypeException("ReleaseStmt: Var is not of type int");
    }

    @Override
    public IStmt deepCopy() {
        return new ReleaseStmt(var);
    }

    @Override
    public String toString(){
        return "Release("+this.var+")";
    }
}
