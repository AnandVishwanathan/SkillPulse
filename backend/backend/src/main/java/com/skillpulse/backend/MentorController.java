
package com.skillpulse.backend;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/mentor")
public class MentorController {
    private final MentorService mentorService;
    public MentorController(MentorService mentorService) { this.mentorService = mentorService; }

    @GetMapping("/students")
    public List<StudentDetailsDTO> getMyStudents(@RequestAttribute("uid") String mentorId) throws ExecutionException, InterruptedException {
        return mentorService.getMenteesWithDetails(mentorId);
    }

    @GetMapping("/courses")
    public List<Course> getAllCourses(@RequestAttribute("uid") String mentorId) throws ExecutionException, InterruptedException {
        return mentorService.getAllCourses();
    }

    @GetMapping("/courses/{courseId}/students")
    public List<StudentDetailsDTO> getStudentsInCourse(@PathVariable String courseId, @RequestAttribute("uid") String mentorId) throws ExecutionException, InterruptedException {
        return mentorService.getStudentsInCourse(courseId, mentorId);
    }

    @GetMapping("/feedback/{studentId}/{courseId}")
    public List<FeedbackMessage> getFeedbackMessages(@PathVariable String studentId, @PathVariable String courseId, @RequestAttribute("uid") String mentorId) throws ExecutionException, InterruptedException {
        return mentorService.getFeedbackMessages(studentId, courseId);
    }

    @PostMapping("/feedback/{studentId}/{courseId}")
    public void sendFeedback(@PathVariable String studentId, @PathVariable String courseId, @RequestBody FeedbackRequest request, @RequestAttribute("uid") String mentorId) throws ExecutionException, InterruptedException {
        mentorService.sendFeedback(studentId, courseId, request.getMessage(), mentorId);
    }
}
