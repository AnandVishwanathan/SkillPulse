# SkillPulse - Learning Management System

A comprehensive mentoring platform with Java Spring Boot backend and HTML/JavaScript frontend.

## Project Structure

```
├── backend/backend/          # Java Spring Boot backend
│   ├── src/main/java/       # Java source code
│   ├── src/main/resources/  # Configuration files
│   └── pom.xml             # Maven dependencies
├── login.html              # Login and registration page
├── home.html               # Landing page
├── dashboard.html          # General dashboard
├── student_dashboard.html  # Student dashboard
├── mentor_dashboard.html   # Mentor dashboard
├── public_dashboard.html   # Public user dashboard
├── skillpulse.png         # Application logo
├── start_backend.bat      # Windows script to start backend
├── stop_backend.bat       # Windows script to stop backend
├── firebase_setup_guide.md # Setup instructions
└── README.md              # This file
```

## Backend Features

- **Spring Boot 3.4.10** with Java 17
- **Firebase Authentication** integration
- **Firebase Firestore** for data storage
- **RESTful API** endpoints
- **CORS** configured for frontend access
- **Security** with JWT token validation

## Frontend Features

- **Responsive Design** with Tailwind CSS
- **Firebase Authentication** integration
- **Real-time Data** from Firestore
- **Interactive Dashboard** with charts
- **Student Management** system
- **Course Progress** tracking
- **Feedback System** for mentors

## Setup Instructions

### Prerequisites

1. **Java 17** or higher
2. **Maven** (included with the project)
3. **Firebase Project** with Firestore enabled
4. **Modern Web Browser**

### Backend Setup

1. **Start the Backend:**
   ```bash
   # Option 1: Use the provided script
   start_backend.bat
   
   # Option 2: Manual start
   cd backend/backend
   ./mvnw spring-boot:run
   ```

2. **Verify Backend is Running:**
   - Open http://localhost:8080 in your browser
   - You should see a Spring Boot error page (this is normal)
   - The API is available at http://localhost:8080/api

### Frontend Setup

1. **Open the Dashboard:**
   - Open `mentor_dashboard.html` in your web browser
   - Or serve it through a local web server

2. **Test Backend Connection:**
   - Open `test_backend.html` to test the connection
   - Use the "Test Backend Connection" button

## API Endpoints

### Mentor Endpoints

- `GET /api/mentor/students` - Get all students for a mentor
  - Requires: Authorization header with Firebase JWT token
  - Returns: List of students with course progress

## Configuration

### Firebase Configuration

The frontend uses Firebase for authentication and real-time data:

```javascript
const firebaseConfig = {
    apiKey: "AIzaSyDofE9-vx5XzqPAT7qUdK_ub0L-283kN4M",
    authDomain: "skillpulse-d121f.firebaseapp.com",
    projectId: "skillpulse-d121f",
    // ... other config
};
```

### Backend Configuration

The backend connects to Firebase using a service account key located at:
`backend/backend/src/main/resources/firebase-service-account.json`

## Troubleshooting

### Backend Issues

1. **Port 8080 already in use:**
   ```bash
   # Stop existing processes
   stop_backend.bat
   # Then restart
   start_backend.bat
   ```

2. **Firebase connection issues:**
   - Check if `firebase-service-account.json` exists
   - Verify the service account has proper permissions

3. **Build issues:**
   ```bash
   cd backend/backend
   ./mvnw clean install
   ```

### Frontend Issues

1. **Authentication failures:**
   - Check Firebase configuration
   - Verify user has 'mentor' role in Firestore

2. **Backend connection failures:**
   - Ensure backend is running on port 8080
   - Check browser console for CORS errors
   - Use the test page (`test_backend.html`) to debug

3. **CORS issues:**
   - Backend is configured to allow all origins
   - If issues persist, check browser security settings

## Development

### Adding New API Endpoints

1. Create a new controller in `backend/backend/src/main/java/com/skillpulse/backend/`
2. Add the endpoint mapping
3. Update the frontend to call the new endpoint

### Database Schema

The application uses Firebase Firestore with these collections:

- `users` - User profiles (students and mentors)
- `courses` - Course information
- `feedback/{studentId}/{courseId}` - Feedback messages

### Testing

1. **Backend Testing:**
   ```bash
   cd backend/backend
   ./mvnw test
   ```

2. **Frontend Testing:**
   - Open `test_backend.html`
   - Use browser developer tools
   - Check network tab for API calls

## Production Deployment

### Backend

1. Build the JAR file:
   ```bash
   cd backend/backend
   ./mvnw clean package
   ```

2. Run the JAR:
   ```bash
   java -jar target/backend-0.0.1-SNAPSHOT.jar
   ```

### Frontend

1. Serve the HTML files through a web server
2. Update Firebase configuration for production
3. Update API base URL if backend is on different domain

## Support

If you encounter issues:

1. Check the browser console for errors
2. Check backend logs for server errors
3. Use the test page to isolate connection issues
4. Verify Firebase configuration and permissions

## Features Overview

### Dashboard
- Student count and statistics
- Course enrollment overview
- Recent activity feed
- Progress charts

### Student Management
- View all mentees
- Track course progress
- Individual student analytics

### Course Management
- View available courses
- Track student enrollment per course
- Course-specific analytics

### Feedback System
- Real-time messaging with students
- Course-specific feedback threads
- Message history

### Settings
- Profile management
- Account settings
- Password reset functionality