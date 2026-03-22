package linas.puplauskas.tasty.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.List;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public abstract class DaoService<T extends DaoObject, K> {
    protected final MongoDatabase mongoDatabase;
    private final Class<T> entityClass;

    public abstract String getTableName();

    public abstract Bson processCriteria(K var1);

    public DaoService(MongoDatabase mongoDatabase, Class<T> entityClass) {
        this.mongoDatabase = mongoDatabase;
        this.entityClass = entityClass;
    }

    protected MongoCollection<T> getCollection() {
        return this.mongoDatabase.getCollection(this.getTableName(), this.entityClass);
    }

    public void save(T obj) {
        this.getCollection().insertOne(obj);
    }

    public void update(T obj) {
        this.getCollection().replaceOne(Filters.eq("_id", obj.getId()), obj);
    }

    public T find(K criteria) {
        Bson filter = this.processCriteria(criteria);
        return (DaoObject)this.getCollection().find(filter).first();
    }

    public List<T> findAll() {
        List<T> results = new ArrayList();
        this.getCollection().find().into(results);
        return results;
    }

    public T get(ObjectId objectId) {
        return (DaoObject)this.getCollection().find(Filters.eq("_id", objectId)).first();
    }

    public void remove(K criteria) {
        Bson query = this.processCriteria(criteria);
        this.getCollection().deleteMany(query);
    }
}