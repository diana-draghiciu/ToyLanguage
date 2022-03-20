package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIList;
import Model.Expression.Exp;
import Exceptions.MyException;
import Model.PrgState;
import Model.Type.Type;
import Model.Value.Value;

public class PrintStmt implements IStmt {
    Exp exp;
    public PrintStmt(Exp e){this.exp=e;}
    public Exp getExp(){return this.exp;}

    @Override
    public String toString(){
        return ("print(" +exp.toString()+")");
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIList<Value> list=state.getOutList();
        MyIDictionary<String, Value> symTbl= state.getSymTable();
        Value v=exp.eval(symTbl,state.getHeap());
        list.add(list.size(),v);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(exp.deepCopy());
    }
}
