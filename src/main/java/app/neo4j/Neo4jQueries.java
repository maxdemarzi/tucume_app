package app.neo4j;

import app.models.User;
import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import org.neo4j.internal.helpers.collection.Iterators;

import java.util.HashMap;
import java.util.Map;

import static app.neo4j.Neo4jParameters.PARAMETERS;
import static app.neo4j.Neo4jParameters.USERNAME;

public class Neo4jQueries {

    private static final String USERS_GET = "CALL me.tucu.users.get($username)";
    private static final String USERS_CREATE = "CALL me.tucu.users.create($parameters)";
    /*
    CALL me.tucu.users.profile($username, $username2);

    CALL me.tucu.posts.get($username, $limit, $since, $username2);
    CALL me.tucu.posts.create($parameters);
    CALL me.tucu.posts.repost($post_id, $username);
    CALL me.tucu.posts.reply($post_id, $parameters);

    CALL me.tucu.follows.followers($username, $limit, $since);
    CALL me.tucu.follows.following($username, $limit, $since);
    CALL me.tucu.follows.create($username, $username2);
    CALL me.tucu.follows.remove($username, $username2);

    CALL me.tucu.mutes.get($username, $limit, $since);
    CALL me.tucu.mutes.create($username, $username2);
    CALL me.tucu.mutes.remove($username, $username2);

    CALL me.tucu.likes.get($username, $limit, $since, $username2);
    CALL me.tucu.likes.create($username, $post_id);
    CALL me.tucu.likes.remove($username, $post_id);

    CALL me.tucu.timeline.get($username, $limit, $since);

    CALL me.tucu.search.get($term, $type, $limit, $offset, $username);

    CALL me.tucu.mentions.get($username, $limit, $since, $username2);
    // mentions are automatically created/removed on Post create/remove/update

    CALL me.tucu.tags.get($hashtag, $limit, $since, $username);
    // tags are automatically created/removed on Post create/remove/update
     */

    public static User getUser(String username) {
        Map<String, Object> result = Iterators.singleOrNull(Neo4jExtension.readQuery(USERS_GET,
                new HashMap<>() {{ put(USERNAME, username); }}));
        Map<String, Object> value = getResultOrError(result);
        return new User(value);
    }

    public static User createUser(Map<String, Object> parameters) {
        Map<String, Object> result = Iterators.singleOrNull(Neo4jExtension.readQuery(USERS_CREATE,
                new HashMap<>() {{ put(PARAMETERS, parameters); }}));
        Map<String, Object> value = getResultOrError(result);
        return new User(value);
    }

    private static Map<String, Object> getResultOrError(Map<String, Object> result) {
        if (result == null || !result.containsKey("value")) { throw new StatusCodeException(StatusCode.SERVER_ERROR); }
        Map<String, Object> value = (Map<String, Object>)result.get("value");
        if (value.containsKey("Error")) {
            String error = (String)value.get("Error");
            throw new StatusCodeException(StatusCode.SERVER_ERROR, error);
        }
        return value;
    }
}
