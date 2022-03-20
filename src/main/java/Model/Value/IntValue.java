package Model.Value;

import Model.Type.IntType;
import Model.Type.Type;

public class IntValue implements Value {
    int val;

    //constructor, getters, setters
    public IntValue(){this.val=0;}
    public IntValue(int v){val=v;}
    public int getValue() {return val;}
    public void setVal(int nv){this.val=nv;}

    @Override
    public String toString() {return Integer.toString(val);}
    public Type getType() { return new IntType();}

    @Override
    public Value deepCopy() {
        return new IntValue(val);
    }

    @Override
    public boolean equals(Object another) {
        if(another instanceof IntValue) {
            return this.val==((IntValue)another).val;
        }
        return false;
    }

}
