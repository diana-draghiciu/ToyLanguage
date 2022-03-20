package Model.Statement;


import Exceptions.MyException;
import Exceptions.VariableException;
import Model.*;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Model.Type.Type;
import Model.Value.Value;

public class AssignStmt implements IStmt {
    String id;
    Exp exp;

    //constructor, getters
    public AssignStmt(String id,Exp ex){ this.exp=ex; this.id=id;}
    public String getId(){return this.id;}
    public Exp getExp(){return this.exp;}

    @Override
    public String toString(){ return id+"="+ exp.toString(); } //x=4

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk=state.getStk();
        MyIDictionary<String, Value> symTbl= state.getSymTable();
        if (symTbl.isDefined(id)) {
            Value val = exp.eval(symTbl, state.getHeap());

            Type typId = (symTbl.lookup(id)).getType();
            if (val.getType().equals(typId))
                symTbl.update(id, val);
            else
                throw new VariableException("declared type of variable" + id + " and type of the assigned expression do not match");
        }
        else throw new VariableException("the used variable" +id + " was not declared before");
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(id);
        Type typexp = exp.typecheck(typeEnv);
        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types ");
    }

    @Override
    public IStmt deepCopy() {
        return new AssignStmt(id,exp.deepCopy());
    }
    //...
}
