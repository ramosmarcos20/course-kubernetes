package org.marcos.springcloud.user.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", url = "course-service:8002/api/courses")
public interface CourseClientRest {

    @DeleteMapping("/delete-course-user/{id}")
    void deleteCourseUserById(@PathVariable Long id);
}
