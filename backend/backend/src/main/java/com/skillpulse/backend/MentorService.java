package com.skillpulse.backend;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class MentorService {
    private final FirebaseRepository firebaseRepository;
    public MentorService(FirebaseRepository firebaseRepository) { this.firebaseRepository = firebaseRepository; }

    public List<StudentDetailsDTO> getMenteesWithDetails(String mentorId) throws ExecutionException, InterruptedException {
        System.out.println("Getting mentees for mentor ID: " + mentorId);
        
        List<Student> myStudents = firebaseRepository.findStudentsByMentorId(mentorId);
        System.out.println("Found " + myStudents.size() + " students for mentor");
        
        Map<String, Course> allCoursesMap = new HashMap<>();
        try {
            allCoursesMap = firebaseRepository.findAllCourses().stream().collect(Collectors.toMap(Course::getId, course -> course));
            System.out.println("Found " + allCoursesMap.size() + " courses");
        } catch (Exception e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
        
        List<StudentDetailsDTO> studentDetailsList = new ArrayList<>();
        for (Student student : myStudents) {
            List<Course> enrolledCourses = new ArrayList<>();
            double totalProgress = 0;
            if (student.getCourseData() != null) {
                for (String courseId : student.getCourseData().keySet()) {
                    if (allCoursesMap.containsKey(courseId)) {
                        enrolledCourses.add(allCoursesMap.get(courseId));
                        totalProgress += student.getCourseData().get(courseId).getProgress();
                    }
                }
            }
            int enrolledCourseCount = enrolledCourses.size();
            double averageProgress = (enrolledCourseCount > 0) ? (totalProgress / enrolledCourseCount) : 0;
            studentDetailsList.add(new StudentDetailsDTO(student.getId(), student.getFullName(), student.getEmail(), enrolledCourseCount, averageProgress, enrolledCourses));
        }
        
        System.out.println("Returning " + studentDetailsList.size() + " student details");
        return studentDetailsList;
    }

    public List<Course> getAllCourses() throws ExecutionException, InterruptedException {
        return firebaseRepository.findAllCourses();
    }

    public List<StudentDetailsDTO> getStudentsInCourse(String courseId, String mentorId) throws ExecutionException, InterruptedException {
        List<Student> myStudents = firebaseRepository.findStudentsByMentorId(mentorId);
        Map<String, Course> allCoursesMap = firebaseRepository.findAllCourses().stream().collect(Collectors.toMap(Course::getId, course -> course));
        
        List<StudentDetailsDTO> studentsInCourse = new ArrayList<>();
        for (Student student : myStudents) {
            if (student.getCourseData() != null && student.getCourseData().containsKey(courseId)) {
                Course course = allCoursesMap.get(courseId);
                List<Course> enrolledCourses = List.of(course);
                double progress = student.getCourseData().get(courseId).getProgress();
                studentsInCourse.add(new StudentDetailsDTO(student.getId(), student.getFullName(), student.getEmail(), 1, progress, enrolledCourses));
            }
        }
        return studentsInCourse;
    }

    public List<FeedbackMessage> getFeedbackMessages(String studentId, String courseId) throws ExecutionException, InterruptedException {
        return firebaseRepository.getFeedbackMessages(studentId, courseId);
    }

    public void sendFeedback(String studentId, String courseId, String message, String mentorId) throws ExecutionException, InterruptedException {
        firebaseRepository.saveFeedbackMessage(studentId, courseId, message, mentorId);
    }
}