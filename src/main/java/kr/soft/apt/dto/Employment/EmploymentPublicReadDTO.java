package kr.soft.apt.dto.Employment;

import lombok.Data;

@Data
public class EmploymentPublicReadDTO {
    private Long jobPostsIdx;

    private String companyName;
    private Long viewCount;

    private String title;
    private Integer recruitCount;
    private String employmentType;
    private String salary;
    private String workTime;
    private String career;
    private String education;
    private String techStack;
    private String applicationPeriod;
    private String postsCreatedAt;

    private String attachFile;
    private String attachFileOrigin;

    private Long companyIdx;
}
