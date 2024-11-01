package org.marcos.springcloud.course.service.services;

import org.marcos.springcloud.course.service.models.User;
import org.marcos.springcloud.course.service.models.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getCourses();
    Optional<Course> getCourseById(Long id);
    Course createCourse(Course course);
    Course updateCourse(Course course);
    void deleteCourse(Long id);
    void deleteCourseUserById(Long id);

    Optional<Course> getUsersById(Long id);

    //logica de negocio
    Optional<User> assigUser(User user, Long course_id);
    Optional<User> createUser(User user, Long course_id);
    Optional<User> deleteUser(User user, Long course_id);
}
