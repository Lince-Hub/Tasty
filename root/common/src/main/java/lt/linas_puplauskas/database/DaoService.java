package lt.linas_puplauskas.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public abstract class DaoService<T extends DaoObject, K> {
    protected final MongoDatabase mongoDatabase =  MongoConfig.getDatabase();
    private final Class<T> entityClass;

    public abstract String getTableName();

    public abstract Bson processCriteria(K var1);

    public DaoService(Class<T> entityClass) {
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

    public T findFirst(K criteria) {
        Bson filter = this.processCriteria(criteria);
        return this.getCollection().find(filter).first();
    }

    public List<T> findAll(K  criteria) {
        Bson filter = this.processCriteria(criteria);
        List<T> results = new ArrayList<>();
        this.getCollection().find(filter).into(results);
        return results;
    }

    public T get(ObjectId objectId) {
        return this.getCollection().find(Filters.eq("_id", objectId)).first();
    }

    public void remove(K criteria) {
        Bson query = this.processCriteria(criteria);
        this.getCollection().deleteMany(query);
    }
}