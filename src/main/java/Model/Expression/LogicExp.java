package Model.Expression;

import Exceptions.InvalidOperationException;
import Exceptions.InvalidTypeException;
import Exceptions.MyException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.Value;

public class LogicExp implements Exp {
    Exp e1;
    Exp e2;
    String op; // & |

    //constructor, getters
    LogicExp(Exp e1,Exp e2,String op){ this.e1=e1; this.e2=e2; this.op=op;}
    public Exp getE1(){return this.e1;}
    public Exp getE2(){return this.e2;}
    public String getOp(){return  this.op;}

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap<Integer,Value> hp) throws MyException {
            Value nr1=e1.eval(tbl,hp);
            if(nr1.getType().equals(new BoolType())){
                Value nr2=e2.eval(tbl,hp);
                if(nr2.getType().equals(new BoolType())){
                    BoolValue n1=(BoolValue) nr1;
                    BoolValue n2=(BoolValue) nr2;
                    if(op.equals("&")) {
                        return new BoolValue(n1.getValue() && n2.getValue());
                    }
                    else if(op.equals("|"))
                        return new BoolValue(n1.getValue() || n2.getValue());
                    else
                        throw new InvalidOperationException("Invalid boolean operation!");
                }
                else
                    throw new InvalidTypeException("Operand2 is not boolean");
            }
            else
                throw new InvalidTypeException("Operand1 is not boolean");
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typ1, typ2;
        typ1=e1.typecheck(typeEnv);
        typ2=e2.typecheck(typeEnv);
        if (typ1.equals(new BoolType())) {
            if (typ2.equals(new BoolType())) {
                return new BoolType();
            }
            else
                throw new MyException("second operand is not a boolean");
        }
        else throw new MyException("first operand is not a boolean");
    }

    @Override
    public Exp deepCopy() {
        return new LogicExp(this.e1.deepCopy(),this.e2.deepCopy(),this.op);
    }

    @Override
    public String toString(){
        return this.e1.toString()+this.op+this.e2.toString();
    }
}
