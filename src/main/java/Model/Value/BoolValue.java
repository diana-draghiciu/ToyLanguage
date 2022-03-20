package Model.Value;

import Model.Type.BoolType;
import Model.Type.Type;

public class BoolValue implements Value {
    private boolean value;

    //constructors, getters, setters
    public BoolValue(){this.value=false;}
    public BoolValue(boolean value) {
        this.value = value;
    }
    public boolean getValue() {
        return this.value;
    }
    public void setValue(boolean nv) {
        this.value = nv;
    }


    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public Value deepCopy() {
        return new BoolValue(value);
    }

    @Override
    public String toString(){
        return Boolean.toString(value);
    }

    @Override
    public boolean equals(Object another) {
        if(another instanceof BoolValue)
            return this.value==((BoolValue)another).value;
        return false;
    }
}
