# SkillPulse Complete Setup Guide

## Overview
This guide will help you set up the complete SkillPulse system with:
1. **Login page** - Account creation with institution selection
2. **Student dashboard** - Mentor selection from same institution
3. **Mentor dashboard** - View students who selected them as mentor
4. **Backend API** - Handles all data operations

## Required Collections and Document Structure

### 1. Users Collection (`users`)

#### Mentor Document Example:
```json
{
  "fullName": "John Doe",
  "email": "mentor@example.com",
  "role": "mentor",
  "institutionId": "inst_001",
  "createdAt": "2024-01-01T00:00:00Z"
}
```

#### Student Document Example:
```json
{
  "fullName": "Jane Smith",
  "email": "student@example.com",
  "role": "student",
  "mentorId": "MENTOR_USER_ID_HERE",
  "institutionName": "Easwari Engineering College",
  "courseData": {
    "course_001": {
      "progress": 75
    },
    "course_002": {
      "progress": 50
    }
  },
  "createdAt": "2024-01-01T00:00:00Z"
}
```

### 2. Courses Collection (`courses`)

#### Course Document Example:
```json
{
  "title": "JavaScript Fundamentals",
  "description": "Learn the basics of JavaScript programming",
  "modules": [
    {
      "id": "module_001",
      "title": "Variables and Data Types",
      "longContent": "In this module, you will learn about..."
    },
    {
      "id": "module_002", 
      "title": "Functions and Scope",
      "longContent": "Functions are reusable blocks of code..."
    }
  ],
  "createdAt": "2024-01-01T00:00:00Z"
}
```

### 3. Feedback Collection (`feedback/{studentId}/{courseId}`)

#### Feedback Message Example:
```json
{
  "text": "Great progress on this module!",
  "sender": "MENTOR_USER_ID",
  "senderName": "John Doe",
  "senderRole": "mentor",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Setup Steps

### Step 1: Create a Mentor User
1. Go to Firebase Console → Authentication
2. Add a new user with email/password
3. Copy the User ID (UID)
4. Go to Firestore Database
5. Create a document in `users` collection with the UID as document ID
6. Add the mentor data structure above

### Step 2: Create Courses
1. Go to Firestore Database
2. Create `courses` collection
3. Add course documents with auto-generated IDs
4. Use the course structure above

### Step 3: Create Student Users
1. Create student users in Authentication
2. Add student documents to `users` collection
3. **Important**: Set `mentorId` to the mentor's UID
4. Add `courseData` with course IDs and progress

### Step 4: Test the System
1. Open `test_backend.html`
2. Use "Test with Firebase Auth" button
3. Enter mentor credentials
4. Verify that students and courses are returned

## Sample Data Script

You can use this JavaScript in the browser console to add sample data:

```javascript
// Add sample course
await db.collection('courses').add({
  title: 'React Fundamentals',
  description: 'Learn React.js from scratch',
  modules: [
    {
      id: 'react_001',
      title: 'Components and JSX',
      longContent: 'React components are the building blocks...'
    }
  ],
  createdAt: new Date()
});

