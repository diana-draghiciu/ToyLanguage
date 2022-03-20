package Model.Statement;

import Exceptions.InvalidTypeException;
import Exceptions.MyException;
import Exceptions.VariableException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.RefType;
import Model.Type.Type;
import Model.Value.RefValue;
import Model.Value.Value;

public class wH implements IStmt{
    String var_name; //contains heap address
    Exp exp; //repr the new value that is going to be stored into the heap

    public wH(String var, Exp e){ this.var_name=var; this.exp=e;}
    public String getVarName(){return this.var_name;}
    public Exp getExp(){return this.exp;}

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // •first we check if var_name is a variable defined in SymTable, if its type is a Ref type and if the address from the RefValue associated in SymTable is a key in Heap.
        // If not, the execution is stopped with an appropriate error message.
        MyIHeap<Integer,Value> heap=state.getHeap();
        MyIDictionary<String, Value> symTbl = state.getSymTable();

        if(symTbl.isDefined(var_name)){
            Type typeId=(symTbl.lookup(var_name)).getType();
            if(typeId instanceof RefType) {
                // •Second the expression is evaluated and the result must have its type equal to the locationType of the var_name type.
                // If not, the execution is stopped with an appropriate message.
                Value val = exp.eval(symTbl,heap);
                if (val.getType().equals(((RefType) typeId).getInner())) {
                    // •Third we access the Heap using the address from var_name and that Heap entry is updated to the result of the expression evaluation
                    heap.update(((RefValue)symTbl.lookup(var_name)).getAddr(),val);
                } else
                    throw new VariableException("declared type of variable" + var_name + " and type of the assigned expression do not match");
            }
            else
                throw new InvalidTypeException("the variable type is not Ref type");
        }
        else
            throw new VariableException("the used variable" +var_name + " was not declared before");
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(var_name);
        Type typexp = exp.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new MyException("wH stmt: right hand side and left hand side have different types ");
    }

    @Override
    public IStmt deepCopy() {
        return new wH(var_name,exp.deepCopy());
    }

    @Override
    public String toString(){return "wH("+this.var_name+","+this.exp+")";}
}
