package com.skillpulse.backend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        System.out.println("Initializing Firebase...");
        
        InputStream serviceAccount = null;
        
        // 1. Try to read from Render's Secret File path first
        File secretFile = new File("/etc/secrets/firebase-service-account.json");
        if (secretFile.exists()) {
            System.out.println("Reading Firebase credentials from Render Secret File...");
            serviceAccount = new FileInputStream(secretFile);
        } else {
            // 2. Fall back to local resource file
            System.out.println("Reading Firebase credentials from classpath...");
            ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
            if (resource.exists()) {
                serviceAccount = resource.getInputStream();
            }
        }
        
        if (serviceAccount == null) {
            System.err.println("ERROR: firebase-service-account.json not found in resources folder or Secret Files");
            throw new IOException("Firebase service account file not found");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase initialized successfully");
        } else {
            System.out.println("Firebase already initialized");
        }
        
        return FirebaseApp.getInstance();
    }
}
