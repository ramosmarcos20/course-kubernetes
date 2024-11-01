package org.marcos.springcloud.course.service.services;

import org.marcos.springcloud.course.service.clients.UserClientRest;
import org.marcos.springcloud.course.service.models.User;
import org.marcos.springcloud.course.service.models.entity.Course;
import org.marcos.springcloud.course.service.models.entity.CourseUser;
import org.marcos.springcloud.course.service.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserClientRest userClient;

    @Override
    @Transactional(readOnly = true)
    public List<Course> getCourses() {
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCourseUserById(Long id) {
        courseRepository.deleteCourseUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getUsersById(Long id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();

            if (!course.getCourseUsers().isEmpty()){
                List<Long> ids = course.getCourseUsers().stream().map(CourseUser::getUserId).toList();
                List<User> users = userClient.getUserCourse(ids);
                course.setUsers(users);
            }
            return Optional.of(course);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> assigUser(User user, Long course_id) {
        Optional<Course> courseO = courseRepository.findById(course_id);
        if (courseO.isPresent()) {
            User userMsvc = userClient.getUser(user.getId());

            Course course = courseO.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            course.addCourseUser(courseUser);
            courseRepository.save(course);

            return Optional.of(userMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> createUser(User user, Long course_id) {
        Optional<Course> courseO = courseRepository.findById(course_id);
        if (courseO.isPresent()) {
            User userNewMsvc = userClient.createUser(user);

            Course course = courseO.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userNewMsvc.getId());

            course.addCourseUser(courseUser);
            courseRepository.save(course);

            return Optional.of(userNewMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> deleteUser(User user, Long course_id) {
        Optional<Course> courseO = courseRepository.findById(course_id);
        if (courseO.isPresent()) {
            User userMsvc = userClient.getUser(user.getId());

            Course course = courseO.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            course.removeCourseUser(courseUser);
            courseRepository.save(course);

            return Optional.of(userMsvc);
        }
        return Optional.empty();
    }
}
