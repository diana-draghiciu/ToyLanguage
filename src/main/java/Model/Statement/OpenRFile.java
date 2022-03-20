package Model.Statement;

import Exceptions.InvalidFileException;
import Exceptions.InvalidKeyException;
import Exceptions.InvalidTypeException;
import Exceptions.MyException;
import Model.ADT.MyIDictionary;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.Type;
import Model.Value.StringValue;
import Model.Value.Value;
import Model.Type.StringType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenRFile implements IStmt{
    Exp exp;

    public OpenRFile(Exp exp){
        this.exp=exp;
    }
    public Exp getExp(){return this.exp;}

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl= state.getSymTable();
        MyIDictionary<StringValue, BufferedReader> fileTbl=state.getFileTable();

        //evaluate the exp and check whether its type is a StringType. If its type is not StringType, the execution is stopped with an appropriate message.
        Value val = exp.eval(symTbl,state.getHeap());
        if (val.getType().equals(new StringType())){
            //check whether the string value is not already a key in the FileTable. If it exists, the execution is stopped with an appropriate error message
            StringValue file=(StringValue) val;
            if(fileTbl.isDefined(file))
                throw new InvalidKeyException("The corresponding string value si already ih the fileTable");
            //open in Java the file having the name equals to the computed string value, using an instance of the BufferedReader class.
            // If the file does not exist or other IO error occurs the execution is stopped with an appropriate error message.
            // create a new entrance into the FileTable which maps the above computed string to the instance of the BufferedReader class created before.
            try{
                fileTbl.add(file,new BufferedReader(new FileReader(file.getValue())));
            }
            catch(IOException ex) {
                throw new InvalidFileException("File does not exist!");
            }
        }
        else{
            throw new InvalidTypeException("declared type of variable and type of the assigned expression do not match");
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typexp= exp.typecheck(typeEnv);
        if(typexp.equals(new StringType()))
            return typeEnv;
        else throw new MyException("OpenRFile: expression not a StringType");
    }

    @Override
    public String toString(){
        return "OpenRFile("+this.exp.toString()+")";
    }

    @Override
    public IStmt deepCopy() {
        return new OpenRFile(exp.deepCopy());
    }
}
