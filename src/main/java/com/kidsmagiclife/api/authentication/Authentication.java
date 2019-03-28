package com.kidsmagiclife.api.authentication;

import com.kidsmagiclife.api.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authentication")
public class Authentication {

    @Id
    private String id;
    @DBRef
    private final User user;

    private String token;

    public Authentication(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
