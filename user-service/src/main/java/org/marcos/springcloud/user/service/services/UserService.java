package org.marcos.springcloud.user.service.services;

import org.marcos.springcloud.user.service.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();
    Optional<User> getUserById(long id);
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(long id);

    List<User> finAllById(Iterable<Long> ids);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
