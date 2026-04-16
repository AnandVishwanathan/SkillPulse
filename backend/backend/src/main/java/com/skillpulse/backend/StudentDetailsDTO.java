package com.skillpulse.backend;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
@Data @AllArgsConstructor
public class StudentDetailsDTO {
    private String id; private String fullName; private String email; private int enrolledCourseCount; private double averageProgress; private List<Course> enrolledCourses;
}