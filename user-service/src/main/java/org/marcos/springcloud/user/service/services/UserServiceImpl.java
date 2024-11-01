package org.marcos.springcloud.user.service.services;

import org.marcos.springcloud.user.service.client.CourseClientRest;
import org.marcos.springcloud.user.service.models.entity.User;
import org.marcos.springcloud.user.service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseClientRest courseClient;

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
        courseClient.deleteCourseUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> finAllById(Iterable<Long> ids) {
        return (List<User>) userRepository.findAllById(ids);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.byEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
