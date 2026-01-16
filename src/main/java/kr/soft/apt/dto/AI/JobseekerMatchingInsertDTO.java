package kr.soft.apt.dto.AI;

import lombok.Data;

@Data
public class JobseekerMatchingInsertDTO {
    private Integer score;
    private String reason;

    private Long jobseekerIdx;
    private Long companyIdx;
    private Long jobPostsIdx;
    private Long coverPostsIdx;
}
