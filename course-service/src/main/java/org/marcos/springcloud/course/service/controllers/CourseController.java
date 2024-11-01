package org.marcos.springcloud.course.service.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.marcos.springcloud.course.service.models.User;
import org.marcos.springcloud.course.service.models.entity.Course;
import org.marcos.springcloud.course.service.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseService.getUsersById(id);
        if (course.isPresent()) {
            return ResponseEntity.ok(course.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody Course course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validateResult(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@Valid @PathVariable Long id, @RequestBody Course course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validateResult(bindingResult);
        }
        Optional<Course> courseById = courseService.getCourseById(id);
        if (courseById.isPresent()) {
            Course courseToUpdate = courseById.get();
            courseToUpdate.setName(course.getName());
            return ResponseEntity.status(HttpStatus.OK).body(courseService.updateCourse(courseToUpdate));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        Optional<Course> courseToDelete = courseService.getCourseById(id);
        if (courseToDelete.isPresent()) {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/assig-user/{course_id}")
    public ResponseEntity<?> assigUser(@RequestBody User user, @PathVariable Long course_id) {
        Optional<User> userOptional;
        try {
            userOptional = courseService.assigUser(user, course_id);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error",
                            "User not exists by Id: " + e.getMessage()));
        }

        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-user/{course_id}")
    public ResponseEntity<?> createUser(@RequestBody User user, @PathVariable Long course_id) {
        Optional<User> userOptional;
        try {
            userOptional = courseService.createUser(user, course_id);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error",
                            "User cant save: " + e.getMessage()));
        }

        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-user/{course_id}")
    public ResponseEntity<?> deleteUser(@RequestBody User user, @PathVariable Long course_id) {
        Optional<User> userOptional;
        try {
            userOptional = courseService.deleteUser(user, course_id);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error",
                            "User not found by id or error comunication: " + e.getMessage()));
        }

        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-course-user/{id}")
    public ResponseEntity<?> deleteCourseUserById(@PathVariable Long id) {
        courseService.deleteCourseUserById(id);
        return ResponseEntity.noContent().build();
    }

    private static ResponseEntity<Map<String, String>> validateResult(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}