// Add sample student (replace MENTOR_UID with actual mentor UID)
await db.collection('users').add({
  fullName: 'Alice Johnson',
  email: 'alice@example.com',
  role: 'student',
  mentorId: 'MENTOR_UID_HERE',
  courseData: {
    'COURSE_ID_HERE': {
      progress: 80
    }
  },
  createdAt: new Date()
});
```

## Verification

After setup, your mentor dashboard should show:
- Students who selected you as mentor in "My Students"
- All available courses in "Courses" 
- Student progress when you click on a course
- Feedback threads for completed courses

## Troubleshooting

1. **No students showing**: Check that student documents have correct `mentorId`
2. **No courses showing**: Verify courses collection exists with proper structure
3. **Authentication errors**: Check Firebase service account configuration
4. **Backend errors**: Check Java console logs for detailed error messages
## 
Complete Flow Setup

### Step 1: Start the Backend
```bash
cd backend/backend
./mvnw spring-boot:run
```
The backend will run on http://localhost:8080

### Step 2: Create Accounts via Login Page
1. Open `login.html` in your browser
2. Click "Create Account"
3. Fill in details and select role (Student/Mentor)
4. **Important**: Select the same institution for both students and mentors
5. Available institutions:
   - Easwari Engineering College
   - Madras Institute of Technology
   - Saveetha Engineering College
   - Sri Sai Ram Engineering College

### Step 3: Create Sample Data

#### Create a Mentor Account:
- Role: Mentor
- Institution: Easwari Engineering College
- Email: mentor@easwari.edu
- Name: Dr. John Smith

#### Create Student Accounts:
- Role: Student  
- Institution: Easwari Engineering College (same as mentor)
- Email: student1@easwari.edu
- Name: Alice Johnson

### Step 4: Add Courses to Firestore
```javascript
// Run this in browser console on any page with Firebase
await db.collection('courses').add({
  title: 'JavaScript Fundamentals',
  description: 'Learn JavaScript from basics to advanced',
  modules: [
    {
      id: 'js_001',
      title: 'Variables and Data Types',
      longContent: 'JavaScript variables can hold different types of data...'
    },
    {
      id: 'js_002', 
      title: 'Functions and Scope',
      longContent: 'Functions are reusable blocks of code...'
    }
  ]
});
```

### Step 5: Test the Complete Flow

#### As a Student:
1. Login to `student_dashboard.html`
2. Go to Settings → Mentor Selection
3. You should see mentors from your institution
4. Select a mentor and save
5. Enroll in courses and make progress

#### As a Mentor:
1. Login to `mentor_dashboard.html`
2. Go to "My Students" - should show students who selected you
3. Go to "Courses" - click on a course to see student progress
4. Go to "Feedback" - send messages to students

## API Endpoints

### Student Endpoints
- `GET /api/student/mentors/{institutionName}` - Get mentors by institution
- `POST /api/student/select-mentor` - Select a mentor
- `GET /api/student/courses` - Get all courses
- `POST /api/student/enroll/{courseId}` - Enroll in course
- `POST /api/student/progress/{courseId}` - Update progress
- `GET /api/student/feedback/{courseId}` - Get feedback messages
- `POST /api/student/feedback/{courseId}` - Send feedback to mentor

### Mentor Endpoints  
- `GET /api/mentor/students` - Get students who selected this mentor
- `GET /api/mentor/courses` - Get all courses
- `GET /api/mentor/courses/{courseId}/students` - Get students in specific course
- `GET /api/mentor/feedback/{studentId}/{courseId}` - Get feedback messages
- `POST /api/mentor/feedback/{studentId}/{courseId}` - Send feedback to student

## Testing

### Test Backend Connectivity:
Open `test_complete_flow.html` to test:
1. Backend connectivity
2. Get mentors by institution
3. API endpoints

### Test Complete Flow:
1. Create accounts via login page
2. Login as student → select mentor
3. Login as mentor → see selected students
4. Test feedback system

## Troubleshooting

### Students not showing for mentor:
- Check that student has `mentorId` field set to mentor's UID
- Verify both have same `institutionName`
- Check backend logs for errors

### Mentors not showing for student:
- Verify mentor has `role: "mentor"` 
- Check `institutionName` matches exactly
- Ensure backend is running on port 8080

### Backend errors:
- Check Java console logs
- Verify Firebase service account is configured
- Ensure Firestore rules allow read/write access

### Frontend errors:
- Check browser console for JavaScript errors
- Verify Firebase configuration
- Test API endpoints with `test_complete_flow.html`

## Data Flow Summary

1. **Account Creation**: Users create accounts with institution selection
2. **Institution Matching**: Students see mentors from same institution
3. **Mentor Selection**: Students select mentors, updates `mentorId` field
4. **Backend Sync**: All operations sync through backend API
5. **Real-time Updates**: Firestore provides real-time updates to frontend
6. **Progress Tracking**: Students' course progress visible to their mentors
7. **Feedback System**: Two-way messaging between students and mentors