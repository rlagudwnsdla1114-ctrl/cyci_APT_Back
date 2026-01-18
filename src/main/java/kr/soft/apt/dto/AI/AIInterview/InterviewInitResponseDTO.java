package kr.soft.apt.dto.AI.AIInterview;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InterviewInitResponseDTO {
    private Long interviewId;
    private List<String> questions;
}
