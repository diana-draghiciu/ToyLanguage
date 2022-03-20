package Model.Statement;

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

public class NewHeapStmt implements IStmt {
    String var_name;
    Exp exp;

    public NewHeapStmt(String var, Exp e){ this.var_name=var; this.exp=e;}
    public String getVarName(){return this.var_name;}
    public Exp getExp(){return this.exp;}

    @Override
    public PrgState execute(PrgState state) throws MyException {
        //check whether var_name is a variable in SymTable and its type is a RefType.
        //If not, the execution is stopped with an appropriate error message.
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIHeap<Integer,Value> heap=state.getHeap();
        if (symTbl.isDefined(var_name)) {
            Type typId = (symTbl.lookup(var_name)).getType();
            if (typId instanceof RefType) {
                //Evaluate the expression to a value and then compare the type of the value to the locationType from the value associated to var_name in SymTable
                //If the types are not equal, the execution is stopped with an appropriate error message.
                Value val = exp.eval(symTbl,heap);
                if (val.getType().equals(((RefType) typId).getInner())) {
                    // Create a new entry in the Heap table such that a new key (new free address) is generated and it is associated to the result of the expression evaluation
                    int key=heap.freeKeyLocation();
                    heap.add(key,val);
                    //in SymTable update the RefValue associated to the var_name such that the new RefValue has the same locationType and the address is equal to the new key generated in the Heap at the previous step
                    symTbl.update(var_name,new RefValue(key,val.getType()));
                } else
                    throw new VariableException("declared type of variable " + var_name + " and locationType from the value associated to var_name do not match (RefType)");
            }
            else
                throw new VariableException("declared type of variable " + var_name + " and type of the assigned expression do not match (RefType)");
        } else throw new VariableException("the used variable" + var_name + " was not declared before");
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(var_name);
        Type typexp = exp.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else throw new MyException("NEW stmt: right hand side and left hand side have different types ");
    }

    @Override
    public IStmt deepCopy() {
        return new NewHeapStmt(var_name,exp.deepCopy());
    }

    @Override
    public String toString(){return "new("+this.var_name+","+this.exp+")";}
}
