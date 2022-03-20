package Repository;

import Exceptions.InvalidFileException;
import Exceptions.MyException;
import Model.PrgState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Repo implements IRepo {
    //List of ProgState objects. Each PrgState corresponds to a thread.
    private List<PrgState> lst;
    //The logFilePath is a new field of the Repository class which contains the path to the log text file.
    //This field is initialized by a string read from the keyboard using Scanner class.
    String logFilePath;

    //constructor, getter
    public Repo(PrgState prg,String logFilePath) {
        this.lst = new ArrayList<PrgState>();
        this.lst.add(0,prg);
        this.logFilePath = logFilePath;
    }

    public List<PrgState> getLst() {
        return this.lst;
    }

    public PrgState getProgWithID(int id){
        for(PrgState p : this.lst)
            if(p.getId() == id)
                return p;
        return null;
    }

    @Override
    public void setLst(List<PrgState> newLst) {
        this.lst=newLst;
    }

    @Override
    public void addPrg(PrgState prg) throws MyException {
        this.lst.add(this.lst.size(), prg);
    }

    @Override
    public void logPrgStateExec(PrgState prg) throws MyException {
        //using PrintWriter in append mode
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, true)));
            pw.println(prg);
        } catch (IOException e) {
            throw new InvalidFileException("Invalid file!");
        } finally {
            if (pw != null)
                pw.close();
        }
    }
}
