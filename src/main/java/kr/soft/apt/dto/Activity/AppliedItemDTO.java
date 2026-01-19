package kr.soft.apt.dto.Activity;

import lombok.Data;

@Data
public class AppliedItemDTO {
    private Long jobPostsIdx;
    private String companyName;
    private String title;
    private String appliedAt;
    private String status;
}
