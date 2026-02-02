package kr.soft.apt.dto.AI.AIComList;

import lombok.Data;

@Data
public class AIComListDTO {
    private Long comMatchingIdx;
    private String jobseekerName;
    private Integer comMatchScore;
    private String comAiReason;
    private Long jobPostsIdx;
    private Integer jobseekerIdx;
    private String keySkill;
    private String apply;
}
