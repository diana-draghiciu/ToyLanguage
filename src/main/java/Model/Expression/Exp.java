package Model.Expression;

import Exceptions.MyException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.Type;
import Model.Value.Value;

public interface Exp {
    Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Integer,Value> hp) throws MyException;
    Type typecheck(MyIDictionary<String,Type> typeEnv) throws MyException;
    public Exp deepCopy();
}
