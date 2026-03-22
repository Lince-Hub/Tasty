package linas.puplauskas.tasty.database;

import java.io.Serializable;
import org.bson.types.ObjectId;

public abstract class DaoObject implements Serializable {
    public abstract void setId(ObjectId var1);

    public abstract ObjectId getId();
}