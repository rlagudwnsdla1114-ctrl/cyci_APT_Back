package kr.soft.apt.dto.Employment;

import lombok.Data;

@Data
public class EmploymentPublicListDTO {
    private Long jobPostsIdx;
    private String companyName;
    private String title;

    private String employmentType;
    private String career;
    private String education;
    private String salary;
    private String techStack;

    private Long viewCount;
    private String postsCreatedAt;
}
