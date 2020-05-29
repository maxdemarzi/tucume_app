package app.neo4j;

import app.models.User;
import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import org.neo4j.internal.helpers.collection.Iterators;

import java.util.HashMap;
import java.util.Map;

import static app.neo4j.Neo4jParameters.*;

public class Neo4jQueries {

    private static final String USERS_GET = "CALL me.tucu.users.get($username)";
    private static final String USERS_CREATE = "CALL me.tucu.users.create($parameters)";
    private static final String USERS_PROFILE = "CALL me.tucu.users.profile($username,$username2)";

    private static final String POSTS_GET = "CALL me.tucu.posts.get($username, $limit, $since, $username2)";
    private static final String POSTS_CREATE = "CALL me.tucu.posts.create($parameters)";
    private static final String POSTS_REPOST = "CALL me.tucu.posts.repost($post_id, $username)";
    private static final String POSTS_REPLY = "CALL me.tucu.posts.reply($post_id, $parameters)";

    private static final String FOLLOWS_FOLLOWERS = "CALL me.tucu.follows.followers($username, $limit, $since)";
    private static final String FOLLOWS_FOLLOWING = "CALL me.tucu.follows.following($username, $limit, $since)";
    private static final String FOLLOWS_CREATE = "CALL me.tucu.follows.create($username, $username2)";
    private static final String FOLLOWS_REMOVE = "CALL me.tucu.follows.remove($username, $username2)";

    private static final String MUTES_GET = "CALL me.tucu.mutes.get($username, $limit, $since)";
    private static final String MUTES_CREATE = "CALL me.tucu.mutes.create($username, $username2)";
    private static final String MUTES_REMOVE = "CALL me.tucu.mutes.remove($username, $username2)";

    private static final String LIKES_GET = "CALL me.tucu.likes.get($username, $limit, $since, $username2)";
    private static final String LIKES_CREATE = "CALL me.tucu.likes.create($username, $post_id)";
    private static final String LIKES_REMOVE = "CALL me.tucu.likes.remove($username, $post_id)";

    private static final String TIMELINE_GET = "CALL me.tucu.timeline.get($username, $limit, $since)";
    private static final String SEARCH_GET = "CALL me.tucu.search.get($term, $type, $limit, $offset, $username)";
    private static final String MENTIONS_GET = "CALL me.tucu.mentions.get($username, $limit, $since, $username2)";
    private static final String TAGS_GET = "CALL me.tucu.tags.get($hashtag, $limit, $since, $username)";

    public static User usersGet(String username) {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put(USERNAME, username);
        Map<String, Object> result = Iterators.singleOrNull(Neo4jExtension.readQuery(USERS_GET, arguments));
        Map<String, Object> value = getResultOrError(result);
        return new User(value);
    }

    public static User usersCreate(Map<String, Object> parameters) {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put(PARAMETERS, parameters);
        Map<String, Object> result = Iterators.singleOrNull(Neo4jExtension.readQuery(USERS_CREATE, arguments));
        Map<String, Object> value = getResultOrError(result);
        return new User(value);
    }

    public static User usersProfile(String username, String username2) {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put(USERNAME, username);
        arguments.put(USERNAME2, orEmptyString(username2));
        Map<String, Object> result = Iterators.singleOrNull(Neo4jExtension.readQuery(USERS_PROFILE,arguments));
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

    private static Object orEmptyString(Object object) {
        if (object == null) {
            object = "";
        }
        return object;
    }
}
