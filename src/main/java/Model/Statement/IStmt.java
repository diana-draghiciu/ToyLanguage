package Model.Statement;

import Exceptions.MyException;
import Model.ADT.MyIDictionary;
import Model.PrgState;
import Model.Type.Type;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;       //which is the execution method for a statement.
    MyIDictionary<String, Type> typecheck(MyIDictionary<String,Type> typeEnv) throws MyException;
    public IStmt deepCopy();
}
