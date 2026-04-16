package com.skillpulse.backend;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class StudentService {
    private final FirebaseRepository firebaseRepository;
    
    public StudentService(FirebaseRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
    }

    public List<MentorDTO> getMentorsByInstitution(String institutionName) throws ExecutionException, InterruptedException {
        List<Student> mentors = firebaseRepository.findMentorsByInstitution(institutionName);
        List<MentorDTO> mentorDTOs = new ArrayList<>();
        
        for (Student mentor : mentors) {
            // Count students for this mentor
            List<Student> students = firebaseRepository.findStudentsByMentorId(mentor.getId());
            mentorDTOs.add(new MentorDTO(
                mentor.getId(),
                mentor.getFullName(),
                mentor.getEmail(),
                mentor.getInstitutionName(),
                students.size()
            ));
        }
        
        return mentorDTOs;
    }

    public void selectMentor(String studentId, String mentorId) throws ExecutionException, InterruptedException {
        System.out.println("StudentService: selectMentor called with studentId=" + studentId + ", mentorId=" + mentorId);
        
        if (studentId == null || mentorId == null) {
            throw new IllegalArgumentException("Student ID and Mentor ID cannot be null");
        }
        
        firebaseRepository.updateStudentMentor(studentId, mentorId);
        System.out.println("StudentService: Mentor selection completed successfully");
    }

    public List<Course> getAllCourses() throws ExecutionException, InterruptedException {
        return firebaseRepository.findAllCourses();
    }

    public void enrollInCourse(String studentId, String courseId) throws ExecutionException, InterruptedException {
        firebaseRepository.enrollStudentInCourse(studentId, courseId);
    }

    public void updateProgress(String studentId, String courseId, int progress) throws ExecutionException, InterruptedException {
        firebaseRepository.updateStudentProgress(studentId, courseId, progress);
    }

    public List<FeedbackMessage> getFeedbackForCourse(String studentId, String courseId) throws ExecutionException, InterruptedException {
        return firebaseRepository.getFeedbackMessages(studentId, courseId);
    }

    public void sendFeedbackToMentor(String studentId, String courseId, String message) throws ExecutionException, InterruptedException {
        firebaseRepository.saveFeedbackMessage(studentId, courseId, message, studentId, "student");
    }
}