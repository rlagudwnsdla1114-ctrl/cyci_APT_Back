package kr.soft.apt.dto.AI.AIComMatch;

import lombok.Data;

// 채용 공고
@Data
public class JobPostsDTO {
    private long jobPostIdx;
    private String title;
    private String techStack;
    private String career;
    private String education;
    private String employmentType;
    private String salary;
    private int companyIdx;
}
