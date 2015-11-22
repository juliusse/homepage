package info.seltenheim.homepage.services.users;

import org.apache.commons.lang3.Validate;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class UserServiceMongoDb implements UserService {
    private final UserMapper userMapper;

    @Inject
    public UserServiceMongoDb(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User upsertUser(User user) throws IOException {
        return userMapper.upsert(user);
    }

    @Override
    public User findUserByName(String name) throws IOException {
        final List<User> users = userMapper.find("name", name);

        Validate.validState(users.size() <= 1);

        if (users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }
}
