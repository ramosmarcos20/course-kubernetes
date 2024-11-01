package org.marcos.springcloud.course.service.clients;

import org.marcos.springcloud.course.service.models.User;
import org.marcos.springcloud.course.service.models.entity.Course;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", url = "user-service:8001/api/users")
public interface UserClientRest {

    @GetMapping("/{id}")
    User getUser(@PathVariable Long id);

    @PostMapping("")
    User createUser(@RequestBody User user);

    @GetMapping("/users-course")
    List<User> getUserCourse(@RequestParam Iterable<Long> ids);
}
