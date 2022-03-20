package Model.Type;

import Model.Value.Value;

public interface Type {
    //declare default value
    Value defaultValue();
    public Type deepCopy();
}
