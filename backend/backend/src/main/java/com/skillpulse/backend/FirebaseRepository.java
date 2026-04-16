package com.skillpulse.backend;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class FirebaseRepository {
    public List<Student> findStudentsByMentorId(String mentorId) throws ExecutionException, InterruptedException {
        System.out.println("Searching for students with mentorId: " + mentorId);
        
        Firestore db = FirestoreClient.getFirestore();
        List<Student> students = new ArrayList<>();
        
        ApiFuture<QuerySnapshot> future = db.collection("users")
            .whereEqualTo("mentorId", mentorId)
            .whereEqualTo("role", "student")
            .get();
            
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        System.out.println("Found " + documents.size() + " students for mentor " + mentorId);
        
        for (QueryDocumentSnapshot document : documents) {
            Student student = document.toObject(Student.class);
            student.setId(document.getId());
            students.add(student);
            System.out.println("Student: " + student.getFullName() + " (" + student.getEmail() + ")");
        }
        
        return students;
    }

    public List<Course> findAllCourses() throws ExecutionException, InterruptedException {
        System.out.println("Fetching all courses from Firebase");
        
        Firestore db = FirestoreClient.getFirestore();
        List<Course> courses = new ArrayList<>();
        
        ApiFuture<QuerySnapshot> future = db.collection("courses").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        System.out.println("Found " + documents.size() + " courses");
        
        for (QueryDocumentSnapshot document : documents) {
            Course course = document.toObject(Course.class);
            course.setId(document.getId());
            courses.add(course);
            System.out.println("Course: " + course.getTitle());
        }
        
        return courses;
    }

    public List<FeedbackMessage> getFeedbackMessages(String studentId, String courseId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        List<FeedbackMessage> messages = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("feedback")
            .document(studentId)
            .collection(courseId)
            .orderBy("timestamp")
            .get();
        
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            FeedbackMessage message = document.toObject(FeedbackMessage.class);
            message.setId(document.getId());
            messages.add(message);
        }
        return messages;
    }

    public void saveFeedbackMessage(String studentId, String courseId, String messageText, String senderId) throws ExecutionException, InterruptedException {
        saveFeedbackMessage(studentId, courseId, messageText, senderId, "mentor");
    }

    public void saveFeedbackMessage(String studentId, String courseId, String messageText, String senderId, String senderRole) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        
        FeedbackMessage message = new FeedbackMessage();
        message.setText(messageText);
        message.setSender(senderId);
        message.setTimestamp(new java.util.Date());
        message.setSenderRole(senderRole);
        
        // Get sender name
        ApiFuture<DocumentSnapshot> senderDoc = db.collection("users").document(senderId).get();
        DocumentSnapshot senderSnapshot = senderDoc.get();
        if (senderSnapshot.exists()) {
            message.setSenderName(senderSnapshot.getString("fullName"));
        }
        
        db.collection("feedback")
            .document(studentId)
            .collection(courseId)
            .add(message);
    }

    public List<Student> findMentorsByInstitution(String institutionName) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        List<Student> mentors = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("users")
            .whereEqualTo("role", "mentor")
            .whereEqualTo("institutionName", institutionName)
            .get();
        
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Student mentor = document.toObject(Student.class);
            mentor.setId(document.getId());
            mentors.add(mentor);
        }
        return mentors;
    }

    public void updateStudentMentor(String studentId, String mentorId) throws ExecutionException, InterruptedException {
        System.out.println("Updating student mentor: studentId=" + studentId + ", mentorId=" + mentorId);
        
        Firestore db = FirestoreClient.getFirestore();
        
        try {
            ApiFuture<com.google.cloud.firestore.WriteResult> future = db.collection("users")
                .document(studentId)
                .update("mentorId", mentorId);
            
            com.google.cloud.firestore.WriteResult result = future.get();
            System.out.println("Mentor update successful at: " + result.getUpdateTime());
            
        } catch (Exception e) {
            System.err.println("Error updating student mentor: " + e.getMessage());
            throw e;
        }
    }

    public void enrollStudentInCourse(String studentId, String courseId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        
        // Get current student data
        ApiFuture<DocumentSnapshot> future = db.collection("users").document(studentId).get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            Student student = document.toObject(Student.class);
            if (student.getCourseData() == null) {
                student.setCourseData(new java.util.HashMap<>());
            }
            
            // Add course with 0% progress if not already enrolled
            if (!student.getCourseData().containsKey(courseId)) {
                Student.CourseProgress progress = new Student.CourseProgress();
                progress.setProgress(0);
                student.getCourseData().put(courseId, progress);
                
                // Update in Firestore
                db.collection("users").document(studentId).update("courseData", student.getCourseData());
            }
        }
    }

    public void updateStudentProgress(String studentId, String courseId, int progress) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        
        // Update the specific course progress
        String fieldPath = "courseData." + courseId + ".progress";
        db.collection("users").document(studentId).update(fieldPath, progress);
    }
}