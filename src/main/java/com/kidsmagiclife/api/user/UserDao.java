package com.kidsmagiclife.api.user;

import com.kidsmagiclife.api.API;
import com.kidsmagiclife.api.user.graphql.input.UserInput;
import com.kidsmagiclife.services.graphql.exceptions.IdNotFoundException;
import com.kidsmagiclife.utility.Merger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;

@Component
public class UserDao implements API<User, UserInput> {

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional
    public User create(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(9)));
        user.setAvatar("https://gravatar.com/avatar/" + DigestUtils.md5DigestAsHex(user.getEmail().getBytes()) + "?d=identicon");
        user.setSeen(new DateTime());
        return repository.save(user);
    }

    @Override
    @Transactional
    public User delete(String id) {
        User user = repository.findById(id).orElseThrow(() -> new IdNotFoundException(id));
        repository.delete(user);
        return user;
    }

    @Override
    @Transactional
    public User update(String id, UserInput input) {
        User user = repository.findById(id).orElseThrow(() -> new IdNotFoundException(id));
        return repository.save(new Merger<User, UserInput>().merge(user, input));
    }

    @Override
    @Transactional
    public User get(String id) {
        return repository.findById(id).orElseThrow(() -> new IdNotFoundException(id));
    }

    @Transactional
    public User getByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new IdNotFoundException(email));
    }

    @Override
    @Transactional
    public List<User> all() {
        return repository.findAll();
    }
}
