package com.skillpulse.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MentorServiceTest {

    private FirebaseRepository firebaseRepository;
    private MentorService mentorService;

    @BeforeEach
    void setUp() {
        firebaseRepository = mock(FirebaseRepository.class);
        mentorService = new MentorService(firebaseRepository);
    }

    @Test
    void testGetMenteesWithDetails_Empty() throws ExecutionException, InterruptedException {
        when(firebaseRepository.findStudentsByMentorId("mentor1")).thenReturn(Collections.emptyList());
        when(firebaseRepository.findAllCourses()).thenReturn(Collections.emptyList());

        List<StudentDetailsDTO> result = mentorService.getMenteesWithDetails("mentor1");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMenteesWithDetails_WithCoursesAndProgress() throws ExecutionException, InterruptedException {
        Student student = new Student();
        student.setId("s1");
        student.setFullName("Alice");
        student.setEmail("alice@example.com");

        Map<String, Student.CourseProgress> courseData = new HashMap<>();
        Student.CourseProgress p1 = new Student.CourseProgress();
        p1.setProgress(80);
        courseData.put("c1", p1);

        Student.CourseProgress p2 = new Student.CourseProgress();
        p2.setProgress(60);
        courseData.put("c2", p2);

        student.setCourseData(courseData);

        Course c1 = new Course();
        c1.setId("c1");
        c1.setTitle("Java Basics");

        Course c2 = new Course();
        c2.setId("c2");
        c2.setTitle("Spring Boot");

        when(firebaseRepository.findStudentsByMentorId("m1")).thenReturn(List.of(student));
        when(firebaseRepository.findAllCourses()).thenReturn(List.of(c1, c2));

        List<StudentDetailsDTO> result = mentorService.getMenteesWithDetails("m1");
        assertEquals(1, result.size());
        StudentDetailsDTO dto = result.get(0);
        assertEquals("s1", dto.getId());
        assertEquals("Alice", dto.getFullName());
        assertEquals(2, dto.getEnrolledCourseCount());
        assertEquals(70.0, dto.getAverageProgress(), 0.001);
    }

    @Test
    void testGetMenteesWithDetails_NullCourseData() throws ExecutionException, InterruptedException {
        Student student = new Student();
        student.setId("s2");
        student.setFullName("Bob");
        student.setEmail("bob@example.com");
        student.setCourseData(null);

        when(firebaseRepository.findStudentsByMentorId("m2")).thenReturn(List.of(student));
        when(firebaseRepository.findAllCourses()).thenReturn(Collections.emptyList());

        List<StudentDetailsDTO> result = mentorService.getMenteesWithDetails("m2");
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getEnrolledCourseCount());
        assertEquals(0.0, result.get(0).getAverageProgress());
    }

    @Test
    void testGetStudentsInCourse() throws ExecutionException, InterruptedException {
        Student s1 = new Student();
        s1.setId("s1");
        s1.setFullName("Alice");
        Map<String, Student.CourseProgress> cd1 = new HashMap<>();
        Student.CourseProgress p1 = new Student.CourseProgress();
        p1.setProgress(50);
        cd1.put("c1", p1);
        s1.setCourseData(cd1);

        Student s2 = new Student();
        s2.setId("s2");
        s2.setFullName("Bob");
        s2.setCourseData(new HashMap<>()); // not enrolled in c1

        Course c1 = new Course();
        c1.setId("c1");
        c1.setTitle("Java");

        when(firebaseRepository.findStudentsByMentorId("m1")).thenReturn(List.of(s1, s2));
        when(firebaseRepository.findAllCourses()).thenReturn(List.of(c1));

        List<StudentDetailsDTO> result = mentorService.getStudentsInCourse("c1", "m1");
        assertEquals(1, result.size());
        assertEquals("s1", result.get(0).getId());
        assertEquals(50.0, result.get(0).getAverageProgress());
    }

    @Test
    void testSendFeedback() throws ExecutionException, InterruptedException {
        mentorService.sendFeedback("s1", "c1", "Great progress!", "m1");
        verify(firebaseRepository, times(1)).saveFeedbackMessage("s1", "c1", "Great progress!", "m1");
    }
}
