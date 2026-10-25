package com.skillpulse.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    private FirebaseRepository firebaseRepository;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        firebaseRepository = mock(FirebaseRepository.class);
        studentService = new StudentService(firebaseRepository);
    }

    @Test
    void testGetMentorsByInstitution() throws ExecutionException, InterruptedException {
        Student mentor = new Student();
        mentor.setId("m1");
        mentor.setFullName("Prof. Smith");
        mentor.setEmail("smith@uni.edu");
        mentor.setInstitutionName("Tech University");

        Student s1 = new Student();
        s1.setId("s1");
        Student s2 = new Student();
        s2.setId("s2");

        when(firebaseRepository.findMentorsByInstitution("Tech University")).thenReturn(List.of(mentor));
        when(firebaseRepository.findStudentsByMentorId("m1")).thenReturn(List.of(s1, s2));

        List<MentorDTO> result = studentService.getMentorsByInstitution("Tech University");
        assertEquals(1, result.size());
        MentorDTO dto = result.get(0);
        assertEquals("m1", dto.getId());
        assertEquals("Prof. Smith", dto.getFullName());
        assertEquals(2, dto.getStudentCount());
    }

    @Test
    void testSelectMentor_Success() throws ExecutionException, InterruptedException {
        studentService.selectMentor("s1", "m1");
        verify(firebaseRepository, times(1)).updateStudentMentor("s1", "m1");
    }

    @Test
    void testSelectMentor_NullThrows() {
        assertThrows(IllegalArgumentException.class, () -> studentService.selectMentor(null, "m1"));
        assertThrows(IllegalArgumentException.class, () -> studentService.selectMentor("s1", null));
    }

    @Test
    void testSendFeedbackToMentor() throws ExecutionException, InterruptedException {
        studentService.sendFeedbackToMentor("s1", "c1", "Need help with assignment");
        verify(firebaseRepository, times(1)).saveFeedbackMessage("s1", "c1", "Need help with assignment", "s1", "student");
    }

    @Test
    void testUpdateProgress() throws ExecutionException, InterruptedException {
        studentService.updateProgress("s1", "c1", 85);
        verify(firebaseRepository, times(1)).updateStudentProgress("s1", "c1", 85);
    }
}
