package kr.soft.apt.dto.AI.AIComList;

import lombok.Data;

@Data
public class AIComListDTO {
    private String jobseekerName;
    private String keySkill;
    private int comMatchScore;
    private String comAiReason;
    private String matchingCreateat;
    private long jobPostsIdx;
    private long jobseekerIdx;
    private long companyApplicantIdx;
}