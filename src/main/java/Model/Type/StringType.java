package Model.Type;

import Model.Value.StringValue;
import Model.Value.Value;

public class StringType implements Type {
    @Override
    public Value defaultValue() {
        return new StringValue("");
    }

    @Override
    public Type deepCopy() {
        return new StringType();
    }

    public boolean equals(Object another){
        return another instanceof StringType;
    }
    @Override
    public String toString() { return "string";}
}
