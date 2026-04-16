package com.skillpulse.backend;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final StudentService studentService;
    
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/mentors/{institutionName}")
    public List<MentorDTO> getMentorsByInstitution(@PathVariable String institutionName) throws ExecutionException, InterruptedException {
        return studentService.getMentorsByInstitution(institutionName);
    }

    @PostMapping("/select-mentor")
    public void selectMentor(@RequestBody SelectMentorRequest request, @RequestAttribute("uid") String studentId) throws ExecutionException, InterruptedException {
        studentService.selectMentor(studentId, request.getMentorId());
    }

    @GetMapping("/courses")
    public List<Course> getAllCourses(@RequestAttribute("uid") String studentId) throws ExecutionException, InterruptedException {
        return studentService.getAllCourses();
    }

    @PostMapping("/enroll/{courseId}")
    public void enrollInCourse(@PathVariable String courseId, @RequestAttribute("uid") String studentId) throws ExecutionException, InterruptedException {
        studentService.enrollInCourse(studentId, courseId);
    }

    @PostMapping("/progress/{courseId}")
    public void updateProgress(@PathVariable String courseId, @RequestBody UpdateProgressRequest request, @RequestAttribute("uid") String studentId) throws ExecutionException, InterruptedException {
        studentService.updateProgress(studentId, courseId, request.getProgress());
    }

    @GetMapping("/feedback/{courseId}")
    public List<FeedbackMessage> getFeedbackForCourse(@PathVariable String courseId, @RequestAttribute("uid") String studentId) throws ExecutionException, InterruptedException {
        return studentService.getFeedbackForCourse(studentId, courseId);
    }

    @PostMapping("/feedback/{courseId}")
    public void sendFeedbackToMentor(@PathVariable String courseId, @RequestBody FeedbackRequest request, @RequestAttribute("uid") String studentId) throws ExecutionException, InterruptedException {
        studentService.sendFeedbackToMentor(studentId, courseId, request.getMessage());
    }
}