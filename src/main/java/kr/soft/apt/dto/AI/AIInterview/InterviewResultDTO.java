package kr.soft.apt.dto.AI.AIInterview;

import lombok.Data;

@Data
public class InterviewResultDTO {
    private Long idinterviewIdx;   // PK (Auto Increment)
    private String interviewScore; // DB 스키마가 VARCHAR(255)이므로 String
    private String interviewReason;
    private Long coverPostsIdx;    // FK
}
