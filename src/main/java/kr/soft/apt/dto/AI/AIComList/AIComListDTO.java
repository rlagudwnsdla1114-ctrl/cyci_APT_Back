package kr.soft.apt.dto.AI.AIComList;

import lombok.Data;

@Data
public class AIComListDTO {
    private String name;
    private String keySkill;
    private int matchRate;
    private String reason;
    private String matchDate;
    private long jobPostsIdx;
    private long jobseekerIdx;
    private long companyApplicantIdx;
}