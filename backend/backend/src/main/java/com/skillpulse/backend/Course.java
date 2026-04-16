package com.skillpulse.backend;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import lombok.Data;
import java.util.List;
@Data @IgnoreExtraProperties
public class Course {
    private String id; private String title; private String description; private List<Module> modules;
    @Data @IgnoreExtraProperties
    public static class Module { private String id; private String title; private String longContent; }
}