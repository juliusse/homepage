package info.seltenheim.homepage.services.users;

import java.io.IOException;

import com.google.inject.ImplementedBy;

@ImplementedBy(UserServiceMongoDb.class)
public interface UserService {

    User findUserByName(String name) throws IOException;

    User upsertUser(User user) throws IOException;
}
