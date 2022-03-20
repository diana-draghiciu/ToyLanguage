package Model.Expression;

import Exceptions.MyException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.Type;
import Model.Value.Value;

public class VarExp implements Exp {
    String id;

    public VarExp(String id){this.id=id;}
    public String getId(){return this.id;}

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer,Value> hp)  throws MyException
    {return tbl.lookup(id);}

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }

    @Override
    public Exp deepCopy() {
        return new VarExp(id);
    }

    @Override
    public String toString(){
        return id;
    }
}
