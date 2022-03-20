package Model.Value;

import Model.Type.RefType;
import Model.Type.Type;

public class RefValue implements Value {
    int address;
    Type locationType;

    public RefValue(int addr, Type loc){this.address=addr; this.locationType=loc;}
    public int getAddr() {return address;}
    public RefType getType() { return new RefType(this.locationType);}

    @Override
    public Value deepCopy() {
        return new RefValue(address,locationType.deepCopy());
    }

    @Override
    public String toString(){return "("+this.address+","+this.locationType+")";}
}

