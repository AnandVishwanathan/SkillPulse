package com.skillpulse.backend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MentorDTO {
    private String id;
    private String fullName;
    private String email;
    private String institutionName;
    private int studentCount;
}