package linas.puplauskas.tasty.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

public class MongoConfig {
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    public static void connect() {
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(new ConnectionString("mongodb://localhost:27017")).build();
        mongoClient = MongoClients.create(settings);
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(new CodecRegistry[]{MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(new CodecProvider[]{PojoCodecProvider.builder().automatic(true).build()})});
        mongoDatabase = mongoClient.getDatabase("admin").withCodecRegistry(pojoCodecRegistry);

        try {
            Bson command = new BsonDocument("ping", new BsonInt64(1L));
            Document result = mongoDatabase.runCommand(command);
            System.out.println("Connected to MongoDB successfully!");
        } catch (MongoException var4) {
            System.err.println("Connection failed: " + String.valueOf(var4));
        }

    }

    public static void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            mongoDatabase = null;
            System.out.println("Disconnected from MongoDB.");
        }

    }

    public static MongoDatabase getDatabase() {
        if (mongoDatabase == null) {
            throw new IllegalStateException("Not connected. Call connect() first.");
        } else {
            return mongoDatabase;
        }
    }
}