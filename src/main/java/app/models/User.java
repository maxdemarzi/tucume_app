package app.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String name;
    private String email;
    public String password;
    private String hash;
    private Long time;
    private Integer likes;
    private Integer posts;
    private Integer followers;
    private Integer following;
    private Boolean i_follow;
    private Boolean follows_me;
    private Integer followers_you_know_count;
    private ArrayList<HashMap<String, Object>> followers_you_know;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String EMAIL = "email";

    public User(Map<String, Object> data) {
        username = (String)data.getOrDefault(USERNAME, null);
        password = (String)data.getOrDefault(PASSWORD, null);
        name = (String)data.getOrDefault(NAME, null);
        email = (String)data.getOrDefault(EMAIL, null);
    }
}
