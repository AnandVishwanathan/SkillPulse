package com.skillpulse.backend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Bean
    public FirebaseApp initializeFirebase() {
        log.info("Initializing Firebase...");
        
        InputStream serviceAccount = null;

        // 1. Check explicit environment variable FIREBASE_CONFIG_PATH
        String customPath = System.getenv("FIREBASE_CONFIG_PATH");
        if (customPath != null && !customPath.isBlank()) {
            File customFile = new File(customPath);
            if (customFile.exists()) {
                log.info("Reading Firebase credentials from FIREBASE_CONFIG_PATH: {}", customPath);
                try {
                    serviceAccount = new FileInputStream(customFile);
                } catch (IOException e) {
                    log.error("Failed to read Firebase credentials from FIREBASE_CONFIG_PATH", e);
                }
            }
        }

        // 2. Check standard Google application credentials
        if (serviceAccount == null) {
            String gCredPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            if (gCredPath != null && !gCredPath.isBlank()) {
                File gFile = new File(gCredPath);
                if (gFile.exists()) {
                    log.info("Reading Firebase credentials from GOOGLE_APPLICATION_CREDENTIALS: {}", gCredPath);
                    try {
                        serviceAccount = new FileInputStream(gFile);
                    } catch (IOException e) {
                        log.error("Failed to read GOOGLE_APPLICATION_CREDENTIALS file", e);
                    }
                }
            }
        }
        
        // 3. Try to read from Render's Secret File path
        if (serviceAccount == null) {
            File secretFile = new File("/etc/secrets/firebase-service-account.json");
            if (secretFile.exists()) {
                log.info("Reading Firebase credentials from Render Secret File...");
                try {
                    serviceAccount = new FileInputStream(secretFile);
                } catch (IOException e) {
                    log.error("Failed to read Render secret file", e);
                }
            }
        }
        
        // 4. Fall back to local classpath resource file
        if (serviceAccount == null) {
            ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
            if (resource.exists()) {
                log.info("Reading Firebase credentials from classpath...");
                try {
                    serviceAccount = resource.getInputStream();
                } catch (IOException e) {
                    log.error("Failed to read classpath service account file", e);
                }
            }
        }
        
        if (serviceAccount == null) {
            log.warn("WARNING: firebase-service-account.json not found in FIREBASE_CONFIG_PATH, Render secret files, or resources. Firebase services will remain uninitialized.");
            return null;
        }

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase initialized successfully");
            } else {
                log.info("Firebase already initialized");
            }
            
            return FirebaseApp.getInstance();
        } catch (IOException e) {
            log.error("Error initializing FirebaseApp with credentials", e);
            return null;
        }
    }
}
