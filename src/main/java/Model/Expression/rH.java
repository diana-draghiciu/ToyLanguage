package Model.Expression;

import Exceptions.InvalidKeyException;
import Exceptions.InvalidTypeException;
import Exceptions.MyException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.RefType;
import Model.Type.Type;
import Model.Value.RefValue;
import Model.Value.Value;

public class rH implements Exp {
    Exp exp;

    public rH(Exp e) {
        this.exp = e;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> hp) throws MyException {
        //•the expression must be evaluated to a RefValue. If not, the execution is stopped with an appropriate error message.
        Value val = exp.eval(tbl, hp);
        if (val instanceof RefValue) {
            //•Take the address component of the RefValue computed before and use it to access Heap table and return the value associated to that address.
            //If the address is not a key in Heap table, the execution is stopped with an appropriate error message.
            RefValue ref = (RefValue) val;
            if (!hp.isDefined(ref.getAddr()))
                throw new InvalidKeyException("Address not a key in Heap Table");
            return hp.lookup(ref.getAddr());
        } else
            throw new InvalidTypeException("The evaluated expression is not a ref Value!");
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ = exp.typecheck(typeEnv);
        if (typ instanceof RefType) {
            RefType reft = (RefType) typ;
            return reft.getInner();
        } else
            throw new MyException("the rH argument is not a Ref Type");
    }

    @Override
    public Exp deepCopy() {
        return new rH(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "rH(" + this.exp + ")";
    }
}
