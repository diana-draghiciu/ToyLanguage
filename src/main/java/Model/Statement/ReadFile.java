package Model.Statement;

import Exceptions.InvalidKeyException;
import Exceptions.InvalidTypeException;
import Exceptions.MyException;
import Exceptions.VariableException;
import Model.ADT.MyIDictionary;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.IntType;
import Model.Type.StringType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStmt {
    String var_name;
    Exp exp;

    //constructor, getters
    public ReadFile(String var_name,Exp exp){
        this.var_name=var_name; this.exp=exp;
    }
    public String getName(){return this.var_name;}
    public Exp getExp(){return this.exp;}

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<StringValue, BufferedReader> fileTbl=state.getFileTable();
        MyIDictionary<String, Value> symTbl= state.getSymTable();

        //check whether var_name is defined in SymTable
        if(symTbl.isDefined(var_name)){
            //and its type is int. If not, the execution is stopped with an appropriate error message.
            Type typId = (symTbl.lookup(var_name)).getType();
            if(typId.equals(new IntType())){
                //evaluate exp to a value that must be a string value. If any error occurs then terminate the execution with an appropriate error message.
                Value val = exp.eval(symTbl,state.getHeap());
                if(val.getType().equals(new StringType())){
                    //using the previous step value we get the BufferedReader object associated in the FileTable
                    //If there is not any entry associated to this value in the FileTable we stop the execution with an appropriate error message.
                    try {
                        BufferedReader reader=fileTbl.lookup((StringValue) val);
                        // Reads a line from the file using readLine method of the BufferedReader.
                        String line=reader.readLine();
                        // If line is null creates a zero int value. Otherwise translate the returned String into an int value (using Integer.parseInt(String)).
                        IntValue readVal;
                        if(line==null)
                            readVal=new IntValue(0);
                        else
                            readVal=new IntValue(Integer.parseInt(line));
                        // Update SymTable such that the var_name is mapped to the int value computed at the previous step.
                        symTbl.update(var_name,readVal);
                    } catch (IOException e) {
                        throw new InvalidKeyException("Key not found!");
                    }
                }
                else{
                    throw new InvalidTypeException("declared type of variable "+exp+" and type of the assigned expression do not match");
                }
            }
            else
                throw new InvalidTypeException("declared type of variable "+var_name+" and type of the assigned expression do not match");
        }
        else{
            throw new VariableException("the used variable " +var_name + " was not declared before");
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.lookup(var_name);
        Type typexp= exp.typecheck(typeEnv);
        if(typexp.equals(new StringType())){
            if(typevar.equals(new IntType())){
                return typeEnv;
            }
            else throw new MyException("ReadFile: variable not an IntType");
        }
        else throw new MyException("ReadFile: expression not a StringType");
    }

    @Override
    public String toString(){
        return "readFile("+this.var_name+","+this.exp.toString()+")";
    }

    @Override
    public IStmt deepCopy() {
        return new ReadFile(this.var_name,this.exp.deepCopy());
    }
}
