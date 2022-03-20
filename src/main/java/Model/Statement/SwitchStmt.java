package Model.Statement;

import Exceptions.MyException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Model.Expression.RelatExp;
import Model.PrgState;
import Model.Type.Type;

import static Model.ADT.MyDictionary.cloneType;

public class SwitchStmt implements IStmt{
    Exp e,e1,e2;
    IStmt stmt1,stmt2,stmt3;

    public SwitchStmt(Exp e, Exp e1, Exp e2, IStmt s1, IStmt s2, IStmt s3){
        this.e=e;
        this.e1=e1;
        this.e2=e2;
        this.stmt1=s1;
        this.stmt2=s2;
        this.stmt3=s3;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException { //switch(exp) (case exp1: stmt1) (case exp2: stmt2) (default: stmt3)
        MyIStack<IStmt> stk=state.getStk();

        //- create the following statement: if(exp==exp1) then stmt1 else (if (exp==exp2) then stmt2 else stmt3)
        IStmt newstmt= new IfStmt(new RelatExp(e,e1,"=="),stmt1,new IfStmt(new RelatExp(e,e2,"=="),stmt2,stmt3));
        //- push the new statement on the stack
        stk.push(newstmt);

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        //The typecheck method of switch statement verifies if exp, exp1 and exp2 have the same type and also typecheck the statements stmt1, stmt2 and stmt3.
        Type typexp=e.typecheck(typeEnv);
        Type typexp1=e1.typecheck(typeEnv);
        Type typexp2=e2.typecheck(typeEnv);
        if (typexp.equals(typexp1) && typexp1.equals(typexp2)) {
            stmt1.typecheck(cloneType(typeEnv));
            stmt2.typecheck(cloneType(typeEnv));
            stmt3.typecheck(cloneType(typeEnv));
            return typeEnv;
        }
        else
            throw new MyException("Switch: The expressions do not have the same type");
    }

    @Override
    public IStmt deepCopy() {
        return new SwitchStmt(e.deepCopy(),e1.deepCopy(),e2.deepCopy(),stmt1.deepCopy(),stmt2.deepCopy(),stmt3.deepCopy());
    }

    @Override
    public String toString(){
        return "switch("+this.e+") (case "+this.e1+": "+this.stmt1+") (case "+this.e2+": "+this.stmt2+") (default: "+this.stmt3+")";
    }
}
