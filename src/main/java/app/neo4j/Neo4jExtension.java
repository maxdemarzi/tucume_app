package app.neo4j;

import com.typesafe.config.Config;
import io.jooby.Environment;
import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.ServiceRegistry;
import org.neo4j.driver.*;
import org.neo4j.internal.helpers.collection.Iterators;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Neo4jExtension implements Extension {

    private static Driver driver;

    @Override
    public void install(@Nonnull Jooby application) throws Exception {
        Environment env = application.getEnvironment();
        Config conf = env.getConfig();

        driver = GraphDatabase.driver(conf.getString("neo4j.uri"),
                AuthTokens.basic(conf.getString("neo4j.username"), conf.getString("neo4j.password"))
//                ,
//                org.neo4j.driver.Config.builder()
//                        .withMaxConnectionLifetime(8, TimeUnit.MINUTES)
//                        .withConnectionLivenessCheckTimeout(2, TimeUnit.MINUTES)
//                        .withEncryption().build()
        );

        ServiceRegistry registry = application.getServices();
        registry.put(Driver.class, driver);
        application.onStop(driver);
    }

    public static List<Map<String, Object>> txRead(Transaction transaction, String query, Map<String, Object> params )  {
        return transaction.run(query, params).list( r -> r.asMap(Neo4jExtension::convert));
    }

    public static Iterator<Map<String, Object>> readQuery(String query, Map<String, Object> params) {
        try (Session session = driver.session()) {
            List<Map<String, Object>> list = new ArrayList<>();
            session.readTransaction((TransactionWork<Object>) transaction ->
                    list.addAll(txRead(transaction, query, params)));
            return list.iterator();
        } catch (Exception e) {
            // ignore exception for now.
        }
        return Iterators.emptyResourceIterator();
    }

    public static Iterator<Map<String, Object>> query(String query, Map<String, Object> params) {
        try (Session session = driver.session()) {
            List<Map<String, Object>> list = session.run(query, params)
                    .list( r -> r.asMap(Neo4jExtension::convert));
            return list.iterator();
        } catch (Exception e) {
            // ignore exception for now.
        }
        return Iterators.emptyResourceIterator();
    }

    public static Object convert(Value value) {
        switch (value.type().name()) {
            case "PATH":
                return value.asList(Neo4jExtension::convert);
            case "NODE":
            case "RELATIONSHIP":
                return value.asMap();
        }
        return value.asObject();
    }
}

