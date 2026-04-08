package lt.linas_puplauskas.database;

import org.bson.types.ObjectId;

import java.io.Serializable;

public abstract class DaoObject implements Serializable {
    public abstract void setId(ObjectId var1);

    public abstract ObjectId getId();
}
