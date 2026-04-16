package com.skillpulse.backend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        System.out.println("Initializing Firebase...");
        
        ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
        
        if (!resource.exists()) {
            System.err.println("ERROR: firebase-service-account.json not found in resources folder");
            throw new IOException("Firebase service account file not found");
        }
        
        InputStream serviceAccount = resource.getInputStream();

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
