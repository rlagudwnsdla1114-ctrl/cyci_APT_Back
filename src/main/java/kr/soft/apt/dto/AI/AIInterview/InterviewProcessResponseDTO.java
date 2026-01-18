package kr.soft.apt.dto.AI.AIInterview;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewProcessResponseDTO {
    private int totalScore;
    private String feedback;
    private String transcribedText;
}
