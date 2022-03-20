package Model.Statement;

import Exceptions.MyException;
import Model.ADT.MyHeap;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.ADT.MyStack;
import Model.PrgState;
import Model.Type.Type;

import static Model.ADT.MyDictionary.cloneDict;
import static Model.ADT.MyDictionary.cloneType;

public class ForkStmt implements IStmt{
    IStmt stmt;

    public ForkStmt(IStmt s){this.stmt=s;}

    @Override
    public PrgState execute(PrgState state) throws MyException {
        //ExeStack1={fork(Stmt1) | Stmt2|Stmt3|....}
        MyIStack<IStmt> stk=new MyStack<IStmt>();
        return new PrgState(stk,cloneDict(state.getSymTable()),state.getOutList(),state.getFileTable(),state.getHeap(),stmt, state.getSemaphore());
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        stmt.typecheck(cloneType(typeEnv));
        return typeEnv;
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(stmt.deepCopy());
    }

    @Override
    public String toString(){
        return "fork("+stmt.toString()+")";
    }
}
