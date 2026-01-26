package kr.soft.apt.dto.AI;

import lombok.Data;

@Data
public class AIComListTopDTO {
    private String name;
    private Integer matchScore;
    private Long jobPostsIdx;
    private String jobTitle;
}
