package Model.Statement;

import Exceptions.InvalidKeyException;
import Exceptions.InvalidTypeException;
import Exceptions.MyException;
import Model.ADT.MyIDictionary;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.StringType;
import Model.Type.Type;
import Model.Value.StringValue;
import Model.Value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFile implements IStmt{
    Exp exp;

    public CloseRFile(Exp exp){this.exp=exp;}
    public Exp getExp(){return this.exp;}

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl=state.getSymTable();
        MyIDictionary<StringValue, BufferedReader> fileTbl=state.getFileTable();

        //evaluate exp to a value that must be a string. If any error occurs then terminate the execution with an appropriate error message.
        Value val=exp.eval(symTbl, state.getHeap());
        if(val.getType().equals(new StringType())){
            try {
                //Use the value (computed at the previous step) to get the entry into the FileTable and get the associated BufferedReader object.
                BufferedReader buff = fileTbl.lookup((StringValue) val);
                //• call the close method of the BufferedReader object
                //• delete the entry from the FileTable
                buff.close();
                fileTbl.remove((StringValue) val);
            }
            catch(MyException | IOException ex){
                // If there is not any entry in FileTable for that value we stop the execution with an appropriate error message.
                throw new InvalidKeyException("Key not found!");
            }
        }
        else{
            throw new InvalidTypeException("The evaluated expression is not a string!");
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp= exp.typecheck(typeEnv);
        if(typexp.equals(new StringType()))
            return typeEnv;
        else throw new MyException("CloseRFile: expression not a StringType");
    }

    @Override
    public String toString(){
        return "CloseRFile("+this.exp.toString()+")";
    }

    @Override
    public IStmt deepCopy() {
        return new CloseRFile(exp.deepCopy());
    }
}
