package info.seltenheim.homepage.services.users;

import java.io.IOException;

public interface UserService {

    User findUserByName(String name) throws IOException;

    User upsertUser(User user) throws IOException;
}
