package info.seltenheim.homepage.services.users;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("usersMongo")
public class UserServiceMongoDb implements UserService {
    private final UserMapper userMapper = new UserMapper();

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
