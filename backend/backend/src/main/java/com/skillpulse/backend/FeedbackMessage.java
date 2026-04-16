package com.skillpulse.backend;

import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@IgnoreExtraProperties
public class FeedbackMessage {
    private String id;
    private String text;
    private String sender;
    private Date timestamp;
    private String senderName;
    private String senderRole;
}