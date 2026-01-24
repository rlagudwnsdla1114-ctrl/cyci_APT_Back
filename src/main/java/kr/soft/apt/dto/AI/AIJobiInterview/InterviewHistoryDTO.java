package kr.soft.apt.dto.AI.AIJobiInterview;

import lombok.Data;

@Data
public class InterviewHistoryDTO {
    private long interviewId;
    private String interviewTitle;
    private String totalScore;
    private String aiFeedback;
}
