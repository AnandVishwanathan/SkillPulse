# Render Deployment Guide for SkillPulse

Your application has two distinct parts that need to be deployed to Render as two separate services:
1. **The Backend:** A Java (Spring Boot) application.
2. **The Frontend:** A collection of static HTML/JS files.

To make things easy, I've already added a `Dockerfile` for your Java backend at `oopj project 1/backend/backend/Dockerfile`.

Here is exactly what you need to provide to Render for both pieces!

---

## Part 1: Deploying the Backend (Web Service)

The backend needs to be deployed as a **Web Service** on Render using Docker.

1. Go to your Render Dashboard and click **New > Web Service**.
2. Connect your GitHub repository.
3. Use the following settings for the Web Service:
   - **Name:** `skillpulse-backend` (or whatever you prefer)
   - **Environment:** `Docker` *(Render will automatically detect the `Dockerfile` I just created).*
   - **Root Directory:** `oopj project 1/backend/backend` (or just `backend/backend` depending on how your repo is structured)
   - **Region:** Pick the one closest to your users.
   - **Branch:** `main` (or the branch you are using)

**Important Security Note on Firebase:** 
Your backend correctly gets its Firebase credentials from `firebase-service-account.json`. If your GitHub repository is **Public**, DO NOT commit this file to GitHub, as it will expose your database. If your repo is **Private**, it is technically safe to push it and Render will be able to read it inside the Docker container. 

*Once the backend finishes deploying, Render will give you a public URL (e.g., `https://skillpulse-backend.onrender.com`). Copy this URL, you will need it for the frontend.*

---

## Part 2: Updating the Frontend (Before Deploying)

Right now, your frontend HTML files are hardcoded to talk to your local computer (`http://localhost:8080/api/..`). Next, you must update this so the frontend knows where the new backend lives.

1. Open your code editor and perform a **Find and Replace All**.
2. **Find:** `http://localhost:8080/api`
3. **Replace:** `https://skillpulse-backend.onrender.com/api` *(use the exact URL Render gave you in Part 1).*
   * *(Note: It appears in `student_dashboard.html`, `mentor_dashboard.html`, `mentor_dashboard_quick.html`, etc.)*

Once you update and save these changes, commit and push the code to your GitHub repo.

---

## Part 3: Deploying the Frontend (Static Site)

Your frontend is a set of static HTML files, which is free to host on Render.

1. Go to your Render Dashboard and click **New > Static Site**.
2. Connect your GitHub repository.
3. Use the following settings for the Static Site:
   - **Name:** `skillpulse-frontend` 
   - **Root Directory:** `oopj project 1` (or leave it blank if your `home.html` is at the absolute root of the repository).
   - **Build Command:** *(Leave this completely empty)*
   - **Publish directory:** `.` (this tells Render to serve the HTML files in the directory you specified).

---

## Part 4: Fixing CORS in the Backend (Crucial)

Once your Frontend is deployed, it will get its own URL (e.g., `https://skillpulse-frontend.onrender.com`).
For security reasons, your backend will block requests from this new Frontend URL until you explicitly allow it using CORS functionality.

1. Go back to your backend code: `WebConfig.java`
2. Update the `.allowedOrigins(...)` to include your new frontend URL.
```java
registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000", "http://127.0.0.1:5500", "https://skillpulse-frontend.onrender.com")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
```
3. Commit and push this single change. Your backend will auto-redeploy and everything will connect together seamlessly!
