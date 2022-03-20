package Model.Statement;

import Exceptions.InvalidTypeException;
import Exceptions.MyException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.Value;

import static Model.ADT.MyDictionary.cloneType;

public class WhileStmt implements IStmt {
    Exp exp;
    IStmt stmt;

    public WhileStmt(Exp exp, IStmt s) {
        this.exp = exp;
        this.stmt = s;
    }

    public Exp getExp() {
        return exp;
    }

    public IStmt getStmt() {
        return stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getStk();
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        Value val = exp.eval(symTbl, state.getHeap());
        //If exp1 is evaluated to BoolValue then
        if (val.getType().equals(new BoolType())) {
            BoolValue bCond = (BoolValue) val;
            //If exp1 is evaluated to True Stack2={Stmt1 | while (exp1) Stmt1 | Stmt2|...}
            if (bCond.getValue()) {
                stk.push(this);
                stk.push(stmt);
            }
        } else
            throw new InvalidTypeException("condition exp is not a boolean");
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp=exp.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            stmt.typecheck(cloneType(typeEnv));
            return typeEnv;
        }
        else
            throw new MyException("The condition of WHILE has not the type bool");
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(exp.deepCopy(),stmt.deepCopy());
    }

    @Override
    public String toString(){
        return "(while("+this.exp +")"+this.stmt+")";
    }
}
