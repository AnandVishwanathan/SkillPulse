package com.skillpulse.backend;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import lombok.Data;
import java.util.Map;
@Data @IgnoreExtraProperties
public class Student {
    private String id; 
    private String fullName; 
    private String email; 
    private String mentorId; 
    private String institutionId; 
    private String institutionName;
    private String role;
    private Map<String, CourseProgress> courseData;
    
    @Data @IgnoreExtraProperties
    public static class CourseProgress { 
        private int progress; 
    }
}