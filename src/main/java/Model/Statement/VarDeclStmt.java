package Model.Statement;

import Exceptions.VariableException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Exceptions.MyException;
import Model.PrgState;
import Model.Type.Type;
import Model.Value.Value;

public class VarDeclStmt implements IStmt {
    String name;
    Type type;

    public VarDeclStmt(String name, Type type){this.name=name; this.type=type; }//this.type.defaultValue();}
    public String getName(){return this.name;}
    public Type getType(){return this.type;}

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk=state.getStk();
        MyIDictionary<String, Value> symTbl= state.getSymTable();
        if(symTbl.isDefined(name))
            throw new VariableException("Variable is already declared");
        else
            symTbl.add(name,type.defaultValue());
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.add(name,type);
        return typeEnv;
    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(name,type.deepCopy());
    }

    @Override
    public String toString(){
        return this.type.toString()+" "+this.name;
    }
}
