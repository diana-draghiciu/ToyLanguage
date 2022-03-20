package Repository;

import Exceptions.MyException;
import Model.PrgState;

import java.util.List;

public interface IRepo {
    void addPrg(PrgState prg) throws MyException;

    //returns the list of the program states.
    public List<PrgState> getLst();

    // replaces the existing list of program from the repository with one given as parameter in this method.
    void setLst(List<PrgState> newLst);

    void logPrgStateExec(PrgState prg) throws MyException;

    public PrgState getProgWithID(int id);
}
