package com.kidsmagiclife.api.user.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.kidsmagiclife.api.user.User;
import com.kidsmagiclife.api.user.UserDao;
import com.kidsmagiclife.api.user.graphql.input.UserInput;
import com.kidsmagiclife.services.security.Unsecured;
import com.kidsmagiclife.utility.resources.ResourceDirectory;
import com.kidsmagiclife.utility.resources.conversion.image.ImageUpload;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.GraphQLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMutation implements GraphQLMutationResolver {

    @Autowired
    private UserDao dao;

    public User create(String username, String email, String password) {
        return dao.create(new User(username, email, password));
    }

    public User delete(String id) {
        return dao.delete(id);
    }

    @Unsecured
    public User update(String id, UserInput input, DataFetchingEnvironment environment) {
        GraphQLContext context = environment.getContext();

        if (!input.getAvatar().isEmpty()) {
            ImageUpload avatar = new ImageUpload(environment.getContext(), ResourceDirectory.STORAGE_AVATAR, id);

            if(avatar.getPart().getSize() > 1_000_000) {
                throw new GraphQLException("File size is too big 1mb limit");
            }

            avatar.store();
            input.setAvatar("http://localhost:8080/" + avatar.getResource().getDirectory() + avatar.getFile().getName());
        }

        return dao.update(id, input);
    }
}
