package Model.Statement;

import Exceptions.InvalidTypeException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Exceptions.MyException;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.Value;

import static Model.ADT.MyDictionary.cloneType;

public class IfStmt implements IStmt {
    Exp exp;
    IStmt thenS;
    IStmt elseS;

    //constructor
    public IfStmt(Exp e, IStmt t, IStmt el) {
        exp=e;
        thenS=t;
        elseS=el;
    }
    //getters
    public Exp getExp(){return this.exp;}
    public IStmt getThenS(){return this.thenS;}
    public IStmt getElseS(){return this.elseS;}

    @Override
    public String toString(){ return "(IF("+ exp.toString()+") THEN(" +thenS.toString()  +")ELSE("+elseS.toString()+"))";}

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk=state.getStk();
        MyIDictionary<String, Value> symTbl= state.getSymTable();

        Value cond=exp.eval(symTbl, state.getHeap());
        BoolValue bCond=(BoolValue) cond;

        if(!cond.getType().equals(new BoolType())){
            throw new InvalidTypeException("Conditional expression is not boolean");
        }
        else if(bCond.getValue()){
            stk.push(thenS);
        }
        else{
            stk.push(elseS);
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp=exp.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            thenS.typecheck(cloneType(typeEnv));
            elseS.typecheck(cloneType(typeEnv));
            return typeEnv;
        }
        else
            throw new MyException("The condition of IF has not the type bool");
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(exp.deepCopy(),thenS.deepCopy(),elseS.deepCopy());
    }

}
