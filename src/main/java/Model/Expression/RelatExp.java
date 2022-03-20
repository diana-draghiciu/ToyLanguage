package Model.Expression;

import Exceptions.InvalidOperationException;
import Exceptions.InvalidTypeException;
import Exceptions.MyException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.Value;

public class RelatExp implements Exp {
    Exp e1;
    Exp e2;
    String op; //<,<=,==,!=,>,>=

    public RelatExp(Exp e1, Exp e2, String op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    public Exp getE1() {
        return this.e1;
    }

    public Exp getE2() {
        return this.e2;
    }

    public String getOp() {
        return this.op;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer, Value> hp) throws MyException {
        Value v1, v2;
        v1 = e1.eval(tbl, hp);
        if (v1.getType().equals(new IntType())) {
            v2 = e2.eval(tbl, hp);
            if (v2.getType().equals(new IntType())) {
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;
                int n1, n2;
                n1 = i1.getValue();
                n2 = i2.getValue();

                return switch (op) {
                    case "<" -> new BoolValue(n1 < n2);
                    case "<=" -> new BoolValue(n1 <= n2);
                    case "==" -> new BoolValue(n1 == n2);
                    case "!=" -> new BoolValue(n1 != n2);
                    case ">" -> new BoolValue(n1 > n2);
                    case ">=" -> new BoolValue(n1 >= n2);
                    default -> throw new InvalidOperationException("Invalid operation!");
                };
            } else
                throw new InvalidTypeException("second operand is not an integer");
        } else
            throw new InvalidTypeException("first operand is not an integer");
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1 = e1.typecheck(typeEnv);
        typ2 = e2.typecheck(typeEnv);
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new BoolType();
            } else
                throw new MyException("second operand is not an integer");
        } else throw new MyException("first operand is not an integer");
    }

    @Override
    public String toString() {
        return e1.toString() + op + e2.toString();
    }

    @Override
    public Exp deepCopy() {
        return new RelatExp(e1.deepCopy(), e2.deepCopy(), op);
    }
}
