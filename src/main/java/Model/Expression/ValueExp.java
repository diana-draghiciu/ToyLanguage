package Model.Expression;

import Exceptions.MyException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.Type;
import Model.Value.Value;

public class ValueExp implements Exp {
    Value e;

    public ValueExp(Value v){this.e=v;}
    public Value getE(){return this.e;}

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer,Value> hp)  throws MyException {
        return e;
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return e.getType();
    }

    @Override
    public Exp deepCopy() {
        return new ValueExp(e.deepCopy());
    }

    @Override
    public String toString(){
        return e.toString();
    }
}
